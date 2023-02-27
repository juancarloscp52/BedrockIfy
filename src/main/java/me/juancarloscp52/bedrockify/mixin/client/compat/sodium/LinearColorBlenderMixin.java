package me.juancarloscp52.bedrockify.mixin.client.compat.sodium;

import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.blender.ColorSampler;
import me.jellysquid.mods.sodium.client.model.quad.blender.FlatColorBlender;
import me.jellysquid.mods.sodium.client.model.quad.blender.LinearColorBlender;
import me.juancarloscp52.bedrockify.common.block.AbstractBECauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = LinearColorBlender.class, remap = false)
public abstract class LinearColorBlenderMixin {
    /**
     * The array of classes to avoid blending color by {@link LinearColorBlender}.<br>
     * These classes are redirected to {@link FlatColorBlender#getColors}.
     */
    @Unique
    private static final Class<?>[] IGNORE_BLENDING_BLOCK_CLASSES = new Class[]{
            AbstractBECauldronBlock.class
    };
    @Unique
    private static final FlatColorBlender FLAT_COLOR_BLENDER_INSTANCE = new FlatColorBlender();

    /**
     * Workaround of <a href="https://github.com/CaffeineMC/sodium-fabric/issues/895">Sodium Issue #895</a>.
     */
    @Inject(method = "getColors", at = @At("HEAD"), cancellable = true, remap = false)
    private <T> void bedrockify$ignoreBlendForCustomBlock(BlockRenderView world, BlockPos origin, ModelQuadView quad, ColorSampler<T> sampler, T state, CallbackInfoReturnable<int[]> cir) {
        final Block block = world.getBlockState(origin).getBlock();

        for (Class<?> clazz : IGNORE_BLENDING_BLOCK_CLASSES) {
            if (clazz.isInstance(block)) {
                cir.setReturnValue(FLAT_COLOR_BLENDER_INSTANCE.getColors(world, origin, quad, sampler, state));
                cir.cancel();
                return;
            }
        }
    }
}