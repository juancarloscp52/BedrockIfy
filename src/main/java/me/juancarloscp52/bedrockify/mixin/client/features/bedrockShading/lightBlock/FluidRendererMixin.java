package me.juancarloscp52.bedrockify.mixin.client.features.bedrockShading.lightBlock;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/**
 * @author Shaddatic
 */
@Mixin(FluidRenderer.class)
public class FluidRendererMixin {
    private boolean isLuminous;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F", ordinal = 0))
    private void getFluidType(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfo ci) {
        this.isLuminous = 0 < world.getLuminance(pos); //state.isIn(FluidTags.LAVA);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F"))
    private float getLavaShade(BlockRenderView instance, Direction direction, boolean shaded, Operation<Float> original) {
        if(!BedrockifyClient.getInstance().settings.bedrockShading)
            return original.call(instance, direction, shaded);

        return BedrockifyClient.getInstance().bedrockBlockShading.getLiquidShade(direction,isLuminous);
    }
}
