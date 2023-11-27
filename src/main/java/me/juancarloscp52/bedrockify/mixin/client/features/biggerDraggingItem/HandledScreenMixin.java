package me.juancarloscp52.bedrockify.mixin.client.features.biggerDraggingItem;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {


    @Shadow protected int x;

    @Shadow protected abstract void drawItem(DrawContext context, ItemStack stack, int x, int y, String amountText);

    @WrapOperation(method = "render", at= @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawItem(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"))
    private void drawBiggerItem(HandledScreen instance, DrawContext context, ItemStack stack, int x, int y, String amountText, Operation<Void> original){
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if(!settings.isBiggerIconsEnabled()){
            this.drawItem(context, stack, x, y, amountText);
            return;
        }
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        float multiplier = 1.3f;
        matrices.scale(multiplier,multiplier,multiplier);
        RenderSystem.applyModelViewMatrix();
        original.call(instance, context, stack, MathHelper.ceil(x/multiplier)-2, MathHelper.ceil(y/multiplier)-2, amountText);
        matrices.pop();
        RenderSystem.applyModelViewMatrix();
    }

}
