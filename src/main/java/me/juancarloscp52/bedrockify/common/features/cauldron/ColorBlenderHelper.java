package me.juancarloscp52.bedrockify.common.features.cauldron;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

import java.util.Arrays;
import java.util.List;

/**
 * Used by {@link me.juancarloscp52.bedrockify.common.block.ColoredWaterCauldronBlock}.
 */
public final class ColorBlenderHelper {
    private ColorBlenderHelper() {
    }

    /**
     * The mostly same as {@link DyedColorComponent#setColor(ItemStack, List)}.<br>
     * Blend the color, and set it as the {@link ItemStack} color.
     *
     * @param base   The base stack of DyeableItem.
     * @param colors Target colors to mix.
     * @return Blended item stack.
     */
    public static ItemStack blendColors(ItemStack base, int... colors) {
        if (!base.isIn(ItemTags.DYEABLE)) {
            return base;
        }
        DyedColorComponent dyedColorComponent = base.get(DataComponentTypes.DYED_COLOR);

        final int[] blendArray;
        blendArray = Arrays.copyOf(colors, colors.length + 1);
        blendArray[blendArray.length - 1] = DyedColorComponent.getColor(base, 0xFFA06540);

        boolean showInTooltip = dyedColorComponent == null || dyedColorComponent.showInTooltip();
        base.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(blendColors(blendArray), showInTooltip));
        return base;
    }

    /**
     * This logic is based on {@link DyedColorComponent#setColor(ItemStack, List)}.
     *
     * @param blender Target colors to mix.
     * @return The blended color.
     */
    public static int blendColors(int... blender) {
        int peekComponent = 0;
        int count = 0;
        int[] blended = new int[3];

        for (int color : blender) {
            final int red = color >> 16 & 255;
            final int green = color >> 8 & 255;
            final int blue = color & 255;
            peekComponent += Math.max(red, Math.max(green, blue));
            blended[0] += red;
            blended[1] += green;
            blended[2] += blue;
            ++count;
        }

        final int normalizedRed = blended[0] / count;
        final int normalizedGreen = blended[1] / count;
        final int normalizedBlue = blended[2] / count;
        final float peekMul = (float) peekComponent / count;
        int peek = Math.max(normalizedRed, Math.max(normalizedGreen, normalizedBlue));
        final int resultRed = (int) (normalizedRed * peekMul / peek);
        final int resultGreen = (int) (normalizedGreen * peekMul / peek);
        final int resultBlue = (int) (normalizedBlue * peekMul / peek);

        return resultRed << 16 | resultGreen << 8 | resultBlue;
    }

    public static int fromDyeItem(DyeItem item) {
        return item.getColor().getEntityColor();
    }
}
