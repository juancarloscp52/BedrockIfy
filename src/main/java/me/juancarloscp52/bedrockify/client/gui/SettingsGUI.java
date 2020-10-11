package me.juancarloscp52.bedrockify.client.gui;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.BedrockifySettings;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.*;

import java.util.List;

public class SettingsGUI {

    BedrockifySettings settings = Bedrockify.getInstance().settings;

    public Screen getConfigScreen(Screen parent, boolean isTransparent){
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(new TranslatableText("bedrockify.options.settings"));
        builder.setSavingRunnable(()-> Bedrockify.getInstance().saveSettings());
        ConfigCategory general = builder.getOrCreateCategory(new LiteralText("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        SubCategoryBuilder bedrockOverlay = entryBuilder.startSubCategory(new TranslatableText("bedrockify.options.subCategory.bedrockOverlay"));
        bedrockOverlay.add(entryBuilder.startTextDescription(new TranslatableText("bedrockify.options.subCategory.bedrockOverlay.description")).build());
        bedrockOverlay.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.showCoordinates"), settings.showPositionHUD).setDefaultValue(true).setSaveConsumer(newValue -> settings.showPositionHUD=newValue).build());
        bedrockOverlay.add(entryBuilder.startSelector(new TranslatableText("bedrockify.options.showFPS"), new Byte []{0,1,2},settings.FPSHUD).setDefaultValue((byte) 0).setNameProvider((value)->{
            switch (value){
                case 0: return new TranslatableText("bedrockify.options.off");
                case 1: return new TranslatableText( "bedrockify.options.withPosition");
                default: return new TranslatableText( "bedrockify.options.underPosition");
            }
        }).setSaveConsumer((newValue)->settings.FPSHUD=newValue).build());
        bedrockOverlay.add(entryBuilder.startIntSlider(new TranslatableText("bedrockify.options.coordinatesPosition"),settings.positionHUDHeight,0,100).setDefaultValue(50).setSaveConsumer((newValue)->settings.positionHUDHeight=newValue).build());
        bedrockOverlay.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.showPaperDoll"), settings.showPaperDoll).setDefaultValue(true).setSaveConsumer(newValue -> settings.showPaperDoll=newValue).build());
        bedrockOverlay.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.showSavingOverlay"), settings.savingOverlay).setDefaultValue(true).setSaveConsumer(newValue -> settings.savingOverlay=newValue).build());
        general.addEntry(bedrockOverlay.build());
        SubCategoryBuilder guiImprovements = entryBuilder.startSubCategory(new TranslatableText("bedrockify.options.subCategory.visualImprovements"));
        guiImprovements.add(entryBuilder.startIntSlider(new TranslatableText("bedrockify.options.screenSafeArea"),settings.screenSafeArea,0,30).setDefaultValue(0).setSaveConsumer((newValue)->settings.screenSafeArea=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.chatStyle"), settings.bedrockChat).setDefaultValue(true).setSaveConsumer(newValue -> settings.bedrockChat=newValue).setYesNoTextSupplier((value)->value ? new TranslatableText("bedrockify.options.chatStyle.bedrock") : new TranslatableText("bedrockify.options.chatStyle.vanilla")).build());
        guiImprovements.add(entryBuilder.startSelector(new TranslatableText("bedrockify.options.tooltips"), new Byte []{0,1,2},settings.heldItemTooltip).setDefaultValue((byte) 2).setNameProvider((value)->{
            switch (value){
                case 0: return new TranslatableText("bedrockify.options.off");
                case 1: return new TranslatableText( "bedrockify.options.on");
                default: return new TranslatableText( "bedrockify.options.withBackground");
            }
        }).setSaveConsumer((newValue)->settings.heldItemTooltip=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.inventoryHighlight"), settings.slotHighlight).setDefaultValue(true).setSaveConsumer(newValue -> settings.slotHighlight=newValue).build());
        guiImprovements.add(entryBuilder.startSelector(new TranslatableText("bedrockify.options.idleAnimation"), new Float []{0.0f,0.5f,1.0f,1.5f,2.0f,2.5f,3.0f,4.0f},settings.idleAnimation).setDefaultValue(1.0f).setNameProvider((value)-> new LiteralText("x"+ value)).setSaveConsumer((newValue)->settings.idleAnimation=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.eatingAnimations"), settings.eatingAnimations).setDefaultValue(true).setSaveConsumer(newValue -> settings.eatingAnimations=newValue).build());
        guiImprovements.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.pickupAnimations"), settings.pickupAnimations).setTooltip(wrapLines(new TranslatableText("bedrockify.options.pickupAnimations.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settings.pickupAnimations=newValue).build());
        general.addEntry(guiImprovements.build());

        SubCategoryBuilder reachAround = entryBuilder.startSubCategory(new TranslatableText("bedrockify.options.subCategory.Reach-Around"));
        reachAround.add(entryBuilder.startTextDescription(new TranslatableText("bedrockify.options.subCategory.Reach-Around.description")).build());
        reachAround.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.reachAround"), settings.reacharound).setDefaultValue(true).setSaveConsumer(newValue -> settings.reacharound=newValue).build());
        reachAround.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.reachAround.multiplayer"), settings.reacharoundMultiplayer).setDefaultValue(true).setSaveConsumer(newValue -> settings.reacharoundMultiplayer=newValue).build());
        reachAround.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.reachAround.sneaking"), settings.reacharoundSneaking).setDefaultValue(true).setSaveConsumer(newValue -> settings.reacharoundSneaking=newValue).build());
        reachAround.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.reachAround.indicator"), settings.reacharoundIndicator).setDefaultValue(true).setSaveConsumer(newValue -> settings.reacharoundIndicator=newValue).build());
        general.addEntry(reachAround.build());
        SubCategoryBuilder otherSettings = entryBuilder.startSubCategory(new TranslatableText("bedrockify.options.subCategory.other"));
        otherSettings.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.loadingScreenChunkMap"), settings.showChunkMap).setTooltip(wrapLines(new TranslatableText("bedrockify.options.loadingScreenChunkMap.tooltip"))).setDefaultValue(false).setSaveConsumer(newValue -> settings.showChunkMap=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.rotationalBackground"), settings.cubeMapBackground).setDefaultValue(true).setTooltip(wrapLines(new TranslatableText("bedrockify.options.rotationalBackground.tooltip"))).setSaveConsumer(newValue -> settings.cubeMapBackground=newValue).build());
        otherSettings.add(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.showBedrockIfyButton"), settings.bedrockIfyButton).setDefaultValue(true).setTooltip(wrapLines(new TranslatableText("bedrockify.options.showBedrockIfyButton.tooltip"))).setSaveConsumer(newValue -> settings.bedrockIfyButton=newValue).build());
        general.addEntry(otherSettings.build());
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.reachAround.recipes"), settings.bedrockRecipes).setTooltip(wrapLines(new TranslatableText("bedrockify.options.reachAround.recipes.tooltip"))).setDefaultValue(true).setSaveConsumer(newValue -> settings.bedrockRecipes=newValue).build());
        return builder.setTransparentBackground(isTransparent).build();
    }

    public Text[] wrapLines(Text text){
        List<StringVisitable> lines = MinecraftClient.getInstance().textRenderer.getTextHandler().wrapLines(text,Math.max(MinecraftClient.getInstance().getWindow().getScaledWidth()/2 - 43,170), Style.EMPTY);
        lines.get(0).getString();
        Text[] textLines = new Text[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            textLines[i]=new LiteralText(lines.get(i).getString());
        }
        return textLines;
    }

}
