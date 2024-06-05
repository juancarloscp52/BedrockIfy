package me.juancarloscp52.bedrockify.mixin.client.features.pickupAnimations;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
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
public abstract class InGameHudMixin{

    private float pickedItemCooldownLeft =0.0f;

    @Inject(method = "renderHotbarItem", at=@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getBobbingAnimationTime()I"))
    private void captureItemStack(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci){
        pickedItemCooldownLeft = stack.getBobbingAnimationTime()-tickCounter.getTickDelta(true);
    }
    @Redirect(method = "renderHotbarItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V"))
    private void applyAnimation(MatrixStack matrixStack, float x, float y, float z){
        if(!BedrockifyClient.getInstance().settings.isPickupAnimationsEnabled()){
            matrixStack.scale(x,y,z);
            return;
        }
        if(pickedItemCooldownLeft >0.0f){
            float animation = 1.0f + pickedItemCooldownLeft / 12.5f;
            matrixStack.scale(animation, animation, 1.0f);
        }
    }
}
