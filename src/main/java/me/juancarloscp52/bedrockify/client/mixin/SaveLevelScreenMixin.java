package me.juancarloscp52.bedrockify.client.mixin;

import me.juancarloscp52.bedrockify.client.gui.LoadingScreenWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SaveLevelScreen.class)
public class SaveLevelScreenMixin {
    /**
     * Renders the loading screen widget.
     */
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SaveLevelScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"))
    public void drawLoadingScreenWidget(MatrixStack matrices, TextRenderer textRenderer, Text text, int x, int y, int color) {
        LoadingScreenWidget.getInstance().render(matrices, MinecraftClient.getInstance().getWindow().getScaledWidth() / 2, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2, new LiteralText(text.getString()), null, -1);
    }
}
