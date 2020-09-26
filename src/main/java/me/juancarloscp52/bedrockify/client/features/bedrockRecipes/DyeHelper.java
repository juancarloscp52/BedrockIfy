package me.juancarloscp52.bedrockify.client.features.bedrockRecipes;

import net.minecraft.item.BoneMealItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class DyeHelper {

    public static boolean isDyeableItem(Item item){
        return item instanceof DyeItem || item instanceof BoneMealItem || item.equals(Items.COCOA_BEANS) || item.equals(Items.LAPIS_LAZULI) || item.equals(Items.INK_SAC);
    }
    public static DyeItem getDyeItem (Item item){
        if(item instanceof DyeItem)
            return (DyeItem) item;
        if(item instanceof BoneMealItem)
            return (DyeItem) Items.WHITE_DYE;
        if(item.equals(Items.INK_SAC))
            return (DyeItem) Items.BLACK_DYE;
        if(item.equals(Items.LAPIS_LAZULI))
            return (DyeItem) Items.BLUE_DYE;
        if(item.equals(Items.COCOA_BEANS))
            return (DyeItem) Items.BROWN_DYE;
        return (DyeItem)Items.WHITE_DYE;
    }

}
