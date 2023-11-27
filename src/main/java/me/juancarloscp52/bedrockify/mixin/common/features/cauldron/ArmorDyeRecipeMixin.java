package me.juancarloscp52.bedrockify.mixin.common.features.cauldron;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.ArmorDyeRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorDyeRecipe.class)
public abstract class ArmorDyeRecipeMixin {
    /**
     * Revokes all dye recipes for the DyeableItem while bedrockCauldron feature is enabled.
     */
    @Inject(method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", at = @At("HEAD"), cancellable = true)
    private void bedrockify$revokeOriginalDyeingRecipe(RecipeInputInventory recipeInputInventory, World world, CallbackInfoReturnable<Boolean> cir) {
        if (!Bedrockify.getInstance().settings.bedrockCauldron) {
            return;
        }

        cir.setReturnValue(false);
    }
}
