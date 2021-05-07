package me.juancarloscp52.bedrockify.client.features.loadingScreens;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ExternalLoadingTips {

    boolean alwaysExternalTips = false;

    String [] externalLoadingTips = new String[]{} ;

    public static ExternalLoadingTips loadSettings() {
        ExternalLoadingTips loadingTips = new ExternalLoadingTips();
        File file = new File("./config/bedrockify-ExternalLoadingTips.json");
        Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader(file);
            loadingTips = gson.fromJson(fileReader, ExternalLoadingTips.class);
            fileReader.close();
        } catch (IOException e) {
            LogManager.getLogger().warn("Could not load external loading tips: " + e.getLocalizedMessage());
        }
        return loadingTips;
    }

    public void saveSettings() {
        Gson gson = new Gson();
        File file = new File("./config/bedrockify-ExternalLoadingTips.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(this));
            fileWriter.close();
        } catch (IOException e) {
            LogManager.getLogger().warn("Could not save external loading tips: " + e.getLocalizedMessage());
        }
    }

}
