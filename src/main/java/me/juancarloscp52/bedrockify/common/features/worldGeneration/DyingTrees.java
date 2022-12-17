package me.juancarloscp52.bedrockify.common.features.worldGeneration;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.function.Predicate;

public class DyingTrees {

    public static TreeDecoratorType<FullTrunkVineTreeDecorator> VINE_DECORATOR;

    public static final RegistryKey<ConfiguredFeature<?, ?>> DYING_OAK_TREE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier("bedrockify", "dying_oak_tree"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> DYING_BIRCH_TREE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier("bedrockify", "dying_birch_tree"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> DYING_SPRUCE_TREE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier("bedrockify", "dying_spruce_tree"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> DYING_PINE_TREE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier("bedrockify", "dying_pine_tree"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> DYING_DARK_OAK_TREE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier("bedrockify", "dying_dark_oak_tree"));


    public static final RegistryKey<PlacedFeature> DYING_BIRCH_TREE_PF = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "dying_birch_tree"));
    public static final RegistryKey<PlacedFeature> DYING_OAK_TREE_PF = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "dying_oak_tree"));
    public static final RegistryKey<PlacedFeature> DYING_OAK_TREE_PLAINS_PF = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "dying_oak_tree_plains"));
    public static final RegistryKey<PlacedFeature> DYING_SPRUCE_TREE_PF = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "dying_spruce_tree"));
    public static final RegistryKey<PlacedFeature> DYING_PINE_TREE_PF = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "dying_pine_tree"));
    public static final RegistryKey<PlacedFeature> DYING_DARK_OAK_TREE_PF = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "dying_dark_oak_tree"));


    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_OAK_TREE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier("bedrockify", "fallen_oak_tree_c"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_BIRCH_TREE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier("bedrockify", "fallen_birch_tree_c"));
    public static final RegistryKey<ConfiguredFeature<?, ?>> FALLEN_SPRUCE_TREE_CONFIGURED = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier("bedrockify", "fallen_spruce_tree_c"));

    public static final Feature<DefaultFeatureConfig> FALLEN_OAK_TREE = Registry.register(Registries.FEATURE,new Identifier("bedrockify", "fallen_oak_tree"),new FallenTreeFeature(DefaultFeatureConfig.CODEC, Blocks.OAK_LOG));
    public static final Feature<DefaultFeatureConfig> FALLEN_BIRCH_TREE = Registry.register(Registries.FEATURE,new Identifier("bedrockify", "fallen_birch_tree"),new FallenTreeFeature(DefaultFeatureConfig.CODEC, Blocks.BIRCH_LOG));
    public static final Feature<DefaultFeatureConfig> FALLEN_SPRUCE_TREE = Registry.register(Registries.FEATURE,new Identifier("bedrockify", "fallen_spruce_tree"),new FallenTreeFeature(DefaultFeatureConfig.CODEC, Blocks.SPRUCE_LOG));

    public static final RegistryKey<PlacedFeature> FALLEN_OAK_TREE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "fallen_oak_tree_placed"));
    public static final RegistryKey<PlacedFeature> FALLEN_OAK_TREE_PLAINS_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "fallen_oak_tree_plains_placed"));

    public static final RegistryKey<PlacedFeature> FALLEN_BIRCH_TREE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "fallen_birch_tree_placed"));
    public static final RegistryKey<PlacedFeature> FALLEN_SPRUCE_TREE_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "fallen_spruce_tree_placed"));

    private static final Predicate<BiomeSelectionContext> BIRCH_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.FOREST,BiomeKeys.BIRCH_FOREST,BiomeKeys.DARK_FOREST,BiomeKeys.FLOWER_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST);
    private static final Predicate<BiomeSelectionContext> OAK_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.FOREST,BiomeKeys.FLOWER_FOREST, BiomeKeys.DARK_FOREST,BiomeKeys.WINDSWEPT_FOREST);
    private static final Predicate<BiomeSelectionContext> OAK_PLAINS_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.PLAINS,BiomeKeys.SUNFLOWER_PLAINS);
    private static final Predicate<BiomeSelectionContext> SPRUCE_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.TAIGA,BiomeKeys.WINDSWEPT_FOREST,BiomeKeys.TAIGA);
    private static final Predicate<BiomeSelectionContext> DARK_OAK_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST);

    public static void registerTrees(){
        registerDyingTrees();
        registerFallenTrees();
    }

    private static void registerDyingTrees (){

        if(!Bedrockify.getInstance().settings.dyingTrees)
            return;

        VINE_DECORATOR = TreeDecoratorType.register("bedrockify:vinedecorator", FullTrunkVineTreeDecorator.CODEC);

        BiomeModifications.create(new Identifier("bedrockify:dyingtrees_birch")).add(ModificationPhase.ADDITIONS, BIRCH_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,DYING_BIRCH_TREE_PF);
        });
        BiomeModifications.create(new Identifier("bedrockify:dyingtrees_oak")).add(ModificationPhase.ADDITIONS, OAK_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,DYING_OAK_TREE_PF);
        });
        BiomeModifications.create(new Identifier("bedrockify:dyingtrees_oak_plains")).add(ModificationPhase.ADDITIONS, OAK_PLAINS_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,DYING_OAK_TREE_PLAINS_PF);
        });
        BiomeModifications.create(new Identifier("bedrockify:dyingtrees_spruce")).add(ModificationPhase.ADDITIONS, SPRUCE_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,DYING_SPRUCE_TREE_PF);
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,DYING_PINE_TREE_PF);
        });
        BiomeModifications.create(new Identifier("bedrockify:dyingtrees_dark_oak")).add(ModificationPhase.ADDITIONS, DARK_OAK_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,DYING_DARK_OAK_TREE_PF);
        });
    }

    private static void registerFallenTrees(){

        if(!Bedrockify.getInstance().settings.fallenTrees)
            return;

        BiomeModifications.create(new Identifier("bedrockify:fallentrees_birch")).add(ModificationPhase.ADDITIONS, BIRCH_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,FALLEN_BIRCH_TREE_PLACED);
        });

        BiomeModifications.create(new Identifier("bedrockify:fallentrees_oak")).add(ModificationPhase.ADDITIONS, OAK_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,FALLEN_OAK_TREE_PLACED);
        });

        BiomeModifications.create(new Identifier("bedrockify:fallentrees_oak_plains")).add(ModificationPhase.ADDITIONS, OAK_PLAINS_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,FALLEN_OAK_TREE_PLAINS_PLACED);
        });

        BiomeModifications.create(new Identifier("bedrockify:fallentrees_spruce")).add(ModificationPhase.ADDITIONS, SPRUCE_BIOME_SELECTION_CONTEXT,biomeModificationContext -> {
            biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION,FALLEN_SPRUCE_TREE_PLACED);
        });

    }

}
