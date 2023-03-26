package me.juancarloscp52.bedrockify.common.block.entity;

import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Allows to keep Dyes and Potions.
 */
public class WaterCauldronBlockEntity extends BlockEntity {
    public static final String KEY_FLUID_TINT = "tint_color";
    public static final String KEY_FLUID_ITEM = "item_id";
    public static final String KEY_POTION_TYPE = "potion_type";

    private static final int COLOR_WHEN_ERROR = 0xff000000;

    private int tintColor;
    private Identifier fluidId;
    private Identifier potionTypeId;

    public WaterCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(BedrockCauldronBlocks.WATER_CAULDRON_ENTITY, pos, state);
    }

    public int getTintColor() {
        return this.tintColor;
    }

    @Nullable
    public Identifier getFluidId() {
        return this.fluidId;
    }

    public Item getPotionType() {
        Item item = Registries.ITEM.get(this.potionTypeId);
        if (Objects.equals(Registries.ITEM.getId(item), Registries.ITEM.getDefaultId())) {
            item = Items.POTION;
        }
        return item;
    }

    public void setPotion(ItemStack potionItem) {
        this.potionTypeId = Registries.ITEM.getId(potionItem.getItem());
        final Potion potion = PotionUtil.getPotion(potionItem);
        this.fluidId = Registries.POTION.getId(potion);
        this.setTintColor(PotionUtil.getColor(potion));
    }

    public <T extends DyeItem> void setDyeItem(T item) {
        this.fluidId = Registries.ITEM.getId(item);
        this.setTintColor(item.getColor().getFireworkColor());
    }

    private void setTintColor(int tintColor) {
        this.tintColor = tintColor;
        this.markDirty();
        this.updateListeners();
    }

    /**
     * Defines the validity of the ID used for {@link WaterCauldronBlockEntity#fluidId}.
     */
    private void checkExactIds() {
        boolean valid = false;
        if (Registries.ITEM.get(this.getFluidId()) instanceof DyeItem dyeItem) {
            valid = true;
            this.setDyeItem(dyeItem);
        } else {
            final Potion potion = Registries.POTION.get(this.getFluidId());
            if (!Objects.equals(Registries.POTION.getId(potion), Registries.POTION.getDefaultId())) {
                valid = true;
                this.setTintColor(PotionUtil.getColor(potion));
            }
        }
        if (!valid) {
            this.setTintColor(COLOR_WHEN_ERROR);
        }
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
        this.fluidId = Identifier.tryParse(nbt.getString(KEY_FLUID_ITEM));
        this.potionTypeId = Identifier.tryParse(nbt.getString(KEY_POTION_TYPE));
        this.checkExactIds();
        this.updateListeners();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt(KEY_FLUID_TINT, this.tintColor);
        nbt.putString(KEY_FLUID_ITEM, (this.fluidId == null) ? "<NULL>" : this.fluidId.toString());
        nbt.putString(KEY_POTION_TYPE, (this.potionTypeId == null) ? "<NULL>" : this.potionTypeId.toString());

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
