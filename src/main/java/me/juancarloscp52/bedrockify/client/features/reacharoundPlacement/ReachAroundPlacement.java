package me.juancarloscp52.bedrockify.client.features.reacharoundPlacement;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;

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

    private boolean canReachAround() {
        if (client.player == null || client.world == null || client.crosshairTarget == null)
            return false;
        return (client.player.isSneaking() || !BedrockifyClient.getInstance().settings.isReacharoundSneakingEnabled()) && client.player.getPitch() > BedrockifyClient.getInstance().settings.getReacharoundPitchAngle() && (!(client.world.getBlockState(client.player.getBlockPos().down()).isAir() || client.world.getBlockState(client.player.getBlockPos().down()).getBlock() instanceof FluidBlock) || isNonFullBlock()) && client.crosshairTarget.getType().equals(HitResult.Type.MISS) && checkRelativeBlockPosition() && ((client.world.getBlockState(client.player.getBlockPos().down().offset(client.player.getHorizontalFacing())).getBlock() instanceof FluidBlock) || (client.world.getBlockState(client.player.getBlockPos().down().offset(client.player.getHorizontalFacing())).getBlock() instanceof AirBlock));
    }

    private boolean isNonFullBlock(){
        if(client.world == null || client.player == null)
            return false;
        Block playerPosBlock = client.world.getBlockState(client.player.getBlockPos()).getBlock();
        return playerPosBlock instanceof SlabBlock || playerPosBlock instanceof  StairsBlock || playerPosBlock instanceof ChainBlock || playerPosBlock instanceof  EndRodBlock || playerPosBlock instanceof  BedBlock || playerPosBlock instanceof  SkullBlock || playerPosBlock instanceof  StonecutterBlock || playerPosBlock instanceof AbstractChestBlock;
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

    public void checkReachAroundAndExecute(Hand hand, ItemStack itemStack) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.interactionManager == null)
            return;
        int count = itemStack.getCount();
        Vector3f facing = player.getHorizontalFacing().getUnitVector();
        if (canReachAround()) {
            BlockHitResult blockHitResult;
            if(isNonFullBlock()){
                blockHitResult = new BlockHitResult(player.getPos().add(facing.x(), facing.y()-1, facing.z()), Direction.fromVector((int) -facing.x(), 0, (int) -facing.z()), player.getBlockPos().offset(player.getHorizontalFacing()), false);
            }else{
                blockHitResult = new BlockHitResult(player.getPos().add(facing.x(), facing.y(), facing.z()), Direction.fromVector((int) -facing.x(), 0, (int) -facing.z()), player.getBlockPos().down().offset(player.getHorizontalFacing()), false);
            }
            ActionResult result = client.interactionManager.interactBlock(player, hand, blockHitResult);
            if (result.isAccepted()) {
                if (result.shouldSwingHand()) {
                    player.swingHand(hand);
                    if (!itemStack.isEmpty() && (itemStack.getCount() != count || client.interactionManager.hasCreativeInventory())) {
                        client.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                    }
                }
            }
        }
    }

}
