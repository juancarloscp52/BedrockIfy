package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBar;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ContextualBar.class, priority = 500)
public interface ContextualBarRendererMixin {
    @WrapOperation(method = "extractExperienceLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)V"))
    private static void drawExperienceBar(GuiGraphicsExtractor drawContext, Font textRenderer, Component text, int x, int y, int color, boolean shadow, Operation<Void> original) {
        int alpha = (int) Math.ceil(BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false) * 255);
        int screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();

        if (!BedrockifyClient.getInstance().settings.isExpTextStyle()) {
            original.call(drawContext, textRenderer, text, x, y - screenBorder, color | ((alpha) << 24), false);
        } else if (color == -8323296) {
            drawContext.text(textRenderer, text, x, y - screenBorder - 3, ARGB.color(alpha, 127, 252, 32));
        }
    }
}
