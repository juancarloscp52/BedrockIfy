package me.juancarloscp52.bedrockify.client.features.hudOpacity;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.MinecraftClient;

public class HudOpacity {

    private final int maxInactiveTicks = 120; // 6 seconds.
    int inactiveTicks = 0;
    int previousSelectedSlot = -1;
    boolean hasReachedMax = false;

    public float getHudOpacity(boolean isHotBarBackground){
        float opacity = BedrockifyClient.getInstance().settings.hudOpacity/100f;
        float max = BedrockifyClient.getInstance().settings.isTransparentHotBarEnabled() && isHotBarBackground? 0.6F:1.0F;

        if(opacity>=max-0.05)
            return max;

        float delta = max - opacity;
        if(inactiveTicks > 100){
            hasReachedMax=true;
            return max - (delta * ((inactiveTicks-100f)/20f));
        }else{
            if(inactiveTicks<=5 && hasReachedMax){
                return opacity + (delta * ((inactiveTicks)/5f));
            } else
                hasReachedMax=false;
            return max;
        }
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
    }

}
