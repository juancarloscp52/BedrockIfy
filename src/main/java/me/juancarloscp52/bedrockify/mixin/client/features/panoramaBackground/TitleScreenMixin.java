package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Shadow
    @Final
    private boolean doBackgroundFade;

    @Shadow
    private long backgroundFadeStart;

    @Shadow @Final private RotatingCubeMapRenderer backgroundRenderer;

    @Shadow @Final private static Identifier PANORAMA_OVERLAY;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V"))
    private void renderCubeMap(RotatingCubeMapRenderer renderer, float delta, float alpha) {
        float f = this.doBackgroundFade ? (float) (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        BedrockifyRotatingCubeMapRenderer.getInstance().update(backgroundRenderer, PANORAMA_OVERLAY, doBackgroundFade, backgroundFadeStart);
        BedrockifyRotatingCubeMapRenderer.getInstance().render(MathHelper.clamp(f, 0.0F, 1.0F), true);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V", ordinal = 0))
    private Identifier getOverlay(Identifier id) {
        BedrockifyRotatingCubeMapRenderer.getInstance().updateOverlayId(id);
        return id;
    }

}
