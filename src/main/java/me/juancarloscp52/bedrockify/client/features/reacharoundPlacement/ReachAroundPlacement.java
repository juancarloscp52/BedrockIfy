package me.juancarloscp52.bedrockify.client.features.reacharoundPlacement;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ReachAroundPlacement {
    private final MinecraftClient client;

    public ReachAroundPlacement(MinecraftClient client) {
        this.client = client;
    }

    public void renderIndicator(DrawContext drawContext) {
        if (BedrockifyClient.getInstance().settings.isReacharoundIndicatorEnabled() && BedrockifyClient.getInstance().settings.isReacharoundEnabled() && (client.isInSingleplayer() || BedrockifyClient.getInstance().settings.isReacharoundMultiplayerEnabled()) && this.canReachAround() ) {
            drawContext.fill((client.getWindow().getScaledWidth() / 2) - 5, (client.getWindow().getScaledHeight() / 2) + 5, (client.getWindow().getScaledWidth() / 2) + 4, (client.getWindow().getScaledHeight() / 2) + 6, (100 << 24) + (255 << 8));
        }
    }

    public boolean canReachAround() {
        if (client.player == null || client.world == null || client.crosshairTarget == null || client.interactionManager == null)
            return false;

        // crosshairTarget must be MISS.
        if (!client.crosshairTarget.getType().equals(HitResult.Type.MISS)) {
            return false;
        }

        final ClientPlayerEntity player = client.player;
        final BlockPos targetPos = getFacingSteppingBlockPos(player);

        // Not sneaking and must sneak in settings.
        if (!player.isSneaking() && BedrockifyClient.getInstance().settings.isReacharoundSneakingEnabled()) {
            return false;
        }
        // Player may be flying, climbing the ladder or vines, or on the Fluid with sneaking.
        if (!player.isOnGround()) {
            return false;
        }
        // There is a non-replaceable block at the ReachAround target position.
        if (!client.world.getBlockState(targetPos).isReplaceable()) {
            return false;
        }

        return getRaycastIntersection(player).isPresent();
    }

    /**
     * Helper method that retrieves Reach-Around block position.
     *
     * @return The position of the block to be placed.
     */
    public static BlockPos getFacingSteppingBlockPos(@NotNull Entity player) {
        return player.getSteppingPos().offset(player.getHorizontalFacing());
    }

    /**
     * Draws a vector from the player's eyes to the end of the reach distance, in the direction the player is facing. We can use this to check if the block is valid for placement.
     * @return The position of the intersection between the raycast and the surface of the target block.
     * @author axialeaa
     */
    private Optional<Vec3d> getRaycastIntersection(@NotNull Entity player) {
        if (client.interactionManager == null) {
            return Optional.empty(); // Redundant in 1.20.5+
        }

        Vec3d rayStartPos = player.getEyePos();
        Vec3d rayEndPos = player.getRotationVec(1.0F).multiply(client.interactionManager.getReachDistance()).add(rayStartPos);
                                                                    // player.getBlockInteractionRange() in 1.20.5+
        return new Box(getFacingSteppingBlockPos(player)).raycast(rayStartPos, rayEndPos);
    }
}
