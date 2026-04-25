package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity;

import me.juancarloscp52.bedrockify.client.features.hudOpacity.IGuiItemOpacity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin implements IGuiItemOpacity {
    @Unique
    private float opacity = 1f;

    @Override
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    @Override
    public float getOpacity() {
        return this.opacity;
    }

    @ModifyArg(method = "itemBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(Lcom/mojang/blaze3d/pipeline/RenderPipeline;IIIII)V"), index = 5)
    private int bedrockify$modifyItemBarOpacity(int original) {
        final float alpha = ARGB.alpha(original) / 255f;
        return ARGB.color(this.opacity * alpha, original);
    }

    @ModifyArg(method = "itemCount", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"), index = 4)
    private int bedrockify$modifyItemCountOpacity(int original) {
        final float alpha = ARGB.alpha(original) / 255f;
        return ARGB.color(this.opacity * alpha, original);
    }
}
