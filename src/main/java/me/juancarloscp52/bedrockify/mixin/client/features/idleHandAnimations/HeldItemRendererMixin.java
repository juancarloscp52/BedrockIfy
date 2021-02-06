package me.juancarloscp52.bedrockify.mixin.client.features.idleHandAnimations;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    float timer = 0;

    /**
     * Adds "breathing" idle animation to items in hand.
     */
    @Inject(method = "applyEquipOffset", at=@At("HEAD"),cancellable = true)
    public void applyEquipOffset (MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo info){
        int i = arm == Arm.RIGHT ? 1 : -1;
        double breath = (i==1 ? MathHelper.sin(((timer))* Bedrockify.getInstance().settings.getIdleAnimation()) : MathHelper.cos((timer)* Bedrockify.getInstance().settings.getIdleAnimation()))*0.01D;
        matrices.translate(((float)i * 0.56F), (-0.52F + equipProgress * -0.6F) + breath, -0.7200000286102295D);
        timer += BedrockifyClient.getInstance().deltaTime * 0.000000001f;
        info.cancel();
    }

}
