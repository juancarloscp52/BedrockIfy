package me.juancarloscp52.bedrockify.mixin.common.features.fertilizableBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FlowerBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(FlowerBlock.class)
public class FlowerBlockMixin implements Fertilizable {
    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.isOf(Blocks.WITHER_ROSE);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int amount = random.nextBetween(1,5);
        for(int i = 0; i<amount;i++){
            int x = random.nextBetween(-3,3);
            int z = random.nextBetween(-3,3);
            BlockPos newPos = pos.add(x,0,z);
            if(world.getBlockState(newPos).isAir() && world.getBlockState(newPos.down()).isIn(BlockTags.DIRT)){
                world.setBlockState(newPos,state);
            }
        }
    }
}
