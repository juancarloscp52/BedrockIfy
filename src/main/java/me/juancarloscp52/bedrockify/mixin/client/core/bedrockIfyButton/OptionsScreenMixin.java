package me.juancarloscp52.bedrockify.mixin.client.core.bedrockIfyButton;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {


    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private ButtonWidget.Builder bedrockify$settingsButtonBuilder() {
        return ButtonWidget.builder(Text.translatable("bedrockify.options.settings"),button -> this.client.setScreen(BedrockifyClient.getInstance().settingsGUI.getConfigScreen(this,this.client.world != null)));
    }

    /**
     * Add bedrockify settings button to the game options screen.
     */
    @Inject(method = "init", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 9,shift = At.Shift.AFTER),locals = LocalCapture.CAPTURE_FAILHARD)
    public void addBedrockIfyButtonInGrid(CallbackInfo ci, DirectionalLayoutWidget directionalLayoutWidget, DirectionalLayoutWidget directionalLayoutWidget2, GridWidget gridWidget, GridWidget.Adder adder){
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if(settings.bedrockIfyButtonPosition == BedrockifyClientSettings.ButtonPosition.IN_GRID){
            adder.add(bedrockify$settingsButtonBuilder().build());
        }
    }

    @Inject(method = "init", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    public void addBedrockIfyButtonBelowSliders(CallbackInfo ci, DirectionalLayoutWidget directionalLayoutWidget, DirectionalLayoutWidget directionalLayoutWidget2, GridWidget gridWidget, GridWidget.Adder adder) {
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if(settings.bedrockIfyButtonPosition == BedrockifyClientSettings.ButtonPosition.BELOW_SLIDERS) {
            adder.add(bedrockify$settingsButtonBuilder().width(310).build(), 2);
        }
    }
    @Inject(method = "init", at = @At("RETURN"))
    public void addBedrockIfyButton(CallbackInfo ci){
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        ButtonWidget.Builder bedrockIfyButton = bedrockify$settingsButtonBuilder().width(150);
        switch (settings.bedrockIfyButtonPosition){
            case DISABLED:
            case IN_GRID:
            case BELOW_SLIDERS: break;
            case TOP_LEFT:
                bedrockIfyButton.position(0,0);
                this.addDrawableChild(bedrockIfyButton.build());
                break;
            case TOP_RIGHT:
                bedrockIfyButton.position(this.width-150,0);
                this.addDrawableChild(bedrockIfyButton.build());
                break;
            case BOTTOM_LEFT:
                bedrockIfyButton.position(0,this.height-20);
                this.addDrawableChild(bedrockIfyButton.build());
                break;
            case BOTTOM_RIGHT:
                bedrockIfyButton.position(this.width-150,this.height-20);
                this.addDrawableChild(bedrockIfyButton.build());
                break;
        }
    }
}
