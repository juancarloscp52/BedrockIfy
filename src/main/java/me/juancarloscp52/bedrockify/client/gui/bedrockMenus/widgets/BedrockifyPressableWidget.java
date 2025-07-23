package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.BedrockifyButtonTextures;
import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public abstract class BedrockifyPressableWidget extends PressableWidget implements BedrockifyDrawableWidget {

    protected static final int MESSAGE_TEXT_HEIGHT = 15;
    private final BedrockifyButtonTextures textures;
    private boolean pressed = false;
    protected @Nullable Text title;

    public BedrockifyPressableWidget(int x, int y, int width, int height, Text text, @Nullable Text title, BedrockifyButtonTextures textures) {
        super(x, y, width, title==null ? height: height+MESSAGE_TEXT_HEIGHT, text);
        this.title = title;
        this.textures = textures;
    }

    public abstract void onPress();



    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        this.hovered = context.scissorContains(mouseX, mouseY) && mouseOver(mouseX, mouseY);
        if (this.title!=null)
            GuiUtils.drawScrollableText(context, MinecraftClient.getInstance().textRenderer, this.title, this.getX(), this.getY(), this.getX() + width, this.getY() + MESSAGE_TEXT_HEIGHT, ColorHelper.getWhite(1f), GuiUtils.VerticalLocation.BOTTOM);
        renderTexture(context);
        this.drawMessage(context, minecraftClient.textRenderer, getTextColor(this.active, this.isHovered(), this.pressed) | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }

    protected void renderTexture(DrawContext context) {
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                textures.get(this.active, this.isHovered(), this.pressed),
                this.getX(),
                this.getButtonY(),
                this.getWidth(),
                this.getButtonHeight(),
                ColorHelper.getWhite(this.alpha)
        );
    }

    private int getTextColor(boolean active, boolean highlighted, boolean pressed) {
        int alpha = ColorHelper.channelFromFloat(this.alpha);
        if (active) {
            if (highlighted) {
                return ColorHelper.getWhite(alpha);
            } else if (pressed) {
                return ColorHelper.getArgb(alpha, 31, 31, 31);
            }
        }
        return ColorHelper.getArgb(alpha, 76, 76, 76);
    }

    public boolean isPressed() {
        return this.pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    protected BedrockifyButtonTextures getTextures() {
        return textures;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.onPress();
    }

//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        if (!this.active || !this.visible) {
//            return false;
//        } else if (KeyCodes.isToggle(keyCode)) {
//            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
//            this.onPress();
//            return true;
//        } else {
//            return false;
//        }
//    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.active && this.visible && mouseOver(mouseX, mouseY);
    }

    private boolean mouseOver(double mouseX, double mouseY){
        return mouseX >= this.getX() && mouseY >= this.getButtonY() && mouseX < this.getRight() && mouseY < this.getBottom();
    }

    protected int getButtonY(){
        return this.title==null ? this.getY() : this.getY()+MESSAGE_TEXT_HEIGHT;
    }

    protected int getButtonHeight(){
        return this.title==null ? this.getHeight() :this.getHeight()-MESSAGE_TEXT_HEIGHT;
    }


}
