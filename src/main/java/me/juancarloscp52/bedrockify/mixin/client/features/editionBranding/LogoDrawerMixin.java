package me.juancarloscp52.bedrockify.mixin.client.features.editionBranding;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LogoDrawer.class)
public class LogoDrawerMixin {

    @WrapOperation(method = "draw(Lnet/minecraft/client/gui/DrawContext;IFI)V", at= @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V",ordinal = 1))
    public void drawTexture(DrawContext instance, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, Operation<Void> original){
        if(!BedrockifyClient.getInstance().settings.hideEditionBranding){
            original.call(instance, texture, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }

}
