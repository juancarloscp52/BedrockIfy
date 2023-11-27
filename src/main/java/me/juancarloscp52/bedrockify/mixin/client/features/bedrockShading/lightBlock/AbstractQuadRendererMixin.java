package me.juancarloscp52.bedrockify.mixin.client.features.bedrockShading.lightBlock;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AbstractBlockRenderContext;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author Shaddatic
 */
@Mixin(AbstractBlockRenderContext.class)
public class AbstractQuadRendererMixin {

    @Shadow @Final protected BlockRenderInfo blockInfo;

    @WrapOperation(method = "shadeFlatQuad", at=@At(value = "INVOKE",target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F"))
    private float getBlockShade(BlockRenderView instance, Direction direction, boolean shaded, Operation<Float> original){

        if(blockInfo.blockState.getLuminance()>2 && shaded && BedrockifyClient.getInstance().settings.bedrockShading)
            return BedrockifyClient.getInstance().bedrockBlockShading.getBlockShade(direction);
        else
            return original.call(instance,direction,shaded);
    }

}
