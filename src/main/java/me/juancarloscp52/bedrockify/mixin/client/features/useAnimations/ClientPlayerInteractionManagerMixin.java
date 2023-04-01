package me.juancarloscp52.bedrockify.mixin.client.features.useAnimations;

import me.juancarloscp52.bedrockify.client.features.useAnimations.AnimationsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    /**
     * This targets the lambda of {@link net.minecraft.client.network.SequencedPacketCreator} in {@link ClientPlayerInteractionManager#interactItem}.<br>
     * It is not possible to retrieve their changes in {@link net.minecraft.client.network.ClientPlayNetworkHandler#onScreenHandlerSlotUpdate}.<br>
     * This result is used in {@link AnimationsHelper#consumeChangedSlot}.
     */
    @Inject(method = "interactItem", at = @At("RETURN"))
    private void bedrockify$consumeItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (player == null) {
            return;
        }

        final ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.GOAT_HORN)) {
            AnimationsHelper.doBobbingAnimation(itemStack);
            return;
        }

        if (player.isCreative()) {
            return;
        }

        AnimationsHelper.notifyChangedSlot((hand == Hand.OFF_HAND) ? PlayerInventory.OFF_HAND_SLOT : player.getInventory().selectedSlot);
    }
}
