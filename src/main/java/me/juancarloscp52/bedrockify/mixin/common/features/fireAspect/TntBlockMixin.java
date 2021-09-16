package me.juancarloscp52.bedrockify.mixin.common.features.fireAspect;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin {

    @Shadow
    public static void primeTnt(World world, BlockPos pos) {}

    @Inject(method = "onUse",at=@At("HEAD"),cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        if(!Bedrockify.getInstance().settings.fireAspectLight)
            return;
        ItemStack itemStack = player.getStackInHand(hand);
        if(null != itemStack && (itemStack.hasEnchantments() || itemStack.getItem() instanceof EnchantedBookItem) && EnchantmentHelper.get(itemStack).containsKey(Enchantments.FIRE_ASPECT)){
            primeTnt(world,pos);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            itemStack.damage(1, player,((e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)));
            world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }

}
