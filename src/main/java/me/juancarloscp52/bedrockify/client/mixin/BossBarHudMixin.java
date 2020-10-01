package me.juancarloscp52.bedrockify.client.mixin;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BossBarHud.class)

/*
  Apply screen safe area to BossBar Hud.
 */
public abstract class BossBarHudMixin {


    @Shadow
    protected abstract void renderBossBar(MatrixStack matrixStack, int i, int j, BossBar bossBar);

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/BossBarHud;renderBossBar(Lnet/minecraft/client/util/math/MatrixStack;IILnet/minecraft/entity/boss/BossBar;)V"))
    private void applyScreenBorderToBossBar(BossBarHud bossBarHud, MatrixStack matrixStack, int i, int j, BossBar bossBar) {
        this.renderBossBar(matrixStack, i, j + Bedrockify.getInstance().settings.getScreenSafeArea(), bossBar);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    public int applyScreenBorderToBossName(TextRenderer textRenderer, MatrixStack matrices, Text text, float x, float y, int color) {
        return textRenderer.drawWithShadow(matrices, text, x, y + Bedrockify.getInstance().settings.getScreenSafeArea(), color);
    }

}
