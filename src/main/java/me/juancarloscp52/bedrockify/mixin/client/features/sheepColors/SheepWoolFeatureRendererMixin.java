package me.juancarloscp52.bedrockify.mixin.client.features.sheepColors;

import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
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
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepWoolFeatureRenderer.class)
public abstract class SheepWoolFeatureRendererMixin extends FeatureRenderer<SheepEntity, SheepEntityModel<SheepEntity>> {
    @Unique
    private static final Identifier TEXTURE_SHEARED = new Identifier(Bedrockify.MOD_ID, "textures/entity/sheep_sheared.png");

    public SheepWoolFeatureRendererMixin(FeatureRendererContext<SheepEntity, SheepEntityModel<SheepEntity>> context) {
        super(context);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void bedrockify$renderWoolColorAfterShearing(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, SheepEntity sheepEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (!sheepEntity.isSheared() || !BedrockifyClient.getInstance().settings.sheepColors) {
            return;
        }

        final Model sheepModel = this.getContextModel();
        final float red;
        final float green;
        final float blue;
        if (sheepEntity.hasCustomName() && "jeb_".equals(sheepEntity.getName().getString())) {
            final int baseColorId = sheepEntity.age / 25 + sheepEntity.getId();
            final int colorLength = DyeColor.values().length;
            final int currentColorId = baseColorId % colorLength;
            final int nextColorId = (baseColorId + 1) % colorLength;
            final float gradientDelta = ((float) (sheepEntity.age % 25) + tickDelta) / 25f;
            final float[] currentColor = SheepEntity.getRgbColor(DyeColor.byId(currentColorId));
            final float[] nextColor = SheepEntity.getRgbColor(DyeColor.byId(nextColorId));
            red = currentColor[0] * (1.0F - gradientDelta) + nextColor[0] * gradientDelta;
            green = currentColor[1] * (1.0F - gradientDelta) + nextColor[1] * gradientDelta;
            blue = currentColor[2] * (1.0F - gradientDelta) + nextColor[2] * gradientDelta;
        } else {
            final float[] color = SheepEntity.getRgbColor(sheepEntity.getColor());
            red = color[0];
            green = color[1];
            blue = color[2];
        }

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(sheepModel.getLayer(TEXTURE_SHEARED));
        sheepModel.render(matrixStack, vertexConsumer, light, LivingEntityRenderer.getOverlay(sheepEntity, 0.075f), red, green, blue, 1f);
    }
}
