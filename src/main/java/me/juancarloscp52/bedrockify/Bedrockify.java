package me.juancarloscp52.bedrockify;

import com.google.gson.Gson;
import me.juancarloscp52.bedrockify.common.payloads.CauldronParticlePayload;
import me.juancarloscp52.bedrockify.common.payloads.EatParticlePayload;
import me.juancarloscp52.bedrockify.common.block.cauldron.BedrockCauldronBehavior;
import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronBlocks;
import me.juancarloscp52.bedrockify.common.features.worldGeneration.DyingTrees;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Bedrockify implements ModInitializer {
    public static final String MOD_ID = "bedrockify";
    public static final Logger LOGGER = LogManager.getLogger();
    public BedrockifySettings settings;
    private static Bedrockify instance;
    public static final EatParticlePayload EAT_PARTICLE_PAYLOAD = new EatParticlePayload();
    public static final CauldronParticlePayload CAULDRON_PARTICLE_PAYLOAD = new CauldronParticlePayload();
    public static Bedrockify getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing BedrockIfy.");
        loadSettings();
        instance = this;
        DyingTrees.registerTrees();
        BedrockCauldronBlocks.register();
        PayloadTypeRegistry.playS2C().register(Bedrockify.EAT_PARTICLE_PAYLOAD.getId(), EatParticlePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(Bedrockify.CAULDRON_PARTICLE_PAYLOAD.getId(), CauldronParticlePayload.CODEC);
        ServerLifecycleEvents.SERVER_STARTED.register(server -> BedrockCauldronBehavior.registerBehavior());
    }

    public void loadSettings() {
        File file = new File("./config/bedrockify/bedrockifyCommon.json");
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                settings = gson.fromJson(fileReader, BedrockifySettings.class);
                fileReader.close();
            } catch (IOException e) {
                LOGGER.warn("Could not load bedrockIfy settings: " + e.getLocalizedMessage());
            }
        } else {
            settings = new BedrockifySettings();
        }
    }

    public void saveSettings() {
        Gson gson = new Gson();
        File file = new File("./config/bedrockify/bedrockifyCommon.json");
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
