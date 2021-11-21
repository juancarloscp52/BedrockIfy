package me.juancarloscp52.bedrockify.mixin.common.features.useAnimations;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {


    @Shadow public abstract void setBobbingAnimationTime(int bobbingAnimationTime);

    @Inject(method = "decrement", at=@At("TAIL"))
    private void applyAnimationDecrement(CallbackInfo info){
        if(Bedrockify.getInstance().settings.isPickupAnimationsEnabled())
            this.setBobbingAnimationTime(5);
    }
    @Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at=@At("TAIL"))
    private void applyAnimationDamage1(CallbackInfo info){
        if(Bedrockify.getInstance().settings.isPickupAnimationsEnabled())
            this.setBobbingAnimationTime(5);
    }
}
