package me.juancarloscp52.bedrockify.client;

import me.juancarloscp52.bedrockify.client.features.HeldItemTooltips.HeldItemTooltips;
import me.juancarloscp52.bedrockify.client.features.ReachAroundPlacement;
import me.juancarloscp52.bedrockify.client.gui.SettingsGUI;
import me.juancarloscp52.bedrockify.client.gui.overlay.Overlay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class BedrockifyClient implements ClientModInitializer {

    private static BedrockifyClient instance;
    public static final Logger LOGGER = LogManager.getLogger();
    public ReachAroundPlacement reachAroundPlacement;
    public Overlay overlay;
    public HeldItemTooltips heldItemTooltips;
    public SettingsGUI settingsGUI;
    private static KeyBinding keyBinding;
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

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("bedrockIfy.key.settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "BedrockIfy"));
        ClientTickEvents.END_CLIENT_TICK.register(client-> {
            while (keyBinding.wasPressed()){
                client.openScreen(settingsGUI.getConfigScreen(client.currentScreen,true));
            }
        });

        instance = this;
    }
}
