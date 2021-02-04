package me.juancarloscp52.bedrockify.mixin.client.features.slotHighlight;


import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.BedrockifySettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
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
        BedrockifySettings settings = Bedrockify.getInstance().settings;
        if(!settings.isSlotHighlightEnabled()){
            fillGradient(matrices, xStart, yStart, xEnd, yEnd, colorStart, colorEnd);
            return;
        }

        Screen currentScreen = MinecraftClient.getInstance().currentScreen;
        if((currentScreen instanceof AbstractFurnaceScreen && focusedSlot.id==2)||(currentScreen instanceof CraftingScreen && focusedSlot.id==0) || (currentScreen instanceof StonecutterScreen && focusedSlot.id==1) ||(currentScreen instanceof CartographyTableScreen && focusedSlot.id==2)){
            this.fillGradient(matrices, xStart - 5, yStart - 5, xEnd + 5, yEnd + 5, settings.getHighLightColor1(), settings.getHighLightColor1());
            this.fillGradient(matrices, xStart-4, yStart-4, xEnd+4, yEnd+4, settings.getHighLightColor2(), settings.getHighLightColor2());
        }else if ((currentScreen instanceof LoomScreen && focusedSlot.id==3)) {
            this.fillGradient(matrices, xStart - 5, yStart - 6, xEnd + 5, yEnd + 4, settings.getHighLightColor1(), settings.getHighLightColor1());
            this.fillGradient(matrices, xStart-4, yStart-5, xEnd+4, yEnd+3, settings.getHighLightColor2(), settings.getHighLightColor2());
        }else if((currentScreen instanceof MerchantScreen && focusedSlot.id==2)){
            this.fillGradient(matrices, xStart - 5, yStart - 4, xEnd + 5, yEnd + 6, settings.getHighLightColor1(), settings.getHighLightColor1());
            this.fillGradient(matrices, xStart-4, yStart-3, xEnd+4, yEnd+5, settings.getHighLightColor2(), settings.getHighLightColor2());
        }else{
            this.fillGradient(matrices, xStart - 1, yStart - 1, xEnd + 1, yEnd + 1, settings.getHighLightColor1(), settings.getHighLightColor1());
            this.fillGradient(matrices, xStart, yStart, xEnd, yEnd, settings.getHighLightColor2(), settings.getHighLightColor2());
        }
        // Draw the slot again over the selected overlay.
        this.drawSlot(matrices, focusedSlot);
    }


}
