package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;


import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "render", at=@At("HEAD"))
    private void addTimeToCubeMap(CallbackInfo info){
        BedrockifyRotatingCubeMapRenderer.getInstance().addTime();
    }
}
