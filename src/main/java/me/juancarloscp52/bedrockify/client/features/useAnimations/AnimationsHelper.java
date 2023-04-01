package me.juancarloscp52.bedrockify.client.features.useAnimations;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.item.ItemStack;

public final class AnimationsHelper {
    public static final int ANIMATION_TIME = 5;

    private static int updatedItemIdx = -1;

    private AnimationsHelper() {
    }

    /**
     * Bobbing!
     *
     * @param target the ItemStack
     */
    public static void doBobbingAnimation(ItemStack target) {
        if (!BedrockifyClient.getInstance().settings.isPickupAnimationsEnabled()) {
            return;
        }
        if (target.isEmpty()) {
            return;
        }

        target.setBobbingAnimationTime(AnimationsHelper.ANIMATION_TIME);
    }

    /**
     * Stores the slot index where the item changed.
     *
     * @param slotIdx Target slot index.
     */
    public static void notifyChangedSlot(int slotIdx) {
        updatedItemIdx = slotIdx;
    }

    /**
     * Returns the stored slot index and reset it.
     *
     * @return Target item.
     */
    public static int consumeChangedSlot() {
        final int ret = updatedItemIdx;
        updatedItemIdx = -1;
        return ret;
    }
}
