package me.juancarloscp52.bedrockify.common.features.worldGeneration;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class FullTrunkVineTreeDecorator extends TreeDecorator {
    public static final FullTrunkVineTreeDecorator INSTANCE = new FullTrunkVineTreeDecorator();

    public static final MapCodec<FullTrunkVineTreeDecorator> CODEC = MapCodec.unit(() -> INSTANCE);

    protected TreeDecoratorType<?> getType() {
        return DyingTrees.VINE_DECORATOR;
    }

    @Override
    public void generate(Generator generator) {
        generator.getLogPositions().forEach((pos) -> setVines(generator.getWorld(),generator,pos));
    }



    public void setVines(TestableWorld world, Generator generator,BlockPos trunkPos){
        for(Direction direction: Direction.Type.HORIZONTAL){
            setVineOnTrunk(world,generator,trunkPos,direction);
        }
    }

    public void setVineOnTrunk(TestableWorld world, Generator generator, BlockPos trunkPos, Direction direction){
        BlockPos vinePos = trunkPos.offset(direction.getOpposite());
        if (world.testBlockState(vinePos, AbstractBlock.AbstractBlockState::isAir))
            generator.replace(vinePos,Blocks.VINE.getDefaultState().with(VineBlock.getFacingProperty(direction),true));
    }
}