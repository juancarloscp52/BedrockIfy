package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
    @Invoker("renderItemBar")
    void invokeRenderItemBar(ItemStack itemStack, int x, int y);

    @Invoker("renderItemCooldown")
    void invokeRenderItemCooldown(ItemStack itemStack, int x, int y);
}
