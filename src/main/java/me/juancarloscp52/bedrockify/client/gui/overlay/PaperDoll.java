package me.juancarloscp52.bedrockify.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.gui.BedrockifyOptionsScreen;
import me.juancarloscp52.bedrockify.client.BedrockifySettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;

public class PaperDoll {
    private final MinecraftClient client;
    private final int posX = 30;
    private final int size = 20;
    private final float angle = 145;
    private int posY = 60;
    private long lastTimeShown = 0;
    private BedrockifySettings settings;
    public PaperDoll(MinecraftClient client) {
        this.client = client;
    }

    /**
     * Render the player at the top left of the screen.
     * The player will be rendered only when the player is not riding another entity and it is sneaking, running, using elytra, using an item, under water, or using a shield.
     */
    public void renderPaperDoll(MatrixStack matrixStack) {
        settings = BedrockifyClient.getInstance().settings;
        if (!settings.isShowPaperDollEnabled())
            return;

        //Determine the position of the doll depending on the position of the overlay text.
        int textPosY = settings.getPositionHUDHeight();
        if (textPosY >= 2 * size + 10) {
            posY = textPosY - 5;
        } else {
            posY = textPosY + size * 2 + 5;
            if (settings.getFPSHUDoption()==2)
                posY += 10;
            if (settings.isShowPositionHUDEnabled())
                posY += 10;
        }

        if (client.player != null) {
            //If the player does an action that must show the player entity gui, set the counter to the current time.
            if (client.player.isSneaking() || client.player.isSubmergedInWater() || client.player.getSpeed() > 1.11 || client.player.isFallFlying() || client.player.isBlocking() || client.player.isUsingItem())
                lastTimeShown = System.currentTimeMillis();

            // If the difference between the current game ticks and showTicks is less than a 100 ticks, draw the player entity.
            if ((!client.player.isRiding() && !client.player.isSleeping() && System.currentTimeMillis() - lastTimeShown < 2000) || client.currentScreen instanceof BedrockifyOptionsScreen)
                drawPaperDoll(matrixStack);
        }
    }

    /**
     * Draw the player entity in the specified position on screen.
     */
    private void drawPaperDoll(MatrixStack matrixStack) {
        client.getProfiler().push("Player");
        ClientPlayerEntity player = client.player;
        if (player == null)
            return;

        matrixStack.push();

        int renderPosY = posY;
        // If the player is elytra flying, the entity must be manually centered depending on the pitch.
        if (player.isFallFlying())
            renderPosY = posY - MathHelper.ceil(size * 2 * toMaxAngleRatio(player.pitch));
        // If the player is swimming, the entity must also be centered in the Y axis.
        else if (player.isSwimming()) {
            renderPosY = posY - size;
        }

        // Position the entity on screen.
        matrixStack.translate(posX + settings.getScreenSafeArea(), renderPosY + settings.getScreenSafeArea(), 0);
        matrixStack.scale((float) size, (float) size, -(float) size);
        Quaternion quaternion = Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
        matrixStack.multiply(quaternion);

        // Store previous entity rotations.
        float bodyYaw = player.bodyYaw;
        float yaw = player.yaw;
        float headYaw = player.headYaw;


        // Set the entity desired rotation for drawing.
        if (!player.isFallFlying()) {
            player.yaw = headYaw - bodyYaw + angle;
            player.headYaw = player.yaw;
        } else {
            player.headYaw = angle;
        }
        player.bodyYaw = angle;

        // Draw the entity.
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderManager();
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(player, 0, 0, 0, 0.0F, 1.0F, matrixStack, immediate, 15728880);
        });
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);

        // Restore previous entity rotations.
        player.bodyYaw = bodyYaw;
        player.yaw = yaw;
        player.headYaw = headYaw;

        matrixStack.pop();
        client.getProfiler().pop();
    }

    private float toMaxAngleRatio(float angle) {
        return (90 + angle) / 180;
    }

}
