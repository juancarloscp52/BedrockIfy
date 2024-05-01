package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageScreen.class)
public class SaveLevelScreenMixin extends ExtendScreenMixin {

    /**
     * Renders the loading screen widget.
     */
    @Override
    protected void bedrockify$screenRender_AfterRenderBG(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        super.bedrockify$screenRender_AfterRenderBG(context, mouseX, mouseY, delta, ci);
        if(BedrockifyClient.getInstance().settings.isLoadingScreenEnabled()){
            LoadingScreenWidget.getInstance().render(context, MinecraftClient.getInstance().getWindow().getScaledWidth() / 2, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2, Text.literal(title.getString()), null, -1);
            ci.cancel();
        }
    }
}
