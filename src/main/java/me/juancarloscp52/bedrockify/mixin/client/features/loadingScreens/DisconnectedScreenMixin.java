package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen {

    @Shadow @Final private Text reason;
    @Shadow @Final private Screen parent;

    protected DisconnectedScreenMixin(Text title) {
        super(title);
    }

    /**
     * Move the Back to Menu button down.
     */
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/DisconnectedScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    public <T extends Element & Drawable & Selectable> T addDrawableChild(DisconnectedScreen disconnectedScreen, T drawableElement) {
        if(Bedrockify.getInstance().settings.isLoadingScreenEnabled()){
            return (T) this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, (int) Math.ceil(MinecraftClient.getInstance().getWindow().getScaledHeight() * 0.75D), 200, 20, Text.translatable("gui.toMenu"),
                    (buttonWidget) -> this.client.setScreen(this.parent)));
        }else{
            return this.addDrawableChild(drawableElement);
        }

    }

    /**
     * Renders the loading screen widget.
     */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if(!Bedrockify.getInstance().settings.isLoadingScreenEnabled())
            return;
        this.renderBackground(matrices);
        LoadingScreenWidget.getInstance().render(matrices, width / 2, height / 2, this.title, this.reason, -1);
        super.render(matrices, mouseX, mouseY, delta);
        info.cancel();
    }
}
