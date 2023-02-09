package me.juancarloscp52.bedrockify.client.features.bedrockShading;

import com.google.common.collect.Maps;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Vector3f;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Helper class for the sun glare and the sky color like Bedrock.<br>
 * Compatible with Custom Shader mods.
 */
public final class BedrockSunGlareShading {
    /**
     * Test cases of the {@link ClassMethodHolder} specified by modID.
     */
    private static final Map<String, ClassMethodHolder> MOD_ID_CLASS_MAP = Util.make(Maps.newHashMap(), (map) -> {
        map.put("iris", new ClassMethodHolder("net.coderbot.iris.Iris", "getCurrentPack", new Object[0], (pack) -> {
            return ((Optional<?>) pack).isPresent();
        }));
    });
    private static final List<ClassMethodHolder> METHOD_INVOCATION_FAILED_LIST = new ArrayList<>();

    private ShaderState shaderState = ShaderState.UNSPECIFIED;
    private float skyAttenuation;

    public BedrockSunGlareShading() {
        onSunlightIntensityChanged();
    }

    /**
     * Shows the state of Custom Shader.
     *
     * @see ShaderState#UNSPECIFIED
     * @see ShaderState#VANILLA
     * @see ShaderState#EXTERNAL
     * @see ShaderState#INVOCATION_FAILED
     */
    private enum ShaderState {
        /**
         * Initial state.
         */
        UNSPECIFIED,
        /**
         * Shader mod is not installed, or external shader is not enabled.
         */
        VANILLA,
        /**
         * Shader mod is present and enabled.
         */
        EXTERNAL,
        /**
         * Shader mod is present, but method invocation failed.
         */
        INVOCATION_FAILED
    }

    /**
     * The data only class that holds the name of class, method, and args of method invocation.<br>
     * Only supports static method.<br>
     * This class is used with package {@link java.lang.reflect}.
     */
    private static class ClassMethodHolder {
        public final String canonicalName;
        public final String methodName;
        @NotNull
        public final Object[] methodArgs;
        public final Predicate<Object> condition;

        /**
         * @param cName     Class canonical name.
         * @param mName     Method name to invoke.
         * @param mArgs     Method arguments to invoke.
         * @param condition {@link Predicate} for the condition whether the external shader is valid. If returns <code>true</code>, Sun Glare shading will be disabled.
         */
        protected ClassMethodHolder(String cName, String mName, @NotNull Object[] mArgs, Predicate<Object> condition) {
            this.canonicalName = cName;
            this.methodName = mName;
            this.methodArgs = mArgs;
            this.condition = condition;
        }
    }

    public boolean shouldApplyShading() {
        return this.shaderState == ShaderState.VANILLA &&
                BedrockifyClient.getInstance().settings.bedrockShading &&
                this.skyAttenuation < 1f;
    }

    public void reloadCustomShaderState() {
        this.shaderState = this.fetchShaderStateInternal();
    }

    /**
     * Determine the state of Custom Shader.
     *
     * @see ShaderState
     */
    private ShaderState fetchShaderStateInternal() {
        if (FabricLoader.getInstance().isModLoaded("optifabric")) {
            // Unreachable statement.
            // BedrockShading feature is disabled by BedrockIfyMixinPlugin#shouldApplyMixin if optifabric detected. How did you reach here?
            return ShaderState.EXTERNAL;
        }

        boolean enabled = false;

        for (Map.Entry<String, ClassMethodHolder> entry : MOD_ID_CLASS_MAP.entrySet()) {
            final String modId = entry.getKey();
            if (!FabricLoader.getInstance().isModLoaded(modId)) {
                // The class probably absent because the mod has not been loaded.
                continue;
            }

            final ClassMethodHolder holder = entry.getValue();
            // Check invalid state.
            if (METHOD_INVOCATION_FAILED_LIST.contains(holder)) {
                return ShaderState.INVOCATION_FAILED;
            }

            try {
                // Get the class.
                final Class<?> clazz = Class.forName(holder.canonicalName);

                // Get the method.
                final Method invoker = clazz.getMethod(holder.methodName, Arrays.stream(holder.methodArgs).map(Object::getClass).toArray(Class[]::new));
                invoker.setAccessible(true);

                // Execute invocation and store the result.
                enabled |= holder.condition.test(invoker.invoke(clazz, holder.methodArgs));
            } catch (Throwable ex) {
                // Method invocation failed.
                if (!METHOD_INVOCATION_FAILED_LIST.contains(holder)) {
                    METHOD_INVOCATION_FAILED_LIST.add(holder);

                    // Output the log only once.
                    final String message = String.format("[%s] method invocation failed into \"%s::%s(%s)\", provided by the mod \"%s\".",
                            Bedrockify.class.getSimpleName(),
                            holder.canonicalName,
                            holder.methodName,
                            Arrays.stream(holder.methodArgs).map(arg -> arg.getClass().getSimpleName()).collect(Collectors.joining(", ")),
                            modId
                    );
                    BedrockifyClient.LOGGER.error(message, ex);
                }

                // Output the log only once.
                if (shaderState != ShaderState.INVOCATION_FAILED) {
                    BedrockifyClient.LOGGER.warn("[{}] Shader mod is present, but cannot determine the shader state. BedrockIfy Sun Glare Shading is now disabled.", Bedrockify.class.getSimpleName());
                }

                return ShaderState.INVOCATION_FAILED;
            }
        }  // End of the MOD_ID_CLASS_MAP for-each loop.

        // Output the log only once when the external shader turned on.
        if (enabled && shaderState != ShaderState.EXTERNAL) {
            BedrockifyClient.LOGGER.info("[{}] External Shader is detected. BedrockIfy Sun Glare Shading is now disabled.", Bedrockify.class.getSimpleName());
        }
        return (enabled) ? ShaderState.EXTERNAL : ShaderState.VANILLA;
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
