package me.juancarloscp52.bedrockify.common.features.worldGeneration;

import com.google.common.collect.Maps;
import me.juancarloscp52.bedrockify.Bedrockify;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public class DyingTrees {

    public static final TreeDecoratorType<FullTrunkVineTreeDecorator> VINE_DECORATOR = TreeDecoratorType.register("bedrockify:vinedecorator", FullTrunkVineTreeDecorator.CODEC);

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

    /**
     * Register modifiers for dying tree<br>
     * BiomeModification -&gt; BiomeDecorator
     *
     * @see BiomeModification
     * @see DyingTrees.BiomeDecorator
     */
    private static final Map<BiomeModification, BiomeDecorator> DYING_TREE_DECORATORS = Util.make(Maps.newHashMap(), map -> {
        map.put(
                BiomeModifications.create(new Identifier("bedrockify:dyingtrees_birch")),
                new BiomeDecorator(BIRCH_BIOME_SELECTION_CONTEXT, DYING_BIRCH_TREE_PF)
        );
        map.put(
                BiomeModifications.create(new Identifier("bedrockify:dyingtrees_oak")),
                new BiomeDecorator(OAK_BIOME_SELECTION_CONTEXT, DYING_OAK_TREE_PF)
        );
        map.put(
                BiomeModifications.create(new Identifier("bedrockify:dyingtrees_oak_plains")),
                new BiomeDecorator(OAK_PLAINS_BIOME_SELECTION_CONTEXT, DYING_OAK_TREE_PLAINS_PF)
        );
        map.put(
                BiomeModifications.create(new Identifier("bedrockify:dyingtrees_spruce")),
                new BiomeDecorator(SPRUCE_BIOME_SELECTION_CONTEXT, DYING_SPRUCE_TREE_PF, DYING_PINE_TREE_PF)
        );
        map.put(
                BiomeModifications.create(new Identifier("bedrockify:dyingtrees_dark_oak")),
                new BiomeDecorator(DARK_OAK_BIOME_SELECTION_CONTEXT, DYING_DARK_OAK_TREE_PF)
        );
    });

    /**
     * Register modifiers for fallen tree<br>
     * BiomeModification -&gt; BiomeDecorator
     *
     * @see BiomeModification
     * @see DyingTrees.BiomeDecorator
     */
    private static final Map<BiomeModification, BiomeDecorator> FALLEN_TREE_DECORATORS = Util.make(Maps.newHashMap(), map -> {
        map.put(
                BiomeModifications.create(new Identifier("bedrockify:fallentrees_birch")),
                new BiomeDecorator(BIRCH_BIOME_SELECTION_CONTEXT, FALLEN_BIRCH_TREE_PLACED)
        );
        map.put(
                BiomeModifications.create(new Identifier("bedrockify:fallentrees_oak")),
                new BiomeDecorator(OAK_BIOME_SELECTION_CONTEXT, FALLEN_OAK_TREE_PLACED)
        );
        map.put(
                BiomeModifications.create(new Identifier("bedrockify:fallentrees_oak_plains")),
                new BiomeDecorator(OAK_PLAINS_BIOME_SELECTION_CONTEXT, FALLEN_OAK_TREE_PLAINS_PLACED)
        );
        map.put(
                BiomeModifications.create(new Identifier("bedrockify:fallentrees_spruce")),
                new BiomeDecorator(SPRUCE_BIOME_SELECTION_CONTEXT, FALLEN_SPRUCE_TREE_PLACED)
        );
    });

    /**
     * A data only class that records BiomeSelectionContext and List of PlacedFeature
     *
     * @see BiomeSelectionContext
     * @see PlacedFeature
     */
    private static final class BiomeDecorator {
        public final Predicate<BiomeSelectionContext> selector;
        public final List<RegistryKey<PlacedFeature>> features;

        @SafeVarargs
        public BiomeDecorator(Predicate<BiomeSelectionContext> selector, RegistryKey<PlacedFeature>... features) {
            this.selector = selector;
            this.features = Arrays.stream(features).filter(Objects::nonNull).toList();
        }
    }

    public static void registerTrees(){
        registerDyingTrees();
        registerFallenTrees();
    }

    private static void registerDyingTrees (){
        DYING_TREE_DECORATORS.forEach((modification, decorator) -> {
            modification.add(ModificationPhase.ADDITIONS, decorator.selector, biomeModificationContext -> {
                if (Bedrockify.getInstance().settings.dyingTrees) {
                    decorator.features.forEach(feature -> {
                        biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION, feature);
                    });
                } else {
                    decorator.features.forEach(feature -> {
                        biomeModificationContext.getGenerationSettings().removeFeature(GenerationStep.Feature.VEGETAL_DECORATION, feature);
                    });
                }
            });
        });
    }

    private static void registerFallenTrees(){
        FALLEN_TREE_DECORATORS.forEach((modification, decorator) -> {
            modification.add(ModificationPhase.ADDITIONS, decorator.selector, biomeModificationContext -> {
                if (Bedrockify.getInstance().settings.fallenTrees) {
                    decorator.features.forEach(feature -> {
                        biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION, feature);
                    });
                } else {
                    decorator.features.forEach(feature -> {
                        biomeModificationContext.getGenerationSettings().removeFeature(GenerationStep.Feature.VEGETAL_DECORATION, feature);
                    });
                }
            });
        });

    }

}
