package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin {
    @Shadow @Final protected MinecraftClient client;
    @Shadow protected int bottom;
    @Shadow protected int top;
    @Shadow protected abstract void renderBackground(MatrixStack matrices);

    @Redirect(method = "render", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/EntryListWidget;renderBackground(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void renderPanorama(EntryListWidget entryListWidget, MatrixStack matrices){
        if(!Bedrockify.getInstance().settings.isCubemapBackgroundEnabled() || shouldIgnoreScreen() || this.client.currentScreen.getClass().getName().equals("net.minecraft.class_5522")) {
            this.renderBackground(matrices);
            return;
        }

        if (!(this.client.currentScreen instanceof PackScreen)) {
            BedrockifyRotatingCubeMapRenderer.getInstance().render();
            DrawableHelper.fill(matrices, 0, this.top, client.getWindow().getScaledWidth(), this.bottom, (100 << 24));
        }
    }

    // Prevent the screen background from drawing
    @Redirect(method = "render", at=@At(value = "FIELD",target = "Lnet/minecraft/client/gui/widget/EntryListWidget;renderBackground:Z", opcode = Opcodes.GETFIELD))
    private boolean doNotDrawBackground(EntryListWidget entryListWidget){
        return !Bedrockify.getInstance().settings.isCubemapBackgroundEnabled() || shouldIgnoreScreen();
    }

    private boolean shouldIgnoreScreen() {
        return this.client.currentScreen.getClass().getName().contains(".modmenu.gui.ModsScreen")/* Mod Menu*/ ||
                this.client.currentScreen.getClass().getName().contains(".iris.gui.") /* Iris Shaders Compat*/ ||
                this.client.currentScreen.getClass().getName().contains(".modmanager.gui."); /* Mod Manager */
    }

    //Prevent top and bottom bars from drawing (Only on pack Screens)
    @Redirect(method = "render", at=@At(value = "FIELD",target = "Lnet/minecraft/client/gui/widget/EntryListWidget;renderHorizontalShadows:Z", opcode = Opcodes.GETFIELD))
    private boolean doNotDrawBars(EntryListWidget entryListWidget){
        return !(this.client.currentScreen instanceof PackScreen) || !Bedrockify.getInstance().settings.isCubemapBackgroundEnabled();
    }
}
