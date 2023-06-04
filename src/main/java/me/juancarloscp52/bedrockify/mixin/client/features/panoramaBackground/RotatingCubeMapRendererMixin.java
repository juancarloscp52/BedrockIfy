package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RotatingCubeMapRenderer.class)
public class RotatingCubeMapRendererMixin{

    @Inject(method = "render", at=@At("HEAD"),cancellable = true)
    public void renderBedrockIfyCubeMap(float delta, float alpha, CallbackInfo ci){
        BedrockifyRotatingCubeMapRenderer.getInstance().render(new DrawContext(MinecraftClient.getInstance(),MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers()),alpha, true);
        ci.cancel();
    }

}
