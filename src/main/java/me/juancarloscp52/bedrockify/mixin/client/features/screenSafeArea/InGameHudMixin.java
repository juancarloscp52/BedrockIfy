package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.Bedrockify;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
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
    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"),index = 1)
    public int modifyHotbarItemPossition(int y){
        return y-screenBorder;
    }

    /**
     * Apply screen border offset to experience bars.
     */
    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),index = 2)
    public int modifyTextureExperienceBar(int y){
        return y - screenBorder;
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
    @ModifyArg(method = "renderMountJumpBar", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),index = 2)
    public int modifyTextureMountJumpBar(int y){
        return y-screenBorder;
    }

    /**
     * Apply screen border offset to mount health bars.
     */
    @ModifyArg(method = "renderMountHealth", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),index = 2)
    public int modifyTextureMountHealth(int y){
        return y-screenBorder;
    }

    /**
     * Apply screen border offset to status bars.
     */
    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),index = 2)
    public int modifyTextureStatusBar(int y){
        return y - screenBorder;
    }

    /**
     * Apply screen border offset to health bars.
     */
    @ModifyArg(method = "renderStatusBars", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHealthBar(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/player/PlayerEntity;IIIIFIIIZ)V"),index = 3)
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
    @ModifyArg(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I",ordinal = 0),index = 3)
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
