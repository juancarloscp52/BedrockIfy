package me.juancarloscp52.bedrockify.mixin.client.features.useAnimations;

import me.juancarloscp52.bedrockify.client.features.useAnimations.AnimationsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
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

        final ItemStack itemStack = player.getMainHandStack();
        if (itemStack.isDamageable()) {
            AnimationsHelper.doBobbingAnimation(itemStack);
        }
    }

    /**
     * Break the block and animate.
     */
    // TODO: When break the block with Flint and Steel (Not mining tools), it gets no damage but animate.
    @Inject(method = "breakBlock", at = @At("RETURN"))
    private void bedrockify$useToolBreakable(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        final PlayerEntity player = this.client.player;
        if (!cir.getReturnValue() || player == null) {
            return;
        }

        final ItemStack itemStack = player.getMainHandStack();
        if (itemStack.isDamageable()) {
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

        if (cir.getReturnValue().shouldSwingHand()) {
            AnimationsHelper.doBobbingAnimation(player.getStackInHand(hand));
        }
    }
}
