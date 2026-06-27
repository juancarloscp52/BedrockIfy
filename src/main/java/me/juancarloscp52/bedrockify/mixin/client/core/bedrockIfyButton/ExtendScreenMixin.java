package me.juancarloscp52.bedrockify.mixin.client.core.bedrockIfyButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ExtendScreenMixin {
    @Shadow
    protected @Final Minecraft minecraft;
    @Shadow
    public int width;
    @Shadow
    public int height;

    @Shadow
    protected abstract void rebuildWidgets();

    @Shadow
    protected abstract <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(final T widget);

    @Inject(method = "added", at = @At("HEAD"))
    protected void bedrockify$injectAdded_AtHead(CallbackInfo ci) {
        // Empty body for overridable method.
    }
}
