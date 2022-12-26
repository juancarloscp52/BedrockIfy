package me.juancarloscp52.bedrockify;

import com.google.gson.Gson;
import me.juancarloscp52.bedrockify.common.features.worldGeneration.DyingTrees;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Bedrockify implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    public BedrockifySettings settings;
    private static Bedrockify instance;
    public static final Identifier EAT_PARTICLES = new Identifier("bedrockify", "eat-particles");
    public static Bedrockify getInstance() {
        return instance;
    }

    //TODO: Fix weird background on resource pack screen.
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing BedrockIfy.");
        loadSettings();
        instance = this;
        init();
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

        // re-initialize
        init();
    }

    /**
     * Will be called to re-initialize
     */
    private void init() {
        DyingTrees.registerTrees();
    }
}
