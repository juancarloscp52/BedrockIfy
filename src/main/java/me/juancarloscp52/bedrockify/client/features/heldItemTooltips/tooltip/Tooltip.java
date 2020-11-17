package me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip;

import net.minecraft.text.TranslatableText;

public abstract class Tooltip {
    public String translationKey;
    public int primaryValue;

    public abstract TranslatableText getTooltipText();
}

