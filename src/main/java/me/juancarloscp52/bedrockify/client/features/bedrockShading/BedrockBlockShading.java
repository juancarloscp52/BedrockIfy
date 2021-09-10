package me.juancarloscp52.bedrockify.client.features.bedrockShading;

import net.minecraft.util.math.Direction;

/**
 * @author Shaddatic
 */
public class BedrockBlockShading {

    public float getBlockShade (Direction direction){
        return switch (direction) {
            case UP -> 1.0f;
            case DOWN -> 0.87f;
            case NORTH, SOUTH -> 0.95f;
            default -> 0.9f;
        };
    }
    public float getLiquidShade(Direction direction, boolean isLava){
        if(direction == Direction.UP)
            return 1.0f;
        if(isLava)
            return 0.9f;
        return 0.6f;
    }

}
