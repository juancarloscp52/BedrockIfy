package me.juancarloscp52.bedrockify.mixin.common.features.fertilizableBlocks;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin implements Fertilizable {
    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        int below = countSugarCaneBelow(world,pos);
        int above = countSugarCaneAbove(world,pos);
        return (below+above)<2;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int below = countSugarCaneBelow(world,pos);
        if(below<=1){
            for(int i = 1; i<=2-below; i++){
                BlockPos newPos = pos.up(i);
                if(world.getBlockState(newPos).isAir()){
                    world.setBlockState(newPos, state.with(Properties.AGE_15, 0));
                }
            }
        }
    }

    protected int countSugarCaneAbove(BlockView world, BlockPos pos) {
        int i;
        for (i = 0; i < 16 && world.getBlockState(pos.up(i + 1)).isOf(Blocks.SUGAR_CANE); ++i) { }
        return i;
    }

    protected int countSugarCaneBelow(BlockView world, BlockPos pos) {
        int i;
        for (i = 0; i < 16 && world.getBlockState(pos.down(i + 1)).isOf(Blocks.SUGAR_CANE); ++i) { }
        return i;
    }
}
