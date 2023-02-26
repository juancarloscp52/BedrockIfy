package me.juancarloscp52.bedrockify.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsScreen;
import net.minecraft.client.gui.screen.pack.PackScreen;

import java.util.Arrays;
import java.util.List;

public class BedrockifyClientSettings {

    private final static List<Class<? extends Screen>> MINECRAFT_IGNORED_SCREENS = Arrays.asList(PackScreen.class, SocialInteractionsScreen.class);
    public final static List<String> PANORAMA_IGNORED_SCREENS = Arrays.asList(".modmenu.gui.ModsScreen", ".iris.gui.", ".modmanager.gui.", "yacl.gui.YACLScreen");
    public boolean loadingScreen = true;
    public boolean bedrockIfyButton = true;
    public boolean showPositionHUD = true;
    public byte FPSHUD = 0;
    public byte heldItemTooltip = 2;
    public boolean showPaperDoll = true;
    public boolean showChunkMap = false;
    public boolean reacharound = true;
    public boolean reacharoundSneaking = true;
    public boolean reacharoundIndicator = true;
    public boolean reacharoundMultiplayer = true;
    public int positionHUDHeight = 50;
    public int screenSafeArea = 0;
    public boolean overlayIgnoresSafeArea = false;
    public boolean cubeMapBackground = true;
    public boolean bedrockChat = true;
    public boolean slotHighlight = true;
    public int highLightColor1 = (255 << 8) + (255) + (255 << 16) + (255 << 24);
    public int highLightColor2 = 64 + (170 << 8) + (109 << 16) + (255 << 24);
    public float idleAnimation = 1;
    public double reacharoundBlockDistance = 0.5d;
    public int reacharoundPitchAngle = 25;
    public boolean savingOverlay = true;
    public boolean eatingAnimations = true;
    public boolean expTextStyle = true;
    public boolean transparentHotBar = true;
    public int hudOpacity = 50;
    public boolean biggerIcons = true;
    public boolean sneakingShield = true;
    public boolean bedrockShading = true;
    public boolean disableFlyingMomentum = true;
    public boolean elytraStop = true;
    public boolean pickupAnimations = true;
    public boolean fishingBobber3D = true;
    public int sunlightIntensity = 50;

    public boolean isPickupAnimationsEnabled() {
        return pickupAnimations;
    }


    public List<String> panoramaIgnoredScreens = PANORAMA_IGNORED_SCREENS;

    public boolean panoramaIgnoreScreen(Screen screen) {
        if (screen != null) {
            // Checks if the screen is a Minecraft screen that would break in any case
            if (MINECRAFT_IGNORED_SCREENS.contains(screen.getClass()))
                return true;

            // Check If screen is in ignore list.
            for (String screenName : panoramaIgnoredScreens) {
                if (screen.getClass().getName().contains(screenName.trim()))
                    return true;
            }
            // check for language reload screen.
            return FabricLoader.getInstance().isModLoaded("languagereload") && screen.getClass().getName().contains("net.minecraft.class_426");
        }
        return false;
    }

    public boolean isSneakingShieldEnabled() {
        return this.sneakingShield;
    }


    public boolean isBiggerIconsEnabled() {
        return biggerIcons;
    }

    public boolean isTransparentHotBarEnabled() {
        return transparentHotBar;
    }

    public boolean isEatingAnimationsEnabled() {
        return eatingAnimations;
    }

    public boolean isCubemapBackgroundEnabled() {
        return cubeMapBackground;
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

    public byte getHeldItemTooltip() {
        return this.heldItemTooltip;
    }

    public byte getFPSHUDoption() {
        return FPSHUD;
    }

    public double getReacharoundBlockDistance() {
        return reacharoundBlockDistance;
    }

    public int getReacharoundPitchAngle() {
        return reacharoundPitchAngle;
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

    public boolean isBedrockIfyButtonEnabled() {
        return bedrockIfyButton;
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
