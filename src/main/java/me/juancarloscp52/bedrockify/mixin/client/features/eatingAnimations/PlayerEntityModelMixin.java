package me.juancarloscp52.bedrockify.mixin.client.features.eatingAnimations;

import me.juancarloscp52.bedrockify.Bedrockify;
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
    float prevPitch = 0;

    public PlayerEntityModelMixin(float scale) {
        super(scale);
    }

    @Inject(method = "setAngles", at=@At("TAIL"))
    private void applyEatingAnimation(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info){
        if(!Bedrockify.getInstance().settings.isEatingAnimationsEnabled())
            return;
        if(livingEntity.getMainArm() == Arm.RIGHT){
            playEatingAnimation(livingEntity, Hand.MAIN_HAND, livingEntity.getMainHandStack(), h, true);
            playEatingAnimation(livingEntity, Hand.OFF_HAND, livingEntity.getOffHandStack(), h, false);
        }else{
            playEatingAnimation(livingEntity, Hand.OFF_HAND, livingEntity.getOffHandStack(), h, true);
            playEatingAnimation(livingEntity, Hand.MAIN_HAND, livingEntity.getMainHandStack(), h, false);
        }
    }

    private void playEatingAnimation(LivingEntity livingEntity, Hand hand, ItemStack itemStack, float ticks, boolean right){
        if(isEatingWithHand(livingEntity,hand,itemStack)){
            if(livingEntity.getItemUseTime() == 1){
                prevPitch = 0;
            }
            prevPitch = MathHelper.lerp(0.15f, prevPitch, 1.5f);
            if(right){
                this.rightArm.pitch = -prevPitch + (MathHelper.cos(ticks*1.5f) *0.15f);
                this.rightArm.yaw=-0.3f;
                this.rightArm.roll = 0.32f;
                this.rightSleeve.copyPositionAndRotation(rightArm);
            }else{
                this.leftArm.pitch = -prevPitch + (MathHelper.cos(ticks*1.5f) *0.15f);
                this.leftArm.yaw=0.3f;
                this.leftArm.roll = 0.32f;
                this.leftSleeve.copyPositionAndRotation(rightArm);
            }
        }
    }

    private boolean isEatingWithHand(LivingEntity livingEntity, Hand hand, ItemStack itemStack){
        return livingEntity.getItemUseTimeLeft() >0 && livingEntity.getActiveHand()== hand && (itemStack.getUseAction() == UseAction.EAT || itemStack.getUseAction() == UseAction.DRINK);
    }
}
