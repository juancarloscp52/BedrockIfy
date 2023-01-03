package me.juancarloscp52.bedrockify.client.features.useAnimations;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.mixin.client.features.useAnimations.MinecraftClientAccessor;
import me.juancarloscp52.bedrockify.mixin.client.features.useAnimations.RenderTickCounterAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;

public final class AnimationsHelper {
    public static final int ANIMATION_TIME = 5;

    /**
     * Cooldown time to prevent double-bobbing [ms]
     */
    private static final double COOLDOWN_TIME_MILLIS;

    private static ItemStack lastBobbingStack = ItemStack.EMPTY;
    private static long lastBobbingTime = 0;

    static {
        double cooldownTimeMillis;
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null) {
                throw new AssertionError("AnimationsHelper::<clinit> failure -- MinecraftClient.getInstance() returns null");
            }
            RenderTickCounter tickCounter = ((MinecraftClientAccessor) client).getRenderTickCounterField();
            if (tickCounter == null) {
                throw new AssertionError("AnimationsHelper::<clinit> failure -- field invocation MinecraftClientAccessor::getRenderTickCounterField() returns null");
            }
            final float tickTimeMillis = ((RenderTickCounterAccessor) tickCounter).getTickTimeField();
            cooldownTimeMillis = ANIMATION_TIME * tickTimeMillis;
        } catch (Throwable ex) {
            BedrockifyClient.LOGGER.error("[BedrockIfy] an unexpected error has caught", ex);
            // TODO: tick per second is 20 (MC 1.19.3)
            cooldownTimeMillis = ANIMATION_TIME * (1000 / 20.0);
        }
        COOLDOWN_TIME_MILLIS = cooldownTimeMillis;
    }

    /**
     * Bobbing!
     *
     * @param target the ItemStack
     */
    public static void doBobbingAnimation(ItemStack target) {
        if (!Bedrockify.getInstance().settings.isPickupAnimationsEnabled()) {
            return;
        }
        if (target.isEmpty()) {
            return;
        }

        final long now = System.currentTimeMillis();
        if (ItemStack.areEqual(target, lastBobbingStack) && lastBobbingTime + COOLDOWN_TIME_MILLIS > now) {
            return;
        }

        target.setBobbingAnimationTime(AnimationsHelper.ANIMATION_TIME);
        lastBobbingStack = target;
        lastBobbingTime = now;
    }
}
