package me.juancarloscp52.bedrockify.datagen;

import com.google.common.collect.ImmutableList;
import me.juancarloscp52.bedrockify.common.features.fernBonemeal.FernBonemeal;
import me.juancarloscp52.bedrockify.common.features.worldGeneration.DyingTrees;
import me.juancarloscp52.bedrockify.common.features.worldGeneration.FullTrunkVineTreeDecorator;
import me.juancarloscp52.bedrockify.mixin.common.features.worldGeneration.TreeConfiguredFeaturesInvoker;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.DarkOakFoliagePlacer;
import net.minecraft.world.gen.foliage.PineFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.Collections;
import java.util.OptionalInt;

public class BedrockIfyWorldGenBootstrap {

    // Create tree configured features
    private static final ConfiguredFeature<?, ?> DYING_BIRCH_TREE_CONFIGURED = new ConfiguredFeature<>(Feature.TREE, TreeConfiguredFeaturesInvoker.invokeBuilder(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 5, 2, 0, 2).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    static final ConfiguredFeature<?,?> DYING_OAK_TREE_CONFIGURED = new ConfiguredFeature<>(Feature.TREE, TreeConfiguredFeaturesInvoker.invokeBuilder(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 4, 2, 0, 2).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    private static final ConfiguredFeature<?, ?> DYING_SPRUCE_TREE_CONFIGURED =  new ConfiguredFeature<>(Feature.TREE, new TreeFeatureConfig.Builder(BlockStateProvider.of(Blocks.SPRUCE_LOG), new StraightTrunkPlacer(5, 2, 1), BlockStateProvider.of(Blocks.SPRUCE_LEAVES), new SpruceFoliagePlacer(UniformIntProvider.create(2, 3), UniformIntProvider.create(0, 2), UniformIntProvider.create(1, 2)), new TwoLayersFeatureSize(2, 0, 2)).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    private static final ConfiguredFeature<?, ?> DYING_PINE_TREE_CONFIGURED = new ConfiguredFeature<>(Feature.TREE, new TreeFeatureConfig.Builder(BlockStateProvider.of(Blocks.SPRUCE_LOG), new StraightTrunkPlacer(6, 4, 0), BlockStateProvider.of(Blocks.SPRUCE_LEAVES), new PineFoliagePlacer(ConstantIntProvider.create(1), ConstantIntProvider.create(1), UniformIntProvider.create(3, 4)), new TwoLayersFeatureSize(2, 0, 2)).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    private static final ConfiguredFeature<?, ?> DYING_DARK_OAK_TREE_CONFIGURED = new ConfiguredFeature<>(Feature.TREE, new TreeFeatureConfig.Builder(BlockStateProvider.of(Blocks.DARK_OAK_LOG), new DarkOakTrunkPlacer(6, 2, 1), BlockStateProvider.of(Blocks.DARK_OAK_LEAVES), new DarkOakFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0)), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());

    // Define tree placed features
    private static PlacedFeature PLACED_DYING_BIRCH_TREE;
    private static PlacedFeature PLACED_DYING_OAK_TREE;
    private static PlacedFeature PLACED_DYING_OAK_TREE_PLAINS;
    private static PlacedFeature PLACED_DYING_SPRUCE_TREE;
    private static PlacedFeature PLACED_DYING_PINE_TREE;
    private static PlacedFeature PLACED_DYING_DARK_OAK_TREE;


    // Fallen Trees
    private static final ConfiguredFeature<?, ?> FALLEN_OAK_TREE_CONFIGURED = new ConfiguredFeature<>(DyingTrees.FALLEN_OAK_TREE,FeatureConfig.DEFAULT);
    private static final ConfiguredFeature<?, ?> FALLEN_BIRCH_TREE_CONFIGURED = new ConfiguredFeature<>(DyingTrees.FALLEN_BIRCH_TREE,FeatureConfig.DEFAULT);
    private static final ConfiguredFeature<?, ?> FALLEN_SPRUCE_TREE_CONFIGURED = new ConfiguredFeature<>(DyingTrees.FALLEN_SPRUCE_TREE,FeatureConfig.DEFAULT);

    private static final ConfiguredFeature<SimpleBlockFeatureConfig, ?> SINGLE_PIECE_OF_FERN = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.FERN.getDefaultState())));

    private static PlacedFeature PLACED_FALLEN_OAK_TREE;
    private static PlacedFeature PLACED_FALLEN_OAK_TREE_PLAINS;
    private static PlacedFeature PLACED_FALLEN_BIRCH_TREE;
    private static PlacedFeature PLACED_FALLEN_SPRUCE_TREE;
    private static PlacedFeature PLACED_FERN_BONEMEAL;


