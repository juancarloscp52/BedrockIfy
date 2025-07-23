package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BedrockifyTextFieldWidget extends TextFieldWidget implements BedrockifyDrawableWidget {

    protected static final int MESSAGE_TEXT_HEIGHT = 15;
    private static final ButtonTextures TEXTURES = new ButtonTextures(
            Identifier.of("bedrockify","widgets/text_field"), Identifier.of("bedrockify", "widgets/text_field_highlighted")
    );
    private int leftPaddingX = 0;

    public BedrockifyTextFieldWidget(TextRenderer textRenderer, int width, Text message, Text text) {
        super(textRenderer, width,  message==null ? 30: 30+MESSAGE_TEXT_HEIGHT, text);
        this.setMessage(message);
    }

    public BedrockifyTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, @Nullable Text message, Text text) {
        super(textRenderer, x, y, width, message==null ? 30: 30+MESSAGE_TEXT_HEIGHT, text);
        this.setMessage(message);
    }

    @Override
    public int getInnerWidth() {
        return super.getInnerWidth() - leftPaddingX;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        int correctedMouseX = MathHelper.floor(mouseX) - this.getX() - leftPaddingX;
        if (this.drawsBackground()) {
            correctedMouseX -= 4;
        }

        String string = this.textRenderer.trimToWidth(this.getText().substring(this.firstCharacterIndex), this.getInnerWidth());
        this.setCursor(this.textRenderer.trimToWidth(string, correctedMouseX).length() + this.firstCharacterIndex, Screen.hasShiftDown());
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if (this.isVisible()) {
            this.hovered = context.scissorContains(mouseX, mouseY) && mouseOver(mouseX, mouseY);
            if (this.getMessage()!=null)
                GuiUtils.drawScrollableText(context, MinecraftClient.getInstance().textRenderer, this.getMessage(), this.getX(), this.getY(), this.getX() + width, this.getY() + MESSAGE_TEXT_HEIGHT, ColorHelper.getWhite(1f), GuiUtils.VerticalLocation.BOTTOM);
            renderTextField(context, this.getX(), this.getY() + MESSAGE_TEXT_HEIGHT, this.getWidth(), getFieldHeight());
        }
    }
    
    protected void renderTextField(DrawContext context, int x, int y, int width, int height){
        if (this.drawsBackground()) {
            Identifier identifier = TEXTURES.get(this.isNarratable(), this.isFocused() || this.isHovered());
            context.drawGuiTexture(RenderLayer::getGuiTextured, identifier, x, y, width, height);
        }

        int textColor = this.editable ? ColorHelper.getWhite(255) : 7368816;
        int cursorPosition = this.selectionStart - this.firstCharacterIndex;
        String inputText = this.textRenderer.trimToWidth(this.getText().substring(this.firstCharacterIndex), this.getInnerWidth());
        boolean cursorWithinBounds = cursorPosition >= 0 && cursorPosition <= inputText.length();
        boolean showCursor = this.isFocused() && (Util.getMeasuringTimeMs() - this.lastSwitchFocusTime) / 300L % 2L == 0L && cursorWithinBounds;
        int xTextStart = (this.drawsBackground() ? x + 4 : x) + this.leftPaddingX;
        int yTextStart = this.drawsBackground() ? y + (height - 8) / 2 : y;
        int inputTextEndX = xTextStart;
        int clampedCursorPosition = MathHelper.clamp(this.selectionEnd - this.firstCharacterIndex, 0, inputText.length());
        if (!inputText.isEmpty()) {
            String textBeforeCursor = cursorWithinBounds ? inputText.substring(0, cursorPosition) : inputText;
            inputTextEndX = context.drawText(this.textRenderer, this.renderTextProvider.apply(textBeforeCursor, this.firstCharacterIndex), xTextStart, yTextStart, textColor, false);
        }

        int cursorStartX = inputTextEndX;
        if (!cursorWithinBounds) {
            cursorStartX = cursorPosition > 0 ? xTextStart + width : xTextStart;
        } else {
            cursorStartX--;
        }

        if (!inputText.isEmpty() && cursorWithinBounds && cursorPosition < inputText.length()) {
            context.drawText(this.textRenderer, this.renderTextProvider.apply(inputText.substring(cursorPosition), this.selectionStart), inputTextEndX, yTextStart, textColor, false);
        }

        if (this.placeholder != null && inputText.isEmpty() && !this.isFocused()) {
            textColor = this.editable ? 14737632 : 7368816;
            context.drawText(this.textRenderer, this.placeholder, inputTextEndX, yTextStart, textColor, false);
        }

        if (showCursor) {
            context.fill(RenderLayer.getGuiOverlay(), cursorStartX, yTextStart - 1, cursorStartX + 1, yTextStart + 1 + 2, ColorHelper.getArgb(0,255,0));
            context.fill(RenderLayer.getGuiOverlay(), cursorStartX, yTextStart + 1 + 4, cursorStartX + 1, yTextStart + 9, ColorHelper.getArgb(0,255,0));
        }

        if (clampedCursorPosition != cursorPosition) {
            int p = xTextStart + this.textRenderer.getWidth(inputText.substring(0, clampedCursorPosition));
            this.drawSelectionHighlight(context, cursorStartX, yTextStart - 1, p - 1, yTextStart + 1 + 9);
        }
    }

    public void setLeftPaddingX(int leftPaddingX) {
        this.leftPaddingX = leftPaddingX;
    }

    public int getLeftPaddingX() {
        return leftPaddingX;
    }

    protected int getFieldY(){
        return this.getMessage()==null ? this.getY() : this.getY()+MESSAGE_TEXT_HEIGHT;
    }

    protected int getFieldHeight(){
        return this.getMessage()==null ? this.getHeight() :this.getHeight()-MESSAGE_TEXT_HEIGHT;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.active && this.visible && mouseOver(mouseX, mouseY);
    }

    protected boolean mouseOver(double mouseX, double mouseY){
        return mouseX >= this.getX() && mouseY >= this.getFieldY() && mouseX < this.getRight() && mouseY < this.getBottom();
    }
}
