package me.juancarloscp52.bedrockify.mixin.common.features.fireAspect;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Redirect(method = "interact",at=@At(value = "INVOKE",target = "Lnet/minecraft/entity/Entity;interact(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"))
    private ActionResult interact(Entity entity, PlayerEntity player, Hand hand){
        if(entity instanceof TntMinecartEntity tntMinecart && Bedrockify.getInstance().settings.fireAspectLight){
            ItemStack itemStack = player.getStackInHand(hand);
            if(null != itemStack && !tntMinecart.isPrimed()  && (((itemStack.hasEnchantments() || itemStack.getItem() instanceof EnchantedBookItem) && EnchantmentHelper.get(itemStack).containsKey(Enchantments.FIRE_ASPECT)) || (itemStack.isOf(Items.FLINT_AND_STEEL) || itemStack.isOf(Items.FIRE_CHARGE)))){
                tntMinecart.prime();
                itemStack.damage(1, player,((e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)));
                player.world.playSound(player, player.getBlockPos(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, player.world.getRandom().nextFloat() * 0.4F + 0.8F);
                return ActionResult.SUCCESS;
            }
        }
        return entity.interact(player, hand);
    }

}
