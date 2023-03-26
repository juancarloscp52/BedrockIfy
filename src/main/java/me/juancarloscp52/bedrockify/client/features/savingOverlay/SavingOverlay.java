package me.juancarloscp52.bedrockify.client.features.savingOverlay;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SavingOverlay extends DrawableHelper {

    private final Identifier WIDGET_TEXTURE = new Identifier("bedrockify", "textures/gui/bedrockify_widgets.png");
    private boolean saving = false;
    private long timer=0;
    private float renderTimer=0;
    private final MinecraftClient client = MinecraftClient.getInstance();

    public void render(MatrixStack matrixStack){
        final BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        if(saving || System.currentTimeMillis()-timer<3000){
            RenderSystem.setShaderTexture(0,WIDGET_TEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1,1,1,BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
            // Draw chest
            drawTexture(matrixStack, client.getWindow().getScaledWidth()-(21+settings.getScreenSafeArea()), 19 + settings.getScreenSafeArea(), 0, 99, 16, 17);
            // Draw arrow
            renderTimer+= BedrockifyClient.getInstance().deltaTime*0.000000001f;
            drawTexture(matrixStack, client.getWindow().getScaledWidth()-(19+settings.getScreenSafeArea()), 5 + settings.getScreenSafeArea() + MathHelper.floor(MathHelper.abs(MathHelper.sin(renderTimer * 3.1415926F) * 6)), 16, 100, 12, 15);
            RenderSystem.setShaderColor(1,1,1,1);
        }
    }

    public void setSaving(boolean saving) {
        if(this.saving && !saving)
            timer=System.currentTimeMillis();
        this.saving = saving;
    }
}
