package me.juancarloscp52.bedrockify.mixin.client.features.useAnimations;

import me.juancarloscp52.bedrockify.client.features.useAnimations.AnimationsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.apache.commons.lang3.mutable.MutableObject;
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
     * This result is used in {@link AnimationsHelper#consumeChangedItem}.
     */
    @Inject(method = "method_41929", at = @At("RETURN"))
    private void bedrockify$consumeItem(Hand hand, PlayerEntity player, MutableObject<ActionResult> mutableObject, int sequence, CallbackInfoReturnable<Packet<?>> cir) {
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

        AnimationsHelper.notifyChangedItem(itemStack);
    }
}
