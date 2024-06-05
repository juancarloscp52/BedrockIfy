package me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class EnchantmentTooltip extends Tooltip {
    boolean showLevels=true;
    MutableText text;

    public EnchantmentTooltip(Enchantment enchantment, int level){
        this.text = enchantment.description().copy();
        this.primaryValue = level;
        if(enchantment.getMaxLevel()==1)
            showLevels=false;
    }

    @Override
    public MutableText getTooltipText(){
        MutableText tooltip =text;
        if(showLevels)
            tooltip.append(" ").append(Text.translatable("enchantment.level." + primaryValue));
        return tooltip;
    }
}
