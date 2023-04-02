package me.juancarloscp52.bedrockify.common.block;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.block.cauldron.BedrockCauldronBehavior;
import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * Allows to dye using Cauldron.<br>
 * Dye items for which {@link net.minecraft.item.DyeableItem} is implemented.
 */
public class ColoredWaterCauldronBlock extends AbstractBECauldronBlock {
    public static final IntProperty LEVEL = BedrockCauldronProperties.LEVEL_6;
    public static final int MAX_LEVEL = BedrockCauldronProperties.MAX_LEVEL_6;

    private static final int BOTTLE_LEVEL = MAX_LEVEL / LeveledCauldronBlock.MAX_LEVEL;

    public ColoredWaterCauldronBlock(Settings settings) {
        super(settings, BedrockCauldronBehavior.COLORED_WATER_CAULDRON_BEHAVIOR);
        this.setDefaultState(this.getStateManager().getDefaultState().with(LEVEL, BOTTLE_LEVEL));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.get(LEVEL) == MAX_LEVEL;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return (int) Math.ceil((float) state.get(LEVEL) / MAX_LEVEL * LeveledCauldronBlock.MAX_LEVEL);
    }

    @Override
    public double getFluidHeight(BlockState state) {
        return MathHelper.lerp((float) state.get(LEVEL) / MAX_LEVEL, 0.375, 0.9375);
    }

    public static void decrementWhenDye(BlockState state, World world, BlockPos pos) {
        final int decremented = state.get(LEVEL) - 1;
        final BlockState newState = (decremented == 0) ? Blocks.CAULDRON.getDefaultState() : state.with(LEVEL, decremented);
        world.setBlockState(pos, newState);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(newState));
    }

    public static int getLevelFromWaterCauldronState(BlockState state) {
        if (!state.contains(LeveledCauldronBlock.LEVEL)) {
            Bedrockify.LOGGER.error(
                    "[%s] state conversion failed".formatted(Bedrockify.class.getSimpleName()),
                    new IllegalStateException("BlockState of %s does not have state: %s".formatted(state.getBlock(), LeveledCauldronBlock.LEVEL)));
            return 0;
        }

        return MathHelper.lerp((float) state.get(LeveledCauldronBlock.LEVEL) / LeveledCauldronBlock.MAX_LEVEL, 0, MAX_LEVEL);
    }

    public static int convertToWaterCauldronLevel(BlockState state) {
        if (!state.contains(LEVEL)) {
            Bedrockify.LOGGER.error(
                    "[%s] state conversion failed".formatted(Bedrockify.class.getSimpleName()),
                    new IllegalStateException("BlockState of %s does not have state: %s".formatted(state.getBlock(), LEVEL)));
            return 0;
        }

        return (int) Math.floor((float) state.get(LEVEL) / MAX_LEVEL * LeveledCauldronBlock.MAX_LEVEL);
    }

    public static boolean tryPickFluid(BlockState state, World world, BlockPos pos) {
        final int nextLevel = state.get(LEVEL) - BOTTLE_LEVEL;
        if (nextLevel < 0) {
            return false;
        }
        BlockState blockState = (nextLevel == 0) ? Blocks.CAULDRON.getDefaultState() : state.with(LEVEL, nextLevel);
        world.setBlockState(pos, blockState);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
        return true;
    }
}
