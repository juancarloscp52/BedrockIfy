package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    public void setShaderColorOpacity(DrawContext context, float tickDelta, CallbackInfo ci){
        RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
    }

    @Inject(method = "renderHotbar", at = @At("RETURN"))
    public void resetShaderColorOpacity(DrawContext context, float tickDelta, CallbackInfo ci){
        RenderSystem.setShaderColor(1,1,1,1);
    }
    @Inject(method = "renderExperienceBar", at= @At("HEAD"))
    public void experienceBar(DrawContext context, int x, CallbackInfo ci){
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
    }

    @Inject(method = "render", at=@At("HEAD"))
    public void setShaderColorOpacity2(DrawContext context, float tickDelta, CallbackInfo ci){
        RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;isAmbient()Z"))
    public void setStatusEffectOpacity(DrawContext context, float tickDelta, CallbackInfo ci){
        RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
    }
    @ModifyConstant(method = "renderStatusEffectOverlay", constant = @Constant(floatValue = 1.f, ordinal = 0))
    public float setOpacityStatusEffectImage (float f){
        return f * BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
    }

}
