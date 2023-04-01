package me.juancarloscp52.bedrockify.mixin.common.features.animalEatingParticles;

import me.juancarloscp52.bedrockify.common.features.animalEatingParticles.EatingParticlesUtil;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfEntity.class)
public class WolfEntityMixin {

    @Inject(method = "interactMob",at=@At(value = "INVOKE",target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        EatingParticlesUtil.spawnItemParticles(player,player.getStackInHand(hand),((AnimalEntity)(Object)this));
    }
    @Inject(method = "interactMob",at=@At(value = "INVOKE",target = "Lnet/minecraft/entity/passive/WolfEntity;heal(F)V"))
    public void interactMobOnHeal(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        EatingParticlesUtil.spawnItemParticles(player,player.getStackInHand(hand),((AnimalEntity)(Object)this));
    }
}
