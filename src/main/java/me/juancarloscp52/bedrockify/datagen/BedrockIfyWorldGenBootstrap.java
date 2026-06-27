package me.juancarloscp52.bedrockify.datagen;

import com.google.common.collect.ImmutableList;
import me.juancarloscp52.bedrockify.common.features.fernBonemeal.FernBonemeal;
import me.juancarloscp52.bedrockify.common.features.worldGeneration.DyingTrees;
import me.juancarloscp52.bedrockify.common.features.worldGeneration.FullTrunkVineTreeDecorator;
import me.juancarloscp52.bedrockify.mixin.common.features.worldGeneration.TreeFeaturesInvoker;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.PineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Collections;
import java.util.OptionalInt;

public class BedrockIfyWorldGenBootstrap {

    // Create tree configured features
    private static final ConfiguredFeature<?, ?> DYING_BIRCH_TREE_CONFIGURED = new ConfiguredFeature<>(Feature.TREE, TreeFeaturesInvoker.invokeCreateStraightBlobTree(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 5, 2, 0, 2, RuleBasedStateProvider.ifTrueThenProvide(BlockPredicate.matchesTag(BlockTags.CANNOT_REPLACE_BELOW_TREE_TRUNK), Blocks.DIRT)).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    static final ConfiguredFeature<?, ?> DYING_OAK_TREE_CONFIGURED = new ConfiguredFeature<>(Feature.TREE, TreeFeaturesInvoker.invokeCreateStraightBlobTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 4, 2, 0, 2, RuleBasedStateProvider.ifTrueThenProvide(BlockPredicate.matchesTag(BlockTags.CANNOT_REPLACE_BELOW_TREE_TRUNK), Blocks.DIRT)).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    private static final ConfiguredFeature<?, ?> DYING_SPRUCE_TREE_CONFIGURED = new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.SPRUCE_LOG), new StraightTrunkPlacer(5, 2, 1), BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(1, 2)), new TwoLayersFeatureSize(2, 0, 2), RuleBasedStateProvider.ifTrueThenProvide(BlockPredicate.matchesTag(BlockTags.CANNOT_REPLACE_BELOW_TREE_TRUNK), Blocks.DIRT)).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    private static final ConfiguredFeature<?, ?> DYING_PINE_TREE_CONFIGURED = new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.SPRUCE_LOG), new StraightTrunkPlacer(6, 4, 0), BlockStateProvider.simple(Blocks.SPRUCE_LEAVES), new PineFoliagePlacer(ConstantInt.of(1), ConstantInt.of(1), UniformInt.of(3, 4)), new TwoLayersFeatureSize(2, 0, 2), RuleBasedStateProvider.ifTrueThenProvide(BlockPredicate.matchesTag(BlockTags.CANNOT_REPLACE_BELOW_TREE_TRUNK), Blocks.DIRT)).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    private static final ConfiguredFeature<?, ?> DYING_DARK_OAK_TREE_CONFIGURED = new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(Blocks.DARK_OAK_LOG), new DarkOakTrunkPlacer(6, 2, 1), BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES), new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty()), RuleBasedStateProvider.ifTrueThenProvide(BlockPredicate.matchesTag(BlockTags.CANNOT_REPLACE_BELOW_TREE_TRUNK), Blocks.DIRT)).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());

    // Define tree placed features
    private static PlacedFeature PLACED_DYING_BIRCH_TREE;
    private static PlacedFeature PLACED_DYING_OAK_TREE;
    private static PlacedFeature PLACED_DYING_OAK_TREE_PLAINS;
    private static PlacedFeature PLACED_DYING_SPRUCE_TREE;
    private static PlacedFeature PLACED_DYING_PINE_TREE;
    private static PlacedFeature PLACED_DYING_DARK_OAK_TREE;

    private static final ConfiguredFeature<SimpleBlockConfiguration, ?> SINGLE_PIECE_OF_FERN = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.FERN.defaultBlockState())));

    private static PlacedFeature PLACED_FERN_BONEMEAL;


    // Create tree placed features
    private static void initializeTreePlacedFeatures(HolderGetter<ConfiguredFeature<?, ?>> lookup) {
        PLACED_DYING_BIRCH_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_BIRCH_TREE), VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.25f, 2), Blocks.BIRCH_SAPLING));
        PLACED_DYING_OAK_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_OAK_TREE), VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.1f, 0), Blocks.OAK_SAPLING));
        PLACED_DYING_OAK_TREE_PLAINS = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_OAK_TREE), VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.01f, 1), Blocks.OAK_SAPLING));
        PLACED_DYING_SPRUCE_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_SPRUCE_TREE), VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.1f, 0), Blocks.SPRUCE_SAPLING));
        PLACED_DYING_PINE_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_PINE_TREE), VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.1f, 0), Blocks.SPRUCE_SAPLING));
        PLACED_DYING_DARK_OAK_TREE = new PlacedFeature(lookup.getOrThrow(DyingTrees.DYING_DARK_OAK_TREE), VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.1f, 0), Blocks.DARK_OAK_SAPLING));

        PLACED_FERN_BONEMEAL = new PlacedFeature(lookup.getOrThrow(FernBonemeal.SINGLE_PIECE_OF_FERN), Collections.singletonList(PlacementUtils.isEmpty()));
    }

    static void placedFeatures(BootstrapContext<PlacedFeature> registry) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureLookup = registry.lookup(Registries.CONFIGURED_FEATURE);
        initializeTreePlacedFeatures(configuredFeatureLookup);

        registry.register(DyingTrees.DYING_BIRCH_TREE_PF, PLACED_DYING_BIRCH_TREE);
        registry.register(DyingTrees.DYING_OAK_TREE_PF, PLACED_DYING_OAK_TREE);
        registry.register(DyingTrees.DYING_OAK_TREE_PLAINS_PF, PLACED_DYING_OAK_TREE_PLAINS);
        registry.register(DyingTrees.DYING_SPRUCE_TREE_PF, PLACED_DYING_SPRUCE_TREE);
        registry.register(DyingTrees.DYING_PINE_TREE_PF, PLACED_DYING_PINE_TREE);
        registry.register(DyingTrees.DYING_DARK_OAK_TREE_PF, PLACED_DYING_DARK_OAK_TREE);

        registry.register(FernBonemeal.SINGLE_PIECE_OF_FERN_PLACED, PLACED_FERN_BONEMEAL);
    }

    public static void configureFeatures(BootstrapContext<ConfiguredFeature<?, ?>> registry) {
        //RegistryEntryLookup<PlacedFeature> placedFeatureLookup = registry.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        registry.register(DyingTrees.DYING_BIRCH_TREE, DYING_BIRCH_TREE_CONFIGURED);
        registry.register(DyingTrees.DYING_OAK_TREE, DYING_OAK_TREE_CONFIGURED);
        registry.register(DyingTrees.DYING_SPRUCE_TREE, DYING_SPRUCE_TREE_CONFIGURED);
        registry.register(DyingTrees.DYING_PINE_TREE, DYING_PINE_TREE_CONFIGURED);
        registry.register(DyingTrees.DYING_DARK_OAK_TREE, DYING_DARK_OAK_TREE_CONFIGURED);

        registry.register(FernBonemeal.SINGLE_PIECE_OF_FERN, SINGLE_PIECE_OF_FERN);

    }
}
