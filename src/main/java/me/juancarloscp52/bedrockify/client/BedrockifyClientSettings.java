package me.juancarloscp52.bedrockify.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

public class BedrockifyClientSettings {

    public enum ButtonPosition {
        DISABLED("bedrockify.options.button.hidden"),
        TOP_LEFT("bedrockify.options.button.topLeft"),
        TOP_RIGHT("bedrockify.options.button.topRight"),
        BOTTOM_LEFT("bedrockify.options.button.bottomLeft"),
        BOTTOM_RIGHT("bedrockify.options.button.bottomRight"),
        IN_GRID("bedrockify.options.button.inGrid"),
        BELOW_SLIDERS("bedrockify.options.button.underSliders");

        public final String text;
        ButtonPosition(String text) {
            this.text = text;
        }
    }

    public boolean loadingScreen = true;
    public ButtonPosition bedrockIfyButtonPosition = ButtonPosition.BELOW_SLIDERS;
    public boolean showPositionHUD = true;
    public byte FPSHUD = 0;
    public boolean heldItemTooltips = true;
    public double heldItemTooltipBackground = .5d;

    public boolean showPaperDoll = true;
    public boolean showChunkMap = false;
    public boolean reacharound = true;
    public boolean reacharoundSneaking = false;
    public boolean reacharoundIndicator = false;
    public boolean reacharoundMultiplayer = true;
    public int positionHUDHeight = 50;
    public int screenSafeArea = 0;
    public boolean overlayIgnoresSafeArea = false;
    public boolean bedrockChat = true;
    public boolean slotHighlight = true;
    public int highLightColor1 = 0xffffffff;
    public int highLightColor2 = 0x8955ba00;
    public float idleAnimation = 1;
    public boolean savingOverlay = true;
    public boolean eatingAnimations = true;
    public boolean expTextStyle = true;
    public boolean bedrockToolbar = true;
    public int hudOpacity = 50;
    public boolean biggerIcons = true;
    public boolean bedrockShading = true;
    public boolean disableFlyingMomentum = true;
    public boolean elytraStop = true;
    public boolean pickupAnimations = true;
    public boolean fishingBobber3D = true;
    public int sunlightIntensity = 50;
    public boolean sheepColors = true;
    public boolean hideEditionBranding = false;

    public boolean isPickupAnimationsEnabled() {
        return pickupAnimations;
    }

    public boolean isBiggerIconsEnabled() {
        return biggerIcons;
    }

    public boolean isBedrockToolbarEnabled() {
        return bedrockToolbar;
    }

    public boolean isEatingAnimationsEnabled() {
        return eatingAnimations;
    }

    public boolean isLoadingScreenEnabled() {
        return loadingScreen;
    }

    public boolean isShowPositionHUDEnabled() {
        return showPositionHUD && !MinecraftClient.getInstance().hasReducedDebugInfo();
    }

    public boolean isExpTextStyle() {
        return expTextStyle && !FabricLoader.getInstance().isModLoaded("colormatic");
    }


    public int getHighLightColor1() {
        return this.highLightColor1;
    }

    public int getHighLightColor2() {
        return this.highLightColor2;
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

    public boolean isReacharoundIndicatorEnabled() {
        return reacharoundIndicator;
    }

    public boolean isShowChunkMapEnabled() {
        return showChunkMap;
    }

    public boolean isSavingOverlayEnabled() {
        return savingOverlay;
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

    public float getIdleAnimation() {
        return idleAnimation;
    }

    public boolean isReacharoundEnabled() {
        return reacharound;
    }

    public boolean isReacharoundSneakingEnabled() {
        return reacharoundSneaking;
    }


    public boolean isBedrockChatEnabled() {
        return bedrockChat;
    }

    public boolean isSlotHighlightEnabled() {
        return slotHighlight;
    }
}
