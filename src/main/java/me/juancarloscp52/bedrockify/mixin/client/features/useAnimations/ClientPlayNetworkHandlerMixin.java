package me.juancarloscp52.bedrockify.mixin.client.features.useAnimations;

import me.juancarloscp52.bedrockify.client.features.useAnimations.AnimationsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    /**
     * Animate always by receiving S2C packet.<br>
     * Original method prevents the bobbing animation when decrementing and damaging.
     *
     * @see ClientPlayNetworkHandler#onScreenHandlerSlotUpdate
     */
    @Inject(method = "onScreenHandlerSlotUpdate", at = @At("RETURN"))
    private void bedrockify$animateAlwaysSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo ci) {
        final ItemStack itemStack = packet.getStack();
        final int slotIdx = packet.getSlot();
        if (packet.getSyncId() != 0 && !PlayerScreenHandler.isInHotbar(slotIdx) || itemStack == null) {
            return;
        }

        AnimationsHelper.doBobbingAnimation(itemStack);
    }

    /**
     * Animate always by receiving S2C packet.<br>
     * This handles a packet that could not be caught by {@link ClientPlayNetworkHandler#onScreenHandlerSlotUpdate}.
     *
     * @see ClientPlayNetworkHandler#onInventory
     */
    @Inject(method = "onInventory", at = @At("RETURN"))
    private void bedrockify$animateAlwaysInventory(InventoryS2CPacket packet, CallbackInfo ci) {
        final PlayerEntity player = MinecraftClient.getInstance().player;
        if (packet.getSyncId() != 0 || player == null) {
            return;
        }

        final int target = AnimationsHelper.consumeChangedSlot();
        if (!PlayerInventory.isValidHotbarIndex(target) && target != PlayerInventory.OFF_HAND_SLOT) {
            return;
        }

        AnimationsHelper.doBobbingAnimation(player.getInventory().getStack(target));
    }
}
