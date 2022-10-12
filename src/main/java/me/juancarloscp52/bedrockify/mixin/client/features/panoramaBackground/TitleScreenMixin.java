package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
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
    public void updatePanorama(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        BedrockifyRotatingCubeMapRenderer.getInstance().update(backgroundRenderer, PANORAMA_OVERLAY, doBackgroundFade, backgroundFadeStart);
    }

    @Group(name = "overlayID")
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/class_2960;)V", ordinal = 0, remap = false),index = 1)
    private Identifier getOverlayDev(Identifier identifier) {
        BedrockifyRotatingCubeMapRenderer.getInstance().updateOverlayId(identifier);
        return identifier;
    }

    @Group(name = "overlayID")
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 0,remap = false),index = 1)
    private Identifier getOverlayProd(Identifier identifier) {
        BedrockifyRotatingCubeMapRenderer.getInstance().updateOverlayId(identifier);
        return identifier;
    }

}
