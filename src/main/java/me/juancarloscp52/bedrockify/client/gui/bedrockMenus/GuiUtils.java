package me.juancarloscp52.bedrockify.client.gui.bedrockMenus;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class GuiUtils {

    public static void drawScrollableText(
            DrawContext context, TextRenderer textRenderer, Text text, int startX, int startY, int endX, int endY, int color, VerticalLocation verticalLocation
    ) {
        int textWidth = textRenderer.getWidth(text);

        int centeredYOrigin = switch (verticalLocation) {
            case TOP -> startY + 2;
            case BOTTOM -> endY - 9 - 1;
            case CENTERED -> (startY + endY - 9) / 2 + 1;
        };

        int textBoxWidth = endX - startX;
        if (textWidth > textBoxWidth) {
            int remainingText = textWidth - textBoxWidth;
            double time = Util.getMeasuringTimeMs() / 1000.0;
            double maxRemaining = Math.max(remainingText * 0.5, 3.0);
            double movementDelta = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * time / maxRemaining)) / 2.0 + 0.5;
            double positionX = MathHelper.lerp(movementDelta, 0.0, remainingText);
            context.enableScissor(startX, startY, endX, endY);
            context.drawText(textRenderer, text, startX - (int) positionX, centeredYOrigin, color, false);
            context.disableScissor();
        } else {
            context.drawText(textRenderer, text, startX, centeredYOrigin, color, false);
        }
    }

    public enum VerticalLocation {
        TOP, CENTERED, BOTTOM
    }

}
