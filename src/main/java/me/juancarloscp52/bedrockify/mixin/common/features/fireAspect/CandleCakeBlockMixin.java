package me.juancarloscp52.bedrockify.mixin.common.features.fireAspect;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CandleCakeBlock.class)
public class CandleCakeBlockMixin {

    @Inject(method = "onUseWithItem", at=@At("HEAD"),cancellable = true)
    private void onUse(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir){
        if(!Bedrockify.getInstance().settings.fireAspectLight)
            return;
        ItemStack itemStack = player.getStackInHand(hand);
        if(isHittingCandleOnCake(hit) && null != itemStack && (itemStack.hasEnchantments() || itemStack.getItem() instanceof EnchantedBookItem) && EnchantmentHelper.getEnchantments(itemStack).getEnchantments().stream().anyMatch(e -> e == Enchantments.FIRE_ASPECT)){
            if(!CandleCakeBlock.isLitCandle(state) && CandleCakeBlock.canBeLit(state)){
                if(world.setBlockState(pos, state.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD)){
                    itemStack.damage(1, player, LivingEntity.getSlotForHand(hand));
                    world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                    world.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos);
                    cir.setReturnValue(ItemActionResult.SUCCESS);
                }
            }else{
                cir.setReturnValue(ItemActionResult.FAIL);
            }
        }
    }

    private boolean isHittingCandleOnCake(BlockHitResult hitResult) {
        return hitResult.getPos().y - (double)hitResult.getBlockPos().getY() > 0.5D;
    }
}
