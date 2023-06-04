package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.hud.SubtitlesHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SubtitlesHud.class)
/*
 * Applies the screen border distance to the subtitles widget.
 */
public class SubtitlesHudMixin {
    @ModifyArgs(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"))
    private void modifyDrawText(Args args){
        int screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
        int x = args.get(3);
        int y = args.get(4);
        args.set(3,x - screenBorder);
        args.set(4,y - screenBorder);
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"))
    public void modifyDrawText2(Args args){
        int screenBorder = BedrockifyClient.getInstance().settings.getScreenSafeArea();
        int x = args.get(3);
        int y = args.get(4);
        args.set(3,x - screenBorder);
        args.set(4,y - screenBorder);
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
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
