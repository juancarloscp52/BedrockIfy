package me.juancarloscp52.bedrockify.mixin.client.features.sheepColors;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.sheepColors.SheepSkinResource;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepWoolFeatureRenderer.class)
public abstract class SheepWoolFeatureRendererMixin extends FeatureRenderer<SheepEntity, SheepEntityModel<SheepEntity>> {
    public SheepWoolFeatureRendererMixin(FeatureRendererContext<SheepEntity, SheepEntityModel<SheepEntity>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/SheepEntity;FFFFFF)V", at = @At("RETURN"))
    private void bedrockify$renderWoolColorAfterShearing(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, SheepEntity sheepEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (!sheepEntity.isSheared() || !BedrockifyClient.getInstance().settings.sheepColors || SheepSkinResource.TEXTURE_SHEARED == null) {
            return;
        }

        final Model sheepModel = this.getContextModel();
        final int color;
        if (sheepEntity.hasCustomName() && "jeb_".equals(sheepEntity.getName().getString())) {
            final int baseColorId = sheepEntity.age / 25 + sheepEntity.getId();
            final int colorLength = DyeColor.values().length;
            final int currentColorId = baseColorId % colorLength;
            final int nextColorId = (baseColorId + 1) % colorLength;
            float gradientDelta = ((float)(sheepEntity.age % 25) + tickDelta) / 25.0f;
            int currentColor = SheepEntity.getRgbColor(DyeColor.byId(currentColorId));
            int nextColor = SheepEntity.getRgbColor(DyeColor.byId(nextColorId));
            color = ColorHelper.Argb.lerp(gradientDelta, currentColor, nextColor);
        } else {
            color = SheepEntity.getRgbColor(sheepEntity.getColor());
        }
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(sheepModel.getLayer(SheepSkinResource.TEXTURE_SHEARED));
        sheepModel.render(matrixStack, vertexConsumer, light, LivingEntityRenderer.getOverlay(sheepEntity, 0.075f), color);
    }
}
