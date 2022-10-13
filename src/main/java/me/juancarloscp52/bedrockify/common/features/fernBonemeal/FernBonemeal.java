package me.juancarloscp52.bedrockify.common.features.fernBonemeal;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.Collections;

public class FernBonemeal {

    private static final ConfiguredFeature<SimpleBlockFeatureConfig, ?> SINGLE_PIECE_OF_FERN = new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.FERN.getDefaultState())));

    public static void registerFernBonemeal(){
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE,new Identifier("bedrockify", "single_piece_of_fern"),SINGLE_PIECE_OF_FERN);
        PlacedFeature PLACED_FERN_BONEMEAL = new PlacedFeature(RegistryEntry.of(SINGLE_PIECE_OF_FERN), Collections.singletonList(PlacedFeatures.isAir()));
        Registry.register(BuiltinRegistries.PLACED_FEATURE,"bedrockify:fern_bonemeal",PLACED_FERN_BONEMEAL);
    }

}
