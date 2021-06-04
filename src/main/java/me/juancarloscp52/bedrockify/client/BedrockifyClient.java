package me.juancarloscp52.bedrockify.client;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.features.quickArmorSwap.ArmorReplacer;
import me.juancarloscp52.bedrockify.client.features.worldColorNoise.WorldColorNoiseSampler;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.HeldItemTooltips;
import me.juancarloscp52.bedrockify.client.features.reacharoundPlacement.ReachAroundPlacement;
import me.juancarloscp52.bedrockify.client.gui.SettingsGUI;
import me.juancarloscp52.bedrockify.client.gui.Overlay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.TypedActionResult;
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
    public WorldColorNoiseSampler worldColorNoiseSampler;
    public long deltaTime = 0;
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
        worldColorNoiseSampler = new WorldColorNoiseSampler();
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("bedrockIfy.key.settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "BedrockIfy"));

        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            if(Bedrockify.getInstance().settings.isQuickArmorSwapEnabled())
                return ArmorReplacer.tryChangeArmor(playerEntity,hand);
            return TypedActionResult.pass(playerEntity.getStackInHand(hand));
        });
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            BedrockifyClient.getInstance().overlay.renderOverlay(matrixStack);
        });
        ClientTickEvents.END_CLIENT_TICK.register(client-> {
            while (keyBinding.wasPressed()){
                client.openScreen(settingsGUI.getConfigScreen(client.currentScreen,true));
            }
        });

        instance = this;
    }
}
