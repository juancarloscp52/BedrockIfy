package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin extends Screen {

    @Shadow
    Connection connection;
    @Shadow boolean aborted;
    @Shadow @Final
    Screen parent;

    protected ConnectScreenMixin(Component title) {
        super(title);
    }

    /**
     * Draws the loading screen widget.
     */
    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;centeredText(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"))
    public void drawLoadingScreenWidget(GuiGraphicsExtractor instance, Font textRenderer, Component text, int x, int y, int color, Operation<Void> original) {
        if(BedrockifyClient.getInstance().settings.isLoadingScreenEnabled()){
            LoadingScreenWidget.getInstance().render(instance, Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2, Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2, Component.literal(text.getString()), null, -1);
        }else{
            original.call(instance, textRenderer, text, x, y, color);
        }
    }

    /**
     * Move the cancel bottom down.
     */
    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/ConnectScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addDrawableChild(ConnectScreen instance, T drawableElement, Operation<T> original) {
        if(BedrockifyClient.getInstance().settings.isLoadingScreenEnabled()){
            return (T) this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (buttonWidget) -> {
                this.aborted = true;
                if (this.connection != null) {
                    this.connection.disconnect(Component.translatable("connect.aborted"));
                }
                this.minecraft.gui.setScreen(this.parent);
            }).pos(this.width / 2 - 100, (int) Math.ceil(Minecraft.getInstance().getWindow().getGuiScaledHeight() * 0.75D)).width(200).build());
        } else {
            return original.call(instance, drawableElement);
        }
    }
}
