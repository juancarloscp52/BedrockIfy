package me.juancarloscp52.bedrockify.common.features.animalEatingParticles;

import me.juancarloscp52.bedrockify.client.payloads.EatParticlePayload;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class EatingParticlesUtil {

    public static void spawnItemParticles(PlayerEntity player, ItemStack stack, AnimalEntity entity) {
        if (player.getWorld().isClient)
            return;
        int count = 16;
        for (int i = 0; i < count; ++i) {
            final EatParticlePayload particlePayload = new EatParticlePayload();
            Vec3d vec3d = new Vec3d(((double)entity.getRandom().nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            vec3d = vec3d.rotateX(-entity.getPitch() * ((float)Math.PI / 180));
            vec3d = vec3d.rotateY(-entity.getYaw() * ((float)Math.PI / 180));
            double d = (double)(-entity.getRandom().nextFloat()) * 0.6 - 0.3;
            Vec3d vec3d2 = new Vec3d(((double)entity.getRandom().nextFloat()- 0.5) * 0.3, d, 0.6);
            vec3d2 = vec3d2.rotateX(-entity.getPitch() * ((float)Math.PI / 180));
            vec3d2 = vec3d2.rotateY(-entity.getHeadYaw() * ((float)Math.PI / 180));
            vec3d2 = vec3d2.add(entity.getX(), entity.getEyeY(), entity.getZ());
            particlePayload.setPosition(vec3d2);
            particlePayload.setVelocity(vec3d);
            particlePayload.setItemStack(stack);
            PlayerLookup.world((ServerWorld) player.getWorld()).forEach(serverPlayerEntity ->
                    ServerPlayNetworking.send(serverPlayerEntity, particlePayload));
        }
    }

}
