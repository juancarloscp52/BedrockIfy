package me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip;

import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ContainerTooltip extends Tooltip {


    public ContainerTooltip(ItemStack item){
        this.translationKey = item.getTranslationKey();
        this.primaryValue = item.getCount();
    }

    @Override
    public MutableText getTooltipText() {
        MutableText tooltip = Text.translatable(translationKey);
        tooltip.append(" x").append(String.valueOf(primaryValue));
        return tooltip;
    }
}
