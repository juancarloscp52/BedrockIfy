package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets.entryList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class BedrockifyEntryListWidget extends ElementListWidget<BedrockifyEntry> {

    private static final Identifier SCROLLER_TEXTURE = Identifier.of("bedrockify", "widgets/scroller");
    private static final Identifier SCROLLER_BACKGROUND_TEXTURE = Identifier.ofVanilla("widget/scroller_background");
    private static final int topPadidng = 2;

    public BedrockifyEntryListWidget(MinecraftClient minecraftClient, int width, int height, int y, int itemHeight, int headerHeight) {
        super(minecraftClient, width, height, y, itemHeight, headerHeight);
    }

    @Override
    protected void renderEntry(DrawContext context, int mouseX, int mouseY, float delta, int index, int x, int y, int entryWidth, int entryHeight) {
        super.renderEntry(context, mouseX, mouseY, delta, index, x, y, entryWidth, this.children().get(index).getHeight() - topPadidng);
    }

    @Override
    public int getRowTop(int index) {
        return this.getY() + topPadidng - (int) this.getScrollY() + getListHeightForEntry(index) + this.headerHeight;
    }

    @Override
    public int getRowLeft() {
        return this.getX() + 4;
    }

    @Override
    public int getRowWidth() {
        return this.width - (this.overflows() ? 15 : 8);
    }

    @Override
    protected int getScrollbarX() {
        return this.getRight() - 8;
    }

    @Override
    protected int getContentsHeightWithPadding() {
        AtomicInteger height = new AtomicInteger(this.headerHeight + topPadidng);
        this.children().forEach(entry -> height.addAndGet(entry.getHeight()));
        return height.get();
    }

    @Override
    public int getRowBottom(int index) {
        return this.getRowTop(index) + this.children().get(index).getHeight();
    }

    private int getListHeightForEntry(int index) {
        int height = 0;
        for (int i = 0; i < index; i++) {
            height += this.children().get(i).getHeight();
        }
        return height;
    }

    @Nullable
    @Override
    protected BedrockifyEntry getEntryAtPosition(double x, double y) {
        int halfWidth = this.getRowWidth() / 2;
        int centerX = this.getX() + this.width / 2;
        int startX = centerX - halfWidth;
        int endX = centerX + halfWidth;
        int currentYPos = MathHelper.floor(y - this.getY()) - this.headerHeight + (int) this.getScrollY() - topPadidng;
        @Nullable BedrockifyEntry entry = getEntryAtYPosition(currentYPos);
        return (x >= startX && x <= endX && currentYPos >= 0 ? entry : null);
    }

    @Nullable
    private BedrockifyEntry getEntryAtYPosition(int y) {
        if (y < 0)
            return null;

        int currentListHeight = 0;
        for (BedrockifyEntry entry : this.children()) {
            currentListHeight += entry.getHeight();
            if (currentListHeight > y)
                return entry;
        }
        return null;
    }

    @Override
    protected int getScrollbarThumbY() {
        return Math.max(this.getY() + topPadidng, (int) this.getScrollY() * (this.height - this.getScrollbarThumbHeight()) / this.getMaxScrollY() + this.getY());
    }

    @Override
    protected void drawScrollbar(DrawContext context) {
        if (this.overflows()) {
            int x = this.getScrollbarX();
            int height = this.getScrollbarThumbHeight();
            int y = this.getScrollbarThumbY();
            context.drawGuiTexture(RenderLayer::getGuiTextured, SCROLLER_BACKGROUND_TEXTURE, x + 1, this.getY() + topPadidng, 3, this.getHeight() - (2 * topPadidng));
            context.drawGuiTexture(RenderLayer::getGuiTextured, SCROLLER_TEXTURE, x, y, 5, height);
        }
    }

    @Override
    protected void ensureVisible(BedrockifyEntry entry) {
        int y = this.getRowTop(this.children().indexOf(entry));
        int yTopOffset = y - this.getY() - entry.getHeight() - 4;
        if (yTopOffset < 0) {
            this.scroll(yTopOffset);
        }

        int yBottomOffset = this.getBottom() - y - (2 * entry.getHeight());
        if (yBottomOffset < 0) {
            this.scroll(-yBottomOffset);
        }
    }

    private void scroll(int amount) {
        this.setScrollY(this.getScrollY() + amount);
    }

    @Override
    protected void centerScrollOn(BedrockifyEntry entry) {
        this.setScrollY(getListHeightForEntry(this.children().indexOf(entry)) + entry.getHeight() / 2 - this.height / 2);
    }

    @Override
    protected void drawMenuListBackground(DrawContext context) {
    }

    @Override
    protected void drawHeaderAndFooterSeparators(DrawContext context) {
    }

}
