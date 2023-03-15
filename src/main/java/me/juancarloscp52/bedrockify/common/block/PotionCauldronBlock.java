package me.juancarloscp52.bedrockify.common.block;

import me.juancarloscp52.bedrockify.common.block.cauldron.BedrockCauldronBehavior;
import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronBlocks;
import me.juancarloscp52.bedrockify.mixin.common.features.cauldron.LeveledCauldronBlockAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * Allows to keep the potion fluid.
 */
public class PotionCauldronBlock extends AbstractBECauldronBlock {
    public PotionCauldronBlock(Settings settings) {
        super(settings, BedrockCauldronBehavior.POTION_CAULDRON_BEHAVIOR);
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
            if (state.getBlock() instanceof LeveledCauldronBlock leveledCauldronBlock) {
                offsetY = ((LeveledCauldronBlockAccessor) leveledCauldronBlock).invokeGetFluidHeight(state);
            } else {
                offsetY = 0.5;
            }
            world.addParticle(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.45 + random.nextDouble() * 0.2, pos.getY() + offsetY, pos.getZ() + 0.45 + random.nextDouble() * 0.2, red, green, blue);
        });
    }
}
