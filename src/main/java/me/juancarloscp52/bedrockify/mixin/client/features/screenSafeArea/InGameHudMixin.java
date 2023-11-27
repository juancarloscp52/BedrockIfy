package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Unique
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
    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    private void drawTextureHotbar(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if((width ==29 && height == 24) || width == 182){
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, BedrockifyClient.getInstance().hudOpacity.getHudOpacity(true));
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            original.call(instance, texture, x, y - screenBorder, width, height);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
        }else{
            original.call(instance, texture, x, y - screenBorder, width, height);
            instance.fill(x,y + height - screenBorder,x+width,y+height+1 - screenBorder, ColorHelper.Argb.getArgb(255,0,0,0));
        }
    }
    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"))
    private void drawTextureHotbar(DrawContext instance, Identifier texture, int i, int j, int k, int l, int x, int y, int width, int height, Operation<Void> original) {
        if((width ==29 && height == 24) || width == 182){
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, BedrockifyClient.getInstance().hudOpacity.getHudOpacity(true));
            original.call(instance, texture, i, j, k, l, x, y - screenBorder, width, height);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
        }else{
            boolean raisedEnabled = FabricLoader.getInstance().isModLoaded("raised");
            original.call(instance, texture, i, j, k, l, x, y - screenBorder, width, (width  == 24 && !raisedEnabled) ? height+2 : height);
        }
    }
    /**
     * Render the items in the Hotbar with the screen border distance.
     */
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"),index = 2)
    public int modifyHotbarItemPossition(int y){
        return y-screenBorder;
    }

    /**
     * Apply screen border offset to experience bars.
     */
    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"),index = 2)
    public int modifyTextureExperienceBar(int y){
        return y - screenBorder;
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"),index = 6)
    public int modifyTextureExperienceBar2(int y){
        return y - screenBorder;
    }

    /**
     * Apply screen border offset to experience bar text.
     */
    @WrapOperation(method = "renderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"))
    private int drawExperienceBar(DrawContext instance, TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow, Operation<Integer> original) {
        int alpha = (int) Math.ceil(BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false)*255);

        if(!BedrockifyClient.getInstance().settings.isExpTextStyle()){
            return original.call(instance, textRenderer, text, x, y-screenBorder, color | ((alpha) << 24),false);
        }

        if(color == 0)
            return 0;
        return instance.drawTextWithShadow(textRenderer, text, x, y-screenBorder-3, ColorHelper.Argb.getArgb(alpha,127, 252, 32));
    }

    /**
     * Apply screen border offset to mount bars.
     */
    @ModifyArg(method = "renderMountJumpBar", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"),index = 6)
    public int modifyTextureMountJumpBar(int y){
        return y-screenBorder;
    }
    /**
     * Apply screen border offset to mount bars.
     */
    @ModifyArg(method = "renderMountJumpBar", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"),index = 2)
    public int modifyTextureMountJumpBar2(int y){
        return y-screenBorder;
    }

    /**
     * Apply screen border offset to mount health bars.
     */
    @ModifyArg(method = "renderMountHealth", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"),index = 2)
    public int modifyTextureMountHealth(int y){
        return y-screenBorder;
    }

    /**
     * Apply screen border offset to status bars.
     */
    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"),index = 2)
    public int modifyTextureStatusBar(int y){
        return y - screenBorder;
    }

    /**
     * Apply screen border offset to health bars.
     */
    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"),index = 3)
    private int modifyTextureStatusBarsHearts(int y){
        return y-screenBorder;
    }

    /**
     * Render the status effect overlay with the screen border distance applied.
     */
    @ModifyVariable(method = "renderStatusEffectOverlay", at = @At("STORE"),ordinal = 2)
    public int modifyStatusEffectOverlayX(int x){
        return x-screenBorder;
    }
    @ModifyVariable(method = "renderStatusEffectOverlay", at = @At("STORE"),ordinal = 3)
    public int modifyStatusEffectOverlayY(int y){
        return y+screenBorder;
    }

    // Apply screen borders to Titles, subtitles and other messages.
    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I",ordinal = 0),index = 3)
    public int modifyOverlayMessage(int y){
        return y-screenBorder;
    }

}
