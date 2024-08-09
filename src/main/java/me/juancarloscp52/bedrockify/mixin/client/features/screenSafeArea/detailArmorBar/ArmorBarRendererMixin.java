package me.juancarloscp52.bedrockify.mixin.client.features.screenSafeArea.detailArmorBar;

import com.redlimerl.detailab.render.ArmorBarRenderer;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ArmorBarRenderer.class)
public class ArmorBarRendererMixin {

    @ModifyVariable(method = "render", at = @At("STORE"),name = "yPos")
    private int modifyYPos(int value){
        return value - BedrockifyClient.getInstance().settings.getScreenSafeArea();
    }

}