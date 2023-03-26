package me.juancarloscp52.bedrockify.client.features.sheepColors;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.io.InputStream;

public class SheepSkinResource implements SimpleSynchronousResourceReloadListener {
    public static Identifier TEXTURE_SHEARED = null;

    private static final Identifier TEXTURE_SHEEP_SKIN = new Identifier("textures/entity/sheep/sheep.png");
    /**
     * Specify the step to check texture resolution.<br>
     * The texture resolution is considered to be this multiple using {@link Math#ceil}.
     */
    private static final int RESOLUTION_CHECK_STEP = 16;

    @Override
    public Identifier getFabricId() {
        return new Identifier(Bedrockify.MOD_ID, "textures/entity/sheep_sheared");
    }

    /**
     * Reload event callback when the Resource has changed, client started, and so on.
     */
    @Override
    public void reload(ResourceManager manager) {
        manager.getResource(TEXTURE_SHEEP_SKIN).ifPresentOrElse(resource -> {
            try (InputStream stream = resource.getInputStream()) {
                final NativeImage nativeImage = NativeImage.read(stream);
                final Point resClamped = clampResolution(nativeImage);

                // Vanilla texture: width = 64, height = 32
                final float imageWidthMul = (float) (resClamped.getX() / 64.);
                final float imageHeightMul = (float) (resClamped.getY() / 32.);
                if (!MathHelper.approximatelyEquals(imageHeightMul, imageWidthMul)) {
                    BedrockifyClient.LOGGER.warn("[{}] Illegal resolution detected on {}! BedrockIfy Sheep Colors may not work properly.", Bedrockify.class.getSimpleName(), TEXTURE_SHEEP_SKIN.getPath());
                }

                // Extract the White pixels without face.
                for (int x = 0; x < nativeImage.getWidth(); x++) {
                    for (int y = 0; y < nativeImage.getHeight(); y++) {
                        final int abgr = nativeImage.getColor(x, y);
                        final int b = abgr >> 16 & 0xFF;
                        final int g = abgr >> 8 & 0xFF;
                        final int r = abgr & 0xFF;
                        final float[] hsb = Color.RGBtoHSB(r, g, b, null);
                        if (!isInFaceRegion(x / imageWidthMul, y / imageHeightMul) && isPixelMostlyWhite(hsb)) {
                            nativeImage.setColor(x, y, abgr);
                        } else {
                            nativeImage.setColor(x, y, 0);
                        }
                    }
                }

                // Register the extracted texture.
                final NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);
                TEXTURE_SHEARED = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("sheep_sheared", texture);
            } catch (Throwable ex) {
                BedrockifyClient.LOGGER.error("[%s] Unable to extract sheep texture.".formatted(Bedrockify.class.getSimpleName()), ex);
            }
        }, () -> {
            BedrockifyClient.LOGGER.error("[{}] Failed to load sheep texture: {}", Bedrockify.class.getSimpleName(), TEXTURE_SHEEP_SKIN.getPath());
        });
    }

    /**
     * Calculates and Clamps the resolution to make easy to determine the face region.
     *
     * @param nativeImage Target image.
     * @return Clamped resolution.
     */
    private static Point clampResolution(NativeImage nativeImage) {
        final Point ret = new Point();
        ret.x = (int) (Math.ceil(nativeImage.getWidth() / (float) RESOLUTION_CHECK_STEP)) * RESOLUTION_CHECK_STEP;
        ret.y = (int) (Math.ceil(nativeImage.getHeight() / (float) RESOLUTION_CHECK_STEP)) * RESOLUTION_CHECK_STEP;
        return ret;
    }

    /**
     * The hardcoded method to determine the face region.<br>
     * Parameters must be normalized to match vanilla resolution.
     *
     * @param normalizedX The normalized coordinate of x.
     * @param normalizedY The normalized coordinate of y.
     * @return <code>true</code> if the position is in the face region.
     */
    private static boolean isInFaceRegion(float normalizedX, float normalizedY) {
        return normalizedX >= 8 && normalizedX < 14 && normalizedY >= 9 && normalizedY < 12;
    }

    /**
     * @param hsb Target HSB color.
     */
    private static boolean isPixelMostlyWhite(float[] hsb) {
        return hsb[1] < 0.11f && hsb[2] > 0.82f;
    }
}
