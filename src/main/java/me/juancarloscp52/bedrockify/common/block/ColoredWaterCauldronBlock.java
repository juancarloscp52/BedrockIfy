package me.juancarloscp52.bedrockify.common.block;

import me.juancarloscp52.bedrockify.common.block.cauldron.BedrockCauldronBehavior;
import me.juancarloscp52.bedrockify.common.block.entity.WaterCauldronBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Allows to dye using Cauldron.<br>
 * Dye items for which {@link net.minecraft.item.DyeableItem} is implemented.
 */
public class ColoredWaterCauldronBlock extends LeveledCauldronBlock implements BlockEntityProvider {
    public ColoredWaterCauldronBlock(Settings settings) {
        super(settings, precipitation -> false, BedrockCauldronBehavior.COLORED_WATER_CAULDRON_BEHAVIOR);
    }

    @Override
    protected boolean canBeFilledByDripstone(Fluid fluid) {
        return false;
    }

    @Override
    protected void fillFromDripstone(BlockState state, World world, BlockPos pos, Fluid fluid) {
    }

    /**
     * Allows to keep water state.
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WaterCauldronBlockEntity(pos, state);
    }
}
