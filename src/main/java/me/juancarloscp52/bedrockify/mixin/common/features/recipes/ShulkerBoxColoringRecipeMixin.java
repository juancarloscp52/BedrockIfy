package me.juancarloscp52.bedrockify.mixin.common.features.recipes;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.features.recipes.DyeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.ShulkerBoxColoringRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxColoringRecipe.class)
public class ShulkerBoxColoringRecipeMixin {

    @Inject(method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z",at=@At("HEAD"),cancellable = true)
    public void customMatches(CraftingRecipeInput craftingInventory, World world, CallbackInfoReturnable<Boolean> infoReturnable){
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled()){
            return;
        }
        int i = 0;
        int j = 0;

        for(int k = 0; k < craftingInventory.getSize(); ++k) {
            ItemStack itemStack = craftingInventory.getStackInSlot(k);
            if (!itemStack.isEmpty()) {
                if (Block.getBlockFromItem(itemStack.getItem()) instanceof ShulkerBoxBlock) {
                    ++i;
                } else {
                    if (!(DyeHelper.isDyeableItem(itemStack.getItem()))) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }
                    ++j;
                }

                if (j > 1 || i > 1) {
                    infoReturnable.setReturnValue(false);
                    return;
                }
            }
        }
        infoReturnable.setReturnValue(i == 1 && j == 1);
    }
    @Inject(method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;", at=@At("HEAD"),cancellable = true)
    public void craft(CraftingRecipeInput craftingInventory, RegistryWrapper.WrapperLookup wrapperLookup, CallbackInfoReturnable<ItemStack> cir) {
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled()){
            return;
        }
        ItemStack itemStack = ItemStack.EMPTY;
        DyeItem dyeItem = (DyeItem)Items.WHITE_DYE;

        for(int i = 0; i < craftingInventory.getSize(); ++i) {
            ItemStack itemStack2 = craftingInventory.getStackInSlot(i);
            if (!itemStack2.isEmpty()) {
                Item item = itemStack2.getItem();
                if (Block.getBlockFromItem(item) instanceof ShulkerBoxBlock) {
                    itemStack = itemStack2;
                } else if (DyeHelper.isDyeableItem(item)) {
                    dyeItem = DyeHelper.getDyeItem(item);
                }
            }
        }

        Block block = ShulkerBoxBlock.get(dyeItem.getColor());
        cir.setReturnValue(itemStack.copyComponentsToNewStack(block, 1));
    }
}
