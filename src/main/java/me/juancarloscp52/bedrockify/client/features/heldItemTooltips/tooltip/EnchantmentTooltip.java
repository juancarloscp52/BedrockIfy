package me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class EnchantmentTooltip extends Tooltip {
    boolean showLevels=true;
    public EnchantmentTooltip(Enchantment enchantment, int level){
        this.translationKey = enchantment.getTranslationKey();
        this.primaryValue = level;
        if(enchantment.getMaxLevel()==1)
            showLevels=false;
    }

    @Override
    public MutableText getTooltipText(){
        MutableText tooltip = Text.translatable(this.translationKey);
        if(showLevels)
            tooltip.append(" ").append(Text.translatable("enchantment.level." + primaryValue));
        return tooltip;
    }
}
