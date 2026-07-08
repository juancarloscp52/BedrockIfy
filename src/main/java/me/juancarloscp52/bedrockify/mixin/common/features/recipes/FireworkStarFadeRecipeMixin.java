package me.juancarloscp52.bedrockify.mixin.common.features.recipes;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.common.features.recipes.DyeHelper;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.FireworkStarFadeRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FireworkStarFadeRecipe.class)
public class FireworkStarFadeRecipeMixin {
    @ModifyVariable(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/CustomRecipe;<init>()V", shift = At.Shift.AFTER), ordinal = 1)
    private Ingredient bedrockify$modifyDyeIngredient(Ingredient original) {
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled())
            return original;

        return Ingredient.of(DyeHelper.ITEMS.stream());
    }

    @WrapOperation(method = "matches", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;has(Lnet/minecraft/core/component/DataComponentType;)Z"))
    private boolean bedrockify$modifyHasDyeComponent(ItemStack instance, DataComponentType<?> componentType, Operation<Boolean> original) {
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled())
            return original.call(instance, componentType);

        return DyeHelper.isDyeableItem(instance.getItem());
    }

    @WrapOperation(method = "assemble", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object bedrockify$modifyDyeColor(ItemStack instance, DataComponentType<?> componentType, Object defaultDyeColor, Operation<Object> original) {
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled())
            return original.call(instance, componentType, defaultDyeColor);

        return DyeHelper.getDyeItem(instance.getItem()).map(dyeItem -> dyeItem.getDefaultInstance().get(DataComponents.DYE)).orElse((DyeColor) defaultDyeColor);
    }
}
