package me.juancarloscp52.bedrockify.mixin.common.features.quickArmourSwap;

import me.juancarloscp52.bedrockify.common.features.quickArmourSwap.ArmourReplacer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin {


    private World world;
    @Inject(method = "use", at=@At(value = "INVOKE", target = "Lnet/minecraft/util/TypedActionResult;fail(Ljava/lang/Object;)Lnet/minecraft/util/TypedActionResult;"))
    private void tryChangeArmor(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info){
        this.world=world;
        ArmourReplacer.tryChangeArmor(user,hand);
    }

    @Redirect(method = "use", at=@At(value = "INVOKE", target = "Lnet/minecraft/util/TypedActionResult;fail(Ljava/lang/Object;)Lnet/minecraft/util/TypedActionResult;"))
    public TypedActionResult<ItemStack> checkSuccess(Object object){
        return TypedActionResult.method_29237((ItemStack) object, world.isClient());
    }
}
