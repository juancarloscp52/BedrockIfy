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
import net.minecraft.util.Identifier;
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

        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/bedrock.png"));
        // Create Categories
        ConfigCategory gameplay = builder.getOrCreateCategory(Text.translatable("bedrockify.options.categories.gameplay"));
        ConfigCategory gui = builder.getOrCreateCategory(Text.translatable("bedrockify.options.categories.gui"));
        ConfigCategory visualImprovements = builder.getOrCreateCategory(Text.translatable("bedrockify.options.categories.visualImprovements"));
        ConfigCategory panorama = builder.getOrCreateCategory(Text.translatable("bedrockify.options.categories.panoramaScreens"));
        ConfigCategory mixins = builder.getOrCreateCategory(Text.translatable("bedrockify.options.categories.mixins"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        /*
        *
        *   Gameplay Category
        *
        */

            // Reach Around Placement Sub Category
            SubCategoryBuilder reachAround = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.Reach-Around"));
            reachAround.add(entryBuilder.startTextDescription(Text.translatable("bedrockify.options.subCategory.Reach-Around.description")).build());
            reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround"), settingsClient.reacharound).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.reacharound=newValue).build());
            reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround.multiplayer"), settingsClient.reacharoundMultiplayer).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.reacharoundMultiplayer=newValue).build());
            reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround.sneaking"), settingsClient.reacharoundSneaking).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.reacharoundSneaking=newValue).build());
            reachAround.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.reachAround.indicator"), settingsClient.reacharoundIndicator).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.reacharoundIndicator=newValue).build());
            reachAround.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.reachAround.pitch"), settingsClient.reacharoundPitchAngle, 0,90).setDefaultValue(25).setSaveConsumer(newValue -> settingsClient.reacharoundPitchAngle=newValue).build());
            reachAround.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.reachAround.distance"), MathHelper.floor(settingsClient.reacharoundBlockDistance*100), 0,100).setTextGetter((integer -> Text.literal(String.valueOf(integer/100d)))).setDefaultValue(75).setSaveConsumer(newValue -> settingsClient.reacharoundBlockDistance=newValue/100d).build());
            gameplay.addEntry(reachAround.build());

            // Dying and Fallen Trees.
            gameplay.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.dyingTrees"), settingsCommon.dyingTrees).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.dyingTrees=newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.fallenTrees"), settingsCommon.fallenTrees).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.fallenTrees=newValue).build());

            // Other Settings.
            gameplay.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.recipes"), settingsCommon.bedrockRecipes).setTooltip(wrapLines(Text.translatable("bedrockify.options.recipes.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.bedrockRecipes=newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.fireAspectLight"), settingsCommon.fireAspectLight).setTooltip(wrapLines(Text.translatable("bedrockify.options.fireAspectLight.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.fireAspectLight=newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.fernBonemeal"), settingsCommon.fernBonemeal).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.fernBonemeal=newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.disableFlyingMomentum"), settingsClient.disableFlyingMomentum).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.disableFlyingMomentum =newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.elytraStop"), settingsClient.elytraStop).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.elytraStop=newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.bedrockCauldron"), settingsCommon.bedrockCauldron).setTooltip(wrapLines(Text.translatable("bedrockify.options.bedrockCauldron.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.bedrockCauldron=newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.sheepcolors"), settingsClient.sheepColors).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.sheepColors=newValue).build());

        /*
         *
         *   GUI Category
         *
         */
            // Bedrock loading screens.
            gui.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.loadingScreen"), settingsClient.loadingScreen).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.loadingScreen=newValue).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.loadingScreenChunkMap"), settingsClient.showChunkMap).setTooltip(wrapLines(Text.translatable("bedrockify.options.loadingScreenChunkMap.tooltip"))).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.showChunkMap=newValue).build());

            // Overlay
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
            gui.addEntry(bedrockOverlay.build());

            //Tooltips
            SubCategoryBuilder tooltips = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.tooltips"));
            tooltips.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.tooltips"), settingsClient.heldItemTooltips).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.heldItemTooltips =newValue).build());
            tooltips.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.tooltips.background"),(int)Math.ceil(settingsClient.heldItemTooltipBackground*100),0,100).setDefaultValue(50).setSaveConsumer(newValue -> settingsClient.heldItemTooltipBackground=newValue/100d).build());
            gui.addEntry(tooltips.build());

            //Item slot Highlight
            SubCategoryBuilder itemHighlight = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.inventoryHighlight"));
            itemHighlight.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.inventoryHighlight"), settingsClient.slotHighlight).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.slotHighlight=newValue).build());
            itemHighlight.add(entryBuilder.startAlphaColorField(Text.translatable("bedrockify.options.inventoryHighlight.color1"), settingsClient.highLightColor1).setDefaultValue((255 << 8) + (255) + (255 << 16) + (255 << 24)).setSaveConsumer(newValue -> settingsClient.highLightColor1=newValue).build());
            itemHighlight.add(entryBuilder.startAlphaColorField(Text.translatable("bedrockify.options.inventoryHighlight.color2"), settingsClient.highLightColor2).setDefaultValue(64 + (170 << 8) + (109 << 16) + (255 << 24)).setSaveConsumer(newValue -> settingsClient.highLightColor2=newValue).build());
            gui.addEntry(itemHighlight.build());

            // Screen Safe Area
            SubCategoryBuilder screenSafeArea = entryBuilder.startSubCategory(Text.translatable("bedrockify.options.subCategory.screenSafeArea"));
            screenSafeArea.add(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.screenSafeArea"), settingsClient.screenSafeArea,0,30).setDefaultValue(0).setSaveConsumer((newValue)-> settingsClient.screenSafeArea=newValue).build());
            screenSafeArea.add(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.ignoreBorder"), settingsClient.overlayIgnoresSafeArea).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.overlayIgnoresSafeArea=newValue).build());
            gui.addEntry(screenSafeArea.build());

            //Other gui improvements.
            gui.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.biggerItems"), settingsClient.biggerIcons).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.biggerIcons=newValue).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.transparentToolBar"), settingsClient.transparentHotBar).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.transparentHotBar=newValue).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.expTextStyle"), settingsClient.expTextStyle).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.expTextStyle=newValue).setYesNoTextSupplier((value)->value ? Text.translatable("bedrockify.options.chatStyle.bedrock") : Text.translatable("bedrockify.options.chatStyle.vanilla")).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.chatStyle"), settingsClient.bedrockChat).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.bedrockChat=newValue).setYesNoTextSupplier((value)->value ? Text.translatable("bedrockify.options.chatStyle.bedrock") : Text.translatable("bedrockify.options.chatStyle.vanilla")).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.pickupAnimations"), settingsClient.pickupAnimations).setTooltip(wrapLines(Text.translatable("bedrockify.options.pickupAnimations.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.pickupAnimations=newValue).build());
            gui.addEntry(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.hudOpacity"), settingsClient.hudOpacity,0,100).setDefaultValue(50).setSaveConsumer((newValue)-> {
                settingsClient.hudOpacity = newValue;
                BedrockifyClient.getInstance().hudOpacity.resetTicks();
            }).build());
            gui.addEntry(entryBuilder.startEnumSelector(Text.translatable("bedrockify.options.showBedrockIfyButton"), BedrockifyClientSettings.ButtonPosition.class, settingsClient.bedrockIfyButtonPosition).setTooltip(wrapLines(Text.translatable("bedrockify.options.showBedrockIfyButton.tooltip"))).setEnumNameProvider(anEnum -> Text.translatable(((BedrockifyClientSettings.ButtonPosition)anEnum).text)).setDefaultValue(BedrockifyClientSettings.ButtonPosition.BELOW_SLIDERS).setSaveConsumer(buttonPosition -> settingsClient.bedrockIfyButtonPosition =buttonPosition).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.editionBranding"), settingsClient.hideEditionBranding).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.hideEditionBranding =newValue).build());


        /*
         *
         *   Visual Improvements Category
         *
         */
            visualImprovements.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.fishingBobber3D"), settingsClient.fishingBobber3D).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.fishingBobber3D=newValue).build());
            visualImprovements.addEntry(entryBuilder.startSelector(Text.translatable("bedrockify.options.idleAnimation"), new Float []{0.0f,0.5f,1.0f,1.5f,2.0f,2.5f,3.0f,4.0f}, settingsClient.idleAnimation).setDefaultValue(1.0f).setNameProvider((value)-> Text.literal("x"+ value)).setSaveConsumer((newValue)-> settingsClient.idleAnimation=newValue).build());
            visualImprovements.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.eatingAnimations"), settingsClient.eatingAnimations).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.eatingAnimations=newValue).build());
            visualImprovements.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.bedrockShading"), settingsClient.bedrockShading).setTooltip(wrapLines(Text.translatable("bedrockify.options.bedrockShading.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> {
                settingsClient.bedrockShading=newValue;
                MinecraftClient.getInstance().worldRenderer.reload();
            }).build());
            visualImprovements.addEntry(entryBuilder.startIntSlider(Text.translatable("bedrockify.options.sunlightIntensity"), settingsClient.sunlightIntensity,0,100).setTooltip(wrapLines(Text.translatable("bedrockify.options.sunlightIntensity.tooltip"))).setDefaultValue(50).setSaveConsumer(newValue -> {
                settingsClient.sunlightIntensity = newValue;
                BedrockifyClient.getInstance().bedrockSunGlareShading.onSunlightIntensityChanged();
            }).build());

        /*
         *
         *   Panorama Screen Category
         *
         */
            panorama.addEntry(entryBuilder.startBooleanToggle(Text.translatable("bedrockify.options.rotationalBackground"), settingsClient.cubeMapBackground).setDefaultValue(true).setTooltip(wrapLines(Text.translatable("bedrockify.options.rotationalBackground.tooltip"))).setSaveConsumer(newValue -> settingsClient.cubeMapBackground=newValue).build());
            panorama.addEntry(entryBuilder.startStrList(Text.translatable("bedrockify.options.panorama.list"), settingsClient.panoramaIgnoredScreens).setDefaultValue(BedrockifyClientSettings.PANORAMA_IGNORED_SCREENS).setTooltip(wrapLines(Text.translatable("bedrockify.options.panorama.list.tooltip"))).setSaveConsumer(strings -> settingsClient.panoramaIgnoredScreens=strings).build());

        /*
         *
         *   Mixins Category
         *
         */
            mixins.addEntry(entryBuilder.startTextDescription(Text.translatable("bedrockify.options.mixins.description")).build());
            for(Map.Entry<String, Boolean> elem : MixinFeatureManager.features.entrySet()){
                mixins.addEntry(entryBuilder.startBooleanToggle(Text.translatable(elem.getKey()), elem.getValue()).setDefaultValue(true).setSaveConsumer(newValue -> MixinFeatureManager.features.put(elem.getKey(),newValue)).build());
            }



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
