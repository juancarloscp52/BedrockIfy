package me.juancarloscp52.bedrockify.common.features.worldGeneration;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class FallenTreeFeature extends Feature<DefaultFeatureConfig> {
    Block trunk;
    public FallenTreeFeature(Codec<DefaultFeatureConfig> configCodec, Block trunk) {
        super(configCodec);
        this.trunk=trunk;
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
                            DefaultFeatureConfig config) {

        int size = random.nextInt(3)+3;
        int distance = random.nextInt(6)>1? 1:2;
        BlockPos trunkTopPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,pos);
        if(!world.getBlockState(trunkTopPos).getMaterial().isReplaceable() || !(world.getBlockState(trunkTopPos.down()).isOf(Blocks.DIRT) || world.getBlockState(trunkTopPos.down()).isOf(Blocks.COARSE_DIRT) || world.getBlockState(trunkTopPos.down()).isOf(Blocks.PODZOL) || world.getBlockState(trunkTopPos.down()).isOf(Blocks.GRASS_BLOCK) || world.getBlockState(trunkTopPos.down()).isOf(Blocks.GRASS_PATH))){
            return false;
        }

        world.setBlockState(trunkTopPos, this.trunk.getDefaultState().with(PillarBlock.AXIS,Direction.UP.getAxis()),3);
        setVines(world,trunkTopPos,true);

        Direction direction = Direction.Type.HORIZONTAL.random(random);

        generateFallenTrunk(world,direction,size, distance+1, trunkTopPos,4);

        return true;
    }

    private boolean generateFallenTrunk(StructureWorldAccess world, Direction direction, int size, int distance, BlockPos pos, int tries){
        if(tries<=0){
            return false;
        }

        BlockPos start = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,pos.offset(direction,distance));
        int maxY=start.getY();
        for(int i = 0 ; i <size; i++){
            BlockPos temp = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,start.offset(direction,i));
            if(temp.getY()>maxY)
                maxY=temp.getY();
        }

        for(int i =0; i < size; i++){
            BlockPos.Mutable temp = start.offset(direction,i).mutableCopy();
            temp.setY(maxY);
            if((!world.getBlockState(temp).getMaterial().isReplaceable() || world.getBlockState(temp.down()).isIn(BlockTags.LOGS) || world.getBlockState(temp.down()).isOf(Blocks.RED_MUSHROOM_BLOCK) || world.getBlockState(temp.down()).isOf(Blocks.BROWN_MUSHROOM_BLOCK)) && i<2){
                return generateFallenTrunk(world, direction.rotateYClockwise(), size, distance, pos, tries-1);
            }
        }
        for(int i = 0; i < size; i++){
            BlockPos.Mutable temp = start.offset(direction,i).mutableCopy();
            temp.setY(maxY);
            if(world.getBlockState(temp).getMaterial().isReplaceable()){
                world.setBlockState(temp, this.trunk.getDefaultState().with(PillarBlock.AXIS,direction.getAxis()),3);
                if(world.getBlockState(temp.up()).getMaterial().isReplaceable()){
                    world.setBlockState(temp.up(), Blocks.AIR.getDefaultState(),3);
                }
                setVines(world,temp,false);
            }else{
                break;
            }
        }
        return true;

    }

    public void setVines(StructureWorldAccess world,BlockPos trunkPos, boolean dontRandomize){
        for(Direction direction: Direction.Type.HORIZONTAL){
            setVineOnTrunk(world,trunkPos,direction, dontRandomize);
        }
    }

    public void setVineOnTrunk(StructureWorldAccess world, BlockPos trunkPos, Direction direction, boolean dontRandomize){
        BlockPos vinePos = trunkPos.offset(direction.getOpposite());
        if (world.getBlockState(vinePos).getMaterial().isReplaceable() && (new Random().nextInt(6)>3 || dontRandomize))
            world.setBlockState(vinePos, Blocks.VINE.getDefaultState().with(VineBlock.getFacingProperty(direction),true),3);
    }
}
