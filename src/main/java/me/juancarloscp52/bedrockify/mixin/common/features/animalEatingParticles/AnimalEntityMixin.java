package me.juancarloscp52.bedrockify.mixin.common.features.animalEatingParticles;

import me.juancarloscp52.bedrockify.common.features.animalEatingParticles.EatingParticlesUtil;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public class AnimalEntityMixin{

    @Inject(method = "eat", at=@At("HEAD"))
    public void spawnEatingParticles(PlayerEntity player, Hand hand, ItemStack stack, CallbackInfo ci){
        EatingParticlesUtil.spawnItemParticles(player, stack, ((AnimalEntity)(Object)this));
    }



}
