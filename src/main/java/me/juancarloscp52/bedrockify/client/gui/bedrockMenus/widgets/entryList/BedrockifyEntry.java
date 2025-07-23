package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets.entryList;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets.BedrockifyDrawableWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ElementListWidget;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BedrockifyEntry extends ElementListWidget.Entry<BedrockifyEntry> {
    private final int height;
    private final BedrockifyDrawableWidget widget;

    public BedrockifyEntry(BedrockifyDrawableWidget widget) {
        this(widget, widget.getHeight());
    }

    public BedrockifyEntry(BedrockifyDrawableWidget widget, int height) {
        this.widget = widget;
        this.height = height;
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        if (widget instanceof Selectable selectable)
            return List.of(selectable);
        return List.of();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {
        widget.setPosition(x, y + getYPositionOffset());
        widget.setWidth(entryWidth);
        widget.render(context, mouseX, mouseY, tickProgress);
    }

    private int getYPositionOffset() {
        if (height > this.widget.getHeight()) {
            return height / 2 - this.widget.getHeight() / 2;
        }
        return 0;
    }

    public int getHeight(){
        return height;
    }

    @Override
    public List<? extends Element> children() {
        if (widget instanceof Element element)
            return List.of(element);
        return List.of();
    }
}
