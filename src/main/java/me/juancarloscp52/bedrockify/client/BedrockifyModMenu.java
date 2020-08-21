package me.juancarloscp52.bedrockify.client;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.juancarloscp52.bedrockify.client.gui.BedrockifyOptionsScreen;

public class BedrockifyModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BedrockifyOptionsScreen::new;
    }
}
