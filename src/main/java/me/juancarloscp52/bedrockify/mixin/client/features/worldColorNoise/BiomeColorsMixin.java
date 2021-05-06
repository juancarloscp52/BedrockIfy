package me.juancarloscp52.bedrockify.mixin.client.features.worldColorNoise;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {

    @Inject(method = "getGrassColor", at=@At("RETURN"), cancellable = true)
    private static void getGrassColorWithNoise(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> info){
        info.setReturnValue(BedrockifyClient.getInstance().worldColorNoiseSampler.applyNoise(pos,info.getReturnValue(),7f,0.06f));
    }

    @Inject(method = "getWaterColor", at=@At("RETURN"), cancellable = true)
    private static void getWaterColorWithNoise(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> info){
        info.setReturnValue(BedrockifyClient.getInstance().worldColorNoiseSampler.applyNoise(pos,info.getReturnValue(), 13f,0.10f));
    }
}
