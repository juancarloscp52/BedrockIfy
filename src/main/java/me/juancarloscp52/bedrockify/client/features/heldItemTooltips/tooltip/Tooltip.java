package me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public abstract class Tooltip {
    public String translationKey;
    public int primaryValue;

    public abstract MutableText getTooltipText();
}

