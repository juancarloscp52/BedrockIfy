package me.juancarloscp52.bedrockify.mixin.client.features.pickupAnimations;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.Bedrockify;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    private float pickedItemCooldownLeft =0.0f;

    @Inject(method = "renderHotbarItem", at=@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCooldown()I"))
    private void captureItemStack(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int i, CallbackInfo ci){
        pickedItemCooldownLeft = stack.getCooldown()-tickDelta;
    }
    @Redirect(method = "renderHotbarItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V"))
    private void applyAnimation(MatrixStack matrixStack, float x, float y, float z){
        if(!Bedrockify.getInstance().settings.isPickupAnimationsEnabled()){
            matrixStack.scale(x,y,z);
            return;
        }
        if(pickedItemCooldownLeft >0.0f){
            float animation = 1.0f + pickedItemCooldownLeft / 12.5f;
            matrixStack.scale(1.0f*animation,1.0f*animation, 1.0f);
        }
    }
}
