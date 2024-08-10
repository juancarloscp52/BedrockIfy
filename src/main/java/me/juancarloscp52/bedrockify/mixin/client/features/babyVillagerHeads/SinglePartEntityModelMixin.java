package me.juancarloscp52.bedrockify.mixin.client.features.babyVillagerHeads;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SinglePartEntityModel.class)
public abstract class SinglePartEntityModelMixin <E extends Entity> extends EntityModel<E> {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    protected void injectCustomBabyRender(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo ci){
        //Empty injection to be modified in child mixin (see https://www.fabricmc.net/wiki/tutorial:mixinheritance)
    }
}
