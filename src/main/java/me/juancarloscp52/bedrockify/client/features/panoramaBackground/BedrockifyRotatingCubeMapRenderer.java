package me.juancarloscp52.bedrockify.client.features.panoramaBackground;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground.RotatingCubeMapRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class BedrockifyRotatingCubeMapRenderer {

    private CubeMapRenderer cubeMap = TitleScreen.PANORAMA_CUBE_MAP;
    private static BedrockifyRotatingCubeMapRenderer INSTANCE;
    private float time= 0;
    private static final Identifier PANORAMA_OVERLAY = new Identifier("textures/gui/title/background/panorama_overlay.png");

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

    public void render(DrawContext drawContext){
        render(drawContext,1, false);
    }

    public void render(DrawContext drawContext, float alpha, boolean titleScreen){
        this.cubeMap.draw(MinecraftClient.getInstance(), MathHelper.sin(time*0.001F)*5.0F + 25.0F,-this.time*0.1F,alpha);
        if(!titleScreen){
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.doBackgroundFade ? (float)MathHelper.ceil(MathHelper.clamp(alpha, 0.0F, 1.0F)) : 1.0F);
            Window window = MinecraftClient.getInstance().getWindow();
            drawContext.drawTexture(overlay, 0, 0, window.getScaledWidth(), window.getHeight(), 0.0F, 0.0F, 16, 128, 16, 128);

            //Render panorama overlay
            RenderSystem.enableBlend();
            float f = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0f : 1.0f;
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.doBackgroundFade ? (float)MathHelper.ceil(MathHelper.clamp(f, 0.0f, 1.0f)) : 1.0f);
            drawContext.drawTexture(PANORAMA_OVERLAY, 0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight(), 0.0f, 0.0f, 16, 128, 16, 128);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
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
