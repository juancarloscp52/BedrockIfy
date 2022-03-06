package me.juancarloscp52.bedrockify.mixin.client.features.bedrockShading;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/**
 * @author Shaddatic
 */
@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    private boolean isLuminous;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F", ordinal = 0))
    private void getFluidType(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        this.isLuminous = 0 < world.getLuminance(pos); //state.isIn(FluidTags.LAVA);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F"))
    private float getLavaShade(BlockRenderView blockRenderView, Direction direction, boolean shaded) {
        if(!Bedrockify.getInstance().settings.bedrockShading)
            return blockRenderView.getBrightness(direction,shaded);

        return BedrockifyClient.getInstance().bedrockBlockShading.getLiquidShade(direction,isLuminous);
    }
}
