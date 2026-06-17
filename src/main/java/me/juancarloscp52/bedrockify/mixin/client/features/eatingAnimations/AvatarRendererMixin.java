package me.juancarloscp52.bedrockify.mixin.client.features.eatingAnimations;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.eatingAnimations.IEatingState;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {
    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void bedrockify$storeEatingState(AvatarlikeEntity avatarlikeEntity, AvatarRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        if (!BedrockifyClient.getInstance().settings.isEatingAnimationsEnabled() || !(playerEntityRenderState instanceof IEatingState state)) {
            return;
        }

        ItemStack mainHandStack = avatarlikeEntity.getMainHandItem();
        ItemStack offHandStack = avatarlikeEntity.getOffhandItem();
        if (bedrockify$checkEatingAction(avatarlikeEntity, playerEntityRenderState, InteractionHand.MAIN_HAND, mainHandStack)) {
            state.setEatingHand(InteractionHand.MAIN_HAND);
        } else if (bedrockify$checkEatingAction(avatarlikeEntity, playerEntityRenderState, InteractionHand.OFF_HAND, offHandStack)) {
            state.setEatingHand(InteractionHand.OFF_HAND);
        } else {
            state.setEatingHand(null);
        }
    }

    @Unique
    private boolean bedrockify$checkEatingAction(AvatarlikeEntity entity, AvatarRenderState state, InteractionHand hand, ItemStack itemStack) {
        return entity.isUsingItem() &&
                entity.getUseItem().is(itemStack.getItem()) &&
                state.useItemHand == hand &&
                (itemStack.getUseAnimation() == ItemUseAnimation.EAT || itemStack.getUseAnimation() == ItemUseAnimation.DRINK);
    }
}
