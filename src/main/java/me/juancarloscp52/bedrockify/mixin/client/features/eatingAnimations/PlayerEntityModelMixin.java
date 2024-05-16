package me.juancarloscp52.bedrockify.mixin.client.features.eatingAnimations;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin <T extends LivingEntity> extends BipedEntityModel<T> {

    @Shadow @Final public ModelPart rightSleeve;

    @Shadow @Final public ModelPart leftSleeve;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setAngles", at=@At("TAIL"))
    private void applyEatingAnimation(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info){
        if(!BedrockifyClient.getInstance().settings.isEatingAnimationsEnabled())
            return;
        if(livingEntity.getMainArm() == Arm.RIGHT){
            playEatingAnimation(livingEntity, Hand.MAIN_HAND, livingEntity.getMainHandStack(), h, true);
            playEatingAnimation(livingEntity, Hand.OFF_HAND, livingEntity.getOffHandStack(), h, false);
        }else{
            playEatingAnimation(livingEntity, Hand.OFF_HAND, livingEntity.getOffHandStack(), h, true);
            playEatingAnimation(livingEntity, Hand.MAIN_HAND, livingEntity.getMainHandStack(), h, false);
        }
    }

    final float itemStartTime = 8f/20f; //in second
    final float itemIntervalTime = 4f/20f; //in second

    private void playEatingAnimation(LivingEntity livingEntity, Hand hand, ItemStack itemStack, float ticks, boolean right){
        if(!isEatingWithHand(livingEntity,hand,itemStack)){
            return;
        }
        float smoothingTicks = false ? (float) (ticks - Math.floor(ticks)) : 0; //if you want to add an option for spothing the anim, it's already here, just replace the false
        float itemStartProgress = Math.min(livingEntity.getItemUseTime() + smoothingTicks, 20f*itemStartTime)/20f/itemStartTime;
        float itemIntervalProgress = (livingEntity.getItemUseTime()/20f < itemStartTime) ? 0.0f : (((livingEntity.getItemUseTime() - (int) itemStartTime*20) % (int) (itemIntervalTime*20)) + smoothingTicks)*itemIntervalTime;
        float animPitch = itemStartProgress * -degToMatAngle(60.0f) + itemIntervalProgress * degToMatAngle(11.25f);
        float animYaw = itemStartProgress * -degToMatAngle(22.5f) + itemIntervalProgress * degToMatAngle(11.25f);
        float animRoll = itemStartProgress * -degToMatAngle(5.625f) + itemIntervalProgress * degToMatAngle(11.25f);

        if(right){
            this.rightArm.pitch += animPitch;
            this.rightArm.yaw += animYaw;
            this.rightArm.roll += animRoll;
            this.rightSleeve.copyTransform(rightArm);
        }
        else {
            this.leftArm.pitch += animPitch;
            this.leftArm.yaw -= animYaw;
            this.leftArm.roll += animRoll;
            this.leftSleeve.copyTransform(leftArm);
        }
    }

    private float degToMatAngle(float angle)
    {
        return (float) 7.07*angle/360;
    }

    private boolean isEatingWithHand(LivingEntity livingEntity, Hand hand, ItemStack itemStack){
        return livingEntity.getItemUseTimeLeft() >0 && livingEntity.getActiveHand()== hand && (itemStack.getUseAction() == UseAction.EAT || itemStack.getUseAction() == UseAction.DRINK);
    }
}
