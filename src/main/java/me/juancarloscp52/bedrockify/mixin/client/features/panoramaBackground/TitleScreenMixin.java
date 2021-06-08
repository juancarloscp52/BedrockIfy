package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.block.Blocks;
import net.minecraft.block.IceBlock;
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
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;


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

    @Group(name = "overlayID")
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V", ordinal = 0, remap = false),index = 1)
    private Identifier getOverlayDev(Identifier identifier) {
        BedrockifyRotatingCubeMapRenderer.getInstance().updateOverlayId(identifier);
        return identifier;
    }

    @Group(name = "overlayID")
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/class_2960;)V", ordinal = 0,remap = false),index = 1)
    private Identifier getOverlayProd(Identifier identifier) {
        BedrockifyRotatingCubeMapRenderer.getInstance().updateOverlayId(identifier);
        return identifier;
    }

}
