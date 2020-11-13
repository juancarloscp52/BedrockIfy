package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    /**
     * Renders the rotating cube map on screens instead of the dirt texture if enabled.
     */
    @Inject(method = "renderBackgroundTexture", at = @At("HEAD"), cancellable = true)
    public void renderTexture(CallbackInfo info) {
        if(!Bedrockify.getInstance().settings.isCubemapBackgroundEnabled())
            return;
        BedrockifyRotatingCubeMapRenderer.getInstance().render();
        info.cancel();
    }

}
