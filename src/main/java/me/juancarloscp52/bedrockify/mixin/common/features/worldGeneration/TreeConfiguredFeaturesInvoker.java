package me.juancarloscp52.bedrockify.mixin.common.features.worldGeneration;


import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TreeConfiguredFeatures.class)
public interface TreeConfiguredFeaturesInvoker {

    @Invoker("builder")
    static TreeFeatureConfig.Builder invokeBuilder(Block log, Block leaves, int baseHeight, int firstRandomHeight, int secondRandomHeight, int radius){
        throw new AssertionError();
    }

}
