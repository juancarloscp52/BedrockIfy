package me.juancarloscp52.bedrockify.mixin.client.core.bedrockIfyButton;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    @Shadow protected abstract ButtonWidget createButton(Text message, Supplier<Screen> screenSupplier);

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    /**
     * Add bedrockify settings button to the game options screen.
     */
    @Inject(method = "init", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 10,shift = At.Shift.AFTER),locals = LocalCapture.CAPTURE_FAILHARD)
    public void addBedrockIfyButton(CallbackInfo ci, GridWidget gridWidget, GridWidget.Adder adder){
        if(BedrockifyClient.getInstance().settings.isBedrockIfyButtonEnabled()){
            adder.add(this.createButton(Text.translatable("bedrockify.options.settings"),() -> BedrockifyClient.getInstance().settingsGUI.getConfigScreen(this,this.client.world != null)));
        }
    }
}
