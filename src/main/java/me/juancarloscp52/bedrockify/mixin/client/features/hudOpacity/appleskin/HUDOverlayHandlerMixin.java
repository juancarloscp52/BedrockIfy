package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity.appleskin;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import squeek.appleskin.client.HUDOverlayHandler;

@Pseudo
@Mixin(HUDOverlayHandler.class)
public class HUDOverlayHandlerMixin {

    @ModifyArg(method = "enableAlpha", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"),index = 3)
    public float clampEnableAlpha(float alpha){
        return BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false)*alpha;
    }

    @ModifyArg(method = "disableAlpha", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"),index = 3)
    public float clampDisableAlpha(float alpha){
        return BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
    }

    @ModifyArg(method = "drawHungerOverlay(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/client/MinecraftClient;IIFZI)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"),index = 3)
    public float clampAlphaHungerOverlay(float alpha){
        return BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false)*alpha;
    }
    @ModifyArg(method = "drawHealthOverlay(Lnet/minecraft/client/gui/DrawContext;FFLnet/minecraft/client/MinecraftClient;IIFI)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"),index = 3)
    public float clampAlphaHealthOverlay(float alpha){
        return BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false)*alpha;
    }

}