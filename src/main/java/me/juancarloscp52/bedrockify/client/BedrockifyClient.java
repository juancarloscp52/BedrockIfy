package me.juancarloscp52.bedrockify.client;

import com.google.gson.Gson;
import me.juancarloscp52.bedrockify.client.features.ItemTooltips;
import me.juancarloscp52.bedrockify.client.gui.overlay.Overlay;
import me.juancarloscp52.bedrockify.client.features.ReachAroundPlacement;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BedrockifyClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    private static BedrockifyClient instance;
    public ReachAroundPlacement reachAroundPlacement;
    public Overlay overlay;
    public BedrockifySettings settings;
    public ItemTooltips itemTooltips;

    public static BedrockifyClient getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing BedrockIfy client.");
        loadSettings();
        overlay = new Overlay((MinecraftClient.getInstance()));
        reachAroundPlacement = new ReachAroundPlacement(MinecraftClient.getInstance());
        itemTooltips = new ItemTooltips();
        instance = this;
    }

    public void loadSettings() {
        File file = new File("./config/bedrockify.json");
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
        File file = new File("./config/bedrockify.json");
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
