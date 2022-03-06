package me.juancarloscp52.bedrockify.common.features.worldGeneration;

import com.google.common.collect.ImmutableList;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.mixin.common.features.worldGeneration.TreeConfiguredFeaturesInvoker;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.ThreeLayersFeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.DarkOakFoliagePlacer;
import net.minecraft.world.gen.foliage.PineFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.OptionalInt;
import java.util.function.Predicate;

public class DyingTrees {

    public static TreeDecoratorType<FullTrunkVineTreeDecorator> VINE_DECORATOR;

    private static final ConfiguredFeature<?, ?> DYING_BIRCH_TREE = new ConfiguredFeature<>(Feature.TREE, TreeConfiguredFeaturesInvoker.invokeBuilder(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 5, 2, 0, 2).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    private static final ConfiguredFeature<?, ?> DYING_OAK_TREE = new ConfiguredFeature<>(Feature.TREE, TreeConfiguredFeaturesInvoker.invokeBuilder(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 4, 2, 0, 2).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    private static final ConfiguredFeature<?, ?> DYING_SPRUCE_TREE =  new ConfiguredFeature<>(Feature.TREE, new TreeFeatureConfig.Builder(BlockStateProvider.of(Blocks.SPRUCE_LOG), new StraightTrunkPlacer(5, 2, 1), BlockStateProvider.of(Blocks.SPRUCE_LEAVES), new SpruceFoliagePlacer(UniformIntProvider.create(2, 3), UniformIntProvider.create(0, 2), UniformIntProvider.create(1, 2)), new TwoLayersFeatureSize(2, 0, 2)).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    private static final ConfiguredFeature<?, ?> DYING_PINE_TREE = new ConfiguredFeature<>(Feature.TREE, new TreeFeatureConfig.Builder(BlockStateProvider.of(Blocks.SPRUCE_LOG), new StraightTrunkPlacer(6, 4, 0), BlockStateProvider.of(Blocks.SPRUCE_LEAVES), new PineFoliagePlacer(ConstantIntProvider.create(1), ConstantIntProvider.create(1), UniformIntProvider.create(3, 4)), new TwoLayersFeatureSize(2, 0, 2)).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());
    private static final ConfiguredFeature<?, ?> DYING_DARK_OAK_TREE = new ConfiguredFeature<>(Feature.TREE, new TreeFeatureConfig.Builder(BlockStateProvider.of(Blocks.DARK_OAK_LOG), new DarkOakTrunkPlacer(6, 2, 1), BlockStateProvider.of(Blocks.DARK_OAK_LEAVES), new DarkOakFoliagePlacer(ConstantIntProvider.create(0), ConstantIntProvider.create(0)), new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())).ignoreVines().decorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)).build());


    private static final Feature<DefaultFeatureConfig> FALLEN_OAK_TREE = new FallenTreeFeature(DefaultFeatureConfig.CODEC, Blocks.OAK_LOG);
    private static final ConfiguredFeature<?, ?> FALLEN_OAK_TREE_CONFIGURED = new ConfiguredFeature<>(FALLEN_OAK_TREE,FeatureConfig.DEFAULT);
    private static final Feature<DefaultFeatureConfig> FALLEN_BIRCH_TREE = new FallenTreeFeature(DefaultFeatureConfig.CODEC, Blocks.BIRCH_LOG);
    private static final ConfiguredFeature<?, ?> FALLEN_BIRCH_TREE_CONFIGURED = new ConfiguredFeature<>(FALLEN_BIRCH_TREE,FeatureConfig.DEFAULT);
    private static final Feature<DefaultFeatureConfig> FALLEN_SPRUCE_TREE = new FallenTreeFeature(DefaultFeatureConfig.CODEC, Blocks.SPRUCE_LOG);
    private static final ConfiguredFeature<?, ?> FALLEN_SPRUCE_TREE_CONFIGURED = new ConfiguredFeature<>(FALLEN_SPRUCE_TREE,FeatureConfig.DEFAULT);

