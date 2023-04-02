package me.juancarloscp52.bedrockify.mixin.client.features.editionBranding;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LogoDrawer.class)
public class LogoDrawerMixin {

    @Redirect(method = "draw(Lnet/minecraft/client/util/math/MatrixStack;IFI)V", at= @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/LogoDrawer;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIFFIIII)V"))
    public void drawTexture(MatrixStack matrixStack, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight){
        if(!BedrockifyClient.getInstance().settings.hideEditionBranding){
            LogoDrawer.drawTexture(matrixStack, x, y, u, v, width, height, textureWidth, textureHeight);
        }
    }

}
