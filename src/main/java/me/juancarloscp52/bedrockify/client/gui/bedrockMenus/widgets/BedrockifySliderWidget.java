package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.BedrockifyButtonTextures;
import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public class BedrockifySliderWidget extends SliderWidget implements BedrockifyDrawableWidget {

    private static final BedrockifyButtonTextures HANDLE_TEXTURE = new BedrockifyButtonTextures(
            Identifier.of("bedrockify", "widgets/slider_button"),
            Identifier.of("bedrockify", "widgets/slider_button_highlighted"),
            Identifier.of("bedrockify", "widgets/slider_button_disabled")
    );
    private final int ySliderOffset = 9 + 7;
    protected final Consumer<BedrockifySliderWidget> onPress;

    public BedrockifySliderWidget(int x, int y, int width, Text text, double value, Consumer<BedrockifySliderWidget> onPress) {
        super(x, y, width, 30, text, value);
        this.onPress = onPress;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.hovered = context.scissorContains(mouseX, mouseY) && mouseOver(mouseX, mouseY);
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        int sliderY = this.getY() + ySliderOffset;
        int xPadding = 5;
        int sliderX = this.getX() + xPadding;

        context.fill(RenderLayer.getGui(), sliderX, sliderY, this.getX() + this.getWidth() - xPadding, sliderY + 10, getBorderColor(this.isHovered() && this.active));
        context.fill(RenderLayer.getGui(), sliderX + 1, sliderY + 1, this.getX() + this.getWidth() - xPadding - 1, sliderY + 10 - 1, getFillColor(this.isHovered() && this.active));
        context.fill(RenderLayer.getGui(), sliderX + 1, sliderY + 1, sliderX + 1 + MathHelper.floor((this.getWidth() - 2 - (2 * xPadding)) * this.value), sliderY + 10 - 1, getProgressFillColor(this.isHovered() && this.active));

        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                HANDLE_TEXTURE.get(this.active, this.isHovered(), false),
                this.getX() + (int) (this.value * (this.width - 10)),
                this.getY() + ySliderOffset - 3,
                10,
                16,
                ColorHelper.getWhite(this.alpha)
        );

        int color = this.active ? ColorHelper.getWhite(1.0f) : ColorHelper.getArgb(152, 152, 152);
        GuiUtils.drawScrollableText(context, minecraftClient.textRenderer, this.getMessage(), this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), color, GuiUtils.VerticalLocation.TOP);
    }

    private int getBorderColor(boolean highlighted) {
        return highlighted ? ColorHelper.getWhite(1) : ColorHelper.getArgb(0, 0, 0);
    }

    private int getFillColor(boolean highlighted) {
        return highlighted ? ColorHelper.getArgb(3, 115, 0) : ColorHelper.getArgb(64, 64, 64);
    }

    private int getProgressFillColor(boolean highlighted) {
        return highlighted ? ColorHelper.getArgb(78, 136, 54) : ColorHelper.getArgb(128, 128, 128);
    }

    @Override
    protected void updateMessage() {
        System.out.println("VALUE " + value);
    }

    @Override
    protected void applyValue() {
        this.onPress.accept(this);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.active && this.visible && mouseOver(mouseX, mouseY);
    }

    private boolean mouseOver(double mouseX, double mouseY){
        return mouseX >= this.getX() && mouseY >= (this.getY() + ySliderOffset - 3) && mouseX < this.getRight() && mouseY < this.getBottom();
    }

}
