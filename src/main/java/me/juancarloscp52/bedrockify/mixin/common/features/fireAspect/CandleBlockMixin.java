package me.juancarloscp52.bedrockify.mixin.common.features.fireAspect;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CandleBlock.class)
public class CandleBlockMixin {

    @Inject(method = "onUse", at=@At("HEAD"),cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        if(!Bedrockify.getInstance().settings.fireAspectLight)
            return;
        ItemStack itemStack = player.getStackInHand(hand);
        if(player.getAbilities().allowModifyWorld && null != itemStack && (itemStack.hasEnchantments() || itemStack.getItem() instanceof EnchantedBookItem) && EnchantmentHelper.get(itemStack).containsKey(Enchantments.FIRE_ASPECT)){
            if(!CandleBlock.isLitCandle(state) && CandleBlock.canBeLit(state)){
                if(world.setBlockState(pos, state.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD)){
                    itemStack.damage(1, player,((e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND)));
                    world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                    world.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos);
                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }else{
                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }


}
