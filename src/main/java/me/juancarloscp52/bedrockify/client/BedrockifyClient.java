package me.juancarloscp52.bedrockify.client;

import com.google.gson.*;
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
import me.juancarloscp52.bedrockify.common.block.cauldron.BedrockCauldronBehavior;
import me.juancarloscp52.bedrockify.common.block.entity.WaterCauldronBlockEntity;
import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronBlocks;
import me.juancarloscp52.bedrockify.common.payloads.CauldronParticlePayload;
import me.juancarloscp52.bedrockify.common.payloads.EatParticlePayload;
import me.juancarloscp52.bedrockify.mixin.featureManager.MixinFeatureManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Pose;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BedrockifyClient implements ClientModInitializer {

    private static final KeyMapping.Category BEDROCKIFY_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("bedrockify","bedrockify"));

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
    private static KeyMapping keyBinding;

    public BedrockifyClientSettings settings;

    private static final Gson SETTINGS_ENUM_DESERIALIZER = new GsonBuilder()
            .registerTypeAdapter(BedrockifyClientSettings.FpsHudOption.class, (JsonDeserializer<BedrockifyClientSettings.FpsHudOption>) (json, typeOfT, context) -> {
                try {
                    return BedrockifyClientSettings.FpsHudOption.valueOf(json.getAsString());
                } catch (Exception ignore) {
                }
                return BedrockifyClientSettings.FpsHudOption.OFF;
            })
            .registerTypeAdapter(BedrockifyClientSettings.ButtonPosition.class, (JsonDeserializer<BedrockifyClientSettings.ButtonPosition>) (json, typeOfT, context) -> {
                try {
                    return BedrockifyClientSettings.ButtonPosition.valueOf(json.getAsString());
                } catch (Exception ignore) {
                }
                return BedrockifyClientSettings.ButtonPosition.BELOW_SLIDERS;
            })
            .create();

    public static BedrockifyClient getInstance() {
        return instance;
    }
    @Override
    public void onInitializeClient() {
        instance = this;
        loadSettings();
        LOGGER.info("Initializing BedrockIfy Client.");
        overlay = new Overlay((Minecraft.getInstance()));
        reachAroundPlacement = new ReachAroundPlacement(Minecraft.getInstance());
        heldItemTooltips = new HeldItemTooltips();
        settingsGUI=new SettingsGUI();
        worldColorNoiseSampler = new WorldColorNoiseSampler();
        bedrockBlockShading = new BedrockBlockShading();
        bedrockSunGlareShading = new BedrockSunGlareShading();
        hudOpacity = new HudOpacity();
        keyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping("bedrockIfy.key.settings", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, BEDROCKIFY_CATEGORY));

        // Register 3D Bobber Entity.
        ModelLayerRegistry.registerModelLayer(FishingBobber3DModel.MODEL_LAYER, FishingBobber3DModel::generateModel);

        // Register the Color Tint of Potion-filled and Colored Cauldron Block if enabled.
        if (MixinFeatureManager.features.get(MixinFeatureManager.FEAT_CAULDRON)) {
            BlockColorRegistry.register(List.of(new BlockTintSource() {
                @Override
                public int color(BlockState state) {
                    return 0;
                }

                @Override
                public int colorInWorld(BlockState state, BlockAndTintGetter world, BlockPos pos) {
                    final Optional<WaterCauldronBlockEntity> entity = world.getBlockEntity(pos, BedrockCauldronBlocks.WATER_CAULDRON_ENTITY);
                    return entity.map(WaterCauldronBlockEntity::getTintColor).orElse(0);
                }
            }), BedrockCauldronBlocks.POTION_CAULDRON, BedrockCauldronBlocks.COLORED_WATER_CAULDRON);

            // Lazy initialization of Bedrock's cauldron behavior after all the registries/tags are ready.
            ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
                BedrockCauldronBehavior.registerBehavior();
            });
        }

        ClientPlayNetworking.registerGlobalReceiver(Bedrockify.EAT_PARTICLE_PAYLOAD.type(), new EatParticlePayload.EatParticleHandler());

        ClientPlayNetworking.registerGlobalReceiver(Bedrockify.CAULDRON_PARTICLE_PAYLOAD.type(), new CauldronParticlePayload.CauldronParticleHandler());

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Bedrockify.MOD_ID, "overlay"), (context, tickCounter) -> BedrockifyClient.getInstance().overlay.renderOverlay(context));
        ClientTickEvents.END_CLIENT_TICK.register(client-> {
            while (keyBinding.consumeClick()){
                client.gui.setScreen(settingsGUI.getConfigScreen(client.gui.screen()));
            }
            hudOpacity.tick();
            bedrockSunGlareShading.tick(client.getDeltaTracker().getGameTimeDeltaPartialTick(true));

            // Stop flying drift
            if(settings.disableFlyingMomentum && null != client.player && client.player.getAbilities().flying){
                if(!(client.options.keyLeft.isDown() || client.options.keyDown.isDown() ||client.options.keyRight.isDown() ||client.options.keyUp.isDown())){
                    client.player.setDeltaMovement(0,client.player.getDeltaMovement().y(),0);
                }
                if(!(client.options.keyShift.isDown()|| client.options.keyJump.isDown())){
                    client.player.setDeltaMovement(client.player.getDeltaMovement().x(), 0,client.player.getDeltaMovement().z());

                }
            }

            // Stop elytra flying by pressing space
            if(null != client.player && settings.elytraStop && client.player.getPose().equals(Pose.FALL_FLYING) && timeFlying > 10 && client.options.keyJump.isDown()){
                client.player.getAbilities().flying = false;
                client.player.onUpdateAbilities();
                client.player.connection.send(new ServerboundPlayerCommandPacket(client.player, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
            }
            if(null != client.player && client.player.getPose().equals(Pose.FALL_FLYING) && !client.options.keyJump.isDown())
                timeFlying++;
            else
                timeFlying = 0;

        });
        LOGGER.info("Initialized BedrockIfy Client");
    }

    public void loadSettings() {
        File file = new File("./config/bedrockify/bedrockifyClient.json");
        try (FileReader fileReader = new FileReader(file)) {
            settings = Objects.requireNonNull(SETTINGS_ENUM_DESERIALIZER.fromJson(fileReader, BedrockifyClientSettings.class));
        } catch (Exception e) {
            LOGGER.warn("Could not load bedrockIfy settings: {}", e.getLocalizedMessage());
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
            LOGGER.warn("Could not save bedrockIfy settings: {}", e.getLocalizedMessage());
        }
    }
}
