package me.juancarloscp52.bedrockify.client.features.panoramaBackground;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BedrockifyRotatingCubeMapRenderer {

    private final CubeMapRenderer cubeMap = new CubeMapRenderer(new Identifier("textures/gui/title/background/panorama"));
    private static BedrockifyRotatingCubeMapRenderer INSTANCE;
    private float time= 0;

    private BedrockifyRotatingCubeMapRenderer(){}

    public static BedrockifyRotatingCubeMapRenderer getInstance(){
        if(INSTANCE==null)
            INSTANCE=new BedrockifyRotatingCubeMapRenderer();
        return INSTANCE;
    }

    public void addPanoramaTime(){
        time+= BedrockifyClient.getInstance().deltaTime * 0.000000008f;
    }

    public void render(){
        render(1);
    }

    public void render(float alpha){
        this.cubeMap.draw(MinecraftClient.getInstance(), MathHelper.sin(time*0.001F)*5.0F + 25.0F,-this.time*0.1F,alpha);
    }

}
