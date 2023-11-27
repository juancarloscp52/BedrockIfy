package me.juancarloscp52.bedrockify.mixin.client.features.slotHighlight;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Unique
    private static final int LINE_RENDER_WIDTH = 1;
    @Unique
    private static final int SLOT_RENDER_SIZE = 16;
    @Unique
    private Slot currentSlot;

    @Shadow
    protected int x;
    @Shadow
    protected int y;

    @Shadow
    protected abstract boolean isPointOverSlot(Slot slot, double pointX, double pointY);

    @Inject(method = "drawSlotHighlight", at = @At("HEAD"), cancellable = true)
    private static void bedrockify$cancelVanillaHighlight(DrawContext context, int x, int y, int z, CallbackInfo ci) {
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if (settings.isSlotHighlightEnabled()) {
            ci.cancel();
        }
    }

    @ModifyVariable(method = "render", at = @At("STORE"))
    private Slot bedrockify$storeSlotInLoop(Slot slot) {
        this.currentSlot = slot;
        return slot;
    }

    /**
     * Draw the current slot in green.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlot(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/screen/slot/Slot;)V"))
    private void bedrockify$customHighlightColor(DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if (!settings.isSlotHighlightEnabled() || this.currentSlot == null) {
            return;
        }
        if (!this.isPointOverSlot(currentSlot, mouseX, mouseY) || !currentSlot.isEnabled()) {
            return;
        }

        final HandledScreen<?> $this = HandledScreen.class.cast(this);
        final int highlight1 = settings.getHighLightColor1();
        final int highlight2 = settings.getHighLightColor2();

        final int expandStartX, expandStartY, expandEndX, expandEndY;
        if (($this instanceof AbstractFurnaceScreen && currentSlot.id == 2) ||
                ($this instanceof CraftingScreen && currentSlot.id == 0) ||
                ($this instanceof StonecutterScreen && currentSlot.id == 1) ||
                ($this instanceof CartographyTableScreen && currentSlot.id == 2)
        ) {
            expandStartX = expandEndX = 4;
            expandStartY = expandEndY = 4;
        } else if ($this instanceof LoomScreen && currentSlot.id == 3) {
            expandStartX = expandEndX = 4;
            expandStartY = 5;
            expandEndY = 3;
        } else if ($this instanceof MerchantScreen && currentSlot.id == 2) {
            expandStartX = expandEndX = 4;
            expandStartY = 3;
            expandEndY = 5;
        } else {
            expandStartX = expandEndX = expandStartY = expandEndY = 0;
        }

        final int fillStartX = currentSlot.x - expandStartX;
        final int fillStartY = currentSlot.y - expandStartY;
        final int fillEndX = currentSlot.x + expandEndX + SLOT_RENDER_SIZE;
        final int fillEndY = currentSlot.y + expandEndY + SLOT_RENDER_SIZE;
        final int outlineLeftX = fillStartX - LINE_RENDER_WIDTH;
        final int outlineTopY = fillStartY - LINE_RENDER_WIDTH;
        final int outlineRightX = fillEndX + LINE_RENDER_WIDTH;
        final int outlineBottomY = fillEndY + LINE_RENDER_WIDTH;

        // ** outlines
        // Top-horizontal
        drawContext.fill(outlineLeftX, outlineTopY, outlineRightX, outlineTopY + LINE_RENDER_WIDTH, highlight1);
        // Bottom-horizontal
        drawContext.fill(outlineLeftX, outlineBottomY - LINE_RENDER_WIDTH, outlineRightX, outlineBottomY, highlight1);
        // Left-vertical
        drawContext.fill(outlineLeftX, outlineTopY, outlineLeftX + LINE_RENDER_WIDTH, outlineBottomY, highlight1);
        // Right-vertical
        drawContext.fill(outlineRightX - LINE_RENDER_WIDTH, outlineTopY, outlineRightX, outlineBottomY, highlight1);
        // ** end of outlines

        // highlight
        drawContext.fill(fillStartX, fillStartY, fillEndX, fillEndY, highlight2);
    }
}
