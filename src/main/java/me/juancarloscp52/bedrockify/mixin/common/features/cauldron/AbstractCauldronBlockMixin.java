package me.juancarloscp52.bedrockify.mixin.common.features.cauldron;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractCauldronBlock.class)
public abstract class AbstractCauldronBlockMixin {
    /**
     * Avoids to throw <code>NullPointerException</code>.
     */
    @Redirect(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/cauldron/CauldronBehavior;interact(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/util/ActionResult;"))
    private ActionResult bedrockify$checkBehaviorNotNull(CauldronBehavior instance, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        return (instance == null) ? ActionResult.PASS : instance.interact(state, world, pos, player, hand, stack);
    }
}
