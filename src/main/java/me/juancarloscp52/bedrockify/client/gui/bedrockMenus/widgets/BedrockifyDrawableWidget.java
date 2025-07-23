package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets;


import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;

import java.util.function.Consumer;

public interface BedrockifyDrawableWidget extends Widget, Drawable {

    void setWidth(int width);

    void setHeight(int height);

    @Deprecated
    @Override
    default void forEachChild(Consumer<ClickableWidget> consumer) {
        BedrockifyClient.LOGGER.warn("Deprecated method forEachChild has been called from a BedrockifyDrawableWidget object. This method is not implemented");
    }

}
