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
import net.minecraft.util.ARGB;
import net.minecraft.world.clock.ClockManager;
import net.minecraft.world.timeline.Timelines;

import java.util.Objects;
import java.util.Optional;

public class Overlay {

    private final Minecraft client;
    private final PaperDoll paperDoll;
    public final SavingOverlay savingOverlay;
    private Component fps;
    private final BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;

    private static final int POSITION_TEXT_BG_HEIGHT = 12;
    private static final int FPS_TEXT_BG_HEIGHT = 10;
    private static final int DAYS_PLAYED_TEXT_BG_HEIGHT = 11;
    private static final int DAYS_PLAYED_COUNT_MAX = 89478;

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

    public int getTextsTopOffset() {
        return POSITION_TEXT_BG_HEIGHT +
                this.getFpsTextHeight() +
                DAYS_PLAYED_TEXT_BG_HEIGHT;
    }

    private int getFpsTextHeight() {
        return (this.settings.getFPSHUDoption() == BedrockifyClientSettings.FpsHudOption.UNDER_POSITION) ? FPS_TEXT_BG_HEIGHT : 0;
    }

    /**
     * Renders the text components for the player position and client fps.
     */
    private void renderText(GuiGraphicsExtractor drawContext) {
        fps = Component.translatable("bedrockify.hud.fps").append(String.valueOf(client.getFps()));
        int y = settings.getPositionHUDHeight();
        renderPositionText(drawContext, y);
        if (settings.getFPSHUDoption() == BedrockifyClientSettings.FpsHudOption.UNDER_POSITION) {
            y += POSITION_TEXT_BG_HEIGHT - 2;
            renderFpsText(drawContext, y + 2);
        }
        renderDaysPlayedText(drawContext, y + POSITION_TEXT_BG_HEIGHT);
    }

    private void renderPositionText(GuiGraphicsExtractor drawContext, int y) {
        int screenBorder = settings.overlayIgnoresSafeArea ? 0 : settings.getScreenSafeArea();
        if (!settings.isShowPositionHUDEnabled())
            return;
        BlockPos blockPos = Objects.requireNonNull(this.client.getCameraEntity(), "Camera Entity cannot be null.").blockPosition();
        MutableComponent position = Component.translatable("bedrockify.hud.position").append(Component.literal(" "+ blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ()));
        if (settings.getFPSHUDoption() == BedrockifyClientSettings.FpsHudOption.WITH_POSITION)
            position.append(" ").append(fps);
        int positionWidth = client.font.width(position);
        float opacity = BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
        drawContext.fill(screenBorder, y + screenBorder, positionWidth + 6 + screenBorder, y + POSITION_TEXT_BG_HEIGHT + screenBorder, ARGB.black((float) (client.options.textBackgroundOpacity().get() * opacity)));
        drawContext.text(client.font, position, 3 + screenBorder, y + 3 + screenBorder, ARGB.white(opacity));
    }

    private void renderFpsText(GuiGraphicsExtractor drawContext, int y) {
        int screenBorder = settings.overlayIgnoresSafeArea ? 0 : settings.getScreenSafeArea();
        int fpsCounterWidth = client.font.width(fps);
        float opacity = BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
        drawContext.fill(screenBorder, y + screenBorder, fpsCounterWidth + 6 + screenBorder, y + FPS_TEXT_BG_HEIGHT + screenBorder, ARGB.black((float) (client.options.textBackgroundOpacity().get() * opacity)));
        drawContext.text(client.font, fps, 3 + screenBorder, y + 1 + screenBorder, ARGB.white(opacity));
    }

    private void renderDaysPlayedText(GuiGraphicsExtractor drawContext, int y) {
        if (!settings.isShowDaysPlayed()) {
            return;
        }
        if (this.client.level == null) {
            return;
        }
        final ClockManager clockManager = this.client.level.clockManager();
        final Optional<Component> days = this.client.level.registryAccess().get(Timelines.OVERWORLD_DAY)
                .map(timelineReference -> {
                    final int count = timelineReference.value().getPeriodCount(clockManager);
                    if (count > DAYS_PLAYED_COUNT_MAX) {
                        return Component.translatable("bedrockify.hud.tooManyToCount");
                    } else {
                        return Component.literal(String.valueOf(count));
                    }
                });
        if (days.isEmpty()) {
            return;
        }
        final int screenBorder = settings.overlayIgnoresSafeArea ? 0 : settings.getScreenSafeArea();
        final Component daysPlayed = Component.translatable("bedrockify.hud.daysPlayed", days.get());
        final float opacity = BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
        drawContext.fill(screenBorder, y + screenBorder, this.client.font.width(daysPlayed) + 6 + screenBorder, y + DAYS_PLAYED_TEXT_BG_HEIGHT + screenBorder, ARGB.black((float) (client.options.textBackgroundOpacity().get() * opacity)));
        drawContext.text(client.font, daysPlayed, 3 + screenBorder, y + 2 + screenBorder, ARGB.white(opacity));
    }
}
