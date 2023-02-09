package me.juancarloscp52.bedrockify.client.features.bedrockShading;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import org.joml.Math;
import org.joml.Vector3f;

/**
 * Helper class for the sun glare and the sky color like Bedrock.
 */
public final class BedrockSunGlareShading {
    private float skyAttenuation;

    public BedrockSunGlareShading() {
        onSunlightIntensityChanged();
    }

    public boolean isEnabled() {
        return BedrockifyClient.getInstance().settings.bedrockShading &&
                this.skyAttenuation < 1f;
    }

    /**
     * Helper method that gets the angle difference between Camera and Sun.<br>
     * Only Daytime works. This will always return <code>1.0</code> at Night.
     *
     * @param tickDelta TickDelta to determine the SkyAngle.
     * @return The dot product of camera vector and sun vector including some factors, clamped between <code>0.0 - 1.0</code>.
     */
    public static float getSunAngleDiffClamped(float tickDelta) {
        final float clampMax = 1f;
        final float clampMin = 0f;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null || client.gameRenderer == null) {
            return clampMax;
        }

        final Camera camera = client.gameRenderer.getCamera();
        final float skyAngleRadian = (float) (client.world.getSkyAngle(tickDelta) * 2f * Math.PI);
        final Vector3f sunVec3f = new Vector3f(-Math.sin(skyAngleRadian), Math.cos(skyAngleRadian), 0).normalize();
        final float sunSetRiseFactor = (sunVec3f.y < 0) ? sunVec3f.y * -5f : 0;
        if (sunSetRiseFactor >= 1f) {
            return clampMax;
        }

        final Vector3f cameraVec3f = new Vector3f(0, 0, 1).rotate(camera.getRotation()).normalize();

        return Math.clamp(clampMin, clampMax, (Math.safeAcos(cameraVec3f.dot(sunVec3f)) - 0.15f) * 2.f + sunSetRiseFactor);
    }

    public float getSkyAttenuation() {
        return this.skyAttenuation;
    }

    public void onSunlightIntensityChanged() {
        this.skyAttenuation = MathHelper.clampedLerp(1f, 0.35f, BedrockifyClient.getInstance().settings.sunlightIntensity / 100f);
    }
}
