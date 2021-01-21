package me.juancarloscp52.bedrockify;

import com.google.gson.Gson;
import me.juancarloscp52.bedrockify.client.features.quickArmorSwap.ArmorReplacer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.TypedActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Bedrockify implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    private static Bedrockify instance;
    public BedrockifySettings settings;

    public static Bedrockify getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing BedrockIfy.");
        loadSettings();
        instance = this;
        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            if(settings.isQuickArmorSwapEnabled())
                return ArmorReplacer.tryChangeArmor(playerEntity,hand);
            return TypedActionResult.pass(playerEntity.getStackInHand(hand));
        });

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
