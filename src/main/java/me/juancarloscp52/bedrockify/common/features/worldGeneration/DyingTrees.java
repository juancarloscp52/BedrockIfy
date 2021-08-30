package me.juancarloscp52.bedrockify.common.features.worldGeneration;

import com.google.common.collect.ImmutableList;
import me.juancarloscp52.bedrockify.Bedrockify;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.function.Predicate;

public class DyingTrees {

    public static TreeDecoratorType<FullTrunkVineTreeDecorator> VINE_DECORATOR;
    private static final ConfiguredFeature<TreeFeatureConfig, ?> DYING_BIRCH_TREE = Feature.TREE.configure(ConfiguredFeatures.BIRCH.getConfig().setTreeDecorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)));
    private static final ConfiguredFeature<TreeFeatureConfig, ?> DYING_OAK_TREE = Feature.TREE.configure(ConfiguredFeatures.OAK.getConfig().setTreeDecorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)));
    private static final ConfiguredFeature<TreeFeatureConfig, ?> DYING_SPRUCE_TREE = Feature.TREE.configure(ConfiguredFeatures.SPRUCE.getConfig().setTreeDecorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)));
    private static final ConfiguredFeature<TreeFeatureConfig, ?> DYING_PINE_TREE = Feature.TREE.configure(ConfiguredFeatures.PINE.getConfig().setTreeDecorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)));
    private static final ConfiguredFeature<TreeFeatureConfig, ?> DYING_DARK_OAK_TREE = Feature.TREE.configure(ConfiguredFeatures.DARK_OAK.getConfig().setTreeDecorators(ImmutableList.of(FullTrunkVineTreeDecorator.INSTANCE)));
    private static final Feature<DefaultFeatureConfig> FALLEN_OAK_TREE = new FallenTreeFeature(DefaultFeatureConfig.CODEC, Blocks.OAK_LOG);
    private static final ConfiguredFeature<?, ?> FALLEN_OAK_TREE_CONFIGURED = FALLEN_OAK_TREE.configure(FeatureConfig.DEFAULT);
    private static final Feature<DefaultFeatureConfig> FALLEN_BIRCH_TREE = new FallenTreeFeature(DefaultFeatureConfig.CODEC, Blocks.BIRCH_LOG);
    private static final ConfiguredFeature<?, ?> FALLEN_BIRCH_TREE_CONFIGURED = FALLEN_BIRCH_TREE.configure(FeatureConfig.DEFAULT);
    private static final Feature<DefaultFeatureConfig> FALLEN_SPRUCE_TREE = new FallenTreeFeature(DefaultFeatureConfig.CODEC, Blocks.SPRUCE_LOG);
    private static final ConfiguredFeature<?, ?> FALLEN_SPRUCE_TREE_CONFIGURED = FALLEN_SPRUCE_TREE.configure(FeatureConfig.DEFAULT);

    private static final Predicate<BiomeSelectionContext> BIRCH_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.FOREST,BiomeKeys.BIRCH_FOREST,BiomeKeys.DARK_FOREST,BiomeKeys.BIRCH_FOREST_HILLS,BiomeKeys.FLOWER_FOREST, BiomeKeys.WOODED_HILLS);
    private static final Predicate<BiomeSelectionContext> OAK_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.FOREST,BiomeKeys.FLOWER_FOREST, BiomeKeys.WOODED_HILLS, BiomeKeys.DARK_FOREST, BiomeKeys.DARK_FOREST_HILLS);
    private static final Predicate<BiomeSelectionContext> OAK_PLAINS_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.PLAINS,BiomeKeys.SUNFLOWER_PLAINS);
    private static final Predicate<BiomeSelectionContext> SPRUCE_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.WOODED_MOUNTAINS,BiomeKeys.TAIGA,BiomeKeys.TAIGA_MOUNTAINS,BiomeKeys.TAIGA_HILLS,BiomeKeys.TAIGA, BiomeKeys.GIANT_SPRUCE_TAIGA, BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS, BiomeKeys.GIANT_TREE_TAIGA, BiomeKeys.GIANT_TREE_TAIGA_HILLS);
    private static final Predicate<BiomeSelectionContext> DARK_OAK_BIOME_SELECTION_CONTEXT = BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST_HILLS, BiomeKeys.DARK_FOREST);

    public static void registerTrees(){
        if(Bedrockify.getInstance().settings.isDyingTreesEnabled()){
            registerDyingTrees();
            registerFallenTrees();
        }
    }

    private static void registerDyingTrees (){
        VINE_DECORATOR = TreeDecoratorType.register("bedrockify:vinedecorator", FullTrunkVineTreeDecorator.CODEC);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "dying_birch_tree"),DYING_BIRCH_TREE.applyChance(16)); // 16
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "dying_oak_tree"),DYING_OAK_TREE.applyChance(8));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "dying_oak_tree_plains"),DYING_OAK_TREE.applyChance(68));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "dying_spruce_tree"),DYING_SPRUCE_TREE.applyChance(8));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "dying_pine_tree"),DYING_PINE_TREE.applyChance(16));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("bedrockify", "dying_dark_oak_tree"), DYING_DARK_OAK_TREE.applyChance(8));
        BiomeModifications.addFeature(BIRCH_BIOME_SELECTION_CONTEXT, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,new Identifier("bedrockify", "dying_birch_tree")));
        BiomeModifications.addFeature(OAK_BIOME_SELECTION_CONTEXT, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,new Identifier("bedrockify", "dying_oak_tree")));
        BiomeModifications.addFeature(OAK_PLAINS_BIOME_SELECTION_CONTEXT, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,new Identifier("bedrockify", "dying_oak_tree_plains")));
        BiomeModifications.addFeature(SPRUCE_BIOME_SELECTION_CONTEXT, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,new Identifier("bedrockify", "dying_spruce_tree")));
        BiomeModifications.addFeature(SPRUCE_BIOME_SELECTION_CONTEXT, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,new Identifier("bedrockify", "dying_pine_tree")));
        BiomeModifications.addFeature(DARK_OAK_BIOME_SELECTION_CONTEXT, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,new Identifier("bedrockify", "dying_dark_oak_tree")));
    }

    private static void registerFallenTrees(){
        Registry.register(Registry.FEATURE, new Identifier("bedrockify","fallen_oak_tree"), FALLEN_OAK_TREE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("bedrockify","fallen_oak_tree_c"), FALLEN_OAK_TREE_CONFIGURED.applyChance(6));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("bedrockify","fallen_oak_tree_plains_c"), FALLEN_OAK_TREE_CONFIGURED.applyChance(54));
        Registry.register(Registry.FEATURE, new Identifier("bedrockify","fallen_birch_tree"), FALLEN_BIRCH_TREE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("bedrockify","fallen_birch_tree_c"), FALLEN_BIRCH_TREE_CONFIGURED.applyChance(10));
        Registry.register(Registry.FEATURE, new Identifier("bedrockify","fallen_spruce_tree"), FALLEN_SPRUCE_TREE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("bedrockify","fallen_spruce_tree_c"), FALLEN_SPRUCE_TREE_CONFIGURED.applyChance(8));

        BiomeModifications.addFeature(BIRCH_BIOME_SELECTION_CONTEXT, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,new Identifier("bedrockify", "fallen_birch_tree_c")));
        BiomeModifications.addFeature(OAK_BIOME_SELECTION_CONTEXT, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier("bedrockify","fallen_oak_tree_c")));
        BiomeModifications.addFeature(OAK_PLAINS_BIOME_SELECTION_CONTEXT, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,new Identifier("bedrockify", "fallen_oak_tree_plains_c")));
        BiomeModifications.addFeature(SPRUCE_BIOME_SELECTION_CONTEXT, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,new Identifier("bedrockify", "fallen_spruce_tree_c")));


    }

}
