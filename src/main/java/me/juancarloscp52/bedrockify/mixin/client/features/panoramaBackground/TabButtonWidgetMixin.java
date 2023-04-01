package me.juancarloscp52.bedrockify.mixin.client.features.panoramaBackground;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TabButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TabButtonWidget.class)
public abstract class TabButtonWidgetMixin extends ClickableWidget {
    private static final Identifier TEXTURE = new Identifier("bedrockify","textures/gui/tab_button.png");

    public TabButtonWidgetMixin(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Shadow public abstract void drawMessage(MatrixStack matrices, TextRenderer textRenderer, int color);

    @Shadow public abstract boolean isCurrentTab();

    @Shadow protected abstract void drawCurrentTabLine(MatrixStack matrices, TextRenderer textRenderer, int color);

    @Shadow protected abstract int getTextureV();

    @Inject(method = "renderButton", at=@At("HEAD"),cancellable = true)
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(!BedrockifyClient.getInstance().settings.cubeMapBackground)
            return;
        RenderSystem.setShaderTexture(0, TEXTURE);
        TabButtonWidget.drawNineSlicedTexture(matrices, this.getX(), this.getY(), this.width, this.height, 2, 2, 2, 0, 130, 24, 0, this.getTextureV());
        if(!this.isCurrentTab()){
            TabButtonWidget.fill(matrices,this.getX()+2, this.getY()+6, this.getX()+this.width-2, this.getY()+this.height-2,(100 << 24));
        }
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int i = this.active ? -1 : -6250336;
        this.drawMessage(matrices, textRenderer, i);
        if (this.isCurrentTab()) {
            this.drawCurrentTabLine(matrices, textRenderer, i);
        }
        ci.cancel();
    }


    }
