package me.juancarloscp52.bedrockify.client.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.gui.BedrockifyRotatingCubeMapRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin {
    @Shadow protected int left;
    @Shadow protected int right;
    @Shadow @Final protected MinecraftClient client;
    @Shadow protected int bottom;
    @Shadow protected int top;
    @Shadow protected int width;
    @Shadow protected int height;
    @Shadow private boolean renderHeader;
    @Shadow public abstract double getScrollAmount();
    @Shadow protected abstract int getScrollbarPositionX();
    @Shadow protected abstract int getRowLeft();
    @Shadow protected abstract void renderList(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta);
    @Shadow protected abstract int getMaxScroll();
    @Shadow protected abstract int getMaxPosition();
    @Shadow protected abstract void renderDecorations(MatrixStack matrixStack, int i, int j);
    @Shadow protected abstract void renderHeader(MatrixStack matrices, int x, int y, Tessellator tessellator);

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if(!Bedrockify.getInstance().settings.isCubemapBackgroundEnabled())
            return;

        if (!(this.client.currentScreen instanceof PackScreen)) {
            BedrockifyRotatingCubeMapRenderer.getInstance().render();
            DrawableHelper.fill(matrices, 0, this.top, client.getWindow().getScaledWidth(), this.bottom, (60 << 24));
        }

        int scrollbarPositionX = this.getScrollbarPositionX();
        int scrollbarWidth = scrollbarPositionX + 6;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.getRowLeft();
        int l = this.top + 4 - (int) this.getScrollAmount();
        if (this.renderHeader) {
            this.renderHeader(matrices, k, l, tessellator);
        }

        this.renderList(matrices, k, l, mouseX, mouseY, delta);

        if (!(this.client.currentScreen instanceof PackScreen)) {
            this.client.getTextureManager().bindTexture(DrawableHelper.BACKGROUND_TEXTURE);
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(519);
            float tilingSize = 32.0F;
            // Top and bottom bars
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(this.left, this.top, -100.0D).texture(0.0F, (float) this.top / tilingSize).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left + this.width, this.top, -100.0D).texture((float) this.width / tilingSize, (float) this.top / tilingSize).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left + this.width, 0.0D, -100.0D).texture((float) this.width / tilingSize, 0.0F).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left, 0.0D, -100.0D).texture(0.0F, 0.0F).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left, this.height, -100.0D).texture(0.0F, (float) this.height / tilingSize).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left + this.width, this.height, -100.0D).texture((float) this.width / tilingSize, (float) this.height / tilingSize).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left + this.width, this.bottom, -100.0D).texture((float) this.width / tilingSize, (float) this.bottom / tilingSize).color(64, 64, 64, 255).next();
            bufferBuilder.vertex(this.left, this.bottom, -100.0D).texture(0.0F, (float) this.bottom / tilingSize).color(64, 64, 64, 255).next();
            tessellator.draw();

            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
            RenderSystem.disableAlphaTest();
            RenderSystem.shadeModel(7425);
            RenderSystem.disableTexture();

            // Top and bottom bars shadows
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(this.left, this.top + 4, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 0).next();
            bufferBuilder.vertex(this.right, this.top + 4, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 0).next();
            bufferBuilder.vertex(this.right, this.top, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.left, this.top, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.left, this.bottom, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.right, this.bottom, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.right, this.bottom - 4, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 0).next();
            bufferBuilder.vertex(this.left, this.bottom - 4, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 0).next();
            tessellator.draw();
        }
        int o = this.getMaxScroll();
        if (o > 0) {
            int p = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getMaxPosition());
            p = MathHelper.clamp(p, 32, this.bottom - this.top - 8);
            int q = (int) this.getScrollAmount() * (this.bottom - this.top - p) / o + this.top;
            if (q < this.top) {
                q = this.top;
            }
            //render scrollbar
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(scrollbarPositionX, this.bottom, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 160).next();
            bufferBuilder.vertex(scrollbarWidth, this.bottom, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 160).next();
            bufferBuilder.vertex(scrollbarWidth, this.top, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 160).next();
            bufferBuilder.vertex(scrollbarPositionX, this.top, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 160).next();
            bufferBuilder.vertex(scrollbarPositionX, q + p, 0.0D).texture(0.0F, 1.0F).color(128, 128, 128, 200).next();
            bufferBuilder.vertex(scrollbarWidth, q + p, 0.0D).texture(1.0F, 1.0F).color(128, 128, 128, 200).next();
            bufferBuilder.vertex(scrollbarWidth, q, 0.0D).texture(1.0F, 0.0F).color(128, 128, 128, 200).next();
            bufferBuilder.vertex(scrollbarPositionX, q, 0.0D).texture(0.0F, 0.0F).color(128, 128, 128, 200).next();
            bufferBuilder.vertex(scrollbarPositionX, q + p - 1, 0.0D).texture(0.0F, 1.0F).color(192, 192, 192, 200).next();
            bufferBuilder.vertex(scrollbarWidth - 1, q + p - 1, 0.0D).texture(1.0F, 1.0F).color(192, 192, 192, 200).next();
            bufferBuilder.vertex(scrollbarWidth - 1, q, 0.0D).texture(1.0F, 0.0F).color(192, 192, 192, 200).next();
            bufferBuilder.vertex(scrollbarPositionX, q, 0.0D).texture(0.0F, 0.0F).color(192, 192, 192, 200).next();
            tessellator.draw();
        }

        this.renderDecorations(matrices, mouseX, mouseY);
        RenderSystem.enableTexture();
        RenderSystem.shadeModel(7424);
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();

        info.cancel();
    }
}
