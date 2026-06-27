package me.juancarloscp52.bedrockify.client.gui;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import me.juancarloscp52.bedrockify.client.features.paperDoll.PaperDoll;
import me.juancarloscp52.bedrockify.client.features.savingOverlay.SavingOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

import java.util.Objects;

public class Overlay {

    private final Minecraft client;
    private final PaperDoll paperDoll;
    public final SavingOverlay savingOverlay;
    private Component fps;
    private final int textPosX = 0;

    public Overlay(Minecraft client) {
        this.client = client;
        this.paperDoll = new PaperDoll(client);
        this.savingOverlay = new SavingOverlay();
    }

    public void renderOverlay(GuiGraphicsExtractor drawContext) {
        // Only render the overlay if HUD is not hidden and debug is NOT enabled.
        if (!client.getDebugOverlay().showDebugScreen() && !client.gui.hud.isHidden()){
            this.renderText(drawContext);
            this.paperDoll.renderPaperDoll(drawContext);
            BedrockifyClient.getInstance().reachAroundPlacement.renderIndicator(drawContext);
            this.savingOverlay.render(drawContext);
        }
    }

    /**
     * Renders the text components for the player position and client fps.
     */
    private void renderText(GuiGraphicsExtractor drawContext) {
        fps = Component.translatable("bedrockify.hud.fps").append(String.valueOf(client.getFps()));
        renderPositionText(drawContext);
        renderFpsText(drawContext);
    }

    private void renderPositionText(GuiGraphicsExtractor drawContext) {
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        int screenBorder = settings.overlayIgnoresSafeArea ? 0 : settings.getScreenSafeArea();
        int posY = settings.getPositionHUDHeight();
        if (!settings.isShowPositionHUDEnabled())
            return;
        BlockPos blockPos = Objects.requireNonNull(this.client.getCameraEntity(), "Camera Entity cannot be null.").blockPosition();
        MutableComponent position = Component.translatable("bedrockify.hud.position").append(Component.literal(" "+ blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ()));
        if(settings.getFPSHUDoption()==1)
            position.append(" ").append(fps);
        int positionWidth = client.font.width(position);
        float opacity = BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
//        RenderSystem.setShaderColor(1,1,1,1);
        drawContext.fill(textPosX + screenBorder, posY + screenBorder, textPosX + positionWidth + 6 + screenBorder, posY + 12 + screenBorder, Mth.ceil((255.0D * client.options.textBackgroundOpacity().get()) * opacity)<<24);
        int alpha = (int) Math.ceil(opacity*255);
        drawContext.text(client.font, position, textPosX + 3 + screenBorder, posY + 3 + screenBorder, 16777215 | ((alpha) << 24));
    }

    private void renderFpsText(GuiGraphicsExtractor drawContext) {
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        int screenBorder = settings.overlayIgnoresSafeArea ? 0 : settings.getScreenSafeArea();
        int posY = settings.getPositionHUDHeight()+2;
        boolean positionEnabled = settings.isShowPositionHUDEnabled();
        if (settings.getFPSHUDoption()!=2)
            return;
        int fpsCounterWidth = client.font.width(fps);
        float opacity = BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
//        RenderSystem.setShaderColor(1,1,1,1);
        drawContext.fill(textPosX + screenBorder, posY + (positionEnabled ? 10 : 0) + screenBorder, textPosX + fpsCounterWidth + 6 + screenBorder, posY + (positionEnabled ? 10 : 0) + 10 + screenBorder, Mth.ceil((255.0D * client.options.textBackgroundOpacity().get()) * opacity)<<24);
        int alpha = (int) Math.ceil(opacity*255);
        drawContext.text(client.font, fps, textPosX + 3 + screenBorder, posY + 1 + (positionEnabled ? 10 : 0) + screenBorder, 16777215 | ((alpha) << 24));
    }

}
