package me.juancarloscp52.bedrockify.mixin.client.core.bedrockIfyButton;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if(settings.bedrockIfyButtonPosition == BedrockifyClientSettings.ButtonPosition.IN_GRID){
            adder.add(this.createButton(Text.translatable("bedrockify.options.settings"),() -> BedrockifyClient.getInstance().settingsGUI.getConfigScreen(this,this.client.world != null)));
        }
    }

    @Redirect(method = "init", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;I)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 0))
    public <T extends Widget> T addBedrockIfyButton(GridWidget.Adder adder, T widget, int occupiedColumns){
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if(settings.bedrockIfyButtonPosition == BedrockifyClientSettings.ButtonPosition.BELOW_SLIDERS)
            return (T) adder.add(ButtonWidget.builder(Text.translatable("bedrockify.options.settings"),button -> this.client.setScreen(BedrockifyClient.getInstance().settingsGUI.getConfigScreen(this,this.client.world != null))).width(310).build(), occupiedColumns);
        return (T) adder.add(EmptyWidget.ofHeight(26), 2);
    }
    @Inject(method = "init", at = @At("RETURN"))
    public void addBedrockIfyButton(CallbackInfo ci){
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        ButtonWidget.Builder bedrockIfyButton = ButtonWidget.builder(Text.translatable("bedrockify.options.settings"), button -> this.client.setScreen(BedrockifyClient.getInstance().settingsGUI.getConfigScreen(this,this.client.world != null))).width(150);
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
