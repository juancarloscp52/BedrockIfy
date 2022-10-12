package me.juancarloscp52.bedrockify.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;

public class BedrockifyModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (parent)-> BedrockifyClient.getInstance().settingsGUI.getConfigScreen(parent, MinecraftClient.getInstance().world != null);
    }
}
