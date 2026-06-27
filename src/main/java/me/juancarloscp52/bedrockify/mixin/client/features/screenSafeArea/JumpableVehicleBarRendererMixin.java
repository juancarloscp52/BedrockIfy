package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.contextualbar.JumpableVehicleBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(JumpableVehicleBar.class)
public abstract class JumpableVehicleBarRendererMixin {
    /**
     * Apply screen border offset to mount bars.
     */
    @ModifyArg(method = "extractBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIIIII)V"), index = 7)
    public int modifyTextureMountJumpBar(int y) {
        return y - BedrockifyClient.getInstance().settings.getScreenSafeArea();
    }

    /**
     * Apply screen border offset to mount bars.
     */
    @ModifyArg(method = "extractBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"), index = 3)
    public int modifyTextureMountJumpBar2(int y) {
        return y - BedrockifyClient.getInstance().settings.getScreenSafeArea();
    }
}
