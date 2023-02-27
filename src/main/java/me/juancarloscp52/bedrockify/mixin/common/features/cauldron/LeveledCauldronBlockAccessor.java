package me.juancarloscp52.bedrockify.mixin.common.features.cauldron;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LeveledCauldronBlock.class)
public interface LeveledCauldronBlockAccessor {
    /**
     * Gets the fluid height to spawn particles.
     */
    @Invoker("getFluidHeight")
    double invokeGetFluidHeight(BlockState state);
}
