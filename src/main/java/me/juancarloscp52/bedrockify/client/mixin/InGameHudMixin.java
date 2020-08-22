package me.juancarloscp52.bedrockify.client.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    @Shadow private ItemStack currentStack;
    @Shadow private int scaledWidth;
    @Shadow private int heldItemTooltipFade;
    @Shadow @Final private MinecraftClient client;
    @Shadow protected abstract void renderHotbarItem(int i, int j, float f, PlayerEntity playerEntity, ItemStack itemStack);

    private ItemStack nextItem = null;
    private int screenBorder;

    /**
     * Set the screenBorder area before anything renders.
     */
    @Inject(method = "render", at = @At("HEAD"))
    private void setScreenBorder(CallbackInfo info) {
        this.screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
    }

    /**
     * Render the item Hotbar applying the screen border distance and transparency.
     */
    @Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    private void drawTextureHotbar(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        if (width == 182 || x == (scaledWidth / 2) - 91 - 29) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.6F);
            inGameHud.drawTexture(matrices, x, y - screenBorder, u, v, width, height);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1);
        } else {
            inGameHud.drawTexture(matrices, x, y - screenBorder, u, v, width, height + 2);
        }
    }

    /**
     * Render the items in the Hotbar with the screen border distance.
     */
    @Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V"))
    private void renderHotbarItemWithOffset(InGameHud inGameHud, int i, int j, float f, PlayerEntity playerEntity, ItemStack itemStack) {
        renderHotbarItem(i, j - screenBorder, f, playerEntity, itemStack);
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
        return fontRenderer.draw(matrices, text, x, y - screenBorder, color);
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
     * Apply screen order offset to status bars.
     */
    @Redirect(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"))
    private void drawTextureStatusBars(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        inGameHud.drawTexture(matrices, x, y - screenBorder, u, v, width, height);
    }

    /**
     * Render the status effect overlay with the screen border distance applied.
     */
    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    public void renderStatusEffectOverlay(MatrixStack matrixStack, CallbackInfo info) {
        Collection<StatusEffectInstance> collection = Objects.requireNonNull(this.client.player).getStatusEffects();
        if (!collection.isEmpty()) {
            RenderSystem.enableBlend();
            int beneficialEffects = 0;
            int harmfulEffects = 0;
            StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
            List<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());
            this.client.getTextureManager().bindTexture(HandledScreen.BACKGROUND_TEXTURE);
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

                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
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
                        this.client.getTextureManager().bindTexture(sprite.getAtlas().getId());
                        RenderSystem.color4f(1.0F, 1.0F, 1.0F, finalAlpha);
                        drawSprite(matrixStack, finalX, finalY, this.getZOffset(), 18, 18, sprite);
                    });
                }
            }

            list.forEach(Runnable::run);
        }
        info.cancel();
    }

    /**
     * Draw custom tooltips for effects and enchantments before the heldItemTooltip is rendered.
     */
    @Redirect(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    private int drawCustomTooltips(TextRenderer fontRenderer, MatrixStack matrices, Text text, float x, float y, int color) {
        return BedrockifyClient.getInstance().itemTooltips.drawItemWithCustomTooltips(fontRenderer, matrices, text, x, y, color, currentStack);
    }
    /**
     * Draw custom tooltips for effects and enchantments before the heldItemTooltip is rendered.
     */
    @Redirect(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
    private void drawCustomTooltips(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {}

    /**
     * Show the item tooltip when changing from a item to another of the same type and name IFF different enchantments.
     */
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getName()Lnet/minecraft/text/Text;", ordinal = 1))
    private void fixSameItemFade(CallbackInfo info) {
        if (!(nextItem.getName().equals(this.currentStack.getName()) && nextItem.getEnchantments().equals(this.currentStack.getEnchantments()))) {
            this.heldItemTooltipFade = 41;
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I",ordinal = 0))
    public int renderOverlayMessage(TextRenderer textRenderer, MatrixStack matrices, Text text, float x, float y, int color){
        return textRenderer.draw(matrices, text, x, y-screenBorder, color);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I", ordinal = 0))
    public int drawTitle(TextRenderer textRenderer,MatrixStack matrices, Text text, float x, float y, int color){
        return textRenderer.drawWithShadow(matrices, text, x, y - (screenBorder/4.0f), color);
    }
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I", ordinal = 1))
    public int drawSubtitle(TextRenderer textRenderer,MatrixStack matrices, Text text, float x, float y, int color){
        return textRenderer.drawWithShadow(matrices, text, x, y - (screenBorder/2.0f), color);
    }
    /**
     * Retrieves the next itemStack that the player will use. This is used in fixSameItemFade.
     */
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
    private boolean interceptItemStack(ItemStack item) {
        nextItem = item;
        return item.isEmpty();
    }

    /**
     * Renders the Bedrockify overlay.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isDemo()Z", ordinal = 0))
    private void renderBedrockIfy(MatrixStack matrixStack, float f, CallbackInfo info) {
        BedrockifyClient.getInstance().overlay.renderOverlay(matrixStack);
    }

}
