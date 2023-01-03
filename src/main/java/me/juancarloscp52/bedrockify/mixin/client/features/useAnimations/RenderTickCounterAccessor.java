package me.juancarloscp52.bedrockify.mixin.client.features.useAnimations;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(RenderTickCounter.class)
public interface RenderTickCounterAccessor {
    @Accessor("tickTime")
    float getTickTimeField();
}
