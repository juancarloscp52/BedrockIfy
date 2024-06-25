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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
        final Arm mainArm = livingEntity.getMainArm();
        if (isEatingWithHand(livingEntity, Hand.MAIN_HAND, livingEntity.getMainHandStack())) {
            playEatingAnimation(livingEntity, Hand.MAIN_HAND, h, mainArm);
        } else if (isEatingWithHand(livingEntity, Hand.OFF_HAND, livingEntity.getOffHandStack())) {
            playEatingAnimation(livingEntity, Hand.OFF_HAND, h, mainArm.getOpposite());
        }
    }

    @Unique
    private static final float ITEM_START_TIME = 8f/20f; //in second
    @Unique
    private static final float ITEM_INTERVAL_TIME = 4f/20f; //in second

    @Unique
    private void playEatingAnimation(LivingEntity livingEntity, Hand hand, float ticks, Arm targetArm) {
        float smoothingTicks = false ? (float) (ticks - Math.floor(ticks)) : 0; //if you want to add an option for spothing the anim, it's already here, just replace the false
//        float itemStartProgress = Math.min(livingEntity.getItemUseTime() + smoothingTicks, 20f* ITEM_START_TIME)/20f/ ITEM_START_TIME;
        float itemStartProgress = Math.min(livingEntity.getItemUseTime() + smoothingTicks, 20f * ITEM_START_TIME) * 0.05f / ITEM_START_TIME;
        float itemIntervalProgress = (livingEntity.getItemUseTime()/20f < ITEM_START_TIME) ? 0.0f : (((livingEntity.getItemUseTime() - (int) ITEM_START_TIME *20) % (int) (ITEM_INTERVAL_TIME *20)) + smoothingTicks)* ITEM_INTERVAL_TIME;
        float animPitch = itemStartProgress * -degToMatAngle(60.0f) + itemIntervalProgress * degToMatAngle(11.25f);
        float animYaw = itemStartProgress * -degToMatAngle(22.5f) + itemIntervalProgress * degToMatAngle(11.25f);
        float animRoll = itemStartProgress * -degToMatAngle(5.625f) + itemIntervalProgress * degToMatAngle(11.25f);

        if (targetArm == Arm.RIGHT) {
            this.rightArm.pitch += animPitch;
            this.rightArm.yaw += animYaw;
            this.rightArm.roll += animRoll;
            this.rightSleeve.copyTransform(rightArm);
        } else {
            this.leftArm.pitch += animPitch;
            this.leftArm.yaw -= animYaw;
            this.leftArm.roll += animRoll;
            this.leftSleeve.copyTransform(leftArm);
        }
    }

    @Unique
    private float degToMatAngle(float angle)
    {
//        return 7.07f * angle / 360;
        return 7.07f * angle * 0.002777778f;
    }

    @Unique
    private boolean isEatingWithHand(LivingEntity livingEntity, Hand hand, ItemStack itemStack){
        return livingEntity.getItemUseTimeLeft() >0 && livingEntity.getActiveHand()== hand && (itemStack.getUseAction() == UseAction.EAT || itemStack.getUseAction() == UseAction.DRINK);
    }
}
