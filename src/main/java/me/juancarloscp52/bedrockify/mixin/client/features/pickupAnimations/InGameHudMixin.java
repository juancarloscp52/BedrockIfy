package me.juancarloscp52.bedrockify.mixin.client.features.pickupAnimations;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.Bedrockify;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
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
    private void captureItemStack(int i, int j, float f, PlayerEntity playerEntity, ItemStack itemStack, CallbackInfo info){
        pickedItemCooldownLeft = itemStack.getCooldown()-f;
    }
    @Redirect(method = "renderHotbarItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;scalef(FFF)V"))
    private void applyAnimation(float x, float y, float z){
        if(!Bedrockify.getInstance().settings.isPickupAnimationsEnabled()){
            RenderSystem.scalef(x,y,z);
            return;
        }
        if(pickedItemCooldownLeft >0.0f){
            float animation = 1.0f + pickedItemCooldownLeft / 12.5f;
            RenderSystem.scalef(1.0f*animation,1.0f*animation, 1.0f);
        }
    }
}
