package me.juancarloscp52.bedrockify.client.features.reacharoundPlacement;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.gui.DrawableHelper.fill;

public class ReachAroundPlacement {
    private final MinecraftClient client;

    public ReachAroundPlacement(MinecraftClient client) {
        this.client = client;
    }

    public void renderIndicator(MatrixStack matrixStack) {
        if (BedrockifyClient.getInstance().settings.isReacharoundIndicatorEnabled() && BedrockifyClient.getInstance().settings.isReacharoundEnabled() && (client.isInSingleplayer() || BedrockifyClient.getInstance().settings.isReacharoundMultiplayerEnabled()) && this.canReachAround() ) {
            fill(matrixStack, (client.getWindow().getScaledWidth() / 2) - 5, (client.getWindow().getScaledHeight() / 2) + 5, (client.getWindow().getScaledWidth() / 2) + 4, (client.getWindow().getScaledHeight() / 2) + 6, (100 << 24) + (255 << 8));
        }
    }

    public boolean canReachAround() {
        if (client.player == null || client.world == null || client.crosshairTarget == null)
            return false;
        final ClientPlayerEntity player = client.player;
        final BlockPos targetPos = getFacingSteppingBlockPos(player);
        return (player.isSneaking() || !BedrockifyClient.getInstance().settings.isReacharoundSneakingEnabled()) && player.getPitch() > BedrockifyClient.getInstance().settings.getReacharoundPitchAngle() && player.isOnGround() && client.crosshairTarget.getType().equals(HitResult.Type.MISS) && checkRelativeBlockPosition() && ((client.world.getBlockState(targetPos).getBlock() instanceof FluidBlock) || (client.world.getBlockState(targetPos).getBlock() instanceof AirBlock));
    }

    /**
     * Helper method that retrieve Reach-Around block position.
     *
     * @return The position of the block to be placed.
     */
    public static BlockPos getFacingSteppingBlockPos(@NotNull Entity player) {
        return player.getSteppingPos().offset(player.getHorizontalFacing());
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
