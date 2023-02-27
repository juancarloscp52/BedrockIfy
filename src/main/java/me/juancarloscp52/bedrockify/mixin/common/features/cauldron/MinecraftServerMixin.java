package me.juancarloscp52.bedrockify.mixin.common.features.cauldron;

import me.juancarloscp52.bedrockify.common.block.cauldron.BedrockCauldronBehavior;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    /**
     * Lazy initialization of Bedrock's cauldron behavior for server.
     */
    @Inject(method = "loadWorld", at = @At("HEAD"))
    private void bedrockify$registerCauldronBehaviorForServer(CallbackInfo ci) {
        BedrockCauldronBehavior.registerBehavior();
    }
}
