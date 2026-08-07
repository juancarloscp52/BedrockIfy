package me.juancarloscp52.bedrockify.mixin.client.features.idleHandAnimations;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Unique
    float delta = 0;
    @Unique
    private static final float ONE_CYCLE = 2 * Mth.PI;
    @Unique
    private final BedrockifyClientSettings bedrockifySettings = BedrockifyClient.getInstance().settings;

    @Inject(method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V", at = @At("HEAD"))
    private void bedrockify$updateSwayDelta(CallbackInfo ci) {
        if (Minecraft.getInstance().isPaused()) {
            return;
        }
        this.delta += BedrockifyClient.getInstance().deltaTime * 0.000000002f * this.bedrockifySettings.getIdleAnimation();
        if (this.delta > ONE_CYCLE) {
            // Prevents float overflow
            this.delta -= ONE_CYCLE;
        }
    }

    /**
     * Adds "breathing" idle animation to items in hand.
     */
    @WrapOperation(method = "applyItemArmTransform", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    public void applyEquipOffset(PoseStack instance, float xOffset, float yOffset, float zOffset, Operation<Void> original, PoseStack poseStack, HumanoidArm arm) {
        final float breath;
        if (this.bedrockifySettings.getIdleAnimation() == 0f) {
            breath = 0;
        } else {
            breath = ((arm == HumanoidArm.RIGHT) ? Mth.sin(this.delta) : Mth.cos(this.delta)) * 0.01f;
        }
        original.call(instance, xOffset, yOffset + breath, zOffset);
    }

}
