package me.juancarloscp52.bedrockify.mixin.common.features.recipes;

import com.google.common.collect.Lists;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.utils.DyeHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.FireworkStarRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(FireworkStarRecipe.class)
public class FireworkStarRecipeMixin {

    @Shadow @Final private static Ingredient TYPE_MODIFIER;

    @Shadow @Final private static Ingredient FLICKER_MODIFIER;

    @Shadow @Final private static Ingredient TRAIL_MODIFIER;

    @Shadow @Final private static Ingredient GUNPOWDER;

    @Shadow @Final private static Map<Item, FireworkItem.Type> TYPE_MODIFIER_MAP;

    @Inject(method = "matches",at=@At("HEAD"),cancellable = true)
    public void matches(CraftingInventory craftingInventory, World world, CallbackInfoReturnable<Boolean> infoReturnable) {
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled())
            return;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;

        for(int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack = craftingInventory.getStack(i);
            if (!itemStack.isEmpty()) {
                if (TYPE_MODIFIER.test(itemStack)) {
                    if (bl3) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl3 = true;
                } else if (FLICKER_MODIFIER.test(itemStack)) {
                    if (bl5) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl5 = true;
                } else if (TRAIL_MODIFIER.test(itemStack)) {
                    if (bl4) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl4 = true;
                } else if (GUNPOWDER.test(itemStack)) {
                    if (bl) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl = true;
                } else {
                    if (!(DyeHelper.isDyeableItem(itemStack.getItem()))) {
                        infoReturnable.setReturnValue(false);
                        return;
                    }

                    bl2 = true;
                }
            }
        }
        infoReturnable.setReturnValue(bl && bl2);
    }

    @Inject(method = "craft",at=@At("HEAD"),cancellable = true)
    public void craft(CraftingInventory craftingInventory, CallbackInfoReturnable<ItemStack> infoReturnable) {
        if(!Bedrockify.getInstance().settings.isBedrockRecipesEnabled())
            return;
        ItemStack itemStack = new ItemStack(Items.FIREWORK_STAR);
        CompoundTag compoundTag = itemStack.getOrCreateSubTag("Explosion");
        FireworkItem.Type type = FireworkItem.Type.SMALL_BALL;
        List<Integer> list = Lists.newArrayList();

        for(int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack2 = craftingInventory.getStack(i);
            if (!itemStack2.isEmpty()) {
                if (TYPE_MODIFIER.test(itemStack2)) {
                    type = TYPE_MODIFIER_MAP.get(itemStack2.getItem());
                } else if (FLICKER_MODIFIER.test(itemStack2)) {
                    compoundTag.putBoolean("Flicker", true);
                } else if (TRAIL_MODIFIER.test(itemStack2)) {
                    compoundTag.putBoolean("Trail", true);
                } else if (DyeHelper.isDyeableItem(itemStack2.getItem())) {
                    list.add((DyeHelper.getDyeItem(itemStack2.getItem()).getColor().getFireworkColor()));
                }
            }
        }

        compoundTag.putIntArray("Colors", list);
        compoundTag.putByte("Type", (byte)type.getId());
        infoReturnable.setReturnValue(itemStack);
    }

}
