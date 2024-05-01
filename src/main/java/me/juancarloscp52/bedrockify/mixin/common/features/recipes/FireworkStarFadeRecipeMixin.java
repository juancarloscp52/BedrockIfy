package me.juancarloscp52.bedrockify.mixin.common.features.recipes;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.features.recipes.DyeHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.FireworkStarFadeRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkStarFadeRecipe.class)
public class FireworkStarFadeRecipeMixin {
    @Shadow @Final private static Ingredient INPUT_STAR;

    @Inject(method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",at=@At("HEAD"),cancellable = true)
    public void matches(RecipeInputInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> infoReturnable) {
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled())
            return;
        boolean bl = false;
        boolean bl2 = false;

        for(int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack = craftingInventory.getStack(i);
            if (!itemStack.isEmpty()) {
                if (DyeHelper.isDyeableItem(itemStack.getItem())) {
                    bl = true;
                } else {
                    if (!INPUT_STAR.test(itemStack)) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    if (bl2) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl2 = true;
                }
            }
        }

        infoReturnable.setReturnValue(bl2 && bl);
    }

    @Inject(method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",at=@At("HEAD"),cancellable = true)
    public void craft(RecipeInputInventory craftingInventory, RegistryWrapper.WrapperLookup wrapperLookup, CallbackInfoReturnable<ItemStack> cir) {
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled())
            return;
        IntList list = new IntArrayList();
        ItemStack itemStack = null;

        for(int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack2 = craftingInventory.getStack(i);
            Item item = itemStack2.getItem();
            if (DyeHelper.isDyeableItem(item)) {
                list.add(DyeHelper.getDyeItem(item).getColor().getFireworkColor());
            } else if (INPUT_STAR.test(itemStack2)) {
                itemStack = itemStack2.copy();
                itemStack.setCount(1);
            }
        }

        if (itemStack != null && !list.isEmpty()) {
            itemStack.apply(DataComponentTypes.FIREWORK_EXPLOSION, FireworkExplosionComponent.DEFAULT, list, FireworkExplosionComponent::withFadeColors);
//            itemStack.getOrCreateSubNbt("Explosion").putIntArray("FadeColors", list);
            cir.setReturnValue(itemStack);
        } else {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
