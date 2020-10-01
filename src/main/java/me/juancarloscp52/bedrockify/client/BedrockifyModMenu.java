package me.juancarloscp52.bedrockify.client;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;

public class BedrockifyModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent)-> BedrockifyClient.getInstance().settingsGUI.getConfigScreen(parent, MinecraftClient.getInstance().world != null);
    }
}
