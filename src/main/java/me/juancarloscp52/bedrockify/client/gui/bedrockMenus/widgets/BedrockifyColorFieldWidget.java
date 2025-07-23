package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

import java.util.HexFormat;
import java.util.function.Consumer;

public class BedrockifyColorFieldWidget extends BedrockifyTextFieldWidget {
    private static final int PADDING = 10;
    private int color = ColorHelper.getWhite(255);
    private boolean alpha = true;
    protected final Consumer<BedrockifyColorFieldWidget> onPress;

    public BedrockifyColorFieldWidget(TextRenderer textRenderer, int x, int y, int width, Text message,boolean alpha, int color, Consumer<BedrockifyColorFieldWidget> onPress) {
        super(textRenderer, x, y, width, message, Text.literal("#"));
        this.setLeftPaddingX(getFieldHeight() - 4 + PADDING);
        setTextPredicate(s -> {
            var result = s.matches("#\\p{XDigit}*");
            return result;
        });
        this.setAlpha(alpha);
        this.color=color;
        this.setText(this.getHexColor(color));
        this.onPress = onPress;
    }

    private void setAlpha(boolean alpha){
        this.alpha = alpha;
        this.setMaxLength(1 + colorSize());
    }

    private String getHexColor(int color){
        String hexString = HexFormat.of().withUpperCase().toHexDigits(color, colorSize());
        return "#" + hexString;
    }

    private int colorSize(){
        return this.alpha ? 8 : 6;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if (this.isVisible()) {
            this.hovered = context.scissorContains(mouseX, mouseY) && mouseOver(mouseX, mouseY);
            if (this.getMessage()!=null)
                GuiUtils.drawScrollableText(context, MinecraftClient.getInstance().textRenderer, this.getMessage(), this.getX(), this.getY(), this.getX() + width, this.getY() + MESSAGE_TEXT_HEIGHT, ColorHelper.getWhite(1f), GuiUtils.VerticalLocation.BOTTOM);
            renderTextField(context, this.getX(), this.getFieldY(), this.getWidth(), getFieldHeight());
            context.fill(RenderLayer.getGui(), this.getX()+2, this.getFieldY()+2, this.getX() + 2 + this.getLeftPaddingX()-PADDING, this.getFieldY()+getFieldHeight()-2, ColorHelper.getWhite(255));
            context.fill(RenderLayer.getGui(), this.getX()+2, this.getFieldY()+2, this.getX() + 2 + this.getLeftPaddingX()-PADDING, this.getFieldY()+getFieldHeight()-2, this.color);
        }
    }

    @Override
    protected void onChanged(String newText) {
        if(newText.length()==(1+colorSize())){
            int color = (int) Long.parseLong(newText.substring(1), 16);
            if(!this.alpha)
                color = ColorHelper.fullAlpha(color);
            this.color = color;
        }
        this.onPress.accept(this);
    }
}
