package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DisconnectedScreen.class)
public abstract class DisconnectedScreenMixin extends ExtendScreenMixin {

    @Shadow @Final private Component buttonText;
    @Shadow @Final private Screen parent;
    @Shadow @Final private static Component TO_TITLE;
    @Shadow @Final private DisconnectionDetails details;

    /**
     * Move the Back to Menu button down.
     */
    @Inject(method = "init", at=@At("HEAD"), cancellable = true)
    public void init(CallbackInfo ci){
        if(BedrockifyClient.getInstance().settings.isLoadingScreenEnabled()){
            Button.Builder buttonWidget = this.minecraft.allowsMultiplayer() ? Button.builder(this.buttonText, button -> this.minecraft.gui.setScreen(this.parent)) : Button.builder(TO_TITLE, button -> this.minecraft.gui.setScreen(new TitleScreen()));
            this.addRenderableWidget(buttonWidget.pos(this.width / 2 - 100, (int) Math.ceil(Minecraft.getInstance().getWindow().getGuiScaledHeight() * 0.75D)).width(200).build());
            ci.cancel();
        }
    }
    /**
     * Renders the loading screen widget.
     */
    @Override
    public void bedrockify$screenRender_AtHead(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float alpha, CallbackInfo ci) {
        super.bedrockify$screenRender_AtHead(drawContext, mouseX, mouseY, alpha, ci);
        if(!BedrockifyClient.getInstance().settings.isLoadingScreenEnabled())
            return;
        LoadingScreenWidget.getInstance().render(drawContext, width / 2, height / 2, this.title, this.details.reason(), -1);
    }
}
