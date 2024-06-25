package me.juancarloscp52.bedrockify.mixin.common.features.fireAspect;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemActionResult;
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

    @Inject(method = "onUseWithItem",at=@At("HEAD"),cancellable = true)
    private void onUse(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir){
        if(!Bedrockify.getInstance().settings.fireAspectLight)
            return;
        ItemStack itemStack = player.getStackInHand(hand);
        if(null != itemStack && (itemStack.hasEnchantments() || itemStack.getItem() instanceof EnchantedBookItem) && EnchantmentHelper.getEnchantments(itemStack).getEnchantments().stream().anyMatch(e -> e.matchesId(Identifier.of("fire_aspect")))){
            primeTnt(world,pos);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            itemStack.damage(1, player, LivingEntity.getSlotForHand(hand));
            world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            cir.setReturnValue(ItemActionResult.success(world.isClient));
        }
    }

}
