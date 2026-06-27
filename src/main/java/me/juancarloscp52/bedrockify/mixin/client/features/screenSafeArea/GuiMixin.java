package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Hud.class, priority = 500)
public abstract class GuiMixin {
    @Unique
    private int screenBorder;

    /**
     * Set the screenBorder area before anything renders.
     */
    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void setScreenBorder(CallbackInfo info) {
        this.screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
    }

    /**
     * Render the item Hotbar applying the screen border distance and transparency.
     */
    @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"))
    private void drawTextureHotbar(GuiGraphicsExtractor drawContext, RenderPipeline pipeline, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        if (texture.equals(Identifier.withDefaultNamespace("hud/hotbar_selection"))) {
            original.call(drawContext, pipeline, texture, x, y - screenBorder, width, height);
            if (BedrockifyClient.getInstance().settings.hotBarOverhang)
                drawContext.fill(x, y + height - screenBorder, x + width, y + height + 1 - screenBorder, ARGB.color((int) (255 * BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false)), 0, 0, 0));
        } else {
            original.call(drawContext, pipeline, texture, x, y - screenBorder, width, height);
        }
    }

    @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIIIII)V"))
    private void drawTextureHotbar(GuiGraphicsExtractor drawContext, RenderPipeline pipeline, Identifier texture, int i, int j, int k, int l, int x, int y, int width, int height, Operation<Void> original) {
        if ((width == 29 && height == 24) || width == 182) {
            drawContext.blitSprite(pipeline, texture, i, j, k, l, x, y - screenBorder, width, height, ARGB.color(BedrockifyClient.getInstance().hudOpacity.getHudOpacity(true), -1));
        } else {
            boolean raisedEnabled = FabricLoader.getInstance().isModLoaded("raised");
            drawContext.blitSprite(pipeline, texture, i, j, k, l, x, y - screenBorder, width, (width == 24 && !raisedEnabled) ? height + 2 : height, ARGB.color(BedrockifyClient.getInstance().hudOpacity.getHudOpacity(true), -1));
        }
    }

    /**
     * Render the items in the Hotbar with the screen border distance.
     */
    @ModifyArg(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;extractSlot(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILnet/minecraft/client/DeltaTracker;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V"), index = 2)
    public int modifyHotbarItemPosition(int y) {
        return y - screenBorder;
    }

    /**
     * Apply screen border offset to mount health bars.
     */
    @ModifyArg(method = "extractVehicleHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"), index = 3)
    public int modifyTextureMountHealth(int y) {
        return y - screenBorder;
    }

    /**
     * Apply screen border offset to health bars.
     */
    @ModifyArg(method = "extractPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;extractHearts(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"), index = 3)
    private int modifyTextureStatusBarsHearts(int y) {
        return y - screenBorder;
    }

    /**
     * Apply screen border offset to armor bars.
     */
    @ModifyArg(method = "extractPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;extractArmor(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/entity/player/Player;IIII)V"), index = 2)
    private int modifyTextureStatusBarsArmor(int i) {
        return i - screenBorder;
    }

    /**
     * Apply screen border offset to food bars.
     */
    @ModifyArg(method = "extractPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;extractFood(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/entity/player/Player;II)V"), index = 2)
    private int modifyTextureStatusBarsFood(int y) {
        return y - screenBorder;
    }

    /**
     * Render the status effect overlay with the screen border distance applied.
     */
    @ModifyVariable(method = "extractEffects", at = @At("STORE"), ordinal = 2)
    public int modifyStatusEffectOverlayX(int x) {
        return x - screenBorder;
    }

    @ModifyVariable(method = "extractEffects", at = @At("STORE"), ordinal = 3)
    public int modifyStatusEffectOverlayY(int y) {
        return y + screenBorder;
    }

    // Apply screen borders to Titles, subtitles and other messages.
    @ModifyArg(method = "extractTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;textWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)V", ordinal = 0), index = 3)
    public int modifyOverlayMessage(int y) {
        return y - screenBorder;
    }

    @ModifyExpressionValue(method = "extractAirBubbles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;getAirBubbleYLine(II)I"))
    private int bedrockify$modifyTextureStatusBarsBubbleY(int original) {
        return original - screenBorder;
    }
}
