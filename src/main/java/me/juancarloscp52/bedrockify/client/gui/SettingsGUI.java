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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;

public class SettingsGUI {

    BedrockifyClientSettings settingsClient = BedrockifyClient.getInstance().settings;
    BedrockifySettings settingsCommon = Bedrockify.getInstance().settings;

    public Screen getConfigScreen(Screen parent){
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Component.translatable("bedrockify.options.settings"));
        builder.setSavingRunnable(()-> {
            Bedrockify.getInstance().saveSettings();
            BedrockifyClient.getInstance().saveSettings();
            MixinFeatureManager.saveMixinSettings();
        });

        builder.setDefaultBackgroundTexture(Identifier.parse("minecraft:textures/block/bedrock.png"));
        // Create Categories
        ConfigCategory gameplay = builder.getOrCreateCategory(Component.translatable("bedrockify.options.categories.gameplay"));
        ConfigCategory gui = builder.getOrCreateCategory(Component.translatable("bedrockify.options.categories.gui"));
        ConfigCategory visualImprovements = builder.getOrCreateCategory(Component.translatable("bedrockify.options.categories.visualImprovements"));
        ConfigCategory mixins = builder.getOrCreateCategory(Component.translatable("bedrockify.options.categories.mixins"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        /*
        *
        *   Gameplay Category
        *
        */

            // Reach Around Placement Sub Category
            SubCategoryBuilder reachAround = entryBuilder.startSubCategory(Component.translatable("bedrockify.options.subCategory.Reach-Around"));
            reachAround.add(entryBuilder.startTextDescription(Component.translatable("bedrockify.options.subCategory.Reach-Around.description")).build());
            reachAround.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.reachAround"), settingsClient.reacharound).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.reacharound=newValue).build());
            reachAround.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.reachAround.multiplayer"), settingsClient.reacharoundMultiplayer).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.reacharoundMultiplayer=newValue).build());
            reachAround.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.reachAround.sneaking"), settingsClient.reacharoundSneaking).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.reacharoundSneaking=newValue).build());
            reachAround.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.reachAround.indicator"), settingsClient.reacharoundIndicator).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.reacharoundIndicator=newValue).build());
            gameplay.addEntry(reachAround.build());
        
            // Dying Trees.
            gameplay.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.dyingTrees"), settingsCommon.dyingTrees).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.dyingTrees=newValue).build());

            // Other Settings.
            gameplay.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.recipes"), settingsCommon.bedrockRecipes).setTooltip(wrapLines(Component.translatable("bedrockify.options.recipes.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.bedrockRecipes=newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.fireAspectLight"), settingsCommon.fireAspectLight).setTooltip(wrapLines(Component.translatable("bedrockify.options.fireAspectLight.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.fireAspectLight=newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.fernBonemeal"), settingsCommon.fernBonemeal).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.fernBonemeal=newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.disableFlyingMomentum"), settingsClient.disableFlyingMomentum).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.disableFlyingMomentum =newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.elytraStop"), settingsClient.elytraStop).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.elytraStop=newValue).build());
            gameplay.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.bedrockCauldron"), settingsCommon.bedrockCauldron).setTooltip(wrapLines(Component.translatable("bedrockify.options.bedrockCauldron.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsCommon.bedrockCauldron=newValue).build());

        /*
         *
         *   GUI Category
         *
         */
            // Bedrock loading screens.
            gui.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.loadingScreen"), settingsClient.loadingScreen).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.loadingScreen=newValue).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.loadingScreenChunkMap"), settingsClient.showChunkMap).setTooltip(wrapLines(Component.translatable("bedrockify.options.loadingScreenChunkMap.tooltip"))).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.showChunkMap=newValue).build());

            // Overlay
            SubCategoryBuilder bedrockOverlay = entryBuilder.startSubCategory(Component.translatable("bedrockify.options.subCategory.bedrockOverlay"));
            bedrockOverlay.add(entryBuilder.startTextDescription(Component.translatable("bedrockify.options.subCategory.bedrockOverlay.description")).build());
            bedrockOverlay.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.showCoordinates"), settingsClient.showPositionHUD).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.showPositionHUD=newValue).build());
            bedrockOverlay.add(entryBuilder.startEnumSelector(Component.translatable("bedrockify.options.showFPS"), BedrockifyClientSettings.FpsHudOption.class, settingsClient.FPSHUD).setDefaultValue(BedrockifyClientSettings.FpsHudOption.OFF).setEnumNameProvider(elem -> Component.translatable(((BedrockifyClientSettings.FpsHudOption) elem).translateKey)).setSaveConsumer((newValue)-> settingsClient.FPSHUD=newValue).build());
            bedrockOverlay.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.showDaysPlayed"), settingsClient.showDaysPlayed).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.showDaysPlayed = newValue).build());
            bedrockOverlay.add(entryBuilder.startIntSlider(Component.translatable("bedrockify.options.coordinatesPosition"), settingsClient.positionHUDHeight,0,100).setDefaultValue(50).setSaveConsumer((newValue)-> settingsClient.positionHUDHeight=newValue).build());
            bedrockOverlay.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.showPaperDoll"), settingsClient.showPaperDoll).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.showPaperDoll=newValue).build());
            bedrockOverlay.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.showSavingOverlay"), settingsClient.savingOverlay).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.savingOverlay=newValue).build());
            gui.addEntry(bedrockOverlay.build());

            //Tooltips
            SubCategoryBuilder tooltips = entryBuilder.startSubCategory(Component.translatable("bedrockify.options.subCategory.tooltips"));
            tooltips.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.tooltips"), settingsClient.heldItemTooltips).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.heldItemTooltips =newValue).build());
            tooltips.add(entryBuilder.startIntSlider(Component.translatable("bedrockify.options.tooltips.background"),(int)Math.ceil(settingsClient.heldItemTooltipBackground*100),0,100).setDefaultValue(50).setSaveConsumer(newValue -> settingsClient.heldItemTooltipBackground=newValue/100d).build());
            gui.addEntry(tooltips.build());

            //Item slot Highlight
            SubCategoryBuilder itemHighlight = entryBuilder.startSubCategory(Component.translatable("bedrockify.options.subCategory.inventoryHighlight"));
            itemHighlight.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.inventoryHighlight"), settingsClient.slotHighlight).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.slotHighlight=newValue).build());
            itemHighlight.add(entryBuilder.startAlphaColorField(Component.translatable("bedrockify.options.inventoryHighlight.color1"), settingsClient.highLightColor1).setDefaultValue(0xffffffff).setSaveConsumer(newValue -> settingsClient.highLightColor1=newValue).build());
            itemHighlight.add(entryBuilder.startAlphaColorField(Component.translatable("bedrockify.options.inventoryHighlight.color2"), settingsClient.highLightColor2).setDefaultValue(0x8955ba00).setSaveConsumer(newValue -> settingsClient.highLightColor2=newValue).build());
            gui.addEntry(itemHighlight.build());

            // Screen Safe Area
            SubCategoryBuilder screenSafeArea = entryBuilder.startSubCategory(Component.translatable("bedrockify.options.subCategory.screenSafeArea"));
            screenSafeArea.add(entryBuilder.startIntSlider(Component.translatable("bedrockify.options.screenSafeArea"), settingsClient.screenSafeArea,0,30).setDefaultValue(2).setSaveConsumer((newValue)-> settingsClient.screenSafeArea=newValue).build());
            screenSafeArea.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.HotBarOverhang"), settingsClient.hotBarOverhang).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.hotBarOverhang = newValue).build());
            screenSafeArea.add(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.ignoreBorder"), settingsClient.overlayIgnoresSafeArea).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.overlayIgnoresSafeArea=newValue).build());
            gui.addEntry(screenSafeArea.build());

            //Other gui improvements.
            gui.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.biggerItems"), settingsClient.biggerIcons).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.biggerIcons=newValue).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.toolbarStyle"), settingsClient.bedrockToolbar).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.bedrockToolbar =newValue).setYesNoTextSupplier((value)->value ? Component.translatable("bedrockify.options.chatStyle.bedrock") : Component.translatable("bedrockify.options.chatStyle.vanilla")).setTooltip(wrapLines(Component.translatable("bedrockify.options.toolbarStyle.tooltip"))).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.expTextStyle"), settingsClient.expTextStyle).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.expTextStyle=newValue).setYesNoTextSupplier((value)->value ? Component.translatable("bedrockify.options.chatStyle.bedrock") : Component.translatable("bedrockify.options.chatStyle.vanilla")).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.chatStyle"), settingsClient.bedrockChat).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.bedrockChat=newValue).setYesNoTextSupplier((value)->value ? Component.translatable("bedrockify.options.chatStyle.bedrock") : Component.translatable("bedrockify.options.chatStyle.vanilla")).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.pickupAnimations"), settingsClient.pickupAnimations).setTooltip(wrapLines(Component.translatable("bedrockify.options.pickupAnimations.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.pickupAnimations=newValue).build());
            gui.addEntry(entryBuilder.startIntSlider(Component.translatable("bedrockify.options.hudOpacity"), settingsClient.hudOpacity,0,100).setDefaultValue(50).setSaveConsumer((newValue)-> {
                settingsClient.hudOpacity = newValue;
                BedrockifyClient.getInstance().hudOpacity.resetTicks();
            }).build());
            gui.addEntry(entryBuilder.startEnumSelector(Component.translatable("bedrockify.options.showBedrockIfyButton"), BedrockifyClientSettings.ButtonPosition.class, settingsClient.bedrockIfyButtonPosition).setTooltip(wrapLines(Component.translatable("bedrockify.options.showBedrockIfyButton.tooltip"))).setEnumNameProvider(anEnum -> Component.translatable(((BedrockifyClientSettings.ButtonPosition)anEnum).text)).setDefaultValue(BedrockifyClientSettings.ButtonPosition.BELOW_SLIDERS).setSaveConsumer(buttonPosition -> settingsClient.bedrockIfyButtonPosition =buttonPosition).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.editionBranding"), settingsClient.hideEditionBranding).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.hideEditionBranding =newValue).build());
            gui.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.bedrockCubeMap"), settingsClient.bedrockCubeMap).setDefaultValue(false).setSaveConsumer(newValue -> settingsClient.bedrockCubeMap = newValue).build());


        /*
         *
         *   Visual Improvements Category
         *
         */
            visualImprovements.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.fishingBobber3D"), settingsClient.fishingBobber3D).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.fishingBobber3D=newValue).build());
            visualImprovements.addEntry(entryBuilder.startSelector(Component.translatable("bedrockify.options.idleAnimation"), new Float []{0.0f,0.5f,1.0f,1.5f,2.0f,2.5f,3.0f,4.0f}, settingsClient.idleAnimation).setDefaultValue(1.0f).setNameProvider((value)-> Component.literal("x"+ value)).setSaveConsumer((newValue)-> settingsClient.idleAnimation=newValue).build());
            visualImprovements.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.eatingAnimations"), settingsClient.eatingAnimations).setDefaultValue(true).setSaveConsumer(newValue -> settingsClient.eatingAnimations=newValue).build());
            visualImprovements.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bedrockify.options.bedrockShading"), settingsClient.bedrockShading).setTooltip(wrapLines(Component.translatable("bedrockify.options.bedrockShading.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> {
                settingsClient.bedrockShading=newValue;
                // final Minecraft client = Minecraft.getInstance();
                // if (client.level != null) {
                //     client.levelRenderer.invalidateCompiledGeometry(client.level, client.options, client.gameRenderer.mainCamera(), client.getBlockColors());
                // }
            }).build());
            visualImprovements.addEntry(entryBuilder.startIntSlider(Component.translatable("bedrockify.options.sunlightIntensity"), settingsClient.sunlightIntensity,0,100).setTooltip(wrapLines(Component.translatable("bedrockify.options.sunlightIntensity.tooltip"))).setDefaultValue(50).setSaveConsumer(newValue -> {
                settingsClient.sunlightIntensity = newValue;
                BedrockifyClient.getInstance().bedrockSunGlareShading.onSunlightIntensityChanged();
            }).build());

        /*
         *
         *   Mixins Category
         *
         */
            mixins.addEntry(entryBuilder.startTextDescription(Component.translatable("bedrockify.options.mixins.description")).build());
            for(Map.Entry<String, Boolean> elem : MixinFeatureManager.features.entrySet()){
                mixins.addEntry(entryBuilder.startBooleanToggle(Component.translatable(elem.getKey()), elem.getValue()).setDefaultValue(true).setSaveConsumer(newValue -> MixinFeatureManager.features.put(elem.getKey(),newValue)).build());
            }



        return builder.setTransparentBackground(true).build();
    }

    public Component[] wrapLines(Component text){
        List<FormattedText> lines = Minecraft.getInstance().font.getSplitter().splitLines(text,Math.max(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2 - 43,170), Style.EMPTY);
        lines.getFirst().getString();
        Component[] textLines = new Component[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            textLines[i]= Component.literal(lines.get(i).getString());
        }
        return textLines;
    }

}
