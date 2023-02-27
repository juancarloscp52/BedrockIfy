package me.juancarloscp52.bedrockify.common.block.cauldron;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.block.entity.WaterCauldronBlockEntity;
import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Defines the behavior of Bedrock's Cauldron.
 */
public interface BedrockCauldronBehavior {
    Map<Item, CauldronBehavior> COLORED_WATER_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();

    CauldronBehavior DYE_ITEM_BY_COLORED_WATER = (state, world, pos, player, hand, stack) -> {
        if (state == null || world == null || pos == null) {
            return ActionResult.PASS;
        }

        Item item = stack.getItem();
        if (!(item instanceof DyeableItem)) {
            return ActionResult.PASS;
        }

        Optional<WaterCauldronBlockEntity> entity = world.getBlockEntity(pos, BedrockCauldronBlocks.WATER_CAULDRON_ENTITY);
        if (entity.isEmpty()) {
            final String message = String.format("[%s] something went wrong to dye item", Bedrockify.class.getSimpleName());
            Bedrockify.LOGGER.error(message, new NullPointerException("RegistryWorldView#getBlockEntity is not present at " + pos));
            return ActionResult.PASS;
        }

        final WaterCauldronBlockEntity blockEntity = entity.get();
        if (!(Registries.ITEM.get(blockEntity.getFluidId()) instanceof final DyeItem dyeItem)) {
            final String message = String.format("[%s] something went wrong to get color", Bedrockify.class.getSimpleName());
            Bedrockify.LOGGER.error(message, new IllegalStateException(String.format("%s#getFluidId is not an instance of DyeItem", WaterCauldronBlockEntity.class.getSimpleName())));
            return ActionResult.PASS;
        }

        if (!world.isClient) {
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
            player.setStackInHand(hand, DyeableItem.blendAndSetColor(stack, List.of(dyeItem)));
            player.incrementStat(Stats.USE_CAULDRON);
            world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.BLOCKS, 0.15f, 1.25f);
            world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }

        return ActionResult.success(world.isClient);
    };

    CauldronBehavior DYE_WATER = (state, world, pos, player, hand, stack) -> {
        if (state == null || world == null || pos == null) {
            return ActionResult.PASS;
        }

        Item item = stack.getItem();
        if (!(item instanceof DyeItem dyeItem)) {
            return ActionResult.PASS;
        }

        Optional<WaterCauldronBlockEntity> entity = world.getBlockEntity(pos, BedrockCauldronBlocks.WATER_CAULDRON_ENTITY);
        if (entity.isPresent()) {
            final WaterCauldronBlockEntity blockEntity = entity.get();
            if (Objects.equals(blockEntity.getFluidId(), Registries.ITEM.getId(dyeItem))) {
                return ActionResult.SUCCESS;
            }
        }

        if (!world.isClient) {
            BlockState newState = BedrockCauldronBlocks.COLORED_WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, state.get(LeveledCauldronBlock.LEVEL));
            world.setBlockState(pos, newState);
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            player.incrementStat(Stats.USED.getOrCreateStat(item));
            world.playSound(null, pos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS);
            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
        world.getBlockEntity(pos, BedrockCauldronBlocks.WATER_CAULDRON_ENTITY).ifPresent(blockEntity -> {
            blockEntity.setDyeItem(dyeItem);
        });

        return ActionResult.success(world.isClient);
    };

    /**
     * Registers the behavior of Bedrock's cauldron.
     */
    static void registerBehavior() {
        // Behavior of the dye item for water cauldron.
        Registries.ITEM.stream().filter(item -> item instanceof DyeItem).forEach(dyeItem -> {
            CauldronBehavior.WATER_CAULDRON_BEHAVIOR.putIfAbsent(dyeItem, DYE_WATER);
            COLORED_WATER_CAULDRON_BEHAVIOR.putIfAbsent(dyeItem, DYE_WATER);
        });

        // Behavior of the colored cauldron.
        Registries.ITEM.stream().filter(item -> item instanceof DyeableItem).forEach(item -> {
            COLORED_WATER_CAULDRON_BEHAVIOR.putIfAbsent(item, DYE_ITEM_BY_COLORED_WATER);
        });
        COLORED_WATER_CAULDRON_BEHAVIOR.putIfAbsent(Items.BUCKET, CauldronBehavior.WATER_CAULDRON_BEHAVIOR.get(Items.BUCKET));
        COLORED_WATER_CAULDRON_BEHAVIOR.putIfAbsent(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (state == null || world == null || pos == null) {
                return ActionResult.PASS;
            }
            if (state.get(LeveledCauldronBlock.LEVEL) >= LeveledCauldronBlock.MAX_LEVEL) {
                return ActionResult.PASS;
            }
            if (PotionUtil.getPotion(stack) != Potions.WATER) {
                return ActionResult.PASS;
            }

            if (!world.isClient) {
                // Replace to Water Cauldron.
                final int nextLevel = state.cycle(LeveledCauldronBlock.LEVEL).get(LeveledCauldronBlock.LEVEL);
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                world.setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, nextLevel));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }

            return ActionResult.success(world.isClient);
        });
        COLORED_WATER_CAULDRON_BEHAVIOR.putIfAbsent(Items.GLASS_BOTTLE, CauldronBehavior.WATER_CAULDRON_BEHAVIOR.get(Items.GLASS_BOTTLE));
        CauldronBehavior.registerBucketBehavior(COLORED_WATER_CAULDRON_BEHAVIOR);
    }
}
