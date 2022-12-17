package me.juancarloscp52.bedrockify.common.features.fernBonemeal;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;


public class FernBonemeal {
    public static final RegistryKey<ConfiguredFeature<?, ?>> SINGLE_PIECE_OF_FERN = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier("bedrockify", "single_piece_of_fern"));
    public static final RegistryKey<PlacedFeature> SINGLE_PIECE_OF_FERN_PLACED = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier("bedrockify", "single_piece_of_fern_placed"));

}
