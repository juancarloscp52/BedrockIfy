package me.juancarloscp52.bedrockify.client.mixin;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.HeldItemTooltips.HeldItemTooltips;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class ItemTooltipsMixin {
    @Shadow private ItemStack currentStack;

    @Shadow private int heldItemTooltipFade;

    @Shadow @Final private MinecraftClient client;

    /**
     * Draw custom tooltips for effects and enchantments before the heldItemTooltip is rendered.
     */
    @Redirect(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    private int drawCustomTooltips(TextRenderer fontRenderer, MatrixStack matrices, Text text, float x, float y, int color) {
        return BedrockifyClient.getInstance().heldItemTooltips.drawItemWithCustomTooltips(fontRenderer, matrices, text, x, y, color, currentStack);
    }
    /**
     * Draw custom tooltips for effects and enchantments before the heldItemTooltip is rendered.
     */
    @Redirect(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
    private void drawCustomTooltips(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {}

    /**
     * Show the item tooltip when changing from a item to another of the same type and name IFF different tooltips.
     */
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 1))
    private boolean interceptItemStack(ItemStack itemStack) {
        ItemStack nextItem = this.client.player.inventory.getMainHandStack();
        HeldItemTooltips heldItemTooltips = BedrockifyClient.getInstance().heldItemTooltips;
        if(itemStack.getItem() == this.currentStack.getItem() && !heldItemTooltips.equals(currentStack,nextItem)){
            this.heldItemTooltipFade = 41;
            return true;
        }

        return currentStack.isEmpty();
    }
}
