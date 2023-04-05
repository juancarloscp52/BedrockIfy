package me.juancarloscp52.bedrockify.client.features.hudOpacity;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class HudOpacity {

    private static final int maxInactiveTicks = 120; // 6 seconds.
    private static final int FADE_OUT_START_TICK = 100;
    private static final int FADE_IN_DURATION_TICK = 5;
    int inactiveTicks = 0;
    int previousSelectedSlot = -1;
    float currentOpacity = 1f;

    public float getHudOpacity(boolean isHotBarBackground){
        float max = BedrockifyClient.getInstance().settings.isTransparentHotBarEnabled() && isHotBarBackground? 0.6F:1.0F;
        return Math.min(currentOpacity, max);
    }

    public void resetTicks(){
        inactiveTicks=0;
    }

    public void tick(){
        if(inactiveTicks< maxInactiveTicks)
            inactiveTicks++;
        if(MinecraftClient.getInstance().player!= null){
            int selectedSlot = MinecraftClient.getInstance().player.getInventory().selectedSlot;
            if(selectedSlot != previousSelectedSlot){
                previousSelectedSlot=selectedSlot;
                resetTicks();
            }
        }

        float opacity = BedrockifyClient.getInstance().settings.hudOpacity/100f;
        float delta = 1f - opacity;
        if(inactiveTicks > FADE_OUT_START_TICK && currentOpacity > opacity){
            currentOpacity -= delta / (maxInactiveTicks - FADE_OUT_START_TICK);
            clampOpacity(opacity);
        }else if(inactiveTicks<=FADE_IN_DURATION_TICK && currentOpacity < 1f){
            currentOpacity += delta / FADE_IN_DURATION_TICK;
            clampOpacity(opacity);
        }
    }

    private void clampOpacity(float minimum) {
        currentOpacity = MathHelper.clamp(currentOpacity, minimum, 1f);
    }
}
