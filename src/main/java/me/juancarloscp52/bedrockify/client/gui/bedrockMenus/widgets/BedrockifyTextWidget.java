package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

public class BedrockifyTextWidget implements BedrockifyDrawableWidget, Drawable, Widget {

    private final Text message;
    private int x, y, width, height;
    private final GuiUtils.VerticalLocation verticalLocation;

    public BedrockifyTextWidget(Text message) {
        this(message, GuiUtils.VerticalLocation.CENTERED);
    }

    public BedrockifyTextWidget(Text message, GuiUtils.VerticalLocation verticalLocation) {
        this(message, verticalLocation, 20);
    }

    public BedrockifyTextWidget(Text message, GuiUtils.VerticalLocation verticalLocation, int height) {
        this.message = message;
        this.verticalLocation = verticalLocation;
        this.height = height;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        GuiUtils.drawScrollableText(context, MinecraftClient.getInstance().textRenderer, message, x, y, x + width, y + height, ColorHelper.getWhite(1f), verticalLocation);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
