package me.juancarloscp52.bedrockify.client;

import me.juancarloscp52.bedrockify.client.features.HeldItemTooltips.HeldItemTooltips;
import me.juancarloscp52.bedrockify.client.features.ReachAroundPlacement;
import me.juancarloscp52.bedrockify.client.gui.SettingsGUI;
import me.juancarloscp52.bedrockify.client.gui.overlay.Overlay;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BedrockifyClient implements ClientModInitializer {

    private static BedrockifyClient instance;
    public static final Logger LOGGER = LogManager.getLogger();
    public ReachAroundPlacement reachAroundPlacement;
    public Overlay overlay;
    public HeldItemTooltips heldItemTooltips;
    public SettingsGUI settingsGUI;
    public static BedrockifyClient getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing BedrockIfy Client.");
        overlay = new Overlay((MinecraftClient.getInstance()));
        reachAroundPlacement = new ReachAroundPlacement(MinecraftClient.getInstance());
        heldItemTooltips = new HeldItemTooltips();
        settingsGUI=new SettingsGUI();
        instance = this;
    }
}
