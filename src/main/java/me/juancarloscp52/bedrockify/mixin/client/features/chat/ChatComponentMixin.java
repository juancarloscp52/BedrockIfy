package me.juancarloscp52.bedrockify.mixin.client.features.chat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {

    @Shadow @Final private List<GuiMessage.Line> trimmedMessages;
    @Shadow protected abstract boolean isChatFocused();
    @Shadow public abstract double getScale();
    @Shadow @Final private Minecraft minecraft;
    @Shadow private int chatScrollbarPos;
    @Shadow public abstract int getLinesPerPage();
    @Shadow protected abstract int getLineHeight();

    @Unique
    private double bottomY;
    @Unique
    BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;

    @Unique
    private int bedrockify$getSafeArea() {
        return settings.overlayIgnoresSafeArea ? 0 : settings.getScreenSafeArea();
    }

    @Unique
    private float bedrockify$getHudOpacity() {
        return BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
    }

    @Unique
    private int bedrockify$calcChatHudTopOffset() {
        final int safeArea = this.bedrockify$getSafeArea();
        int top = settings.getPositionHUDHeight();
        if (settings.getPositionHUDHeight() < 50) {
            top += 50;
        }
        return top + BedrockifyClient.getInstance().overlay.getTextsTopOffset() + safeArea - 8;
    }

    @ModifyExpressionValue(method = "forEachLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent$AlphaCalculator;calculate(Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;)F"))
    private float bedrockify$changeHudOpacity(float original) {
        if (this.isChatFocused()) {
            return original;
        }

        return original * this.bedrockify$getHudOpacity();
    }

    @ModifyReturnValue(method = "getLinesPerPage", at = @At("RETURN"))
    private int bedrockify$modifyLineCount(int original) {
        if (!settings.isBedrockChatEnabled() || minecraft.getDebugOverlay().showDebugScreen()) {
            return original;
        }

        final int height = this.minecraft.getWindow().getGuiScaledHeight() - this.bedrockify$calcChatHudTopOffset();
        final int lines = Mth.ceil((float) height / this.getLineHeight()) - 4;
        return Math.min(original, lines);
    }

    @ModifyExpressionValue(method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(F)I"))
    private int bedrockify$moveChatHud(int original) {
        if (!settings.isBedrockChatEnabled() || minecraft.getDebugOverlay().showDebugScreen()) {
            return original;
        }

        return (int) this.bottomY;
    }

    /**
     * Use bedrock-like chat if enabled.
     */
    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V", at = @At("HEAD"))
    private void bedrockify$gatherInfo(ChatComponent.ChatGraphicsAccess backend, int windowHeight, int currentTick, ChatComponent.DisplayMode displayMode, CallbackInfo ci) {
        if (!settings.isBedrockChatEnabled() || minecraft.getDebugOverlay().showDebugScreen()) {
            return;
        }

        int notifications = 0;
        for (GuiMessage.Line line : this.trimmedMessages) {
            if (currentTick - line.addedTime() < 200) {
                ++notifications;
            }
        }
        final int visibleLines = this.getLinesPerPage();
        final int shownLines = this.isChatFocused() ?
                Math.min(visibleLines, this.trimmedMessages.size() - this.chatScrollbarPos) :
                Math.min(visibleLines, notifications);
        final double shownHeight = shownLines * (this.getLineHeight() + this.minecraft.options.chatLineSpacing().get());
        this.bottomY = (8 + shownHeight + this.bedrockify$calcChatHudTopOffset()) / this.getScale();
    }
}
