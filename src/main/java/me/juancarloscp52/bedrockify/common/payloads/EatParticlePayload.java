package me.juancarloscp52.bedrockify.common.payloads;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;

import java.util.Objects;

public final class EatParticlePayload extends AbstractVelocityParticlePayload {
    private ItemStack itemStack;

    public static final PacketCodec<RegistryByteBuf, EatParticlePayload> CODEC = new PacketCodec<>() {
        @Override
        public EatParticlePayload decode(RegistryByteBuf buf) {
            EatParticlePayload result = new EatParticlePayload();
            result.itemStack = ItemStack.OPTIONAL_PACKET_CODEC.decode(buf);
            OptionalCodec.decode(buf, result);
            return result;
        }

        @Override
        public void encode(RegistryByteBuf buf, EatParticlePayload value) {
            ItemStack.OPTIONAL_PACKET_CODEC.encode(buf, value.itemStack);
            OptionalCodec.encode(buf, value);
        }
    };

    @Override
    public Id<EatParticlePayload> getId() {
        return new Id<>(new Identifier(Bedrockify.MOD_ID, "eat-particles"));
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack.copy();
    }

    public static final class EatParticleHandler implements ClientPlayNetworking.PlayPayloadHandler<EatParticlePayload> {
        @Override
        public void receive(EatParticlePayload payload, ClientPlayNetworking.Context context) {
            if (payload == null || context == null) {
                return;
            }
            final MinecraftClient client = context.client();
            try {
                ItemStack stack = Objects.requireNonNull(payload.itemStack);
                double x = payload.position.x;
                double y = payload.position.y;
                double z = payload.position.z;
                double velx = payload.velocity.x;
                double vely = payload.velocity.y;
                double velz = payload.velocity.z;

                client.execute(() -> {
                    if (null != client.world)
                        client.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), x, y, z, velx, vely, velz);
                });
            } catch (Exception ignored) {
            }
        }
    }
}
