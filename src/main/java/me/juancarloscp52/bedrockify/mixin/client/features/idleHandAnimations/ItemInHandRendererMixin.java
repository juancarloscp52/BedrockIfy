package me.juancarloscp52.bedrockify.mixin.client.features.idleHandAnimations;

import com.mojang.blaze3d.vertex.PoseStack;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
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
    float timer = 0;
    @Unique
    private static final float ONE_CYCLE = 2 * Mth.PI;

    @Inject(method = "submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V", at = @At("HEAD"))
    private void bedrockify$updateSwayDelta(CallbackInfo ci) {
        if (Minecraft.getInstance().isPaused()) {
            return;
        }
        timer += BedrockifyClient.getInstance().deltaTime * 0.000000002f;
        if (timer > ONE_CYCLE) {
            // Prevents float overflow
            timer -= ONE_CYCLE;
        }
    }

    /**
     * Adds "breathing" idle animation to items in hand.
     */
    @Inject(method = "applyItemArmTransform", at=@At("HEAD"),cancellable = true)
    public void applyEquipOffset (PoseStack matrices, HumanoidArm arm, float equipProgress, CallbackInfo info){
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        double breath = (i==1 ? Mth.sin(((timer))* BedrockifyClient.getInstance().settings.getIdleAnimation()) : Mth.cos((timer)* BedrockifyClient.getInstance().settings.getIdleAnimation()))*0.01D;
        matrices.translate(((float)i * 0.56F), (-0.52F + equipProgress * -0.6F) + breath, -0.7200000286102295D);
        info.cancel();
    }

}
