package me.juancarloscp52.bedrockify.mixin.client.features.reacharoundPlacement;


import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> {

    @Shadow public ClientPlayerEntity player;
    @Shadow public abstract boolean isInSingleplayer();

    public MinecraftClientMixin(String string) {
        super(string);
    }


    /**
     * Allows the player to use the reachAround placement feature if enabled.
     */
    @Redirect(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack onItemUse(ClientPlayerEntity player, Hand hand) {
        ItemStack itemStack = this.player.getStackInHand(hand);

        if (Bedrockify.getInstance().settings.isReacharoundEnabled() && (isInSingleplayer() || Bedrockify.getInstance().settings.isReacharoundMultiplayerEnabled()))
            BedrockifyClient.getInstance().reachAroundPlacement.checkReachAroundAndExecute(hand, itemStack);

        return itemStack;
    }
}
