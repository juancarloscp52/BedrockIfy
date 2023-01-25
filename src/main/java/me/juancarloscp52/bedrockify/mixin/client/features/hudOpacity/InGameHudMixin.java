package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"),index = 3)
    public float setOpacity (float alpha){
        return BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
    }

    @Inject(method = "renderExperienceBar", at= @At("HEAD"))
    public void experienceBar(MatrixStack matrices, int x, CallbackInfo ci){
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V",ordinal = 2),index = 3)
    public float setOpacity2 (float alpha){
        return BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
    }


    @ModifyArg(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V",ordinal = 0),index = 3)
    public float setOpacityStatusEffect (float alpha){
        return BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
    }
    @ModifyVariable(method = "renderStatusEffectOverlay", at = @At(value = "STORE"),ordinal = 0)
    public float setOpacityStatusEffectImage (float f){
        return f * BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false);
    }

}
