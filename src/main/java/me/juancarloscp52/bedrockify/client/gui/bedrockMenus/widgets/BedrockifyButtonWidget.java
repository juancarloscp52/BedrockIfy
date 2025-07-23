package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.BedrockifyButtonTextures;
import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.BedrockifyIconButtonTextures;
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

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BedrockifyButtonWidget extends BedrockifyPressableWidget {

    public static final int DEFAULT_WIDTH_SMALL = 120;
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 30;
    public static final int TEXT_MARGIN = 11;
    public static final int TEXT_MARGIN_WITH_ICON = 37;
    protected static final BedrockifyButtonWidget.NarrationSupplier DEFAULT_NARRATION_SUPPLIER = Supplier::get;
    protected final Consumer<BedrockifyButtonWidget> onPress;
    protected final BedrockifyButtonWidget.NarrationSupplier narrationSupplier;
    @Nullable
    private BedrockifyIconButtonTextures icon = null; // ICON TEXT MARGIN 37 from x origin

    protected BedrockifyButtonWidget(int x, int y, int width, int height, Text message, @Nullable Text title, @Nullable BedrockifyIconButtonTextures icon, Consumer<BedrockifyButtonWidget> onPress, BedrockifyButtonWidget.NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, title, new BedrockifyButtonTextures(
                Identifier.of("bedrockify", "widgets/button"),
                Identifier.of("bedrockify", "widgets/button_highlighted"),
                Identifier.of("bedrockify", "widgets/button_pressed"),
                Identifier.of("bedrockify", "widgets/button_highlighted_pressed"),
                Identifier.of("bedrockify", "widgets/button_disabled")
        ));
        this.icon = icon;
        this.onPress = onPress;
        this.narrationSupplier = narrationSupplier;
    }

    public static BedrockifyButtonWidget.Builder builder(Text message, Consumer<BedrockifyButtonWidget> onPress) {
        return new BedrockifyButtonWidget.Builder(message, onPress);
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
        if (icon != null) {
            context.drawGuiTexture(
                    RenderLayer::getGuiTextured,
                    icon.get(this.isHovered() && this.active),
                    this.getX() + 8,
                    this.getButtonY() + ((this.getButtonHeight() - icon.height()) / 2),
                    15,
                    12,
                    ColorHelper.getWhite(this.alpha)
            );
        }
    }

    @Override
    public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        int xMargin = icon == null ? TEXT_MARGIN : TEXT_MARGIN_WITH_ICON;
        GuiUtils.drawScrollableText(context, textRenderer, this.getMessage(), this.getX() + xMargin, this.getButtonY(), this.getX() + this.getWidth() - 5, this.getButtonY() + this.getButtonHeight(), color, GuiUtils.VerticalLocation.CENTERED);
    }

    @Override
    public void onPress() {
        this.onPress.accept(this);
    }

    @Environment(EnvType.CLIENT)
    public interface NarrationSupplier {
        MutableText createNarrationMessage(Supplier<MutableText> textSupplier);
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Text message;
        @Nullable
        private Text title;
        private final Consumer<BedrockifyButtonWidget> onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = DEFAULT_WIDTH;
        private int height = DEFAULT_HEIGHT;
        private BedrockifyButtonWidget.NarrationSupplier narrationSupplier = BedrockifyButtonWidget.DEFAULT_NARRATION_SUPPLIER;
        @Nullable
        private BedrockifyIconButtonTextures icon = null;

        public Builder(Text message, Consumer<BedrockifyButtonWidget> onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public BedrockifyButtonWidget.Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public BedrockifyButtonWidget.Builder width(int width) {
            this.width = width;
            return this;
        }

        public BedrockifyButtonWidget.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public BedrockifyButtonWidget.Builder dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        public BedrockifyButtonWidget.Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public BedrockifyButtonWidget.Builder title(@Nullable Text title) {
            this.title = title;
            return this;
        }

        public BedrockifyButtonWidget.Builder narrationSupplier(BedrockifyButtonWidget.NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public BedrockifyButtonWidget.Builder icon(@Nullable BedrockifyIconButtonTextures icon) {
            this.icon = icon;
            return this;
        }

        public BedrockifyButtonWidget build() {
            BedrockifyButtonWidget BedrockifyButtonWidget = new BedrockifyButtonWidget(this.x, this.y, this.width, this.height, this.message, this.title, icon, this.onPress, this.narrationSupplier);
            BedrockifyButtonWidget.setTooltip(this.tooltip);
            return BedrockifyButtonWidget;
        }
    }
}
