package me.juancarloscp52.bedrockify.client.features.HeldItemTooltips.Tooltip;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.TranslatableText;

public class EnchantmentTooltip extends Tooltip {
    boolean showLevels=true;
    public EnchantmentTooltip(Enchantment enchantment, int level){
        this.translationKey = enchantment.getTranslationKey();
        this.primaryValue = level;
        if(enchantment.getMaxLevel()==1)
            showLevels=false;
    }

    @Override
    public TranslatableText getTooltipText(){
        TranslatableText tooltip = new TranslatableText(this.translationKey);
        if(showLevels)
            tooltip.append(" ").append(new TranslatableText("enchantment.level." + primaryValue));
        return tooltip;
    }
}
