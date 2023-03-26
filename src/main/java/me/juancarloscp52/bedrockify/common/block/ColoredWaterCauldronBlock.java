package me.juancarloscp52.bedrockify.common.block;

import me.juancarloscp52.bedrockify.common.block.cauldron.BedrockCauldronBehavior;

/**
 * Allows to dye using Cauldron.<br>
 * Dye items for which {@link net.minecraft.item.DyeableItem} is implemented.
 */
public class ColoredWaterCauldronBlock extends AbstractBECauldronBlock {
    public ColoredWaterCauldronBlock(Settings settings) {
        super(settings, BedrockCauldronBehavior.COLORED_WATER_CAULDRON_BEHAVIOR);
    }
}
