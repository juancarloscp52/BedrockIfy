package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(PackScreen.class)
public class AbstractPackScreenMixin extends Screen {
    int headerLeft = 0;
    int headerWidth;
    int headerHeight;
    int headerTop = 32;
    int headerBottom;

    protected AbstractPackScreenMixin(Text title) {
        super(title);
    }

    /**
     * Render top and bottom "dirt" bars when custom rotating background is enabled.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/pack/PackScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V", ordinal = 0))
    private void renderHeaderAndBottom(CallbackInfo info) {
        if(!Bedrockify.getInstance().settings.isCubemapBackgroundEnabled())
            return;

        headerWidth = Objects.requireNonNull(client).getWindow().getScaledWidth();
        headerHeight = client.getWindow().getScaledHeight();
        headerBottom = headerHeight - 51;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0,DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(519);
        float tilingSize = 32.0F;
        // Top and bottom bars.
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(this.headerLeft, this.headerTop, -100.0D).texture(0.0F, (float) this.headerTop / tilingSize).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(this.headerLeft + this.headerWidth, this.headerTop, -100.0D).texture((float) this.headerWidth / tilingSize, (float) this.headerTop / tilingSize).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(this.headerLeft + this.headerWidth, 0.0D, -100.0D).texture((float) this.headerWidth / tilingSize, 0.0F).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(this.headerLeft, 0.0D, -100.0D).texture(0.0F, 0.0F).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(this.headerLeft, this.headerHeight, -100.0D).texture(0.0F, (float) this.headerHeight / tilingSize).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(this.headerLeft + this.headerWidth, this.headerHeight, -100.0D).texture((float) this.headerWidth / tilingSize, (float) this.headerHeight / tilingSize).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(this.headerLeft + this.headerWidth, this.headerBottom, -100.0D).texture((float) this.headerWidth / tilingSize, (float) this.headerBottom / tilingSize).color(64, 64, 64, 255).next();
        bufferBuilder.vertex(this.headerLeft, this.headerBottom, -100.0D).texture(0.0F, (float) this.headerBottom / tilingSize).color(64, 64, 64, 255).next();
        tessellator.draw();

        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // Top and bottom bar shadows.
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(this.headerLeft, this.headerTop + 4, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 0).next();
        bufferBuilder.vertex(this.headerWidth, this.headerTop + 4, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 0).next();
        bufferBuilder.vertex(this.headerWidth, this.headerTop, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(this.headerLeft, this.headerTop, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(this.headerLeft, this.headerBottom, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(this.headerWidth, this.headerBottom, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(this.headerWidth, this.headerBottom - 4, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 0).next();
        bufferBuilder.vertex(this.headerLeft, this.headerBottom - 4, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 0).next();
        tessellator.draw();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/pack/PackListWidget;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", ordinal = 0))
    private void renderDarkRectangle(PackListWidget packListWidget, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(Bedrockify.getInstance().settings.isCubemapBackgroundEnabled())
            DrawableHelper.fill(matrices, 0, this.headerTop, Objects.requireNonNull(client).getWindow().getScaledWidth(), this.headerBottom, (60 << 24));
        packListWidget.render(matrices, mouseX, mouseY, delta);
    }
}
