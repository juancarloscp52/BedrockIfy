package me.juancarloscp52.bedrockify.mixin.client.features.bedrockShading.sunGlare;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.bedrockShading.BedrockSunGlareShading;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @Shadow
    private @Final MinecraftClient client;

    /**
     * Modify the Sky color based on Camera angle.
     */
    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private void bedrockify$modifySkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        final BedrockSunGlareShading sunGlareShading = BedrockifyClient.getInstance().bedrockSunGlareShading;
        if (!sunGlareShading.shouldApplyShading()) {
            return;
        }

        if (this.client.world == null) {
            return;
        }

        final float rainGradient = this.client.world.getRainGradient(tickDelta);
        final float angleDiff = sunGlareShading.getSunAngleDiff();

        // Closer to the Sun, Darken the Sky, based on camera angle. Use a different multiplier for each channel in order to better match bedrock edition sky color.
        final float multiplierBlue = MathHelper.clampedLerp(sunGlareShading.getSkyAttenuation(), 1f, angleDiff + rainGradient);
        if (MathHelper.approximatelyEquals(multiplierBlue, 1f)) {
            return;
        }
        final float multiplierRed = MathHelper.clampedLerp(sunGlareShading.getSkyAttenuation()-0.16f, 1f, angleDiff + rainGradient);
        final float multiplierGreen = MathHelper.clampedLerp(sunGlareShading.getSkyAttenuation()-0.06f, 1f, angleDiff + rainGradient);

        final Vec3d colorVec3d = cir.getReturnValue();
        cir.setReturnValue(colorVec3d.multiply(multiplierRed, multiplierGreen, multiplierBlue));
    }
}
