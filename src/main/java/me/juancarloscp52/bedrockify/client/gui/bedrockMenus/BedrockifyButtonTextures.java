package me.juancarloscp52.bedrockify.client.gui.bedrockMenus;

import net.minecraft.util.Identifier;

public record BedrockifyButtonTextures(Identifier enabled, Identifier enabledFocused, Identifier pressed, Identifier pressedFocused, Identifier disabled) {

    public BedrockifyButtonTextures(Identifier enabled, Identifier enabledFocused, Identifier disabled){
        this(enabled, enabledFocused, enabled, enabled, disabled);
    }

    public Identifier get(boolean enabled, boolean focused, boolean pressed) {
        if (enabled) {
            if(pressed){
                return focused ? this.pressedFocused : this.pressed;
            }else {
                return focused ? this.enabledFocused : this.enabled;
            }
        } else {
            return this.disabled;
        }
    }

}
