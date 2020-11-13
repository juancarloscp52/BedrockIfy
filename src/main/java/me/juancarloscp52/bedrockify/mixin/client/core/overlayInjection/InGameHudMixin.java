package me.juancarloscp52.bedrockify.mixin.client.core.overlayInjection;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    /**
     * Renders the Bedrockify overlay.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isDemo()Z", ordinal = 0))
    private void renderBedrockIfy(MatrixStack matrixStack, float f, CallbackInfo info) {
        BedrockifyClient.getInstance().overlay.renderOverlay(matrixStack);
    }
}
