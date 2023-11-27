package me.juancarloscp52.bedrockify.mixin.client.features.heldItemTooltips;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.heldItemTooltips.HeldItemTooltips;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public class ItemTooltipsMixin {
    @Shadow private ItemStack currentStack;

    @Shadow private int heldItemTooltipFade;

    @Shadow @Final private MinecraftClient client;

    /**
     * Draw custom tooltips for effects and enchantments before the heldItemTooltip is rendered.
     */
    @WrapOperation(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"))
    private int drawCustomTooltips(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, Operation<Integer> original) {
        if (BedrockifyClient.getInstance().settings.heldItemTooltips)
            return BedrockifyClient.getInstance().heldItemTooltips.drawItemWithCustomTooltips(instance,textRenderer, text, x, MinecraftClient.getInstance().getWindow().getScaledHeight() - 38, color, currentStack, original);
        return original.call(instance, textRenderer, text, x, y, color);
    }
    /**
     * Draw custom tooltips for effects and enchantments before the heldItemTooltip is rendered.
     */
    @WrapOperation(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private void drawCustomTooltips(DrawContext instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        if (!BedrockifyClient.getInstance().settings.heldItemTooltips)
            original.call(instance, x1, y1, x2, y2, color);
    }

    /**
     * Show the item tooltip when changing from an item to another of the same type and name IFF different tooltips.
     */
    @WrapOperation(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
    private boolean interceptItemStack(ItemStack stack, Operation<Boolean> original) {
        if (!BedrockifyClient.getInstance().settings.heldItemTooltips)
            return original.call(stack);

        ItemStack nextItem = this.client.player.getInventory().getMainHandStack();
        HeldItemTooltips heldItemTooltips = BedrockifyClient.getInstance().heldItemTooltips;
        if(!heldItemTooltips.equals(stack,nextItem)){
            this.heldItemTooltipFade = 41;
            return true;
        }

        return original.call(stack);
    }
}
