package me.juancarloscp52.bedrockify.mixin.client.core.bedrockIfyButton;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    /**
     * Add bedrockify settings button to the game options screen.
     */
    @Inject(method = "init", at = @At("HEAD"))
    private void addBedrockifyOption(CallbackInfo info) {
        if(BedrockifyClient.getInstance().settings.isBedrockIfyButtonEnabled()){
            int width = 310;
            if(FabricLoader.getInstance().isModLoaded("essential") || FabricLoader.getInstance().isModLoaded("essential-container") || FabricLoader.getInstance().isModLoaded("essential-loader"))
                width = 150;
            this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 144 - 6, width, 20, Text.translatable("bedrockify.options.settings"), (buttonWidget) -> this.client.setScreen(BedrockifyClient.getInstance().settingsGUI.getConfigScreen(this,this.client.world != null))));
        }
    }
}
