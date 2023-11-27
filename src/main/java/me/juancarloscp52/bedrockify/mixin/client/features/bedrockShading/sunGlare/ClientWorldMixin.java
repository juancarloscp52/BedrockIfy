package me.juancarloscp52.bedrockify.mixin.client.features.bedrockShading.sunGlare;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
    @Shadow
    private @Final MinecraftClient client;

    /**
     * Modify the Sky color based on Camera angle.
     */
    @ModifyReturnValue(method = "getSkyColor", at = @At("RETURN"))
    private Vec3d bedrockify$modifySkyColor(Vec3d colorVec3d, Vec3d cameraPos, float tickDelta) {
        final BedrockSunGlareShading sunGlareShading = BedrockifyClient.getInstance().bedrockSunGlareShading;
        if (!sunGlareShading.shouldApplyShading()) {
            return colorVec3d;
        }

        if (this.client.world == null) {
            return colorVec3d;
        }

        final float rainGradient = this.client.world.getRainGradient(tickDelta);
        final float angleDiff = sunGlareShading.getSunAngleDiff();
        final float multiplier = MathHelper.clampedLerp(sunGlareShading.getSkyAttenuation(), 1f, angleDiff + rainGradient);
        if (MathHelper.approximatelyEquals(multiplier, 1f)) {
            return colorVec3d;
        }

        // Closer to the Sun, Darker the Sky, based on camera angle.
        return colorVec3d.multiply(multiplier, multiplier, multiplier + (1f - multiplier) * 0.45f);
    }
}
