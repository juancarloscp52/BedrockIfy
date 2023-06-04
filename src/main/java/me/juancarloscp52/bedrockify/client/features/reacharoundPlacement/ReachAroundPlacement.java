package me.juancarloscp52.bedrockify.client.features.reacharoundPlacement;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

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
        if (client.player == null || client.world == null || client.crosshairTarget == null)
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
        if (!player.isOnGround() || !isSolidBlock(client.world.getBlockState(player.getSteppingPos()))) {
            return false;
        }
        // There is already a block at the ReachAround target position.
        if (isSolidBlock(client.world.getBlockState(targetPos))) {
            return false;
        }

        return player.getPitch() > BedrockifyClient.getInstance().settings.getReacharoundPitchAngle() && checkRelativeBlockPosition();
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
     * Helper method that checks the BlockState is not AIR and FLUIDS.
     *
     * @param blockState The target blockState.
     * @return <code>true</code> if the block is not AIR and FLUIDS.
     */
    private static boolean isSolidBlock(@NotNull BlockState blockState) {
        return !blockState.isAir() && !(blockState.getBlock() instanceof FluidBlock);
    }

    private boolean checkRelativeBlockPosition() {
        if (client.player == null)
            return false;
        return checkRelativeBlockPosition((client.player.getPos().getX() - client.player.getBlockPos().getX()), client.player.getHorizontalFacing().getUnitVector().x()) || checkRelativeBlockPosition((client.player.getPos().getZ() - client.player.getBlockPos().getZ()), client.player.getHorizontalFacing().getUnitVector().z());
    }

    private boolean checkRelativeBlockPosition(double pos, float direction) {
        double distance = BedrockifyClient.getInstance().settings.getReacharoundBlockDistance();
        if (direction > 0) {
            return 1-pos < distance;
        } else if (direction < 0) {
            return pos < distance;
        }
        return false;
    }
}
