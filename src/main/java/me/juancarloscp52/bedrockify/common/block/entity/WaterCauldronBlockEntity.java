package me.juancarloscp52.bedrockify.common.block.entity;

import me.juancarloscp52.bedrockify.common.block.ColoredWaterCauldronBlock;
import me.juancarloscp52.bedrockify.common.features.cauldron.BedrockCauldronBlocks;
import me.juancarloscp52.bedrockify.common.features.cauldron.ColorBlenderHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
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
        final var component = potionItem.get(DataComponentTypes.POTION_CONTENTS);
        if (component == null) {
            return;
        }
        var optionalPotion = component.potion();
        if (optionalPotion.isEmpty()) {
            return;
        }
        var potion = optionalPotion.get();
        this.potionTypeId = Registries.ITEM.getId(potionItem.getItem());
        this.fluidId = Registries.POTION.getId(potion.value());
        this.setTintColor(PotionContentsComponent.getColor(potion));
    }

    public <T extends DyeItem> void blendDyeItem(T item) {
        final int itemColor = ColorBlenderHelper.fromDyeItem(item);
        final int resultColor;
        if (Objects.equals(this.fluidId, Registries.BLOCK.getId(BedrockCauldronBlocks.COLORED_WATER_CAULDRON))) {
            resultColor = ColorBlenderHelper.blendColors(this.getTintColor(), itemColor);
        } else {
            this.fluidId = Registries.BLOCK.getId(BedrockCauldronBlocks.COLORED_WATER_CAULDRON);
            resultColor = itemColor;
        }
        this.setTintColor(resultColor);
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
            this.blendDyeItem(dyeItem);
            valid = true;
        } else if (Registries.BLOCK.get(this.getFluidId()) instanceof ColoredWaterCauldronBlock) {
            valid = true;
        } else {
            var potionEntry = Registries.POTION.getEntry(this.getFluidId());
            if (potionEntry.isPresent()) {
                valid = true;
                this.setTintColor(PotionContentsComponent.getColor(potionEntry.get()));
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
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        this.tintColor = nbt.getInt(KEY_FLUID_TINT);
        this.fluidId = Identifier.tryParse(nbt.getString(KEY_FLUID_ITEM));
        this.potionTypeId = Identifier.tryParse(nbt.getString(KEY_POTION_TYPE));
        this.checkExactIds();
        this.updateListeners();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt(KEY_FLUID_TINT, this.tintColor);
        nbt.putString(KEY_FLUID_ITEM, (this.fluidId == null) ? "<NULL>" : this.fluidId.toString());
        nbt.putString(KEY_POTION_TYPE, (this.potionTypeId == null) ? "<NULL>" : this.potionTypeId.toString());

        super.writeNbt(nbt, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
