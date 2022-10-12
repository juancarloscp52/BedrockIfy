package me.juancarloscp52.bedrockify.client.features.sneakingShield;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;

public class SneakingShield {
    public static void tryActivation(boolean sneaking){
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(BedrockifyClient.getInstance().settings.isSneakingShieldEnabled() && sneaking && player !=null){
            ItemStack shield = player.getMainHandStack();
            if(shield!=null && shield.getItem() instanceof ShieldItem){
                shield.use(player.world,player, Hand.MAIN_HAND);
                return;
            }
            shield = player.getOffHandStack();
            if(shield!=null && shield.getItem() instanceof ShieldItem){
                shield.use(player.world,player, Hand.OFF_HAND);
            }
        }
    }
}
