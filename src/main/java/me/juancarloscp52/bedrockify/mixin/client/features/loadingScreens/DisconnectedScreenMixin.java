package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen {

    @Shadow @Final private Text buttonLabel;
    @Shadow @Final private Screen parent;
    @Shadow @Final private static Text TO_TITLE_TEXT;
    @Shadow @Final private Text reason;


    protected DisconnectedScreenMixin(Text title) {
        super(title);
    }

    /**
     * Move the Back to Menu button down.
     */
    @Inject(method = "init", at=@At("HEAD"), cancellable = true)
    public void init(CallbackInfo ci){
        if(BedrockifyClient.getInstance().settings.isLoadingScreenEnabled()){
            ButtonWidget.Builder buttonWidget = this.client.isMultiplayerEnabled() ? ButtonWidget.builder(this.buttonLabel, button -> this.client.setScreen(this.parent)) : ButtonWidget.builder(TO_TITLE_TEXT, button -> this.client.setScreen(new TitleScreen()));
            this.addDrawableChild(buttonWidget.position(this.width / 2 - 100, (int) Math.ceil(MinecraftClient.getInstance().getWindow().getScaledHeight() * 0.75D)).width(200).build());
            ci.cancel();
        }
    }
    /**
     * Renders the loading screen widget.
     */
    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
        if(!BedrockifyClient.getInstance().settings.isLoadingScreenEnabled())
            return;
        LoadingScreenWidget.getInstance().render(drawContext, width / 2, height / 2, this.title, this.reason, -1);
    }
}
