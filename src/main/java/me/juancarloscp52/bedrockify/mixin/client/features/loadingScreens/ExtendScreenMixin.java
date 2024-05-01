package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ExtendScreenMixin {
    @Shadow
    protected @Final Text title;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V", shift = At.Shift.AFTER), cancellable = true)
    protected void bedrockify$screenRender_AfterRenderBG(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Empty body for overridable mixin
    }
}
