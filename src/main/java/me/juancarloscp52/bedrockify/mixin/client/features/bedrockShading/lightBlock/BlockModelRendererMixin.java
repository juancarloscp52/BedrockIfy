package me.juancarloscp52.bedrockify.mixin.client.features.bedrockShading.lightBlock;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;
import java.util.List;
/**
 * @author Shaddatic
 */
@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {
    @Unique
    private boolean luminant = false;

    @Inject(method = "renderQuadsFlat",at=@At(value = "INVOKE",target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F",ordinal = 0))
    private void getLuminant(BlockRenderView world, BlockState state, BlockPos pos, int light, int overlay, boolean useWorldLight, MatrixStack matrices, VertexConsumer vertexConsumer, List<BakedQuad> quads, BitSet flags, CallbackInfo ci){
        this.luminant = state.getLuminance() > 2;
    }

    @WrapOperation(method = "renderQuadsFlat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F"))
    private float getBlockShade(BlockRenderView instance, Direction direction, boolean shaded, Operation<Float> original) {
        if(luminant && shaded && BedrockifyClient.getInstance().settings.bedrockShading)
            return BedrockifyClient.getInstance().bedrockBlockShading.getBlockShade(direction);
        else
            return original.call(instance, direction, shaded);
    }
}
