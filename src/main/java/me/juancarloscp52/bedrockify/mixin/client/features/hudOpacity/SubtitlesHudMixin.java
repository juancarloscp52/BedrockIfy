package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.hud.SubtitlesHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SubtitlesHud.class)
public class SubtitlesHudMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public void resetOpacity(MatrixStack matrices, CallbackInfo ci){
        RenderSystem.setShaderColor(1,1,1,1);
    }

}
