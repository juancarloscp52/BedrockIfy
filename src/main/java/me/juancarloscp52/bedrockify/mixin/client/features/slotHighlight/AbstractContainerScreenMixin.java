package me.juancarloscp52.bedrockify.mixin.client.features.slotHighlight;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.world.inventory.Slot;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Unique
    private static final int LINE_RENDER_WIDTH = 1;
    @Unique
    private static final int SLOT_RENDER_SIZE = 16;

    @Shadow
    protected @Nullable Slot hoveredSlot;

    @Inject(method = "extractSlotHighlightFront", at = @At("HEAD"), cancellable = true)
    private void bedrockify$cancelVanillaHighlightFront(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if (settings.isSlotHighlightEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method = "extractSlotHighlightBack", at = @At("HEAD"), cancellable = true)
    private void bedrockify$replaceVanillaHighlightBack(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if (!settings.isSlotHighlightEnabled() || hoveredSlot == null || !hoveredSlot.isHighlightable()) {
            return;
        }
        ci.cancel();

        final AbstractContainerScreen<?> $this = AbstractContainerScreen.class.cast(this);
        final int highlight1 = settings.getHighLightColor1();
        final int highlight2 = settings.getHighLightColor2();

        final int expandStartX, expandStartY, expandEndX, expandEndY;
        if (($this instanceof AbstractFurnaceScreen && hoveredSlot.index == 2) ||
                ($this instanceof CraftingScreen && hoveredSlot.index == 0) ||
                ($this instanceof StonecutterScreen && hoveredSlot.index == 1) ||
                ($this instanceof CartographyTableScreen && hoveredSlot.index == 2)
        ) {
            expandStartX = expandEndX = 4;
            expandStartY = expandEndY = 4;
        } else if ($this instanceof LoomScreen && hoveredSlot.index == 3) {
            expandStartX = expandEndX = 4;
            expandStartY = 4;
            expandEndY = 4;
        } else if ($this instanceof MerchantScreen && hoveredSlot.index == 2) {
            expandStartX = expandEndX = 4;
            expandStartY = 4;
            expandEndY = 4;
        } else {
            expandStartX = expandEndX = expandStartY = expandEndY = 0;
        }

        final int fillStartX = hoveredSlot.x - expandStartX;
        final int fillStartY = hoveredSlot.y - expandStartY;
        final int fillEndX = hoveredSlot.x + expandEndX + SLOT_RENDER_SIZE;
        final int fillEndY = hoveredSlot.y + expandEndY + SLOT_RENDER_SIZE;
        final int outlineLeftX = fillStartX - LINE_RENDER_WIDTH;
        final int outlineTopY = fillStartY - LINE_RENDER_WIDTH;
        final int outlineRightX = fillEndX + LINE_RENDER_WIDTH;
        final int outlineBottomY = fillEndY + LINE_RENDER_WIDTH;

        // ** outlines
        // Top-horizontal
        graphics.fill(outlineLeftX, outlineTopY, outlineRightX, outlineTopY + LINE_RENDER_WIDTH, highlight1);
        // Bottom-horizontal
        graphics.fill(outlineLeftX, outlineBottomY - LINE_RENDER_WIDTH, outlineRightX, outlineBottomY, highlight1);
        // Left-vertical
        graphics.fill(outlineLeftX, outlineTopY, outlineLeftX + LINE_RENDER_WIDTH, outlineBottomY, highlight1);
        // Right-vertical
        graphics.fill(outlineRightX - LINE_RENDER_WIDTH, outlineTopY, outlineRightX, outlineBottomY, highlight1);
        // ** end of outlines

        // highlight
        graphics.fill(fillStartX, fillStartY, fillEndX, fillEndY, highlight2);
    }
}
