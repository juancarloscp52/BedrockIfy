package me.juancarloscp52.bedrockify.mixin.client.features.useAnimations;

import me.juancarloscp52.bedrockify.client.features.useAnimations.AnimationsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
    @Shadow
    private @Final MinecraftClient client;

    /**
     * Attack the entity, animate only damageable item.<br>
     * When no damage may prevent Bobbing animation. e.g.) Unbreaking Enchantment
     */
    @Inject(method = "attackEntity", at = @At("RETURN"))
    private void bedrockify$useWeaponBreakable(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (player == null) {
            return;
        }

        if (!(target instanceof LivingEntity)) {
            return;
        }

        final ItemStack itemStack = player.getMainHandStack();
        if (itemStack.isDamageable()) {
            AnimationsHelper.doBobbingAnimation(itemStack);
        }
    }

    /**
     * Break the block and animate.
     */
    @Inject(method = "breakBlock", at = @At("RETURN"))
    private void bedrockify$useToolBreakable(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        final PlayerEntity player = this.client.player;
        if (!cir.getReturnValue() || player == null) {
            return;
        }

        final ItemStack itemStack = player.getMainHandStack();
        final Item item = itemStack.getItem();
        if (item instanceof ToolItem || item == Items.SHEARS) {
            AnimationsHelper.doBobbingAnimation(itemStack);
        }
    }

    /**
     * Placing a block, Using an item to block.
     */
    @Redirect(method = "interactBlockInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"))
    private ActionResult bedrockify$useItemToBlock(ItemStack instance, ItemUsageContext context) {
        final ActionResult actionResult = instance.useOnBlock(context);
        if (actionResult.isAccepted() && context.getPlayer() != null) {
            AnimationsHelper.doBobbingAnimation(context.getPlayer().getStackInHand(context.getHand()));
        }
        return actionResult;
    }

    /**
     * Do animate of consuming item.
     */
    @Inject(method = "interactItem", at = @At("RETURN"))
    private void bedrockify$consumeItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (player == null) {
            return;
        }

        final ItemStack itemStack = player.getStackInHand(hand);
        final ActionResult actionResult = cir.getReturnValue();
        final boolean bNotSwingHandAnim = actionResult.isAccepted() && (itemStack.isOf(Items.GOAT_HORN) || itemStack.isOf(Items.ENDER_PEARL));
        if (actionResult.shouldSwingHand() || bNotSwingHandAnim) {
            AnimationsHelper.doBobbingAnimation(player.getStackInHand(hand));
        }
    }
}
