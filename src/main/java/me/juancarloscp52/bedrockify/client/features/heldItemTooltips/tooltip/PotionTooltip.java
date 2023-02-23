package me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class PotionTooltip extends Tooltip {

    Text tooltip;

    public PotionTooltip (Text tooltip){
        this.tooltip = tooltip;
    }

    @Override
    public MutableText getTooltipText() {
        return (MutableText) this.tooltip;
    }
}
