package me.juancarloscp52.bedrockify.client.features.panoramaBackground;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground.RotatingCubeMapRendererAccessor;
import me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground.ScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.*;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BedrockifyRotatingCubeMapRenderer {

    private CubeMapRenderer cubeMap = TitleScreen.PANORAMA_CUBE_MAP;
    private static BedrockifyRotatingCubeMapRenderer INSTANCE;
    private float time= 0;
    private boolean doBackgroundFade;
    private long backgroundFadeStart;
    private Identifier overlay = new Identifier("textures/gui/title/background/panorama_overlay.png");

    private BedrockifyRotatingCubeMapRenderer(){}

    public static BedrockifyRotatingCubeMapRenderer getInstance(){
        if(INSTANCE==null)
            INSTANCE=new BedrockifyRotatingCubeMapRenderer();
        return INSTANCE;
    }

    public void addPanoramaTime(float delta){
        this.time += delta;
    }

    public void render(){
        render(1, false);
    }

    public void render(float alpha, boolean titleScreen){
        this.cubeMap.draw(MinecraftClient.getInstance(), MathHelper.sin(time*0.001F)*5.0F + 25.0F,-this.time*0.1F,alpha);
        if(!titleScreen){
            RenderSystem.setShaderTexture(0,overlay);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.doBackgroundFade ? (float)MathHelper.ceil(MathHelper.clamp(alpha, 0.0F, 1.0F)) : 1.0F);
            Window window = MinecraftClient.getInstance().getWindow();
            DrawableHelper.drawTexture(new MatrixStack(), 0, 0, window.getScaledWidth(), window.getScaledHeight(), 0.0F, 0.0F, 16, 128, 16, 128);
        }
    }

    public void update(RotatingCubeMapRenderer renderer, Identifier panoramaOverlay, boolean doBackgroundFade, long backgroundFadeStart) {
        this.cubeMap = ((RotatingCubeMapRendererAccessor) renderer).getCubeMap();
        this.overlay = panoramaOverlay;
        this.doBackgroundFade = doBackgroundFade;
        this.backgroundFadeStart = backgroundFadeStart;
    }
    public void updateOverlayId(Identifier panoramaOverlay){
        this.overlay = panoramaOverlay;
    }

}
