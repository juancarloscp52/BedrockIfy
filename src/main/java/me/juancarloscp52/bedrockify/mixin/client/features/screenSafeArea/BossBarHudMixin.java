package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BossBarHud.class)

/*
  Apply screen safe area to BossBar Hud.
 */
public abstract class BossBarHudMixin {

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/BossBarHud;renderBossBar(Lnet/minecraft/client/util/math/MatrixStack;IILnet/minecraft/entity/boss/BossBar;)V"),index = 2)
    public int applyScreenBorderToBossBar(int y){
        return y + BedrockifyClient.getInstance().settings.getScreenSafeArea();
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"),index = 3)
    public float applyScreenBorderToBossName(float y){
        return y + BedrockifyClient.getInstance().settings.getScreenSafeArea();
    }

}
