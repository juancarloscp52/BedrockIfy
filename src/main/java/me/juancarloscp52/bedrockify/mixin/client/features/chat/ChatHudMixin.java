package me.juancarloscp52.bedrockify.mixin.client.features.chat;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;
    @Shadow protected abstract boolean isChatFocused();
    @Shadow public abstract double getChatScale();
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scrolledLines;
    @Shadow public abstract int getVisibleLineCount();
    @Shadow protected abstract int getLineHeight();

    @Unique
    private float bottomY;
    @Unique
    BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
    @Unique
    private static final String DRAW_CONTEXT_FILL_METHOD_SIGNATURE = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V";

    @Unique
    private int bedrockify$getSafeArea() {
        return settings.overlayIgnoresSafeArea ? 0 : settings.getScreenSafeArea();
    }

    @Unique
    private int bedrockify$applyHudOpacity(int color) {
        if (this.isChatFocused()) {
            return color;
        }

        final float opacity = BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
        if (MathHelper.approximatelyEquals(opacity, 1.0f)) {
            return color;
        }

        final int rgb = color & 0xffffff;
        final int alpha = color >> 24 & 0xff;
        final int targetAlpha = Math.max(4, (int) (opacity * alpha));

        return rgb | targetAlpha << 24;
    }

    @Unique
    private int bedrockify$calcChatHudTopOffset() {
        final int safeArea = this.bedrockify$getSafeArea();
        return settings.getPositionHUDHeight() + ((settings.getPositionHUDHeight() < 50) ? 50 : 0) + (settings.isShowPositionHUDEnabled() ? 10 : 0) + ((settings.getFPSHUDoption() == 2) ? 10 : 0) + safeArea - 6;
    }

    @Inject(method = "getVisibleLineCount", at = @At("RETURN"), cancellable = true)
    private void bedrockify$modifyLineCount(CallbackInfoReturnable<Integer> cir) {
        if (!settings.isBedrockChatEnabled() || client.inGameHud.getDebugHud().shouldShowDebugHud()) {
            return;
        }

        final int original = cir.getReturnValue();
        final int height = this.client.getWindow().getScaledHeight() - this.bedrockify$calcChatHudTopOffset();
        final int lines = MathHelper.ceil((float) height / this.getLineHeight()) - 3;
        cir.setReturnValue(Math.min(original, lines));
    }

    /**
     * Use bedrock-like chat if enabled.
     */
    @Inject(method = "render", at = @At("HEAD"))
    private void bedrockify$gatherInfo(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        if (!settings.isBedrockChatEnabled() || client.inGameHud.getDebugHud().shouldShowDebugHud()) {
            return;
        }

        int notifications = 0;
        for (ChatHudLine.Visible line : this.visibleMessages) {
            if (currentTick - line.addedTime() < 200) {
                ++notifications;
            }
        }
        final int visibleLines = this.getVisibleLineCount();
        final int shownLines = this.isChatFocused() ?
                Math.min(visibleLines, this.visibleMessages.size() - this.scrolledLines) :
                Math.min(visibleLines, notifications);
        final double shownHeight = shownLines * this.getLineHeight() * this.getChatScale();
        this.bottomY = (float) (48 - this.client.getWindow().getScaledHeight() + shownHeight) + this.bedrockify$calcChatHudTopOffset();
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    private void bedrockify$moveChatHud(Args args) {
        args.set(0, (float) args.get(0) + this.bedrockify$getSafeArea());
        if (settings.isBedrockChatEnabled() && !client.inGameHud.getDebugHud().shouldShowDebugHud()) {
            args.set(1, this.bottomY);
        }
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = DRAW_CONTEXT_FILL_METHOD_SIGNATURE, ordinal = 0), index = 4)
    private int bedrockify$modifyMsgBackgroundOpacity(int color) {
        return this.bedrockify$applyHudOpacity(color);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = DRAW_CONTEXT_FILL_METHOD_SIGNATURE, ordinal = 1), index = 4)
    private int bedrockify$modifyIndicatorOpacity(int color) {
        return this.bedrockify$applyHudOpacity(color);
    }

    @ModifyArg(method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I",
                    ordinal = 0
            ),
            index = 4
    )
    private int bedrockify$modifyRenderTextOpacity(int color) {
        return this.bedrockify$applyHudOpacity(color);
    }

    @ModifyArg(method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I",
                    ordinal = 0
            ),
            index = 4
    )
    private int bedrockify$modifyMsgQueueOpacity(int color) {
        return this.bedrockify$applyHudOpacity(color);
    }

    @Inject(method = "toChatLineY", at = @At("RETURN"), cancellable = true)
    private void bedrockify$calcChatLineY(double mouseY, CallbackInfoReturnable<Double> cir) {
        if (!settings.isBedrockChatEnabled() || client.inGameHud.getDebugHud().shouldShowDebugHud()) {
            return;
        }

        final int topY = this.bedrockify$calcChatHudTopOffset();
        final int lines = Math.min(this.getVisibleLineCount(), this.visibleMessages.size() - this.scrolledLines) + 1;
        final double position = topY + lines * this.getLineHeight() - mouseY-1;
        cir.setReturnValue(position / (this.getChatScale() * this.getLineHeight()));
    }

    @Inject(method = "toChatLineX", at = @At("RETURN"), cancellable = true)
    private void bedrockify$calcChatLineX(double mouseX, CallbackInfoReturnable<Double> cir) {
        final double original = cir.getReturnValue();
        cir.setReturnValue(original - this.bedrockify$getSafeArea());
    }
}
