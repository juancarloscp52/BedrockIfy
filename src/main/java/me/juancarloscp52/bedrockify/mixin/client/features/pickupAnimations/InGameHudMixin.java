package me.juancarloscp52.bedrockify.mixin.client.features.pickupAnimations;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin{

    private float pickedItemCooldownLeft =0.0f;

    @Inject(method = "renderHotbarItem", at=@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getBobbingAnimationTime()I"))
    private void captureItemStack(DrawContext drawContext, int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int i, CallbackInfo ci){
        pickedItemCooldownLeft = stack.getBobbingAnimationTime()-tickDelta;
    }
    @WrapOperation(method = "renderHotbarItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V"))
    private void applyAnimation(MatrixStack instance, float x, float y, float z, Operation<Void> original){
        if(!BedrockifyClient.getInstance().settings.isPickupAnimationsEnabled()){
            original.call(instance, x, y, z);
            return;
        }
        if(pickedItemCooldownLeft >0.0f){
            float animation = 1.0f + pickedItemCooldownLeft / 12.5f;
            original.call(instance, animation, animation, 1.0f);
        }
    }
}
