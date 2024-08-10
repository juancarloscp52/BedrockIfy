package me.juancarloscp52.bedrockify.mixin.client.features.babyVillagerHeads;

import com.google.common.collect.ImmutableList;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerResemblingModel.class)
public abstract class VillagerResemblingModelMixin<T extends Entity> extends SinglePartEntityModelMixin<T> {

    @Shadow @Final private ModelPart rightLeg;
    @Shadow @Final private ModelPart leftLeg;
    @Shadow @Final private ModelPart head;
    @Unique
    public ModelPart body;
    @Unique
    public ModelPart arms;

    @Inject(method = "<init>", at=@At("RETURN"))
    private void ctr(ModelPart root, CallbackInfo ci){
        this.body = root.getChild(EntityModelPartNames.BODY);
        this.arms = root.getChild(EntityModelPartNames.ARMS);
    }

    //Override parent injection with baby villager renderer. For more information see: https://www.fabricmc.net/wiki/tutorial:mixinheritance
    @Override
    protected void injectCustomBabyRender(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo ci) {
        if(this.child && BedrockifyClient.getInstance().settings.babyVillagerHeads){
            // Render scaled head.
            float scale = 1.5f;
            matrices.push();
            matrices.scale(scale,scale,scale);
            this.getHeadParts().forEach(modelPart -> modelPart.render(matrices,vertices,light,overlay,color));
            matrices.pop();

            // Render rest of the body.
            this.getBodyParts().forEach(modelPart -> modelPart.render(matrices,vertices,light,overlay,color));
            ci.cancel();
        }
    }

    @Unique
    public Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Unique
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.arms);
    }
}
