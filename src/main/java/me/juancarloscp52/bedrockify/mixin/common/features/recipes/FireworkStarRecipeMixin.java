package me.juancarloscp52.bedrockify.mixin.common.features.recipes;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.features.recipes.DyeHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.FireworkStarRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(FireworkStarRecipe.class)
public class FireworkStarRecipeMixin {

    @Shadow @Final private static Ingredient TYPE_MODIFIER;

    @Shadow @Final private static Ingredient FLICKER_MODIFIER;

    @Shadow @Final private static Ingredient TRAIL_MODIFIER;

    @Shadow @Final private static Ingredient GUNPOWDER;

    @Shadow @Final private static Map<Item, FireworkExplosionComponent.Type> TYPE_MODIFIER_MAP;

    @Inject(method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",at=@At("HEAD"),cancellable = true)
    public void matches(CraftingRecipeInput craftingInventory, World world, CallbackInfoReturnable<Boolean> infoReturnable) {
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled())
            return;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;

        for(int i = 0; i < craftingInventory.getSize(); ++i) {
            ItemStack itemStack = craftingInventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (TYPE_MODIFIER.test(itemStack)) {
                    if (bl3) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl3 = true;
                } else if (FLICKER_MODIFIER.test(itemStack)) {
                    if (bl5) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl5 = true;
                } else if (TRAIL_MODIFIER.test(itemStack)) {
                    if (bl4) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl4 = true;
                } else if (GUNPOWDER.test(itemStack)) {
                    if (bl) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl = true;
                } else {
                    if (!(DyeHelper.isDyeableItem(itemStack.getItem()))) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl2 = true;
                }
            }
        }
        infoReturnable.setReturnValue(bl && bl2);
    }

    @Inject(method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;",at=@At("HEAD"),cancellable = true)
    public void craft(CraftingRecipeInput craftingInventory, RegistryWrapper.WrapperLookup wrapperLookup, CallbackInfoReturnable<ItemStack> cir) {
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled())
            return;
        ItemStack itemStack = new ItemStack(Items.FIREWORK_STAR);
        FireworkExplosionComponent.Type type = FireworkExplosionComponent.Type.SMALL_BALL;
        IntList list = new IntArrayList();
        boolean hasTwinkleMod = false;
        boolean hasTrailMod = false;

        for(int i = 0; i < craftingInventory.getSize(); ++i) {
            ItemStack itemStack2 = craftingInventory.getStackInSlot(i);
            if (!itemStack2.isEmpty()) {
                if (TYPE_MODIFIER.test(itemStack2)) {
                    type = TYPE_MODIFIER_MAP.get(itemStack2.getItem());
                } else if (FLICKER_MODIFIER.test(itemStack2)) {
                    hasTwinkleMod = true;
                } else if (TRAIL_MODIFIER.test(itemStack2)) {
                    hasTrailMod = true;
                } else if (DyeHelper.isDyeableItem(itemStack2.getItem())) {
                    list.add((DyeHelper.getDyeItem(itemStack2.getItem()).getColor().getFireworkColor()));
                }
            }
        }

        itemStack.set(DataComponentTypes.FIREWORK_EXPLOSION, new FireworkExplosionComponent(type, list, IntList.of(), hasTrailMod, hasTwinkleMod));
        cir.setReturnValue(itemStack);
    }

}
