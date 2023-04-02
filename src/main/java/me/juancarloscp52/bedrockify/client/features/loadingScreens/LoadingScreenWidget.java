package me.juancarloscp52.bedrockify.client.features.loadingScreens;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Random;


public class LoadingScreenWidget extends DrawableHelper {

    private static LoadingScreenWidget instance = null;
    private static final int TIPS_NUM = 62;
    private final Identifier WIDGET_TEXTURE = new Identifier("bedrockify", "textures/gui/bedrockify_widgets.png");
    private Text tip;
    private static final List<Integer> EXCLUDED_TIPS = Lists.asList(15,new Integer[]{23,28,29,32,33,34,35,62});
    private long lastTipUpdate = 0;
    private final ExternalLoadingTips externalLoadingTips;
    private final LogoDrawer logoDrawer;

    private LoadingScreenWidget() {
        externalLoadingTips = ExternalLoadingTips.loadSettings();
        externalLoadingTips.saveSettings();
        logoDrawer = new LogoDrawer(false);
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

            int random = new Random().nextInt(TIPS_NUM+1)+1;
            if((random>TIPS_NUM || externalLoadingTips.alwaysExternalTips)&& externalLoadingTips.externalLoadingTips.length>0){
                random = new Random().nextInt(externalLoadingTips.externalLoadingTips.length);
                tip = Text.literal(externalLoadingTips.externalLoadingTips[random]);
            }else{
                if(EXCLUDED_TIPS.contains(random))
                    return getTip();
                tip = Text.translatable("bedrockify.loadingTips." + random);//Text.literal(tipTextSupplier.getRandomTip());
            }
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
        MinecraftClient client = MinecraftClient.getInstance();

        logoDrawer.draw(matrices,client.getWindow().getScaledWidth(),1,(height/2) - (89 / 2));
        renderLoadingWidget(matrices, width, height);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        textRenderer.draw(matrices, title, width - textRenderer.getWidth(title) / 2, height - 9 / 2 - 32, 76 + (76 << 8) + (76 << 16));
        renderTextBody(matrices, width, height, message, textRenderer);

        if (progress >= 0) {
            renderLoadingBar(matrices, width, height, progress);
        }
    }

    private void renderLoadingWidget(MatrixStack matrices, int x, int y) {
        RenderSystem.setShaderTexture(0,WIDGET_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        DrawableHelper.drawTexture(matrices, x - 256 / 2, y - 89 / 2, 0, 0, 256, 89);
    }


    private void renderTextBody(MatrixStack matrices, int x, int y, Text message, TextRenderer textRenderer) {
        if (message == null)
            message = getTip();
        List<OrderedText> text = textRenderer.wrapLines(message, 230);
        int maxLineWidth = getMaxLineWidth(textRenderer, text);
        for (int i = 0; i < 4 && i < text.size(); i++) {
            textRenderer.draw(matrices, text.get(i), x - maxLineWidth / 2, y - 15 + (i * 9), 16777215);
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


    private void renderLoadingBar(MatrixStack matrices, int x, int y, int progress) {
        RenderSystem.setShaderTexture(0,WIDGET_TEXTURE);
        int barProgress = (int) ((MathHelper.clamp(progress,0,100)/100.0f) * 223.0f);
        DrawableHelper.drawTexture(matrices, x - 111, y + 26, 0, 89, 222, 5);
        if (barProgress > 0)
            DrawableHelper.drawTexture(matrices, x - 111, y + 26, 0, 94, barProgress, 5);
    }

}
