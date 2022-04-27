package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.Bedrockify;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    @Shadow private int scaledWidth;
    @Shadow @Final private MinecraftClient client;
    @Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int i);
    @Shadow protected abstract void renderHealthBar(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking);

    private int screenBorder;

    /**
     * Set the screenBorder area before anything renders.
     */
    @Inject(method = "render", at = @At("HEAD"))
    private void setScreenBorder(CallbackInfo info) {
        this.screenBorder = Bedrockify.getInstance().settings.getScreenSafeArea();
    }

    /**
     * Render the item Hotbar applying the screen border distance and transparency.
     */
    @Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    private void drawTextureHotbar(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        if((width ==29 && height == 24) || width == 182){
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Bedrockify.getInstance().settings.isTransparentHotBarEnabled()? 0.6F:1.0F);
            inGameHud.drawTexture(matrices, x, y - screenBorder, u, v, width, height);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }else{
            inGameHud.drawTexture(matrices, x, y - screenBorder, u, v, width, width == 24 ? height+2 : height);
        }
    }

    /**
     * Render the items in the Hotbar with the screen border distance.
     */
    @Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"))
    private void renderHotbarItemWithOffset(InGameHud inGameHud, int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int i) {
        renderHotbarItem(x,y-screenBorder,tickDelta,player,stack,i);
    }
    /**
     * Apply screen border offset to experience bars.
     */
    @Redirect(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    private void drawTextureExperienceBar(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        inGameHud.drawTexture(matrices, x, y - screenBorder, u, v, width, height);
    }
    /**
     * Apply screen border offset to experience bar text.
     */
    @Redirect(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"))
    private int drawExperienceBar(TextRenderer fontRenderer, MatrixStack matrices, String text, float x, float y, int color) {
        if(!Bedrockify.getInstance().settings.isExpTextStyle()){
            return fontRenderer.draw(matrices, text, x, y-screenBorder, color);
        }

        if(color == 0)
            return 0;
        return fontRenderer.drawWithShadow(matrices, text, x, y-screenBorder-3, MathHelper.packRgb(127, 252, 32));
    }

    /**
     * Apply screen border offset to mount bars.
     */
    @Redirect(method = "renderMountJumpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    private void drawTextureMountJumpBar(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        inGameHud.drawTexture(matrices, x, y - screenBorder, u, v, width, height);
    }

    /**
     * Apply screen border offset to mount health bars.
     */
    @Redirect(method = "renderMountHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    private void drawTextureMountHealth(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        inGameHud.drawTexture(matrices, x, y - screenBorder, u, v, width, height);
    }

    /**
     * Apply screen border offset to status bars.
     */
    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    private void drawTextureStatusBars(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        inGameHud.drawTexture(matrices, x, y - screenBorder, u, v, width, height);
    }
    /**
     * Apply screen border offset to health bars.
     */
    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"))
    private void drawTextureStatusBarsHearts(InGameHud inGameHud, MatrixStack matrixStack, PlayerEntity playerEntity, int i, int j, int k, int l, float f, int m, int n, int o, boolean bl) {
        renderHealthBar(matrixStack, playerEntity, i, j-screenBorder, k, l, f, m, n, o, bl);
    }


    /**
     * Render the status effect overlay with the screen border distance applied.
     */
    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    public void renderStatusEffectOverlay(MatrixStack matrixStack, CallbackInfo info) {
        if(FabricLoader.getInstance().isModLoaded("inventoryhud"))
            return;
        Collection<StatusEffectInstance> collection = Objects.requireNonNull(this.client.player).getStatusEffects();
        if (!collection.isEmpty()) {
            RenderSystem.enableBlend();
            int beneficialEffects = 0;
            int harmfulEffects = 0;
            StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
            List<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());
            RenderSystem.setShaderTexture(0,HandledScreen.BACKGROUND_TEXTURE);
            for (StatusEffectInstance statusEffectInstance : Ordering.natural().reverse().sortedCopy(collection)) {
                StatusEffect statusEffect = statusEffectInstance.getEffectType();
                if (statusEffectInstance.shouldShowIcon()) {
                    int x = this.scaledWidth - screenBorder;
                    int y = 1 + screenBorder;
                    if (this.client.isDemo()) {
                        y += 15;
                    }

                    if (statusEffect.isBeneficial()) {
                        ++beneficialEffects;
                        x -= 25 * beneficialEffects;
                    } else {
                        ++harmfulEffects;
                        x -= 25 * harmfulEffects;
                        y += 26;
                    }

                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    float spriteAlpha = 1.0F;
                    if (statusEffectInstance.isAmbient()) {
                        this.drawTexture(matrixStack, x, y, 165, 166, 24, 24);
                    } else {
                        this.drawTexture(matrixStack, x, y, 141, 166, 24, 24);
                        if (statusEffectInstance.getDuration() <= 200) {
                            int m = 10 - statusEffectInstance.getDuration() / 20;
                            spriteAlpha = MathHelper.clamp((float) statusEffectInstance.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float) statusEffectInstance.getDuration() * 3.1415927F / 5.0F) * MathHelper.clamp((float) m / 10.0F * 0.25F, 0.0F, 0.25F);
                        }
                    }

                    Sprite sprite = statusEffectSpriteManager.getSprite(statusEffect);
                    int finalX = x + 3;
                    int finalY = y + 3;
                    float finalAlpha = spriteAlpha;
                    list.add(() -> {
                        RenderSystem.setShaderTexture(0,sprite.getAtlas().getId());
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, finalAlpha);
                        drawSprite(matrixStack, finalX, finalY, this.getZOffset(), 18, 18, sprite);
                    });
                }
            }

            list.forEach(Runnable::run);
        }
        info.cancel();
    }

    // Apply screen borders to Titles, subtitles and other messages.
    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I",ordinal = 0),index = 3)
    public float modifyOverlayMessage(float y){
        return y-screenBorder;
    }

/*    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I",ordinal = 0),index = 3)
    public float modifyTittle(float y){
        return y - (screenBorder/4.0f);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I",ordinal = 1),index = 3)
    public float modifySubtitle(float y){
        return y - (screenBorder/2.0f);
    }*/

}
