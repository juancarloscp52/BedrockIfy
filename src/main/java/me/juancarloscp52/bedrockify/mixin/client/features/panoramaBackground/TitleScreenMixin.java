package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


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

    @Inject(method = "render", at=@At(value = "INVOKE",target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V"))
    public void updatePanorama(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        BedrockifyRotatingCubeMapRenderer.getInstance().update(backgroundRenderer, PANORAMA_OVERLAY, doBackgroundFade, backgroundFadeStart);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIFFIIII)V"),index = 0)
    private Identifier getOverlayDev(Identifier texture) {
        BedrockifyRotatingCubeMapRenderer.getInstance().updateOverlayId(texture);
        return texture;
    }

}
