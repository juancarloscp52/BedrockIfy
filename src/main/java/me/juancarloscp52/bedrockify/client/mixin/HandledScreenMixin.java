package me.juancarloscp52.bedrockify.client.mixin;


import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends DrawableHelper {

    @Shadow
    protected Slot focusedSlot;

    @Shadow
    protected abstract void drawSlot(MatrixStack matrices, Slot slot);

    /**
     * Draw the current slot in green.
     */
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;fillGradient(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    protected void renderGuiQuad(HandledScreen handledScreen, MatrixStack matrices, int xStart, int yStart, int xEnd, int yEnd, int colorStart, int colorEnd) {
        if(!BedrockifyClient.getInstance().settings.isSlotHighlightEnabled()){
            fillGradient(matrices, xStart, yStart, xEnd, yEnd, colorStart, colorEnd);
            return;
        }

        this.fillGradient(matrices, xStart - 1, yStart - 1, xEnd + 1, yEnd + 1, (255 << 8) + (255) + (255 << 16) + (255 << 24), (255 << 8) + (255) + (255 << 16) + (255 << 24));
        this.fillGradient(matrices, xStart, yStart, xEnd, yEnd, 64 + (170 << 8) + (109 << 16) + (255 << 24), 64 + (170 << 8) + (109 << 16) + (255 << 24));
        // Draw the slot again over the selected overlay.
        this.drawSlot(matrices, focusedSlot);
    }


}
