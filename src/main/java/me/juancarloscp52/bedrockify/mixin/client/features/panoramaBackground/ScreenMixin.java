package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow protected MinecraftClient client;

    /**
     * Renders the rotating cube map on screens instead of the dirt texture if enabled.
     */
    @Inject(method = "renderBackgroundTexture", at = @At("HEAD"), cancellable = true)
    public void renderTexture(CallbackInfo info) {
        if(!Bedrockify.getInstance().settings.isCubemapBackgroundEnabled() || Bedrockify.getInstance().settings.panoramaIgnoreScreen(this.client.currentScreen) /*|| this.client.currentScreen.getClass().getName().contains(".modmanager.gui.") /* Mod Manager */)
            return;
        BedrockifyRotatingCubeMapRenderer.getInstance().render();
        info.cancel();
    }

}
