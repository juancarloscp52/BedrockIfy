package me.juancarloscp52.bedrockify.client.gui;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Random;


public class LoadingScreenWidget extends DrawableHelper {

    private static LoadingScreenWidget instance = null;
    private final Identifier WIDGET_TEXTURE = new Identifier("bedrockify", "textures/gui/bedrockify_widgets.png");
    private final Identifier MINECRAFT_TITLE_TEXTURE = new Identifier("textures/gui/title/minecraft.png");
    private final Identifier EDITION_TITLE_TEXTURE = new Identifier("textures/gui/title/edition.png");
    private Text tip;
    private long lastTipUpdate = 0;

    private LoadingScreenWidget() {
    }

    public static LoadingScreenWidget getInstance() {
        if (instance == null) {
            instance = new LoadingScreenWidget();
        }
        return instance;
    }

    /**
     * Retrieve a loading screen tip. This tip will change every 6 seconds.
     * @return Text with the current tip.
     */
    private Text getTip() {
        if (tip == null || System.currentTimeMillis() - lastTipUpdate > 6000) {
            tip = new TranslatableText("bedrockify.loadingTips." + new Random().nextInt(111));//new LiteralText(tipTextSupplier.getRandomTip());
            lastTipUpdate = System.currentTimeMillis();
        }
        return tip;
    }

    /**
     * Renders the bedrockify loading screen.
     * @param matrices Current MatrixStack.
     * @param width window width
     * @param height window height
     * @param title Title of the loading screen.
     * @param message Message of the loading screen. Set to null to use a random tip.
     * @param progress Loading screen progress. Set to -1 is the screen has no progress bar.
     */
    public void render(MatrixStack matrices, int width, int height, Text title, Text message, int progress) {
        //If the background cube map is disabled for other screens, manually render the cube map for the loading screen.
        if(!Bedrockify.getInstance().settings.isCubemapBackgroundEnabled())
            BedrockifyRotatingCubeMapRenderer.getInstance().render();

        MinecraftClient client = MinecraftClient.getInstance();

        renderLogo(matrices, width, height, client);
        renderLoadingWidget(matrices, width, height, client);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        textRenderer.draw(matrices, title, width - textRenderer.getWidth(title) / 2.0f, height - 9.0f / 2.0f - 32, 76 + (76 << 8) + (76 << 16));
        renderTextBody(matrices, width, height, message, textRenderer);

        if (progress >= 0) {
            renderLoadingBar(matrices, client, width, height, progress);
        }
    }

    private void renderLoadingWidget(MatrixStack matrices, int x, int y, MinecraftClient client) {
        client.getTextureManager().bindTexture(WIDGET_TEXTURE);
        this.drawTexture(matrices, x - 256 / 2, y - 89 / 2, 0, 0, 256, 89);
    }

    private void renderLogo(MatrixStack matrices, int x, int y, MinecraftClient client) {
        client.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURE);
        this.method_29343(x - 137, (y / 2) - (89 / 2), (integer, integer2) -> {
            this.drawTexture(matrices, integer, integer2, 0, 0, 155, 44);
            this.drawTexture(matrices, integer + 155, integer2, 0, 45, 155, 44);
        });
        client.getTextureManager().bindTexture(EDITION_TITLE_TEXTURE);
        drawTexture(matrices, x - 137 + 88, (y / 2) - (89 / 2) + 37, 0.0F, 0.0F, 98, 14, 128, 16);
    }

    private void renderTextBody(MatrixStack matrices, int x, int y, Text message, TextRenderer textRenderer) {
        if (message == null)
            message = getTip();
        List<OrderedText> text = textRenderer.wrapLines(message, 230);
        int maxLineWidth = getMaxLineWidth(textRenderer, text);
        for (int i = 0; i < 4 && i < text.size(); i++) {
            textRenderer.draw(matrices, text.get(i), x - maxLineWidth / 2f, y - 15 + (i * 9), 16777215);
        }

    }

    private int getMaxLineWidth(TextRenderer textRenderer, List<OrderedText> text) {
        int maxLineWidth = 0;
        for (int i = 0; i < 4 && i < text.size(); i++) {
            int lineWidth = textRenderer.getWidth(text.get(i));
            if (lineWidth > maxLineWidth)
                maxLineWidth = lineWidth;
        }
        return maxLineWidth;
    }


    private void renderLoadingBar(MatrixStack matrices, MinecraftClient client, int x, int y, int progress) {
        client.getTextureManager().bindTexture(WIDGET_TEXTURE);
        int barProgress = (int) ((MathHelper.clamp(progress,0,100)/100.0f) * 223.0f);
        this.drawTexture(matrices, x - 111, y + 26, 0, 89, 222, 5);
        if (barProgress > 0)
            this.drawTexture(matrices, x - 111, y + 26, 0, 94, barProgress, 5);
    }

}
