package me.juancarloscp52.bedrockify.common.features.recipes;

import net.minecraft.util.Util;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DyeHelper {
    public static final Set<Item> ITEMS = Util.make(new HashSet<>(), set -> {
        set.addAll(Items.DYE.asList());
        set.addAll(Set.of(Items.BONE_MEAL, Items.LAPIS_LAZULI, Items.COCOA_BEANS, Items.INK_SAC));
    });

    public static boolean isDyeableItem(Item item) {
        return ITEMS.contains(item);
    }

    public static Optional<DyeItem> getDyeItem(Item item) {
        if (item instanceof DyeItem)
            return Optional.of((DyeItem) item);
        if (item instanceof BoneMealItem)
            return Optional.of((DyeItem) Items.DYE.white());
        if (item.equals(Items.INK_SAC))
            return Optional.of((DyeItem) Items.DYE.black());
        if (item.equals(Items.LAPIS_LAZULI))
            return Optional.of((DyeItem) Items.DYE.blue());
        if (item.equals(Items.COCOA_BEANS))
            return Optional.of((DyeItem) Items.DYE.brown());
        return Optional.empty();
    }
}
