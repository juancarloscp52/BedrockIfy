package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.panoramaBackground.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TabButtonWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TabNavigationWidget.class)
public abstract class TabNavigationWidgetMixin {
    @Shadow @Final private GridWidget grid;
    @Shadow private int tabNavWidth;
    @Shadow @Final private ImmutableList<TabButtonWidget> tabButtons;
    @Shadow @Nullable protected abstract TabButtonWidget getCurrentTabButton();
    private static final Identifier HEADER_SEPARATOR_TEXTURE = new Identifier("bedrockify","textures/gui/header_separator.png");
    private static final Identifier FOOTER_SEPARATOR_TEXTURE = new Identifier("bedrockify","textures/gui/footer_separator.png");
    private static final Identifier OPTIONS_BACKGROUND_TEXTURE = new Identifier("textures/gui/options_background.png");

    @Inject(method = "render",at=@At("HEAD"),cancellable = true)
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        if(!BedrockifyClient.getInstance().settings.cubeMapBackground)
            return;

        //Panorama background
        BedrockifyRotatingCubeMapRenderer.getInstance().render(1,false);

        //Footer background
        RenderSystem.setShaderTexture(0, OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, 1.0f);
        Screen.drawTexture(matrices, 0, MinecraftClient.getInstance().getWindow().getScaledHeight()-36, 0, 0.0f, 0.0f, MinecraftClient.getInstance().getWindow().getScaledWidth(), 36, 32, 32);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        //Footer separator
        RenderSystem.setShaderTexture(0, FOOTER_SEPARATOR_TEXTURE);
        CreateWorldScreen.drawTexture(matrices, 0, MathHelper.roundUpToMultiple(MinecraftClient.getInstance().getWindow().getScaledHeight() - 36 - 2, 2), 0.0f, 0.0f, MinecraftClient.getInstance().getWindow().getScaledWidth(), 2, 32, 2);

        // Darken panorama background
        DrawableHelper.fill(matrices, 0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight()-36, (100 << 24));

        //Header backgrounds
        RenderSystem.setShaderTexture(0, OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.setShaderColor(0.25f, 0.25f, 0.25f, 1.0f);
        //Left background
        Screen.drawTexture(matrices, 0, 0, 0, 0.0f, 0.0f, this.getCurrentTabButton().getX(), 22, 32, 32);
        //right background
        Screen.drawTexture(matrices, this.getCurrentTabButton().getX()+this.getCurrentTabButton().getWidth(), 0, 0, 0.0f, 0.0f, MinecraftClient.getInstance().getWindow().getScaledWidth(), 22, 32, 32);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        RenderSystem.setShaderTexture(0,HEADER_SEPARATOR_TEXTURE);
        TabNavigationWidget.drawTexture(matrices, 0, this.grid.getY() + this.grid.getHeight() - 2, 0.0f, 0.0f, this.getCurrentTabButton().getX()+2, 2, 32, 2);
        TabNavigationWidget.drawTexture(matrices, this.getCurrentTabButton().getX()+this.getCurrentTabButton().getWidth(), this.grid.getY() + this.grid.getHeight() - 2, 0.0f, 0.0f, this.tabNavWidth, 2, 32, 2);

        //Render buttons
        for (TabButtonWidget tabButtonWidget : this.tabButtons) {
            tabButtonWidget.render(matrices, mouseX, mouseY, delta);
        }
        ci.cancel();
    }

}