    private static final Predicate<BiomeSelectionContext> BIRCH_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.FOREST,BiomeKeys.BIRCH_FOREST,BiomeKeys.DARK_FOREST,BiomeKeys.FLOWER_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST);
    private static final Predicate<BiomeSelectionContext> OAK_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.FOREST,BiomeKeys.FLOWER_FOREST, BiomeKeys.DARK_FOREST,BiomeKeys.WINDSWEPT_FOREST);
    private static final Predicate<BiomeSelectionContext> OAK_PLAINS_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.PLAINS,BiomeKeys.SUNFLOWER_PLAINS);
    private static final Predicate<BiomeSelectionContext> SPRUCE_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.TAIGA,BiomeKeys.WINDSWEPT_FOREST,BiomeKeys.TAIGA);
    private static final Predicate<BiomeSelectionContext> DARK_OAK_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST);

    public static void registerTrees(){
        if(Bedrockify.getInstance().settings.isDyingTreesEnabled()){
            registerDyingTrees();
            registerFallenTrees();
        }
    }

    private static void registerDyingTrees (){
        VINE_DECORATOR = TreeDecoratorType.register("bedrockify:vinedecorator", FullTrunkVineTreeDecorator.CODEC);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "dying_birch_tree"),DYING_BIRCH_TREE); // 16
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "dying_oak_tree"),DYING_OAK_TREE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "dying_spruce_tree"),DYING_SPRUCE_TREE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "dying_pine_tree"),DYING_PINE_TREE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "dying_dark_oak_tree"),DYING_DARK_OAK_TREE);

        PlacedFeature PLACED_DYING_BIRCH_TREE = new PlacedFeature(RegistryEntry.of(DYING_BIRCH_TREE),VegetationPlacedFeatures.modifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.25f, 2), Blocks.BIRCH_SAPLING));
        PlacedFeature PLACED_DYING_OAK_TREE = new PlacedFeature(RegistryEntry.of(DYING_OAK_TREE),VegetationPlacedFeatures.modifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(1, 0.1f, 0), Blocks.OAK_SAPLING));
        PlacedFeature PLACED_DYING_OAK_TREE_PLAINS = new PlacedFeature(RegistryEntry.of(DYING_OAK_TREE),VegetationPlacedFeatures.modifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.01f, 1), Blocks.OAK_SAPLING));
        PlacedFeature PLACED_DYING_SPRUCE_TREE = new PlacedFeature(RegistryEntry.of(DYING_SPRUCE_TREE),VegetationPlacedFeatures.modifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(1, 0.1f, 0), Blocks.SPRUCE_SAPLING));
        PlacedFeature PLACED_DYING_PINE_TREE = new PlacedFeature(RegistryEntry.of(DYING_PINE_TREE),VegetationPlacedFeatures.modifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(1, 0.1f, 0), Blocks.SPRUCE_SAPLING));
        PlacedFeature PLACED_DYING_DARK_OAK_TREE = new PlacedFeature(RegistryEntry.of(DYING_DARK_OAK_TREE),VegetationPlacedFeatures.modifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(1, 0.1f, 0), Blocks.DARK_OAK_SAPLING));

        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:dying_birch_tree",PLACED_DYING_BIRCH_TREE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:dying_oak_tree",PLACED_DYING_OAK_TREE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:dying_oak_tree_plains",PLACED_DYING_OAK_TREE_PLAINS);
        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:dying_spruce_tree",PLACED_DYING_SPRUCE_TREE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:dying_pine_tree",PLACED_DYING_PINE_TREE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:dying_dark_oak_tree",PLACED_DYING_DARK_OAK_TREE);

        BiomeModifications.create(new Identifier("bedrockify:dyingtrees_birch")).add(ModificationPhase.ADDITIONS, BIRCH_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,BuiltinRegistries.PLACED_FEATURE.getKey(PLACED_DYING_BIRCH_TREE).get());
        });
        BiomeModifications.create(new Identifier("bedrockify:dyingtrees_oak")).add(ModificationPhase.ADDITIONS, OAK_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,BuiltinRegistries.PLACED_FEATURE.getKey(PLACED_DYING_OAK_TREE).get());
        });
        BiomeModifications.create(new Identifier("bedrockify:dyingtrees_oak_plains")).add(ModificationPhase.ADDITIONS, OAK_PLAINS_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,BuiltinRegistries.PLACED_FEATURE.getKey(PLACED_DYING_OAK_TREE_PLAINS).get());
        });
        BiomeModifications.create(new Identifier("bedrockify:dyingtrees_spruce")).add(ModificationPhase.ADDITIONS, SPRUCE_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,BuiltinRegistries.PLACED_FEATURE.getKey(PLACED_DYING_SPRUCE_TREE).get());
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,BuiltinRegistries.PLACED_FEATURE.getKey(PLACED_DYING_PINE_TREE).get());
        });
        BiomeModifications.create(new Identifier("bedrockify:dyingtrees_dark_oak")).add(ModificationPhase.ADDITIONS, DARK_OAK_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,BuiltinRegistries.PLACED_FEATURE.getKey(PLACED_DYING_DARK_OAK_TREE).get());
        });
    }

    private static void registerFallenTrees(){
        Registry.register(Registry.FEATURE, new Identifier("bedrockify","fallen_oak_tree"), FALLEN_OAK_TREE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("bedrockify","fallen_oak_tree_c"), FALLEN_OAK_TREE_CONFIGURED);
        Registry.register(Registry.FEATURE, new Identifier("bedrockify","fallen_birch_tree"), FALLEN_BIRCH_TREE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("bedrockify","fallen_birch_tree_c"), FALLEN_BIRCH_TREE_CONFIGURED);
        Registry.register(Registry.FEATURE, new Identifier("bedrockify","fallen_spruce_tree"), FALLEN_SPRUCE_TREE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("bedrockify","fallen_spruce_tree_c"), FALLEN_SPRUCE_TREE_CONFIGURED);


        PlacedFeature PLACED_FALLEN_OAK_TREE = new PlacedFeature(RegistryEntry.of(FALLEN_OAK_TREE_CONFIGURED),VegetationPlacedFeatures.modifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.1f, 1), Blocks.OAK_SAPLING));
        PlacedFeature PLACED_FALLEN_OAK_TREE_PLAINS = new PlacedFeature(RegistryEntry.of(FALLEN_OAK_TREE_CONFIGURED),VegetationPlacedFeatures.modifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.01f, 1), Blocks.OAK_SAPLING));
        PlacedFeature PLACED_FALLEN_BIRCH_TREE = new PlacedFeature(RegistryEntry.of(FALLEN_BIRCH_TREE_CONFIGURED),VegetationPlacedFeatures.modifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.1f, 1), Blocks.BIRCH_SAPLING));
        PlacedFeature PLACED_FALLEN_SPRUCE_TREE = new PlacedFeature(RegistryEntry.of(FALLEN_SPRUCE_TREE_CONFIGURED),VegetationPlacedFeatures.modifiersWithWouldSurvive(PlacedFeatures.createCountExtraModifier(0, 0.2f, 1), Blocks.SPRUCE_SAPLING));

        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:fallen_birch_tree",PLACED_FALLEN_BIRCH_TREE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:fallen_oak_tree",PLACED_FALLEN_OAK_TREE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:fallen_oak_tree_plains",PLACED_FALLEN_OAK_TREE_PLAINS);
        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:fallen_spruce_tree",PLACED_FALLEN_SPRUCE_TREE);

        BiomeModifications.create(new Identifier("bedrockify:fallentrees_birch")).add(ModificationPhase.ADDITIONS, BIRCH_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,BuiltinRegistries.PLACED_FEATURE.getKey(PLACED_FALLEN_BIRCH_TREE).get());
        });

        BiomeModifications.create(new Identifier("bedrockify:fallentrees_oak")).add(ModificationPhase.ADDITIONS, OAK_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,BuiltinRegistries.PLACED_FEATURE.getKey(PLACED_FALLEN_OAK_TREE).get());
        });

        BiomeModifications.create(new Identifier("bedrockify:fallentrees_oak_plains")).add(ModificationPhase.ADDITIONS, OAK_PLAINS_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,BuiltinRegistries.PLACED_FEATURE.getKey(PLACED_FALLEN_OAK_TREE_PLAINS).get());
        });

        BiomeModifications.create(new Identifier("bedrockify:fallentrees_spruce")).add(ModificationPhase.ADDITIONS, SPRUCE_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,BuiltinRegistries.PLACED_FEATURE.getKey(PLACED_FALLEN_SPRUCE_TREE).get());
        });

    }

}
