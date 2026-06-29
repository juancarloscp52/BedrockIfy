package me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens.fancyMenu;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.juancarloscp52.bedrockify.mixin.client.features.loadingScreens.ExtendScreenMixin;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ConnectScreen.class)
public abstract class ConnectScreenMixin extends ExtendScreenMixin {
    /**
     * Removes widgets that used by FancyMenu within the {@link Screen#extractRenderState} method.
     */
    @WrapMethod(method = "init")
    private void bedrockify$ctor_compatFancyMenu(Operation<Void> original) {
        original.call();
        final List<Renderable> widgets = this.bedrockify$access_getRenderables();
        widgets.removeIf(widget -> widget.getClass().getCanonicalName().equals("de.keksuccino.fancymenu.util.rendering.ui.widget.TextWidget"));
    }
}
