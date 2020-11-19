package me.juancarloscp52.bedrockify.mixin.common.features.useAnimations;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract void setCooldown(int cooldown);

    @Inject(method = "decrement", at=@At("TAIL"))
    private void applyAnimationDecrement(CallbackInfo info){
        this.setCooldown(5);
    }
    @Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at=@At("TAIL"))
    private void applyAnimationDamage1(CallbackInfo info){
        this.setCooldown(5);
    }
}
