package me.juancarloscp52.bedrockify.mixin.client.features.bedrockShading.lightBlock;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AbstractBlockRenderContext;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
/**
 * @author Shaddatic
 */
@Mixin(AbstractBlockRenderContext.class)
public class AbstractQuadRendererMixin {

    @Shadow @Final protected BlockRenderInfo blockInfo;

    @Redirect(method = "shadeFlatQuad", at=@At(value = "INVOKE",target = "Lnet/minecraft/world/BlockRenderView;getBrightness(Lnet/minecraft/util/math/Direction;Z)F"))
    private float getBlockShade(BlockRenderView blockRenderView, Direction direction, boolean shaded){

        if(blockInfo.blockState.getLuminance()>2 && shaded && BedrockifyClient.getInstance().settings.bedrockShading)
            return BedrockifyClient.getInstance().bedrockBlockShading.getBlockShade(direction);
        else
            return blockRenderView.getBrightness(direction, shaded);
    }

}
