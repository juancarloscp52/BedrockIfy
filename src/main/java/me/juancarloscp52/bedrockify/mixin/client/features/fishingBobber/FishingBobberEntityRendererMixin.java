package me.juancarloscp52.bedrockify.mixin.client.features.fishingBobber;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.fishingBobber.FishingBobber3DModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntityRenderer.class)
public abstract class FishingBobberEntityRendererMixin extends EntityRenderer<FishingBobberEntity> {
    @Unique
    private final Model bobberModel = new FishingBobber3DModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(FishingBobber3DModel.MODEL_LAYER));

    protected FishingBobberEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "vertex", at = @At("HEAD"), cancellable = true)
    private static void bedrockify$cancelOriginalBobberRendering(VertexConsumer buffer, MatrixStack.Entry matrix, int light, float x, int y, int u, int v, CallbackInfo ci) {
        if (!BedrockifyClient.getInstance().settings.fishingBobber3D) {
            return;
        }

        ci.cancel();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void bedrockify$render3DBobber(FishingBobberEntity fishingBobberEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        if (!BedrockifyClient.getInstance().settings.fishingBobber3D) {
            return;
        }

        matrixStack.push();
        matrixStack.translate(0f, -0.0075f, 0f);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(FishingBobber3DModel.RENDER_LAYER);
        this.bobberModel.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        matrixStack.pop();
    }
}
