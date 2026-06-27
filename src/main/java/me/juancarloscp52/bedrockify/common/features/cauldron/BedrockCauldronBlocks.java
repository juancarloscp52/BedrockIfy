package me.juancarloscp52.bedrockify.common.features.cauldron;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.block.ColoredWaterCauldronBlock;
import me.juancarloscp52.bedrockify.common.block.PotionCauldronBlock;
import me.juancarloscp52.bedrockify.common.block.entity.WaterCauldronBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public final class BedrockCauldronBlocks {
    public static final Block POTION_CAULDRON;
    public static final Block COLORED_WATER_CAULDRON;

    private static final Identifier ID_POTION_CAULDRON = Identifier.fromNamespaceAndPath(Bedrockify.MOD_ID, "potion_cauldron");
    private static final Identifier ID_COLORED_WATER_CAULDRON = Identifier.fromNamespaceAndPath(Bedrockify.MOD_ID, "colored_water_cauldron");

    public static final BlockEntityType<WaterCauldronBlockEntity> WATER_CAULDRON_ENTITY;

    public static void register() {
        Registry.register(BuiltInRegistries.BLOCK, ID_POTION_CAULDRON, POTION_CAULDRON);
        Registry.register(BuiltInRegistries.BLOCK, ID_COLORED_WATER_CAULDRON, COLORED_WATER_CAULDRON);

        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(Bedrockify.MOD_ID, "water_cauldron_entity"), WATER_CAULDRON_ENTITY);
    }

    private static Block prepare(Identifier id, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
        return factory.apply(settings.setId(key));
    }

    static {
        POTION_CAULDRON = prepare(ID_POTION_CAULDRON, PotionCauldronBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON).emissiveRendering((state) -> true));
        COLORED_WATER_CAULDRON = prepare(ID_COLORED_WATER_CAULDRON, ColoredWaterCauldronBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON));

        WATER_CAULDRON_ENTITY = FabricBlockEntityTypeBuilder.create(WaterCauldronBlockEntity::new, POTION_CAULDRON, COLORED_WATER_CAULDRON).build();
    }
}
