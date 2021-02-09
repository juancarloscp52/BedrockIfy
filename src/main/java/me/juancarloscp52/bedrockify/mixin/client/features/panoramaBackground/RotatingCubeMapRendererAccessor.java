package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RotatingCubeMapRenderer.class)
public interface RotatingCubeMapRendererAccessor {
    @Accessor
    CubeMapRenderer getCubeMap();
}
