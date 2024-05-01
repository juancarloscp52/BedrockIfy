package me.juancarloscp52.bedrockify.client.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public abstract class AbstractVelocityParticlePayload implements CustomPayload {
    protected Vec3d position;
    protected Vec3d velocity;

    public void setPosition(Vec3d position) {
        this.position = position;
    }

    public void setVelocity(Vec3d velocity) {
        this.velocity = velocity;
    }

    public static final class OptionalCodec {
        public static void encode(RegistryByteBuf buf, AbstractVelocityParticlePayload target) {
            buf.writeVec3d(target.position);
            buf.writeVec3d(target.velocity);
        }

        public static void decode(RegistryByteBuf buf, AbstractVelocityParticlePayload target) {
            target.position = buf.readVec3d();
            target.velocity = buf.readVec3d();
        }
    }
}
