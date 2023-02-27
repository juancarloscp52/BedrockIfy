package me.juancarloscp52.bedrockify.client;

import com.google.gson.Gson;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.features.bedrockShading.BedrockBlockShading;
import me.juancarloscp52.bedrockify.client.features.bedrockShading.BedrockSunGlareShading;
import me.juancarloscp52.bedrockify.client.features.fishingBobber.FishingBobber3DModel;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.HeldItemTooltips;
import me.juancarloscp52.bedrockify.client.features.hudOpacity.HudOpacity;
import me.juancarloscp52.bedrockify.client.features.reacharoundPlacement.ReachAroundPlacement;
import me.juancarloscp52.bedrockify.client.features.worldColorNoise.WorldColorNoiseSampler;
import me.juancarloscp52.bedrockify.client.gui.Overlay;
import me.juancarloscp52.bedrockify.client.gui.SettingsGUI;
import me.juancarloscp52.bedrockify.common.block.entity.WaterCauldronBlockEntity;
import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class BedrockifyClient implements ClientModInitializer {

    private static BedrockifyClient instance;
    public static final Logger LOGGER = LogManager.getLogger();
    public ReachAroundPlacement reachAroundPlacement;
    public Overlay overlay;
    public HeldItemTooltips heldItemTooltips;
    public SettingsGUI settingsGUI;
    public WorldColorNoiseSampler worldColorNoiseSampler;
    public BedrockBlockShading bedrockBlockShading;
    public BedrockSunGlareShading bedrockSunGlareShading;
    public HudOpacity hudOpacity;
    public long deltaTime = 0;
    private int timeFlying = 0;
    private static KeyBinding keyBinding;

    public BedrockifyClientSettings settings;

    public static BedrockifyClient getInstance() {
        return instance;
    }
    @Override
    public void onInitializeClient() {
        instance = this;
        loadSettings();
        LOGGER.info("Initializing BedrockIfy Client.");
        overlay = new Overlay((MinecraftClient.getInstance()));
        reachAroundPlacement = new ReachAroundPlacement(MinecraftClient.getInstance());
        heldItemTooltips = new HeldItemTooltips();
        settingsGUI=new SettingsGUI();
        worldColorNoiseSampler = new WorldColorNoiseSampler();
        bedrockBlockShading = new BedrockBlockShading();
        bedrockSunGlareShading = new BedrockSunGlareShading();
        hudOpacity = new HudOpacity();
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("bedrockIfy.key.settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "BedrockIfy"));

        // Register 3D Bobber Entity.
        EntityModelLayerRegistry.registerModelLayer(FishingBobber3DModel.MODEL_LAYER, FishingBobber3DModel::generateModel);

        // Register the Color Tint of Colored Cauldron Block.
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return -1;
            }

            final Optional<WaterCauldronBlockEntity> entity = world.getBlockEntity(pos, BedrockCauldronBlocks.WATER_CAULDRON_ENTITY);
            return entity.map(WaterCauldronBlockEntity::getTintColor).orElse(-1);
        }, BedrockCauldronBlocks.COLORED_WATER_CAULDRON);


        ClientPlayNetworking.registerGlobalReceiver(Bedrockify.EAT_PARTICLES, (client, handler, buf, responseSender) -> {
            ItemStack stack = buf.readItemStack();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            double velx = buf.readDouble();
            double vely = buf.readDouble();
            double velz = buf.readDouble();

            client.execute(() -> {
                if(null != client.world)
                    client.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, stack),x,y,z,velx,vely,velz);
            });
        });

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> BedrockifyClient.getInstance().overlay.renderOverlay(matrixStack));
        ClientTickEvents.END_CLIENT_TICK.register(client-> {
            while (keyBinding.wasPressed()){
                client.setScreen(settingsGUI.getConfigScreen(client.currentScreen,true));
            }
            hudOpacity.tick();
            bedrockSunGlareShading.tick(client.getTickDelta());

            // Stop flying drift
            if(settings.disableFlyingMomentum && null != client.player && client.player.getAbilities().flying){
                if(!(client.options.leftKey.isPressed() || client.options.backKey.isPressed() ||client.options.rightKey.isPressed() ||client.options.forwardKey.isPressed())){
                    client.player.setVelocity(0,client.player.getVelocity().getY(),0);
                }
                if(!(client.options.sneakKey.isPressed()|| client.options.jumpKey.isPressed())){
                    client.player.setVelocity(client.player.getVelocity().getX(), 0,client.player.getVelocity().getZ());

                }
            }

            // Stop elytra flying by pressing space
            if(null != client.player && settings.elytraStop && client.player.isFallFlying() && timeFlying > 10 && client.options.jumpKey.isPressed()){
                client.player.stopFallFlying();
                client.player.networkHandler.sendPacket(new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            }
            if(null != client.player && client.player.isFallFlying() && !client.options.jumpKey.isPressed())
                timeFlying++;
            else
                timeFlying = 0;

        });
        LOGGER.info("Initialized BedrockIfy Client");
    }

    public void loadSettings() {
        File file = new File("./config/bedrockify/bedrockifyClient.json");
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                settings = gson.fromJson(fileReader, BedrockifyClientSettings.class);
                fileReader.close();
            } catch (IOException e) {
                LOGGER.warn("Could not load bedrockIfy settings: " + e.getLocalizedMessage());
            }
        } else {
            settings = new BedrockifyClientSettings();
        }
    }

    public void saveSettings() {
        Gson gson = new Gson();
        File file = new File("./config/bedrockify/bedrockifyClient.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(settings));
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.warn("Could not save bedrockIfy settings: " + e.getLocalizedMessage());
        }
    }
}
