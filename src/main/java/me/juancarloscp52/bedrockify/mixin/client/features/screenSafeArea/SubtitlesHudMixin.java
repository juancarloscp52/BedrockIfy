package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SubtitlesHud.class)
/*
 * Applies the screen border distance to the subtitles widget.
 */
public class SubtitlesHudMixin extends DrawableHelper {
    @ModifyArgs(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"))
    private void modifyDrawText(Args args){
        int screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
        float x = args.get(2);
        float y = args.get(3);
        args.set(2,x - screenBorder);
        args.set(3,y - screenBorder);
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    public void modifyDrawText2(Args args){
        int screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
        float x = args.get(2);
        float y = args.get(3);
        args.set(2,x - screenBorder);
        args.set(3,y - screenBorder);
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/SubtitlesHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
    public void ModifyDrawText3(Args args){
        int screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
        int x1 = args.get(1);
        int y1 = args.get(2);
        int x2 = args.get(3);
        int y2 = args.get(4);
        args.set(1,x1 - screenBorder);
        args.set(3,x2 - screenBorder);
        args.set(2,y1 - screenBorder);
        args.set(4,y2 - screenBorder);
    }
}
