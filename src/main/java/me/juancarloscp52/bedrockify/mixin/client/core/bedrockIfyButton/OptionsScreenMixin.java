package me.juancarloscp52.bedrockify.mixin.client.core.bedrockIfyButton;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends ExtendScreenMixin {
    @Shadow
    private @Final HeaderAndFooterLayout layout;

    @Unique
    private Button.Builder bedrockify$settingsButtonBuilder() {
        return Button.builder(Component.translatable("bedrockify.options.settings"), button -> this.minecraft.gui.setScreen(BedrockifyClient.getInstance().settingsGUI.getConfigScreen(OptionsScreen.class.cast(this))));
    }

    /**
     * Add bedrockify settings button to the game options screen.
     */
    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 9, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void addBedrockIfyButtonInGrid(CallbackInfo ci, LinearLayout directionalLayoutWidget, LinearLayout directionalLayoutWidget2, GridLayout gridWidget, GridLayout.RowHelper adder) {
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if (settings.bedrockIfyButtonPosition == BedrockifyClientSettings.ButtonPosition.IN_GRID) {
            adder.addChild(bedrockify$settingsButtonBuilder().build());
        }
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    public void addBedrockIfyButtonBelowSliders(CallbackInfo ci, LinearLayout directionalLayoutWidget, LinearLayout directionalLayoutWidget2, GridLayout gridWidget, GridLayout.RowHelper adder) {
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if (settings.bedrockIfyButtonPosition == BedrockifyClientSettings.ButtonPosition.BELOW_SLIDERS) {
            adder.addChild(bedrockify$settingsButtonBuilder().width(310).build(), 2);
        }
    }

    @Override
    protected void bedrockify$injectAdded_AtHead(CallbackInfo ci) {
        super.bedrockify$injectAdded_AtHead(ci);

        // Hide current widgets.
        this.layout.visitChildren(element -> element.visitWidgets(widget -> widget.visible = false));

        // Execute Screen#clearWidgets and then OptionsScreen#init
        this.rebuildWidgets();

        // Add settings button.
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        Button.Builder bedrockIfyButton = bedrockify$settingsButtonBuilder().width(150);
        switch (settings.bedrockIfyButtonPosition) {
            case DISABLED:
            case IN_GRID:
            case BELOW_SLIDERS:
                break;
            case TOP_LEFT:
                bedrockIfyButton.pos(0, 0);
                this.addRenderableWidget(bedrockIfyButton.build());
                break;
            case TOP_RIGHT:
                bedrockIfyButton.pos(this.width - 150, 0);
                this.addRenderableWidget(bedrockIfyButton.build());
                break;
            case BOTTOM_LEFT:
                bedrockIfyButton.pos(0, this.height - 20);
                this.addRenderableWidget(bedrockIfyButton.build());
                break;
            case BOTTOM_RIGHT:
                bedrockIfyButton.pos(this.width - 150, this.height - 20);
                this.addRenderableWidget(bedrockIfyButton.build());
                break;
        }
    }
}
