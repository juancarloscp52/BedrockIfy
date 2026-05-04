package me.juancarloscp52.bedrockify.client.features.heldItemTooltips.tooltip;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ContainerTooltip extends Tooltip {
    Component itemName;

    public ContainerTooltip(ItemStack itemStack){
        this.primaryValue = itemStack.getCount();
        this.itemName = itemStack.getOrDefault(DataComponents.CUSTOM_NAME, itemStack.getItemName());
    }

    @Override
    public MutableComponent getTooltipText() {
        MutableComponent tooltip = this.itemName.copy();
        tooltip.append(" x").append(String.valueOf(primaryValue));
        return tooltip;
    }
}
