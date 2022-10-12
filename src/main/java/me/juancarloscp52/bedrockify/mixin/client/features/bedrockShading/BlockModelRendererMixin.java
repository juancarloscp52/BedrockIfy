package me.juancarloscp52.bedrockify.mixin.client.features.bedrockShading;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;
import java.util.List;
/**
 * @author Shaddatic
 */
@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {
    private boolean luminant = false;
    @Inject(method = "renderQuadsFlat",at=@At(value = "INVOKE",target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F",ordinal = 0))
    private void getLuminant(BlockRenderView world, BlockState state, BlockPos pos, int light, int overlay, boolean useWorldLight, MatrixStack matrices, VertexConsumer vertexConsumer, List<BakedQuad> quads, BitSet flags, CallbackInfo ci){
        this.luminant = state.getLuminance() > 2;
    }

    @Redirect(method = "renderQuadsFlat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F"))
    private float getBlockShade(BlockRenderView blockRenderView, Direction direction, boolean shaded){
        if(luminant && shaded && BedrockifyClient.getInstance().settings.bedrockShading)
            return BedrockifyClient.getInstance().bedrockBlockShading.getBlockShade(direction);
        else
            return blockRenderView.getBrightness(direction, shaded);
    }
}
