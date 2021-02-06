package me.juancarloscp52.bedrockify.mixin.client.core.clientRenderTimer;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow public Screen currentScreen;
    long newTime=0;
    @Inject(method = "render", at=@At("HEAD"))
    private void computeDeltaTime(boolean tick, CallbackInfo ci){
        long oldTime = newTime;
        newTime = System.nanoTime();
        BedrockifyClient.getInstance().deltaTime= newTime - oldTime;
        if(this.currentScreen != null){
            BedrockifyRotatingCubeMapRenderer.getInstance().addPanoramaTime();
        }
    }
}
