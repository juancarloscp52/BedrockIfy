package me.juancarloscp52.bedrockify.mixin.common.features.cauldron;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.block.cauldron.BedrockCauldronBehavior;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {
    /**
     * The lambda of <code>EMPTY_CAULDRON_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> { ... });</code>
     */
    @Inject(method = "method_32222", at = @At("RETURN"), cancellable = true)
    private static void bedrockify$addCauldronEmptyBehavior(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, CallbackInfoReturnable<ActionResult> cir) {
        if (!Bedrockify.getInstance().settings.bedrockCauldron || PotionUtil.getPotion(stack) == Potions.WATER) {
            return;
        }

        // Redirect to customized behavior.
        final ActionResult result = BedrockCauldronBehavior.PLACE_POTION_FLUID.interact(state, world, pos, player, hand, stack);
        cir.setReturnValue(result);
    }
}
