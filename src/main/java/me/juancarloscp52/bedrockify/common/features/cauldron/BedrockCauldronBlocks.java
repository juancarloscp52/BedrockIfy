package me.juancarloscp52.bedrockify.common.features.cauldron;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.block.ColoredWaterCauldronBlock;
import me.juancarloscp52.bedrockify.common.block.PotionCauldronBlock;
import me.juancarloscp52.bedrockify.common.block.entity.WaterCauldronBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class BedrockCauldronBlocks {
    public static final Block POTION_CAULDRON = new PotionCauldronBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON).emissiveLighting((state, world, pos) -> true));
    public static final Block COLORED_WATER_CAULDRON = new ColoredWaterCauldronBlock(FabricBlockSettings.copyOf(Blocks.CAULDRON));

    public static final BlockEntityType<WaterCauldronBlockEntity> WATER_CAULDRON_ENTITY = FabricBlockEntityTypeBuilder.create(WaterCauldronBlockEntity::new, POTION_CAULDRON, COLORED_WATER_CAULDRON).build();

    public static void register() {
        Registry.register(Registries.BLOCK, Identifier.of(Bedrockify.MOD_ID, "potion_cauldron"), POTION_CAULDRON);
        Registry.register(Registries.BLOCK, Identifier.of(Bedrockify.MOD_ID, "colored_water_cauldron"), COLORED_WATER_CAULDRON);

        Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Bedrockify.MOD_ID, "water_cauldron_entity"), WATER_CAULDRON_ENTITY);
    }
}
