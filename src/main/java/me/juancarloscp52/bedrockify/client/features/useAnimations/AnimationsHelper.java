package me.juancarloscp52.bedrockify.client.features.useAnimations;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class AnimationsHelper {
    public static final int ANIMATION_TIME = 5;

    private static Item updatedItemInInventory = Items.AIR;

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
     * Stores the changed item.
     *
     * @param itemStack Target itemStack.
     */
    public static void notifyChangedItem(ItemStack itemStack) {
        updatedItemInInventory = itemStack.getItem();
    }

    /**
     * Returns the stored item and reset it.
     *
     * @return Target item.
     */
    public static Item consumeChangedItem() {
        final Item ret = updatedItemInInventory;
        updatedItemInInventory = Items.AIR;
        return ret;
    }
}
