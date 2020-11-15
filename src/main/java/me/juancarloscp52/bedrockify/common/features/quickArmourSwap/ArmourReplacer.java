package me.juancarloscp52.bedrockify.common.features.quickArmourSwap;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ArmourReplacer {

    public static void tryChangeArmor(PlayerEntity user, Hand hand){
        ItemStack newArmor = user.getStackInHand(hand);
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(newArmor);
        ItemStack currentArmor = user.getEquippedStack(equipmentSlot);
        user.equipStack(equipmentSlot, newArmor.copy());
        currentArmor.setCooldown(5);
        user.setStackInHand(hand,currentArmor);
    }

}
