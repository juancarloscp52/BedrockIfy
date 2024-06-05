package me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip;

import net.minecraft.text.MutableText;

public abstract class Tooltip {
    public int primaryValue;

    public abstract MutableText getTooltipText();

    /**
     * Overrides the original equals method to compare easier.
     * @param that Another {@link Tooltip} object.
     * @return <code>true</code> if they {@link Tooltip#getTooltipText()} are equal.
     */
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Tooltip tooltip)) {
            return false;
        }
        return tooltip.getTooltipText().equals(this.getTooltipText());
    }
}

