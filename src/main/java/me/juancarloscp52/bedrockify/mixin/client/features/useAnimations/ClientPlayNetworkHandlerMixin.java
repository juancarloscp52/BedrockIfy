package me.juancarloscp52.bedrockify.mixin.client.features.useAnimations;

import me.juancarloscp52.bedrockify.client.features.useAnimations.AnimationsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Shadow
    private @Final MinecraftClient client;

    /**
     * Animate always by receiving S2C packet.<br>
     * Original method prevents the bobbing animation when decrementing and damaging.
     *
     * @see ClientPlayNetworkHandler#onScreenHandlerSlotUpdate
     */
    @Inject(method = "onScreenHandlerSlotUpdate", at = @At("RETURN"))
    private void bedrockify$animateAlways(ScreenHandlerSlotUpdateS2CPacket packet, CallbackInfo ci) {
        final ItemStack itemStack = packet.getItemStack();
        final int slotIdx = packet.getSlot();
        if (packet.getSyncId() != 0 && !PlayerScreenHandler.isInHotbar(slotIdx) || itemStack == null) {
            return;
        }

        AnimationsHelper.doBobbingAnimation(itemStack);
    }
}
