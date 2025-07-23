package me.juancarloscp52.bedrockify.client.gui.bedrockMenus;

import net.minecraft.util.Identifier;

public record BedrockifyIconButtonTextures(Identifier icon, Identifier highlightedIcon, int width, int height) {

    public BedrockifyIconButtonTextures(Identifier icon, Identifier highlightedIcon){
        this(icon, highlightedIcon, 15,12);
    }

    public Identifier get(boolean focused) {
        return focused ? this.highlightedIcon : this.icon;
    }

}
