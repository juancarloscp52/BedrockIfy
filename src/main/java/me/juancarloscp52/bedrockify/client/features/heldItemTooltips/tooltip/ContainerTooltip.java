package me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip;

import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

public class ContainerTooltip extends Tooltip {


    public ContainerTooltip(ItemStack item){
        this.translationKey = item.getTranslationKey();
        this.primaryValue = item.getCount();
    }

    @Override
    public TranslatableText getTooltipText() {
        TranslatableText tooltip = new TranslatableText(translationKey);
        tooltip.append(" x").append(String.valueOf(primaryValue));
        return tooltip;
    }
}
