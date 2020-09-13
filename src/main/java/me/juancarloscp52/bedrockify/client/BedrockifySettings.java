package me.juancarloscp52.bedrockify.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.*;
import java.util.List;

public class BedrockifySettings {
    private boolean bedrockIfyButton = true;
    private boolean showPositionHUD = true;
    private byte FPSHUD = 0;
    private byte heldItemTooltip=2;
    private boolean showPaperDoll = true;
    private boolean showChunkMap = false;
    private boolean reacharound = true;
    private boolean reacharoundMultiplayer = true;
    private int positionHUDHeight = 50;
    private int screenSafeArea = 0;
    private boolean cubeMapBackground = true;
    private boolean bedrockChat=true;
    private boolean slotHighlight = true;
    private float idleAnimation = 1;

    public boolean isCubemapBackgroundEnabled() {
        return cubeMapBackground;
    }

    public boolean isShowPositionHUDEnabled() {
        return showPositionHUD;
    }

    public byte getHeldItemTooltip(){
        return this.heldItemTooltip;
    }

    public byte getFPSHUDoption() {
        return FPSHUD;
    }

    public boolean isShowPaperDollEnabled() {
        return showPaperDoll;
    }

    public boolean isReacharoundMultiplayerEnabled() {
        return reacharoundMultiplayer;
    }

    public boolean isShowChunkMapEnabled() {
        return showChunkMap;
    }

    public boolean isBedrockIfyButtonEnabled() {
        return bedrockIfyButton;
    }

    public int getPositionHUDHeight() {
        if (positionHUDHeight > 100)
            positionHUDHeight = 100;
        return positionHUDHeight;
    }

    public int getScreenSafeArea() {
        if (screenSafeArea > 30)
            screenSafeArea = 30;
        return screenSafeArea;
    }

    public float getIdleAnimation(){
        return idleAnimation;
    }

    public boolean isReacharoundEnabled() {
        return reacharound;
    }

    public boolean isBedrockChatEnabled() {
        return bedrockChat;
    }

    public boolean isSlotHighlightEnabled() {
        return slotHighlight;
    }

    public Screen getConfigScreen(Screen parent, boolean isTransparent){
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(new TranslatableText("bedrockify.options.settings"));
        builder.setSavingRunnable(()->{
            BedrockifyClient.getInstance().saveSettings();
        });
        ConfigCategory general = builder.getOrCreateCategory(new LiteralText("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        general.addEntry(entryBuilder.startIntSlider(new TranslatableText("bedrockify.options.screenSafeArea"),screenSafeArea,0,30).setDefaultValue(0).setSaveConsumer((newValue)->screenSafeArea=newValue).build());
        general.addEntry(entryBuilder.startIntSlider(new TranslatableText("bedrockify.options.coordinatesPosition"),positionHUDHeight,0,100).setDefaultValue(50).setSaveConsumer((newValue)->positionHUDHeight=newValue).build());
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.showCoordinates"), showPositionHUD).setDefaultValue(true).setSaveConsumer(newValue -> showPositionHUD=newValue).build());
        general.addEntry(entryBuilder.startSelector(new TranslatableText("bedrockify.options.showFPS"), new Byte []{0,1,2},FPSHUD).setDefaultValue((byte) 0).setNameProvider((value)->{
            switch (value){
                case 0: return new TranslatableText("bedrockify.options.off");
                case 1: return new TranslatableText( "bedrockify.options.withPosition");
                default: return new TranslatableText( "bedrockify.options.underPosition");
            }
        }).setSaveConsumer((newValue)->FPSHUD=newValue).build());
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.showPaperDoll"), showPaperDoll).setDefaultValue(true).setSaveConsumer(newValue -> showPaperDoll=newValue).build());
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.loadingScreenChunkMap"), showChunkMap).setTooltip(wrapLines(new TranslatableText("bedrockify.options.loadingScreenChunkMap.tooltip"))).setDefaultValue(false).setSaveConsumer(newValue -> showChunkMap=newValue).build());
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.reachAround"), reacharound).setDefaultValue(true).setSaveConsumer(newValue -> reacharound=newValue).build());
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.reachAround.multiplayer"), reacharoundMultiplayer).setDefaultValue(true).setSaveConsumer(newValue -> reacharoundMultiplayer=newValue).build());
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.chatStyle"), bedrockChat).setDefaultValue(true).setSaveConsumer(newValue -> bedrockChat=newValue).setYesNoTextSupplier((value)->value ? new TranslatableText("bedrockify.options.chatStyle.bedrock") : new TranslatableText("bedrockify.options.chatStyle.vanilla")).build());
        general.addEntry(entryBuilder.startSelector(new TranslatableText("bedrockify.options.tooltips"), new Byte []{0,1,2},heldItemTooltip).setDefaultValue((byte) 2).setNameProvider((value)->{
            switch (value){
                case 0: return new TranslatableText("bedrockify.options.off");
                case 1: return new TranslatableText( "bedrockify.options.on");
                default: return new TranslatableText( "bedrockify.options.withBackground");
            }
        }).setSaveConsumer((newValue)->heldItemTooltip=newValue).build());
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.inventoryHighlight"), slotHighlight).setDefaultValue(true).setSaveConsumer(newValue -> slotHighlight=newValue).build());
        general.addEntry(entryBuilder.startSelector(new TranslatableText("bedrockify.options.idleAnimation"), new Float []{0.0f,0.5f,1.0f,1.5f,2.0f},idleAnimation).setDefaultValue(1.0f).setNameProvider((value)-> new LiteralText("x"+ value)).setSaveConsumer((newValue)->idleAnimation=newValue).build());
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.rotationalBackground"), cubeMapBackground).setDefaultValue(true).setTooltip(wrapLines(new TranslatableText("bedrockify.options.rotationalBackground.tooltip"))).setSaveConsumer(newValue -> cubeMapBackground=newValue).build());
        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("bedrockify.options.showBedrockIfyButton"), bedrockIfyButton).setDefaultValue(true).setTooltip(wrapLines(new TranslatableText("bedrockify.options.showBedrockIfyButton.tooltip"))).setSaveConsumer(newValue -> bedrockIfyButton=newValue).build());
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
