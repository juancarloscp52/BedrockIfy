package me.juancarloscp52.bedrockify.mixin.client.features.heldItemTooltips;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.HeldItemTooltips;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class ItemTooltipsMixin {
    @Shadow private ItemStack lastToolHighlight;

    @Shadow private int toolHighlightTimer;

    @Shadow @Final private Minecraft minecraft;

    @Unique
    private final HeldItemTooltips heldItemTooltips = BedrockifyClient.getInstance().heldItemTooltips;

    /**
     * Draw custom tooltips for effects and enchantments before the heldItemTooltip is rendered.
     */
    @WrapOperation(method = "extractSelectedItemName", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;textWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)V"))
    private void drawCustomTooltips(GuiGraphicsExtractor instance, Font textRenderer, Component text, int x, int y, int width, int color, Operation<Void> original) {
        if (BedrockifyClient.getInstance().settings.heldItemTooltips) {
            heldItemTooltips.drawItemWithCustomTooltips(instance, textRenderer, text, x, Minecraft.getInstance().getWindow().getGuiScaledHeight() - 38, color, lastToolHighlight);
        } else {
            original.call(instance, textRenderer, text, x, y, width, color);
        }
    }

    /**
     * Show the item tooltip when changing from an item to another of the same type and name IFF different tooltips.
     */
    @WrapOperation(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1))
    private boolean interceptItemStack(ItemStack instance, Operation<Boolean> original) {
        if (this.minecraft.player == null) {
            return original.call(instance);
        }

        ItemStack nextItem = this.minecraft.player.getMainHandItem();
        if(instance.getItem() == this.lastToolHighlight.getItem() && !heldItemTooltips.equals(lastToolHighlight,nextItem)){
            this.toolHighlightTimer = 41;
            return true;
        }

        return original.call(instance);
    }
}
