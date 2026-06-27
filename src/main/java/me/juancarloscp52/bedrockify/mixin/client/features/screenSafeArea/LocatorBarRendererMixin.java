package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.Window;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.contextualbar.LocatorBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LocatorBar.class)
public class LocatorBarRendererMixin {

    @ModifyArg(method = "extractBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"), index = 3)
    public int modifyTextureLocatorBar(int y) {
        return y - BedrockifyClient.getInstance().settings.getScreenSafeArea();
    }

    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/LocatorBar;top(Lcom/mojang/blaze3d/platform/Window;)I"))
    private int modfiyLocator(LocatorBar instance, Window window, Operation<Integer> original) {
        return original.call(instance, window) - BedrockifyClient.getInstance().settings.getScreenSafeArea();
    }
}
