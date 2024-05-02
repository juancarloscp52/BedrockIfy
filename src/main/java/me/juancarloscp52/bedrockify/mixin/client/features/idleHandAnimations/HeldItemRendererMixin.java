package me.juancarloscp52.bedrockify.mixin.client.features.idleHandAnimations;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Unique
    float timer = 0;
    @Unique
    private static final double ONE_CYCLE = 2 * Math.PI;

    @Inject(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At("HEAD"))
    private void bedrockify$updateSwayDelta(float tickDelta, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci) {
        if (MinecraftClient.getInstance().isPaused()) {
            return;
        }
        timer += tickDelta * 0.05f;
        if (timer > ONE_CYCLE) {
            // Prevents float overflow
            timer = (float) MathHelper.floorMod(timer, ONE_CYCLE);
        }
    }

    /**
     * Adds "breathing" idle animation to items in hand.
     */
    @Inject(method = "applyEquipOffset", at=@At("HEAD"),cancellable = true)
    public void applyEquipOffset (MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo info){
        int i = arm == Arm.RIGHT ? 1 : -1;
        double breath = (i==1 ? MathHelper.sin(((timer))* BedrockifyClient.getInstance().settings.getIdleAnimation()) : MathHelper.cos((timer)* BedrockifyClient.getInstance().settings.getIdleAnimation()))*0.01D;
        matrices.translate(((float)i * 0.56F), (-0.52F + equipProgress * -0.6F) + breath, -0.7200000286102295D);
        info.cancel();
    }

}
