package me.juancarloscp52.bedrockify.client.features.worldColorNoise;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.world.gen.ChunkRandom;

public class WorldColorNoiseSampler {
    SimplexNoiseSampler noiseSampler;
    public WorldColorNoiseSampler(){
        noiseSampler = new SimplexNoiseSampler(new ChunkRandom(98756L));
    }

    public double getSample (double x, double z, float scale){
        return noiseSampler.sample(x/scale,z/scale);
    }

    public int applyNoise(BlockPos pos, int previousColor, float scale, float intensity){
        double noiseValue = BedrockifyClient.getInstance().worldColorNoiseSampler.getSample(pos.getX(),pos.getZ(),scale);
        if(noiseValue>0)
            noiseValue=noiseValue/3;
        return blend(previousColor,(float)noiseValue *intensity);
    }

    private float[] getAlphaColorArray(final int hex) {
        return new float[]{((hex >> 24) & 255) / 255f, ((hex >> 16) & 255) / 255f, ((hex >> 8) & 255) / 255f, ((hex) & 255) / 255f};
    }

    private int toIntColor(final float[] alphaColorArray) {
        return (((int) Math.floor(alphaColorArray[0] * 255) & 255) << 24) + (((int) Math.floor(alphaColorArray[1] * 255) & 255) << 16) + (((int) Math.floor(alphaColorArray[2] * 255) & 255) << 8) + ((int) Math.floor(alphaColorArray[3] * 255) & 255);
    }

    private int blend(final int color1, final float ratio) {
        final float[] rgb1 = getAlphaColorArray(color1);
        final float[] rgb2 = getAlphaColorArray(0);
        final float negative = 1-ratio;
        return toIntColor(new float[]{MathHelper.clamp(rgb2[0] * ratio + rgb1[0] * negative,0,1), MathHelper.clamp(rgb2[1] * ratio + rgb1[1] * negative,0,1), MathHelper.clamp(rgb2[2] * ratio + rgb1[2] * negative,0,1), MathHelper.clamp(rgb2[3] * ratio + rgb1[3] * negative,0,1)});
    }

}
