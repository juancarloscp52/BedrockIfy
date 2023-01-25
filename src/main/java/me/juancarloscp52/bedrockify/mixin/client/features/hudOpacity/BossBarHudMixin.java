package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"),index = 3)
    public float applyAlphaBossBar(float alpha){
        return BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"),index = 4)
    public int applyAlphaTextBossBar(int color){
        int alpha = (int) Math.ceil(BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false)*255);
        return color | (alpha << 24);
    }

}
