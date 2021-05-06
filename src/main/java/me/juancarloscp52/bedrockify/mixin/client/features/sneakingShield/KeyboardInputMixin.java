package me.juancarloscp52.bedrockify.mixin.client.features.sneakingShield;

import me.juancarloscp52.bedrockify.client.features.sneakingShield.SneakingShield;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick",at=@At("TAIL"))
    private void checkShield(boolean slowDown, CallbackInfo ci){
        SneakingShield.tryActivation(this.sneaking);
    }

}
