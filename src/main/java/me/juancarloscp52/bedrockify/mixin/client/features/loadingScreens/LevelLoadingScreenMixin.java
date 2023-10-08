package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {
    @Shadow @Final private WorldGenerationProgressTracker progressProvider;

    @Shadow private long lastNarrationTime;

    protected LevelLoadingScreenMixin(Text title) {
        super(title);
    }

    /**
     * Draws the loading screen widget and allows to toggle the chunk map loading widget.
     */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if(!BedrockifyClient.getInstance().settings.isLoadingScreenEnabled())
            return;
        this.renderBackground(context, mouseX, mouseY, delta);
        int xPosition = this.width / 2;
        int yPosition = this.height / 2;
        LoadingScreenWidget.getInstance().render(context, xPosition, yPosition, Text.translatable("menu.generatingLevel"), null, this.progressProvider.getProgressPercentage());

        long l = Util.getMeasuringTimeMs();
        if (l - this.lastNarrationTime > 2000L) {
            this.lastNarrationTime = l;
            this.narrateScreenIfNarrationEnabled(true);
        }

        if (BedrockifyClient.getInstance().settings.isShowChunkMapEnabled())
            LevelLoadingScreen.drawChunkMap(context, this.progressProvider, xPosition, yPosition + yPosition / 2 + 89 / 4, 1, 0);

        info.cancel();
    }

}
