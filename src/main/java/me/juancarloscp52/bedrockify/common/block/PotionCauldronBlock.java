package me.juancarloscp52.bedrockify.common.block;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.block.cauldron.BedrockCauldronBehavior;
import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronBlocks;
import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * Allows to keep the potion fluid.
 */
public class PotionCauldronBlock extends AbstractBECauldronBlock {
    public static final IntProperty LEVEL = BedrockCauldronProperties.LEVEL_8;
    public static final int MAX_LEVEL = BedrockCauldronProperties.MAX_LEVEL_8;
    public static final int BOTTLE_LEVEL = 3;
    public static final int ARROW_TIP_LEVEL_PER_STEP = 2;

    private static final int ARROW_TIP_STEP = 4;

    public PotionCauldronBlock(Settings settings) {
        super(settings, BedrockCauldronBehavior.POTION_CAULDRON_BEHAVIOR);
        this.setDefaultState(this.getStateManager().getDefaultState().with(LEVEL, 2));
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
    public double getFluidHeight(BlockState state) {
        return MathHelper.lerp((float) state.get(LEVEL) / MAX_LEVEL, 0.375, 0.9375);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (world == null || pos == null || state == null) {
            return;
        }
        if (random.nextInt() % 7 != 0) {
            return;
        }

        world.getBlockEntity(pos, BedrockCauldronBlocks.WATER_CAULDRON_ENTITY).ifPresent(blockEntity -> {
            final int effectColor = blockEntity.getTintColor();
            final double red = ((effectColor >> 16) & 0xff) / 255.;
            final double green = ((effectColor >> 8) & 0xff) / 255.;
            final double blue = (effectColor & 0xff) / 255.;
            final double offsetY;
            if (state.getBlock() instanceof PotionCauldronBlock potionCauldronBlock) {
                offsetY = potionCauldronBlock.getFluidHeight(state);
            } else {
                offsetY = 0.5;
            }
            world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.45 + random.nextDouble() * 0.2, pos.getY() + offsetY, pos.getZ() + 0.45 + random.nextDouble() * 0.2, red, green, blue);
        });
    }

    /**
     * Tries to take out the fluid. If succeeded, the {@link BlockState} will be changed.
     *
     * @param state The BlockState that contains the state of {@link PotionCauldronBlock}.
     * @param world The instance of {@link World}.
     * @param pos   Target block position.
     * @return <code>true</code> if it can be taken out.
     */
    public static boolean tryPickFluid(BlockState state, World world, BlockPos pos) {
        if (world.isClient) {
            return false;
        }

        if (!state.contains(LEVEL)) {
            Bedrockify.LOGGER.error(
                    "[%s] cannot retrieve fluid level".formatted(Bedrockify.class.getSimpleName()),
                    new IllegalStateException("BlockState of %s does not have state: LEVEL".formatted(state.getBlock())));
            return false;
        }

        final int currentLevel = state.get(LEVEL);
        if (currentLevel < MAX_LEVEL - BOTTLE_LEVEL * 2) {
            return false;
        }

        final int nextLevel = currentLevel - BOTTLE_LEVEL;
        final BlockState blockState = (nextLevel <= 0) ? Blocks.CAULDRON.getDefaultState() : state.with(LEVEL, nextLevel);
        world.setBlockState(pos, blockState);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));

        return true;
    }

    /**
     * Gets the maximum number of available stack count for creating Tipped Arrow.<br>
     * This calculation is based on {@link PotionCauldronBlock#ARROW_TIP_STEP} and {@link PotionCauldronBlock#ARROW_TIP_LEVEL_PER_STEP}.
     * The result is as follows:<br>
     * <ul>
     *     <li>fluid: 2 - Potion 1x<br>
     *     <pre>16 * floor( 2 / 2) => 16 * 1 => returns 16</pre></li>
     *     <li>fluid: 5 - Potion 2x<br>
     *     <pre>16 * floor( 5 / 2) => 16 * 2 => returns 32</pre></li>
     *     <li>fluid: 8: MAX - Potion 3x<br>
     *     <pre>16 * floor( 8 / 2) => 16 * 4 => returns 64</pre></li>
     *     <li>fluid: 6 - when level is full and then 16 arrows tipped<br>
     *     <pre>16 * floor( 6 / 2) => 16 * 3 => returns 48</pre></li>
     * </ul>
     *
     * @param itemStack Target {@link ItemStack} that for creating Tipped Arrow.
     * @param state     The {@link BlockState} that contains the state of {@link PotionCauldronBlock}.
     * @return The stack count.
     * @see PotionCauldronBlock#getArrowTipStepCount
     */
    public static int getMaxTippedArrowCount(ItemStack itemStack, BlockState state) {
        if (!state.contains(LEVEL)) {
            Bedrockify.LOGGER.error(
                    "[%s] cannot retrieve fluid level".formatted(Bedrockify.class.getSimpleName()),
                    new IllegalStateException("BlockState of %s does not have the state: LEVEL".formatted(state.getBlock())));
            return 0;
        }

        final int mul = (int) Math.floor((float) state.get(LEVEL) / ARROW_TIP_LEVEL_PER_STEP);
        return Math.min(getArrowTipStepCount(itemStack) * mul, itemStack.getCount());
    }

    /**
     * Gets the level to decrease the fluid.<br>
     * This calculation is based on {@link PotionCauldronBlock#ARROW_TIP_STEP} and {@link PotionCauldronBlock#ARROW_TIP_LEVEL_PER_STEP}.
     * The result is as follows:<br>
     * <ul>
     *     <li>count: 16<br>
     *     <pre>(ceil(16 / 16) + 0) * 2 - 0 => 1 * 2 - 0 => returns 2</pre></li>
     *     <li>count: 32<br>
     *     <pre>(ceil(32 / 16) + 1) * 2 - 1 => 3 * 2 - 1 => returns 5</pre></li>
     *     <li>count: 48<br>
     *     <pre>(ceil(48 / 16) + 1) * 2 - 2 => 4 * 2 - 2 => returns 6</pre></li>
     *     <li>count: 64<br>
     *     <pre>(ceil(64 / 16) + 2) * 2 - 3 => 6 * 2 - 3 => returns 9</pre></li>
     * </ul>
     *
     * @param itemStack Target {@link ItemStack} that for creating Tipped Arrow.
     * @param count     The item count in stack.
     * @return The fluid level to decrease.
     * @see PotionCauldronBlock#getArrowTipStepCount
     */
    public static int getDecLevelByStack(ItemStack itemStack, int count) {
        if (count <= 0) {
            return 0;
        }

        final int step = (int) Math.ceil((float) count / getArrowTipStepCount(itemStack));
        return (step + (step >> 1)) * ARROW_TIP_LEVEL_PER_STEP - (step - 1);
    }

    /**
     * Returns the step count.<br>
     * The count will be divided by {@link PotionCauldronBlock#ARROW_TIP_STEP} for the maximum number that can retrieve by {@link ItemStack#getMaxCount}.<br>
     * Default is <code>16</code>.
     *
     * @param itemStack Target item stack.
     * @return Divided by {@link PotionCauldronBlock#ARROW_TIP_STEP}.
     */
    private static int getArrowTipStepCount(ItemStack itemStack) {
        return (int) Math.ceil((float) itemStack.getMaxCount() / ARROW_TIP_STEP);
    }
}
