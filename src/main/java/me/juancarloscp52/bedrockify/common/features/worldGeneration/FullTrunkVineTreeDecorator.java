package me.juancarloscp52.bedrockify.common.features.worldGeneration;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class FullTrunkVineTreeDecorator extends TreeDecorator {
    public static final FullTrunkVineTreeDecorator INSTANCE = new FullTrunkVineTreeDecorator();

    public static final Codec<FullTrunkVineTreeDecorator> CODEC = Codec.unit(() -> INSTANCE);

    protected TreeDecoratorType<?> getType() {
        return DyingTrees.VINE_DECORATOR;
    }

    @Override
    public void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions) {
        logPositions.forEach((pos) -> setVines(world,replacer,pos));
    }

    public void setVines(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer,BlockPos trunkPos){
        for(Direction direction: Direction.Type.HORIZONTAL){
            setVineOnTrunk(world,replacer,trunkPos,direction);
        }
    }

    public void setVineOnTrunk(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, BlockPos trunkPos, Direction direction){
        BlockPos vinePos = trunkPos.offset(direction.getOpposite());
        if (Feature.isAir(world, vinePos))
            replacer.accept(vinePos,Blocks.VINE.getDefaultState().with(VineBlock.getFacingProperty(direction),true));
//            world.setBlockState(vinePos, Blocks.VINE.getDefaultState().with(VineBlock.getFacingProperty(direction),true),3);
    }
}