package me.juancarloscp52.bedrockify.client.features.bedrockShading;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 * @author Shaddatic
 */
public class BedrockBlockShading {

    public float getBlockShade (Direction direction){
        MinecraftClient client = MinecraftClient.getInstance();
        return switch (direction) {
            case UP -> 1.0f;
            case DOWN -> client.player.world.getRegistryKey() == World.NETHER ? 0.9f : 0.87f;
            case NORTH, SOUTH -> 0.95f;
            default -> 0.9f;
        };
    }
    public float getLiquidShade(Direction direction, boolean isLuminous){
        return switch (direction) {
            case UP -> 1.0f;
            case DOWN -> isLuminous ? 0.9f : 0.5f;
            default -> isLuminous ? 0.9f : 0.6f;
        };
    }
}
