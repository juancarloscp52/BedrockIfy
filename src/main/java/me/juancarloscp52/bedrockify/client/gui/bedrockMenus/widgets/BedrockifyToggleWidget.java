package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.BedrockifyButtonTextures;
import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.GuiUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BedrockifyToggleWidget extends BedrockifyPressableWidget {

    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 16;
    public static final int TEXT_MARGIN = 35;

    @Nullable
    protected static final BedrockifyToggleWidget.NarrationSupplier DEFAULT_NARRATION_SUPPLIER = Supplier::get;
    protected final BedrockifyToggleWidget.PressAction onPress;
    protected final BedrockifyToggleWidget.NarrationSupplier narrationSupplier;

    protected BedrockifyToggleWidget(int x, int y, int width, int height, Text message, BedrockifyToggleWidget.PressAction onPress, BedrockifyToggleWidget.NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, null, new BedrockifyButtonTextures(
                Identifier.of("bedrockify", "widgets/toggle_off"),
                Identifier.of("bedrockify", "widgets/toggle_off_highlighted"),
                Identifier.of("bedrockify", "widgets/toggle_on"),
                Identifier.of("bedrockify", "widgets/toggle_on_highlighted"),
                Identifier.of("bedrockify", "widgets/toggle_off_disabled")
        ));
        this.onPress = onPress;
        this.narrationSupplier = narrationSupplier;
    }

    public static BedrockifyToggleWidget.Builder builder(Text message, BedrockifyToggleWidget.PressAction onPress) {
        return new BedrockifyToggleWidget.Builder(message, onPress);
    }

    @Override
    protected void renderTexture(DrawContext context) {
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                this.getTextures().get(this.active, this.isHovered(), this.isPressed()),
                this.getX(),
                this.getY() + (this.getHeight() / 2) - 8,
                30,
                16,
                ColorHelper.getWhite(this.alpha)
        );
    }

    @Override
    public void onPress() {
        this.setPressed(!this.isPressed());
        this.onPress.onPress(this);
    }

    @Override
    protected MutableText getNarrationMessage() {
        return this.narrationSupplier.createNarrationMessage(super::getNarrationMessage);
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        color = this.active ? ColorHelper.getWhite(1.0f) : ColorHelper.getArgb(152, 152, 152);
        GuiUtils.drawScrollableText(context, textRenderer, this.getMessage(), this.getX() + TEXT_MARGIN, this.getY(), this.getX() + this.getWidth() - 5, this.getY() + this.getHeight(), color, GuiUtils.VerticalLocation.CENTERED);
    }

    @Environment(EnvType.CLIENT)
    public interface NarrationSupplier {
        MutableText createNarrationMessage(Supplier<MutableText> textSupplier);
    }

    @Environment(EnvType.CLIENT)
    public interface PressAction {
        void onPress(BedrockifyToggleWidget button);
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Text message;
        private final BedrockifyToggleWidget.PressAction onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = DEFAULT_WIDTH;
        private int height = DEFAULT_HEIGHT;
        private BedrockifyToggleWidget.NarrationSupplier narrationSupplier = BedrockifyToggleWidget.DEFAULT_NARRATION_SUPPLIER;

        public Builder(Text message, BedrockifyToggleWidget.PressAction onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public BedrockifyToggleWidget.Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public BedrockifyToggleWidget.Builder width(int width) {
            this.width = width;
            return this;
        }

        public BedrockifyToggleWidget.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public BedrockifyToggleWidget.Builder dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        public BedrockifyToggleWidget.Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public BedrockifyToggleWidget.Builder narrationSupplier(BedrockifyToggleWidget.NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public BedrockifyToggleWidget build() {
            BedrockifyToggleWidget BedrockifyButtonWidget = new BedrockifyToggleWidget(this.x, this.y, this.width, this.height, this.message, this.onPress, this.narrationSupplier);
            BedrockifyButtonWidget.setTooltip(this.tooltip);
            return BedrockifyButtonWidget;
        }
    }
}
