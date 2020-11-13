package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProgressScreen.class)
public class ProgressScreenMixin extends Screen {


    @Shadow private Text task;
    @Shadow private int progress;
    protected ProgressScreenMixin(Text title) {
        super(title);
    }

    /**
     * Renders the loading screen widgets with progress bar if necessary.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ProgressScreen;renderBackground(Lnet/minecraft/client/util/math/MatrixStack;)V"), cancellable = true)
    public void renderLoadScreen(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if(!Bedrockify.getInstance().settings.isLoadingScreenEnabled()){
            return;
        }
        this.renderBackground(matrices);
        if (title != null) {
            if (this.task != null && this.progress != 0) {
                LoadingScreenWidget.getInstance().render(matrices, client.getWindow().getScaledWidth() / 2, client.getWindow().getScaledHeight() / 2, this.title, this.task, this.progress);
            } else {
                LoadingScreenWidget.getInstance().render(matrices, client.getWindow().getScaledWidth() / 2, client.getWindow().getScaledHeight() / 2, this.title, null, -1);
            }
        } else if (this.task != null && this.progress != 0) {
            LoadingScreenWidget.getInstance().render(matrices, client.getWindow().getScaledWidth() / 2, client.getWindow().getScaledHeight() / 2, this.task, null, this.progress);
        } else {
            LoadingScreenWidget.getInstance().render(matrices, client.getWindow().getScaledWidth() / 2, client.getWindow().getScaledHeight() / 2, new LiteralText(""), null, -1);
        }

        super.render(matrices, mouseX, mouseY, delta);
        info.cancel();
    }

}
