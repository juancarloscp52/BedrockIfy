package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity.detailArmorBar;

import com.mojang.blaze3d.systems.RenderSystem;
import com.redlimerl.detailab.render.InGameDrawer;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(InGameDrawer.class)
public class InGameDrawerMixin {

    @Inject(method = "drawTexturedQuad", at = @At(value = "HEAD"))
    private static void applyOpacity(Identifier identifier, Matrix4f matrices, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1, boolean mirror, CallbackInfo ci){
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        var currentColors = RenderSystem.getShaderColor();
        //Cap original transparency to one
        if(currentColors[3] > 1){
            currentColors[3] = 1.f;
        }
        RenderSystem.setShaderColor(currentColors[0],currentColors[1],currentColors[2],BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false)*(currentColors[3]));
    }

}
