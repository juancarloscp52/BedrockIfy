package me.juancarloscp52.bedrockify.mixin.client.features.biggerDraggingItem;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Shadow protected abstract void drawItem(MatrixStack matrices, ItemStack stack, int x, int y, String amountText);

    @Shadow protected int x;

    @Redirect(method = "render", at= @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"))
    private void drawBiggerItem(HandledScreen instance, MatrixStack matrices, ItemStack stack, int xPosition, int yPosition, String amountText){
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if(!settings.isBiggerIconsEnabled()){
            this.drawItem(matrices,stack, xPosition, yPosition, amountText);
            return;
        }
        matrices.push();
        float multiplier = 1.3f;
        matrices.scale(multiplier,multiplier,multiplier);
        RenderSystem.applyModelViewMatrix();
        this.drawItem(matrices, stack, MathHelper.ceil(xPosition/multiplier)-2, MathHelper.ceil(yPosition/multiplier)-2, amountText);
        matrices.pop();
        RenderSystem.applyModelViewMatrix();
    }

}
