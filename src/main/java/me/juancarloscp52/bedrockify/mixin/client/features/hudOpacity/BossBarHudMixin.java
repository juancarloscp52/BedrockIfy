package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {

    @Inject(method = "render", at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"))
    public void applyAlphaBossBar (MatrixStack matrices, CallbackInfo ci){
        RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"),index = 4)
    public int applyAlphaTextBossBar(int color){
        int alpha = (int) Math.ceil(BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false)*255);
        return color | (alpha << 24);
    }

}
