package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.ClientConnection;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin extends Screen {

    @Shadow ClientConnection connection;
    @Shadow boolean connectingCancelled;
    @Shadow @Final Screen parent;

    protected ConnectScreenMixin(Text title) {
        super(title);
    }

    /**
     * Draws the loading screen widget.
     */
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"))
    public void drawLoadingScreenWidget(DrawContext instance, TextRenderer textRenderer, Text text, int centerX, int y, int color, Operation<Void> original) {
        if(BedrockifyClient.getInstance().settings.isLoadingScreenEnabled()){
            LoadingScreenWidget.getInstance().render(instance, MinecraftClient.getInstance().getWindow().getScaledWidth() / 2, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2, Text.literal(text.getString()), null, -1);
        }else{
            original.call(instance, textRenderer, text, centerX, y, color);
        }
    }

    /**
     * Move the cancel bottom down.
     */
    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ConnectScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    public <T extends Element & Drawable & Selectable> T addDrawableChild(ConnectScreen connectScreen, T drawableElement, Operation<T> original) {
        if(BedrockifyClient.getInstance().settings.isLoadingScreenEnabled()){
            return original.call(connectScreen, ButtonWidget.builder(ScreenTexts.CANCEL, (buttonWidget) -> {
                this.connectingCancelled = true;
                if (this.connection != null) {
                    this.connection.disconnect(Text.translatable("connect.aborted"));
                }
                this.client.setScreen(this.parent);
            }).position(this.width / 2 - 100, (int) Math.ceil(MinecraftClient.getInstance().getWindow().getScaledHeight() * 0.75D)).width(200).build());
        }else{
            return original.call(connectScreen, drawableElement);
        }
    }
}
