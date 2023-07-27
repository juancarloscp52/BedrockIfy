package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    /**
     * Renders the rotating cube map on screens instead of the dirt texture if enabled.
     */
    @Inject(method = "renderBackgroundTexture", at = @At("HEAD"), cancellable = true)
    public void renderTexture(DrawContext drawContext, CallbackInfo info) {
        if(!BedrockifyClient.getInstance().settings.isCubemapBackgroundEnabled() || BedrockifyClient.getInstance().settings.panoramaIgnoreScreen() /*|| this.client.currentScreen.getClass().getName().contains(".modmanager.gui.") /* Mod Manager */)
            return;
        BedrockifyRotatingCubeMapRenderer.getInstance().render(drawContext);
        info.cancel();
    }

}
