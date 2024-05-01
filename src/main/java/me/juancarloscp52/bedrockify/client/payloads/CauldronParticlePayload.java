package me.juancarloscp52.bedrockify.client.payloads;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class CauldronParticlePayload extends AbstractVelocityParticlePayload {
    private Identifier particleType;

    public static final PacketCodec<RegistryByteBuf, CauldronParticlePayload> CODEC = new PacketCodec<>() {
        @Override
        public CauldronParticlePayload decode(RegistryByteBuf buf) {
            CauldronParticlePayload result = new CauldronParticlePayload();
            result.particleType = buf.readIdentifier();
            OptionalCodec.decode(buf, result);
            return result;
        }

        @Override
        public void encode(RegistryByteBuf buf, CauldronParticlePayload value) {
            buf.writeIdentifier(value.particleType);
            OptionalCodec.encode(buf, value);
        }
    };

    @Override
    public Id<CauldronParticlePayload> getId() {
        return new Id<>(new Identifier(Bedrockify.MOD_ID, "cauldron_particles"));
    }

    public void setParticleType(Identifier particleType) {
        this.particleType = particleType;
    }

    public static final class CauldronParticleHandler implements ClientPlayNetworking.PlayPayloadHandler<CauldronParticlePayload> {
        @Override
        public void receive(CauldronParticlePayload payload, ClientPlayNetworking.Context context) {
            if (payload == null || context == null) {
                return;
            }
            final MinecraftClient client = context.client();
            try {
                var particle = Registries.PARTICLE_TYPE.get(payload.particleType);
                double x = payload.position.x;
                double y = payload.position.y;
                double z = payload.position.z;
                float vx = (float) payload.velocity.x;
                float vy = (float) payload.velocity.y;
                float vz = (float) payload.velocity.z;

                if (particle instanceof ParticleEffect generic) {
                    client.execute(() -> {
                        if (null != client.world) {
                            client.world.addParticle(generic, x, y, z, vx, vy, vz);
                        }
                    });
                } else if (particle.equals(ParticleTypes.ENTITY_EFFECT)) {
                    client.execute(() -> {
                        if (null != client.world) {
                            client.world.addParticle(EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, vx, vy, vz), x, y, z, vx, vy, vz);
                        }
                    });
                }
            } catch (Exception ignored) {
            }
        }
    }
}
