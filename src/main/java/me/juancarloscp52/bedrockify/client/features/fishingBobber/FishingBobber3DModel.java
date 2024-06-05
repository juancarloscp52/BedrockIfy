package me.juancarloscp52.bedrockify.client.features.fishingBobber;

import me.juancarloscp52.bedrockify.Bedrockify;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FishingBobber3DModel<T extends FishingBobberEntity> extends EntityModel<T> {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Identifier.of(Bedrockify.MOD_ID, "fishing_hook"), "main");
    public static final Identifier TEXTURE = Identifier.of(Bedrockify.MOD_ID, "textures/entity/fishing_hook.png");
    public static final RenderLayer RENDER_LAYER = RenderLayer.getEntityTranslucent(TEXTURE);

    private static final String NAME_HEAD_X = "head_axis_x";
    private static final String NAME_HEAD_Z = "head_axis_z";
    private static final String NAME_BOBBER = "bobber";
    private static final String NAME_HOOK = "hook";
    private static final float ANGLE_180_DEGREES = (float) (1f * Math.PI);

    private final List<ModelPart> black = new ArrayList<>();
    private final List<ModelPart> textured = new ArrayList<>();

    public FishingBobber3DModel(@NotNull ModelPart root) {
        this.black.add(root.getChild(NAME_HEAD_X));
        this.black.add(root.getChild(NAME_HEAD_Z));
        this.textured.add(root.getChild(NAME_BOBBER));
        this.textured.add(root.getChild(NAME_HOOK));
    }

    @NotNull
    public static TexturedModelData generateModel() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(NAME_HEAD_X, ModelPartBuilder.create().cuboid(-0.5f, 3f, 0f, 1f, 1f, 0f), ModelTransform.NONE);
        modelPartData.addChild(NAME_HEAD_Z, ModelPartBuilder.create().cuboid(0f, 3f, -0.5f, 0f, 1f, 1f), ModelTransform.NONE);
        modelPartData.addChild(NAME_BOBBER, ModelPartBuilder.create().uv(0, 0).cuboid(-1.5f, 0f, -1.5f, 3f, 3f, 3f), ModelTransform.of(0f, 3f, 0f, ANGLE_180_DEGREES, 0f, 0f));
        modelPartData.addChild(NAME_HOOK, ModelPartBuilder.create().uv(0, 6).cuboid(-0.5f, -3f, 0f, 3f, 3f, 0f), ModelTransform.of(0f, -3f, 0f, ANGLE_180_DEGREES, 0f, 0f));

        return TexturedModelData.of(modelData, 12, 9);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.black.forEach(modelPart -> modelPart.render(matrices, vertices, light, overlay, ColorHelper.Argb.getArgb(ColorHelper.Argb.getAlpha(color),0,0,0)));
        this.textured.forEach(modelPart -> modelPart.render(matrices, vertices, light, overlay, color));
    }
}
