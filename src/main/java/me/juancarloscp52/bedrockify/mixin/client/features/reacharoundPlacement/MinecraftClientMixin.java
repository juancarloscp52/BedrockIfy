package me.juancarloscp52.bedrockify.mixin.client.features.reacharoundPlacement;


import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.reacharoundPlacement.ReachAroundPlacement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> {
    @Shadow
    public ClientPlayerEntity player;
    @Shadow
    @Nullable
    public HitResult crosshairTarget;
    @Shadow
    public abstract boolean isInSingleplayer();

    public MinecraftClientMixin(String string) {
        super(string);
    }


    /**
     * Allows the player to use the reachAround placement feature if enabled.
     */
    @Inject(method = "doItemUse", at = @At("HEAD"))
    private void bedrockify$modifyCrosshairTarget(CallbackInfo ci) {
        if (this.player == null) {
            return;
        }
        if (!BedrockifyClient.getInstance().settings.isReacharoundEnabled() || (!isInSingleplayer() && !BedrockifyClient.getInstance().settings.isReacharoundMultiplayerEnabled())) {
            return;
        }

        if (BedrockifyClient.getInstance().reachAroundPlacement.canReachAround()) {
            final ClientPlayerEntity player = this.player;
            final BlockPos targetPos = ReachAroundPlacement.getFacingSteppingBlockPos(player);
            this.crosshairTarget = new BlockHitResult(new Vec3d(targetPos.getX(), player.getY(), targetPos.getZ()), player.getHorizontalFacing(), targetPos, false);
        }
    }
}
