package me.juancarloscp52.bedrockify.client.mixin;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringRenderable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SubtitlesHud.class)
/*
 * Applies the screen border distance to the subtitles widget.
 */
public class SubtitlesHudMixin extends DrawableHelper {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"))
    private int drawWithScreenBorder(TextRenderer textRenderer, MatrixStack matrices, String text, float x, float y, int color) {
        int screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
        return textRenderer.draw(matrices, text, x - screenBorder, y - screenBorder, color);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/StringRenderable;FFI)I"))
    private int drawWithScreenBorder(TextRenderer textRenderer, MatrixStack matrices, StringRenderable text, float x, float y, int color) {
        int screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
        return textRenderer.draw(matrices, text, x - screenBorder, y - screenBorder, color);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/SubtitlesHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
    private void drawWithScreenBorder(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
        int screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
        fill(matrices, x1 - screenBorder, y1 - screenBorder, x2 - screenBorder, y2 - screenBorder, color);
    }
}
