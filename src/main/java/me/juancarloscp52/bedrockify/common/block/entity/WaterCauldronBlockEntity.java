package me.juancarloscp52.bedrockify.common.block.entity;

import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * Allows to keep Dyes.
 */
public class WaterCauldronBlockEntity extends BlockEntity {
    public static final String KEY_FLUID_TINT = "tint_color";
    public static final String KEY_FLUID_ITEM = "item_id";

    private int tintColor;
    private Identifier fluidItemId;

    public WaterCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(BedrockCauldronBlocks.WATER_CAULDRON_ENTITY, pos, state);
    }

    public int getTintColor() {
        return this.tintColor;
    }

    @Nullable
    public Identifier getFluidId() {
        return this.fluidItemId;
    }

    public <T extends DyeItem> void setDyeItem(T item) {
        this.fluidItemId = Registries.ITEM.getId(item);
        this.setTintColor(item.getColor().getFireworkColor());
    }

    private void setTintColor(int tintColor) {
        this.tintColor = tintColor;
        this.markDirty();
        this.updateListeners();
    }

    private void updateListeners() {
        if (this.world != null) {
            this.world.updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.tintColor = nbt.getInt(KEY_FLUID_TINT);
        this.fluidItemId = Identifier.tryParse(nbt.getString(KEY_FLUID_ITEM));
        this.updateListeners();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt(KEY_FLUID_TINT, this.tintColor);
        nbt.putString(KEY_FLUID_ITEM, (this.fluidItemId == null) ? "<NULL>" : this.fluidItemId.toString());

        super.writeNbt(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
