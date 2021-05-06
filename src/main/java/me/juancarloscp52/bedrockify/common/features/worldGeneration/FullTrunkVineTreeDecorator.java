package me.juancarloscp52.bedrockify.common.features.worldGeneration;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.tree.TreeDecorator;
import net.minecraft.world.gen.tree.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class FullTrunkVineTreeDecorator extends TreeDecorator {
    public static final FullTrunkVineTreeDecorator INSTANCE = new FullTrunkVineTreeDecorator();

    public static final Codec<FullTrunkVineTreeDecorator> CODEC = Codec.unit(() -> INSTANCE);

    protected TreeDecoratorType<?> getType() {
        return DyingTrees.VINE_DECORATOR;
    }

    public void generate(StructureWorldAccess world, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions, Set<BlockPos> placedStates, BlockBox box) {
        logPositions.forEach((pos) -> setVines(world,pos));
    }


    public void setVines(StructureWorldAccess world,BlockPos trunkPos){
        for(Direction direction: Direction.Type.HORIZONTAL){
            setVineOnTrunk(world,trunkPos,direction);
        }
    }

    public void setVineOnTrunk(StructureWorldAccess world, BlockPos trunkPos, Direction direction){
        BlockPos vinePos = trunkPos.offset(direction.getOpposite());
        if (Feature.isAir(world, vinePos))
            world.setBlockState(vinePos, Blocks.VINE.getDefaultState().with(VineBlock.getFacingProperty(direction),true),3);
    }
}