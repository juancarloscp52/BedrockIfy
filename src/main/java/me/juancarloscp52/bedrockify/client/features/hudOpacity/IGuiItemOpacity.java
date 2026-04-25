package me.juancarloscp52.bedrockify.client.features.hudOpacity;

public interface IGuiItemOpacity {
    default void setOpacity(float opacity) {

    }

    default float getOpacity() {
        return 1f;
    }
}
