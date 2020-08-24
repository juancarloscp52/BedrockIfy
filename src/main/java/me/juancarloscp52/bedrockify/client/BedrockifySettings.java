package me.juancarloscp52.bedrockify.client;

import net.minecraft.util.math.MathHelper;

public class BedrockifySettings {
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

    public void toggleBedrockChat(){
        this.bedrockChat=!this.bedrockChat;
    }

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

    public int getPositionHUDHeight() {
        if (positionHUDHeight > 100)
            positionHUDHeight = 100;
        return positionHUDHeight;
    }

    public void setPositionHUDHeight(double positionHUDHeight) {
        this.positionHUDHeight = MathHelper.ceil(positionHUDHeight * 100);
    }

    public int getScreenSafeArea() {
        if (screenSafeArea > 30)
            screenSafeArea = 30;
        return screenSafeArea;
    }

    public void setScreenSafeArea(double screenSafeArea) {
        this.screenSafeArea = MathHelper.ceil(screenSafeArea * 30);
    }

    public void toggleCubemapBackground() {
        this.cubeMapBackground = !this.cubeMapBackground;
    }

    public void toggleChunkMap() {
        this.showChunkMap = !this.showChunkMap;
    }

    public void togglePositionHUD() {
        this.showPositionHUD = !this.showPositionHUD;
    }

    public void toggleFPSHUD() {
        this.FPSHUD++;
        if (this.FPSHUD>2)
            this.FPSHUD=0;
    }
    public void toggleTooltipBackground() {
        this.heldItemTooltip++;
        if (this.heldItemTooltip>2)
            this.heldItemTooltip=0;
    }

    public void togglePaperDoll() {
        this.showPaperDoll = !this.showPaperDoll;
    }

    public void toggleReacharoundMultiplayer() {
        this.reacharoundMultiplayer = !reacharoundMultiplayer;
    }

    public boolean isReacharoundEnabled() {
        return reacharound;
    }

    public void toggleReacharound() {
        this.reacharound = !this.reacharound;
    }

    public boolean isBedrockChatEnabled() {
        return bedrockChat;
    }
}
