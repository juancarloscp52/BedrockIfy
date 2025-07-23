package me.juancarloscp52.bedrockify.mixin.client.features.hudOpacity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V",
            at = @At("HEAD"),
            argsOnly = true)
    private static RenderLayer bedrockify$applyTranslucencyForBakedItemModel(RenderLayer original) {
        return TexturedRenderLayers.getItemEntityTranslucentCull(); //TODO: ADD EXCEPTION FOR VISUAL OVERHAUL MOD ITEMS OR ENTIRELY
    }
}
