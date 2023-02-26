package me.juancarloscp52.bedrockify.client.gui;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.BedrockifySettings;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import me.juancarloscp52.bedrockify.mixin.featureManager.MixinFeatureManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Map;

public class SettingsGUI {

    BedrockifyClientSettings settingsClient = BedrockifyClient.getInstance().settings;
    BedrockifySettings settingsCommon = Bedrockify.getInstance().settings;

    public Screen getConfigScreen(Screen parent, boolean isTransparent){
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Text.translatable("bedrockify.options.settings"));
        builder.setSavingRunnable(()-> {
            Bedrockify.getInstance().saveSettings();
            BedrockifyClient.getInstance().saveSettings();
            MixinFeatureManager.saveMixinSettings();
        });
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigCategory mixins = builder.getOrCreateCategory(Text.literal("Mixins"));
        ConfigCategory panorama = builder.getOrCreateCategory(Text.literal("Panorama Screens"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        SubCategoryBuilder bedrockOverlay = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.bedrockOverlay"));
        bedrockOverlay.add(entryBuilder.startTextDescription(Text.translatable("bedrockify.options.subCategory.bedrockOverlay.description")).build());
        bedrockOverlay.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.showCoordinates"), settingsClient.showPositionHUD).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.showPositionHUD=newValue).build());
        bedrockOverlay.add(entryBuilder.startSelector(Text.translatable("bedrockify.options.showFPS"), new Byte []{0,1,2}, settingsClient.FPSHUD).setDefaultValue((byte) 0).setNameProvider((value)-> switch (value) {
            case 0 -> Text.translatable("bedrockify.options.off");
            case 1 -> Text.translatable("bedrockify.options.withPosition");
            default -> Text.translatable("bedrockify.options.underPosition");
        }).setSaveConsumer((newValue)-> settingsClient.FPSHUD=newValue).build());
        bedrockOverlay.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.coordinatesPosition"), settingsClient.positionHUDHeight,0,100).setDefaultValue(50).setSaveConsumer((newValue)-> settingsClient.positionHUDHeight=newValue).build());
        bedrockOverlay.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.showPaperDoll"), settingsClient.showPaperDoll).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.showPaperDoll=newValue).build());
        bedrockOverlay.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.showSavingOverlay"), settingsClient.savingOverlay).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.savingOverlay=newValue).build());
        general.addEntry(bedrockOverlay.build());
        SubCategoryBuilder guiImprovements = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.visualImprovements"));
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.biggerItems"), settingsClient.biggerIcons).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.biggerIcons=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.transparentToolBar"), settingsClient.transparentHotBar).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.transparentHotBar=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.expTextStyle"), settingsClient.expTextStyle).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.expTextStyle=newValue).setYesNoTextSupplier((value)->value ? Text.translatable("bedrockify.options.chatStyle.bedrock") : Text.translatable("bedrockify.options.chatStyle.vanilla")).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.chatStyle"), settingsClient.bedrockChat).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.bedrockChat=newValue).setYesNoTextSupplier((value)->value ? Text.translatable("bedrockify.options.chatStyle.bedrock") : Text.translatable("bedrockify.options.chatStyle.vanilla")).build());
        guiImprovements.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.screenSafeArea"), settingsClient.screenSafeArea,0,30).setDefaultValue(0).setSaveConsumer((newValue)-> settingsClient.screenSafeArea=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.ignoreBorder"), settingsClient.overlayIgnoresSafeArea).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.overlayIgnoresSafeArea=newValue).build());
        guiImprovements.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.hudOpacity"), settingsClient.hudOpacity,0,100).setDefaultValue(50).setSaveConsumer((newValue)-> settingsClient.hudOpacity=newValue).build());
        guiImprovements.add(entryBuilder.startSelector(Text.translatable("bedrockify.options.tooltips"), new Byte []{0,1,2}, settingsClient.heldItemTooltip).setDefaultValue((byte) 2).setNameProvider((value)-> switch (value) {
            case 0 -> Text.translatable("bedrockify.options.off");
            case 1 -> Text.translatable("bedrockify.options.on");
            default -> Text.translatable("bedrockify.options.withBackground");
        }).setSaveConsumer((newValue)-> settingsClient.heldItemTooltip=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.inventoryHighlight"), settingsClient.slotHighlight).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.slotHighlight=newValue).build());
        guiImprovements.add(entryBuilder.startAlphaColorField(Text.translatable("bedrockify.options.inventoryHighlight.color1"), settingsClient.highLightColor1).setDefaultValue((255 << 8) + (255) + (255 << 16) + (255 << 24)).setSaveConsumer(newValue -> settingsClient.highLightColor1=newValue).build());
        guiImprovements.add(entryBuilder.startAlphaColorField(Text.translatable("bedrockify.options.inventoryHighlight.color2"), settingsClient.highLightColor2).setDefaultValue(64 + (170 << 8) + (109 << 16) + (255 << 24)).setSaveConsumer(newValue -> settingsClient.highLightColor2=newValue).build());
        guiImprovements.add(entryBuilder.startSelector(Text.translatable("bedrockify.options.idleAnimation"), new Float []{0.0f,0.5f,1.0f,1.5f,2.0f,2.5f,3.0f,4.0f}, settingsClient.idleAnimation).setDefaultValue(1.0f).setNameProvider((value)-> Text.literal("x"+ value)).setSaveConsumer((newValue)-> settingsClient.idleAnimation=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.eatingAnimations"), settingsClient.eatingAnimations).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.eatingAnimations=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.pickupAnimations"), settingsClient.pickupAnimations).setTooltip(wrapLines(Text.translatable("bedrockify.options.pickupAnimations.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.pickupAnimations=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.loadingScreen"), settingsClient.loadingScreen).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.loadingScreen=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.bedrockShading"), settingsClient.bedrockShading).setTooltip(wrapLines(Text.translatable("bedrockify.options.bedrockShading.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> {
            settingsClient.bedrockShading=newValue;
            MinecraftClient.getInstance().worldRenderer.reload();
        }).build());
        guiImprovements.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.sunlightIntensity"), settingsClient.sunlightIntensity,0,100).setTooltip(wrapLines(Text.translatable("bedrockify.options.sunlightIntensity.tooltip"))).setDefaultValue(50).setSaveConsumer(newValue -> {
            settingsClient.sunlightIntensity = newValue;
            BedrockifyClient.getInstance().bedrockSunGlareShading.onSunlightIntensityChanged();
        }).build());
        general.addEntry(guiImprovements.build());
        SubCategoryBuilder reachAround = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.Reach-Around"));
        reachAround.add(entryBuilder.startTextDescription(Text.translatable("bedrockify.options.subCategory.Reach-Around.description")).build());
        reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround"), settingsClient.reacharound).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.reacharound=newValue).build());
        reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround.multiplayer"), settingsClient.reacharoundMultiplayer).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.reacharoundMultiplayer=newValue).build());
        reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround.sneaking"), settingsClient.reacharoundSneaking).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.reacharoundSneaking=newValue).build());
        reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround.indicator"), settingsClient.reacharoundIndicator).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.reacharoundIndicator=newValue).build());
        reachAround.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.reachAround.pitch"), settingsClient.reacharoundPitchAngle, 0,90).setDefaultValue(25).setSaveConsumer(newValue -> settingsClient.reacharoundPitchAngle=newValue).build());
        reachAround.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.reachAround.distance"), MathHelper.floor(settingsClient.reacharoundBlockDistance*100), 0,100).setTextGetter((integer -> Text.literal(String.valueOf(integer/100d)))).setDefaultValue(75).setSaveConsumer(newValue -> settingsClient.reacharoundBlockDistance=newValue/100d).build());
        general.addEntry(reachAround.build());
        SubCategoryBuilder otherSettings = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.other"));
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.loadingScreenChunkMap"), settingsClient.showChunkMap).setTooltip(wrapLines(Text.translatable("bedrockify.options.loadingScreenChunkMap.tooltip"))).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.showChunkMap=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.showBedrockIfyButton"), settingsClient.bedrockIfyButton).setDefaultValue(true).setTooltip(wrapLines(Text.translatable("bedrockify.options.showBedrockIfyButton.tooltip"))).setSaveConsumer(newValue -> settingsClient.bedrockIfyButton=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.dyingTrees"), settingsCommon.dyingTrees).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.dyingTrees=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.fallenTrees"), settingsCommon.fallenTrees).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.fallenTrees=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.fireAspectLight"), settingsCommon.fireAspectLight).setTooltip(wrapLines(Text.translatable("bedrockify.options.fireAspectLight.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.fireAspectLight=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.fernBonemeal"), settingsCommon.fernBonemeal).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.fernBonemeal=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.disableFlyingMomentum"), settingsClient.disableFlyingMomentum).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.disableFlyingMomentum =newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.elytraStop"), settingsClient.elytraStop).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.elytraStop=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.fishingBobber3D"), settingsClient.fishingBobber3D).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.fishingBobber3D=newValue).build());

        //        otherSettings.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.sneakingShield"), settings.sneakingShield).setDefaultValue(true).setSaveConsumer(newValue -> settings.sneakingShield=newValue).build());
        general.addEntry(otherSettings.build());
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.recipes"), settingsCommon.bedrockRecipes).setTooltip(wrapLines(Text.translatable("bedrockify.options.recipes.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.bedrockRecipes=newValue).build());

        mixins.addEntry(entryBuilder.startTextDescription(Text.translatable("bedrockify.options.mixins.description")).build());
        for(Map.Entry<String, Boolean> elem : MixinFeatureManager.features.entrySet()){
            mixins.addEntry(entryBuilder.startBooleanToggle(Text.translatable(elem.getKey()), elem.getValue()).setDefaultValue(true).setSaveConsumer(newValue -> MixinFeatureManager.features.put(elem.getKey(),newValue)).build());
        }

        panorama.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.rotationalBackground"), settingsClient.cubeMapBackground).setDefaultValue(true).setTooltip(wrapLines(Text.translatable("bedrockify.options.rotationalBackground.tooltip"))).setSaveConsumer(newValue -> settingsClient.cubeMapBackground=newValue).build());
        panorama.addEntry(entryBuilder.startStrList(Text.translatable("bedrockify.options.panorama.list"), settingsClient.panoramaIgnoredScreens).setDefaultValue(BedrockifyClientSettings.PANORAMA_IGNORED_SCREENS).setTooltip(wrapLines(Text.translatable("bedrockify.options.panorama.list.tooltip"))).setSaveConsumer(strings -> settingsClient.panoramaIgnoredScreens=strings).build());
        return builder.setTransparentBackground(isTransparent).build();
    }

    public Text[] wrapLines(Text text){
        List<StringVisitable> lines = MinecraftClient.getInstance().textRenderer.getTextHandler().wrapLines(text,Math.max(MinecraftClient.getInstance().getWindow().getScaledWidth()/2 - 43,170), Style.EMPTY);
        lines.get(0).getString();
        Text[] textLines = new Text[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            textLines[i]=Text.literal(lines.get(i).getString());
        }
        return textLines;
    }

}
