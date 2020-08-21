package me.juancarloscp52.bedrockify.client.mixin;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
/*
 * Updates the saving status for the saving overlay.
 */
public class MinecraftServerMixin {

    @Inject(method = "save", at= @At("HEAD"))
    private void startSaving(CallbackInfoReturnable info){
        BedrockifyClient.getInstance().overlay.savingOverlay.setSaving(true);
    }

    @Inject(method = "save", at= @At("RETURN"))
    private void stopSaving(CallbackInfoReturnable info){
        BedrockifyClient.getInstance().overlay.savingOverlay.setSaving(false);
    }
}
