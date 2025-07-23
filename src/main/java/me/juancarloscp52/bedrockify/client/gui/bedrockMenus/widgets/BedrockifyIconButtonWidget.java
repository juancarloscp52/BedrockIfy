package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.BedrockifyButtonTextures;
import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.BedrockifyIconButtonTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

public class BedrockifyIconButtonWidget extends BedrockifyPressableWidget {

    public static final int DEFAULT_WIDTH = 10;
    public static final int DEFAULT_HEIGHT = 10;
    protected static final BedrockifyIconButtonWidget.NarrationSupplier DEFAULT_NARRATION_SUPPLIER = Supplier::get;
    protected final Consumer<BedrockifyIconButtonWidget> onPress;
    protected final BedrockifyIconButtonWidget.NarrationSupplier narrationSupplier;
    private BedrockifyIconButtonTextures icon = null;

    protected BedrockifyIconButtonWidget(int x, int y, int width, int height, Text message, BedrockifyIconButtonTextures icon, Consumer<BedrockifyIconButtonWidget> onPress, BedrockifyIconButtonWidget.NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, null, new BedrockifyButtonTextures(
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

    public static BedrockifyIconButtonWidget.Builder builder(Text message, Consumer<BedrockifyIconButtonWidget> onPress, BedrockifyIconButtonTextures icon) {
        return new BedrockifyIconButtonWidget.Builder(message, onPress, icon);
    }

    @Override
    public void onPress() {
        this.onPress.accept(this);
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
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                icon.get(this.isHovered() && this.active),
                this.getX() + ((this.width - icon.width()) / 2),
                this.getY() + ((this.height - icon.height()) / 2),
                icon.width(),
                icon.height(),
                ColorHelper.getWhite(this.alpha)
        );
    }

    @Environment(EnvType.CLIENT)
    public interface NarrationSupplier {
        MutableText createNarrationMessage(Supplier<MutableText> textSupplier);
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Text message;
        private final Consumer<BedrockifyIconButtonWidget> onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = DEFAULT_WIDTH;
        private int height = DEFAULT_HEIGHT;
        private BedrockifyIconButtonWidget.NarrationSupplier narrationSupplier = BedrockifyIconButtonWidget.DEFAULT_NARRATION_SUPPLIER;
        private BedrockifyIconButtonTextures icon;

        public Builder(Text message, Consumer<BedrockifyIconButtonWidget> onPress, BedrockifyIconButtonTextures icon) {
            this.message = message;
            this.onPress = onPress;
            this.icon = icon;
        }

        public BedrockifyIconButtonWidget.Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public BedrockifyIconButtonWidget.Builder width(int width) {
            this.width = width;
            return this;
        }

        public BedrockifyIconButtonWidget.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public BedrockifyIconButtonWidget.Builder dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        public BedrockifyIconButtonWidget.Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public BedrockifyIconButtonWidget.Builder narrationSupplier(BedrockifyIconButtonWidget.NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public BedrockifyIconButtonWidget.Builder icon(BedrockifyIconButtonTextures icon) {
            this.icon = icon;
            return this;
        }

        public BedrockifyIconButtonWidget build() {
            BedrockifyIconButtonWidget BedrockifyButtonWidget = new BedrockifyIconButtonWidget(this.x, this.y, this.width, this.height, this.message, icon, this.onPress, this.narrationSupplier);
            BedrockifyButtonWidget.setTooltip(this.tooltip);
            return BedrockifyButtonWidget;
        }
    }
}
