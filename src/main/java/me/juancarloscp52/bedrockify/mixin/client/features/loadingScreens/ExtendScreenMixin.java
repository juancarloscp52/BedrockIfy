package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public abstract class ExtendScreenMixin extends AbstractContainerEventHandler implements Renderable {
    @Shadow
    protected @Final Component title;
    @Shadow
    protected @Final Minecraft minecraft;
    @Shadow
    public int width;
    @Shadow
    public int height;

    @Shadow
    protected abstract <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(final T widget);

    @Accessor("renderables")
    protected abstract List<Renderable> bedrockify$access_getRenderables();

    @Inject(method = "extractRenderStateWithTooltipAndSubtitles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", shift = At.Shift.AFTER), cancellable = true)
    protected void bedrockify$screenRender_AfterRenderBG(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Empty body for overridable mixin
    }

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    protected void bedrockify$screenRender_AtHead(GuiGraphicsExtractor context, int mouseX, int mouseY, float alpha, CallbackInfo ci) {
        // Empty body for overridable mixin
    }
}
