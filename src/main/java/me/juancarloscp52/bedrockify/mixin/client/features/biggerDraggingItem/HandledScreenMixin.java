package me.juancarloscp52.bedrockify.mixin.client.features.biggerDraggingItem;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.BedrockifySettings;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Shadow protected abstract void drawItem(ItemStack stack, int xPosition, int yPosition, String amountText);

    @Redirect(method = "render", at= @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawItem(Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"))
    private void drawBiggerItem(HandledScreen handledScreen, ItemStack stack, int xPosition, int yPosition, String amountText){
        BedrockifySettings settings = Bedrockify.getInstance().settings;
        if(!settings.isBiggerIconsEnabled()){
            this.drawItem(stack, xPosition, yPosition, amountText);
            return;
        }
        RenderSystem.pushMatrix();
        float multiplier = 1.25f;
        RenderSystem.scalef(multiplier,multiplier,1);
        this.drawItem(stack, MathHelper.ceil(xPosition/multiplier)-2, MathHelper.ceil(yPosition/multiplier)-2, amountText);
        RenderSystem.popMatrix();
    }

}
