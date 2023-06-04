package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.ExperimentsScreen;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperimentsScreen.class)
public class ExperimentsScreenMixin extends Screen {

    @Shadow @Final private ThreePartsLayoutWidget experimentToggleList;

    protected ExperimentsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"),cancellable = true)
    public void render (DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci){
        // Background
        this.renderBackground(drawContext);
        drawContext.fill(0, 0, this.width, this.height, (100 << 24));

        //Header and footer
        RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, 1.0f);
        drawContext.drawTexture(OPTIONS_BACKGROUND_TEXTURE, 0, 0, 0, 0.0f, 0.0f, this.width, this.experimentToggleList.getHeaderHeight(), 32, 32);
        drawContext.drawTexture(OPTIONS_BACKGROUND_TEXTURE, 0, this.height-this.experimentToggleList.getFooterHeight(), 0, 0.0f, 0.0f, this.width, this.height, 32, 32);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        //Header and footer shadows.
        drawContext.fillGradient(0, this.experimentToggleList.getHeaderHeight(), this.width, this.experimentToggleList.getHeaderHeight() + 4, -16777216, 0);
        drawContext.fillGradient(0, this.height-this.experimentToggleList.getFooterHeight() - 4, this.width, this.height-this.experimentToggleList.getFooterHeight(), 0, -16777216);

        super.render(drawContext, mouseX, mouseY, delta);

        ci.cancel();
    }

}
