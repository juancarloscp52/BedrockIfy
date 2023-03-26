package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    public void setShaderColorOpacity(float tickDelta, MatrixStack matrices, CallbackInfo ci){
        RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
    }
    @Inject(method = "renderHotbar", at = @At("RETURN"))
    public void resetShaderColorOpacity(float tickDelta, MatrixStack matrices, CallbackInfo ci){
        RenderSystem.setShaderColor(1,1,1,1);
    }
    @Inject(method = "renderExperienceBar", at= @At("HEAD"))
    public void experienceBar(MatrixStack matrices, int x, CallbackInfo ci){
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
    }

    @Inject(method = "render", at=@At(value = "INVOKE",target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V",ordinal = 1))
    public void setShaderColorOpacity2(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;isAmbient()Z"))
    public void setStatusEffectOpacity(MatrixStack matrices, CallbackInfo ci){
        RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
    }
    @ModifyVariable(method = "renderStatusEffectOverlay", at = @At(value = "STORE"),ordinal = 0)
    public float setOpacityStatusEffectImage (float f){
        return f * BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
    }

}
