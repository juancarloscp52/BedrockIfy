package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin {
    @Shadow
    @Final
    protected MinecraftClient client;
    @Shadow
    protected int bottom;
    @Shadow
    protected int top;

    @Shadow
    public abstract void setRenderBackground(boolean renderBackground);

    @Shadow
    public abstract void setRenderHorizontalShadows(boolean renderHorizontalShadows);

    @Shadow private boolean renderBackground;

    @Shadow protected abstract void renderBackground(DrawContext context);

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/EntryListWidget;renderBackground(Lnet/minecraft/client/gui/DrawContext;)V"))
    private void renderPanorama(EntryListWidget instance, DrawContext drawContext) {
        if (!BedrockifyClient.getInstance().settings.isCubemapBackgroundEnabled() || shouldIgnoreScreen()) {
            this.renderBackground(drawContext);
            return;
        }

        BedrockifyRotatingCubeMapRenderer.getInstance().render(drawContext);
        drawContext.fill(0, this.top, client.getWindow().getScaledWidth(), this.bottom, (100 << 24));
    }

    @Inject(method = "<init>(Lnet/minecraft/client/MinecraftClient;IIIII)V", at = @At("RETURN"))
    private void bedrockify$ctor(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight, CallbackInfo ci) {
        // Prevent the screen background from drawing
        this.setRenderBackground(!BedrockifyClient.getInstance().settings.isCubemapBackgroundEnabled() || shouldIgnoreScreen());
        // Prevent top and bottom bars from drawing (Only on pack Screens)
        this.setRenderHorizontalShadows(!(this.client.currentScreen instanceof PackScreen) || !BedrockifyClient.getInstance().settings.isCubemapBackgroundEnabled());
    }

    private boolean shouldIgnoreScreen() {
        return BedrockifyClient.getInstance().settings.panoramaIgnoreScreen(this.client.currentScreen);
    }
}
