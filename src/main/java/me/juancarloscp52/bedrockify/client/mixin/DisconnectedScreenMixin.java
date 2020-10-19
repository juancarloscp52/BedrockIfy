package me.juancarloscp52.bedrockify.client.mixin;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.gui.LoadingScreenWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/DisconnectedScreen;addButton(Lnet/minecraft/client/gui/widget/AbstractButtonWidget;)Lnet/minecraft/client/gui/widget/AbstractButtonWidget;"))
    public AbstractButtonWidget addButton(DisconnectedScreen screen, AbstractButtonWidget abstractButtonWidget) {
        if(Bedrockify.getInstance().settings.isLoadingScreenEnabled()){
            return this.addButton(new ButtonWidget(this.width / 2 - 100, (int) Math.ceil(MinecraftClient.getInstance().getWindow().getScaledHeight() * 0.75D), 200, 20, new TranslatableText("gui.toMenu"), (buttonWidget) -> {
                this.client.openScreen(this.parent);
            }));
        }else{
            return this.addButton(abstractButtonWidget);
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
