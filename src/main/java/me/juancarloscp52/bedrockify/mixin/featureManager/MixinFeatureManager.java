package me.juancarloscp52.bedrockify.mixin.featureManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Unique;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MixinFeatureManager {

    @Unique
    public static final Map<String, Boolean> FEATURES = new HashMap<>();

    static {
        FEATURES.put("client.core.clientRenderTimer", true);
        FEATURES.put("client.core.bedrockIfyButton", true);
        FEATURES.put("client.features.chat", true);
        FEATURES.put("client.features.eatingAnimations", true);
        FEATURES.put("client.features.fishingBobber", true);
        FEATURES.put("client.features.heldItemTooltips",true);
        FEATURES.put("client.features.idleHandAnimations", true);
        FEATURES.put("client.features.loadingScreens", true);
        FEATURES.put("client.features.pickupAnimations", true);
        FEATURES.put("client.features.reacharoundPlacement", true);
        FEATURES.put("client.features.savingOverlay", true);
        FEATURES.put("client.features.screenSafeArea", true);
        FEATURES.put("client.features.slotHighlight", true);
        FEATURES.put("client.features.sheepColors", true);
        FEATURES.put("client.features.worldColorNoise",true);
        FEATURES.put("client.features.biggerDraggingItem",true);
        FEATURES.put("common.features.recipes", true);
        FEATURES.put("client.features.useAnimations", true);
        FEATURES.put("client.features.bedrockShading.lightBlock", true);
        FEATURES.put("client.features.bedrockShading.sunGlare", true);
        FEATURES.put("common.features.fireAspect", true);
        FEATURES.put("common.features.fertilizableBlocks", true);
        FEATURES.put("common.features.animalEatingParticles", true);
        FEATURES.put("common.features.cauldron", true);
        FEATURES.put("common.features.fernBonemeal", true);
        FEATURES.put("client.features.hudOpacity", true);
        FEATURES.put("client.features.editionBranding", true);

    }

    public static boolean isFeatureEnabled(String mixin){
        mixin = mixin.replace("me.juancarloscp52.bedrockify.mixin.","");
        String [] split = mixin.split("\\.");
        mixin = mixin.replace("."+split[split.length-1],"");
        if(mixin.contains("worldGeneration")){
            return true;
        }
        return FEATURES.get(mixin);
    }

    public static void loadMixinSettings() {
        File file = new File("./config/bedrockify/bedrockifyMixins.json");
        Gson gson = new Gson();
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file)) {;
                Type mapType = new TypeToken<Map<String,Boolean>>() {}.getType();
                Map<String,Boolean> newFeatures = gson.fromJson(fileReader, mapType);
                FEATURES.replaceAll((key, value) -> {
                   if(newFeatures.get(key) !=null){
                       return newFeatures.get(key);
                   }else{
                      return value;
                   }
                });
            } catch (Exception e) {
                LogManager.getLogger().warn("Could not load bedrockIfy Mixin settings, creating new config. ERROR: " + e.getLocalizedMessage());
                saveMixinSettings();
            }
        } else {
            LogManager.getLogger().warn("BedrockIfy Mixin Config not found, creating new config.");

            saveMixinSettings();
        }
    }

    public static void saveMixinSettings() {
        Gson gson = new Gson();
        File file = new File("./config/bedrockify/bedrockifyMixins.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(gson.toJson(FEATURES));
        } catch (IOException e) {
            LogManager.getLogger().warn("Could not save bedrockIfy Mixin settings: " + e.getLocalizedMessage());
        }
    }
    

}