    // Create tree placed features
    private static void initializeTreePlacedFeatures(RegistryEntryLookup<ConfiguredFeature<?,?>> lookup){
        PLACED_DYING_BIRCH_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_BIRCH_TREE),VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.25f, 2), Blocks.BIRCH_SAPLING));
        PLACED_DYING_OAK_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_OAK_TREE),VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(1, 0.1f, 0), Blocks.OAK_SAPLING));
        PLACED_DYING_OAK_TREE_PLAINS = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_OAK_TREE),VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.01f, 1), Blocks.OAK_SAPLING));
        PLACED_DYING_SPRUCE_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_SPRUCE_TREE),VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(1, 0.1f, 0), Blocks.SPRUCE_SAPLING));
        PLACED_DYING_PINE_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_PINE_TREE),VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(1, 0.1f, 0), Blocks.SPRUCE_SAPLING));
        PLACED_DYING_DARK_OAK_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_DARK_OAK_TREE),VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(1, 0.1f, 0), Blocks.DARK_OAK_SAPLING));

        PLACED_FALLEN_OAK_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.FALLEN_OAK_TREE_CONFIGURED),VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.1f, 1), Blocks.OAK_SAPLING));
        PLACED_FALLEN_OAK_TREE_PLAINS = new PlacedFeature(lookup.getOrThrow(DyingTrees.FALLEN_OAK_TREE_CONFIGURED),VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.01f, 1), Blocks.OAK_SAPLING));
        PLACED_FALLEN_BIRCH_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.FALLEN_BIRCH_TREE_CONFIGURED),VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.1f, 1), Blocks.BIRCH_SAPLING));
        PLACED_FALLEN_SPRUCE_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.FALLEN_SPRUCE_TREE_CONFIGURED),VegetationPlacedFeatures.treeModifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.2f, 1), Blocks.SPRUCE_SAPLING));

        PLACED_FERN_BONEMEAL = new PlacedFeature(lookup.getOrThrow(FernBonemeal.SINGLE_PIECE_OF_FERN), Collections.singletonList(PlacedFeatures.isAir()));

    }

    static void placedFeatures(Registerable<PlacedFeature> registry){
        RegistryEntryLookup<ConfiguredFeature<?,?>> configuredFeatureLookup = registry.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        initializeTreePlacedFeatures(configuredFeatureLookup);
        registry.register(DyingTrees.DYING_BIRCH_TREE_PF,PLACED_DYING_BIRCH_TREE);
        registry.register(DyingTrees.DYING_OAK_TREE_PF,PLACED_DYING_OAK_TREE);
        registry.register(DyingTrees.DYING_OAK_TREE_PLAINS_PF,PLACED_DYING_OAK_TREE_PLAINS);
        registry.register(DyingTrees.DYING_SPRUCE_TREE_PF,PLACED_DYING_SPRUCE_TREE);
        registry.register(DyingTrees.DYING_PINE_TREE_PF,PLACED_DYING_PINE_TREE);
        registry.register(DyingTrees.DYING_DARK_OAK_TREE_PF,PLACED_DYING_DARK_OAK_TREE);

        registry.register(DyingTrees.FALLEN_BIRCH_TREE_PLACED,PLACED_FALLEN_BIRCH_TREE);
        registry.register(DyingTrees.FALLEN_OAK_TREE_PLAINS_PLACED,PLACED_FALLEN_OAK_TREE_PLAINS);
        registry.register(DyingTrees.FALLEN_OAK_TREE_PLACED,PLACED_FALLEN_OAK_TREE);
        registry.register(DyingTrees.FALLEN_SPRUCE_TREE_PLACED,PLACED_FALLEN_SPRUCE_TREE);

        registry.register(FernBonemeal.SINGLE_PIECE_OF_FERN_PLACED,PLACED_FERN_BONEMEAL);

    }

    public static void configureFeatures(Registerable<ConfiguredFeature<?, ?>> registry){
        //RegistryEntryLookup<PlacedFeature> placedFeatureLookup = registry.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        registry.register(DyingTrees.DYING_BIRCH_TREE, DYING_BIRCH_TREE_CONFIGURED);
        registry.register(DyingTrees.DYING_OAK_TREE, DYING_OAK_TREE_CONFIGURED);
        registry.register(DyingTrees.DYING_SPRUCE_TREE, DYING_SPRUCE_TREE_CONFIGURED);
        registry.register(DyingTrees.DYING_PINE_TREE, DYING_PINE_TREE_CONFIGURED);
        registry.register(DyingTrees.DYING_DARK_OAK_TREE, DYING_DARK_OAK_TREE_CONFIGURED);

        registry.register(DyingTrees.FALLEN_BIRCH_TREE_CONFIGURED,FALLEN_BIRCH_TREE_CONFIGURED);
        registry.register(DyingTrees.FALLEN_OAK_TREE_CONFIGURED,FALLEN_OAK_TREE_CONFIGURED);
        registry.register(DyingTrees.FALLEN_SPRUCE_TREE_CONFIGURED,FALLEN_SPRUCE_TREE_CONFIGURED);

        registry.register(FernBonemeal.SINGLE_PIECE_OF_FERN,SINGLE_PIECE_OF_FERN);

    }
}
