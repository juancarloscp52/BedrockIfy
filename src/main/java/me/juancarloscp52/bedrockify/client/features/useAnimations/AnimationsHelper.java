package me.juancarloscp52.bedrockify.client.features.useAnimations;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.item.ItemStack;

public final class AnimationsHelper {
    public static final int ANIMATION_TIME = 5;

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

        target.setBobbingAnimationTime(AnimationsHelper.ANIMATION_TIME);
    }
}
