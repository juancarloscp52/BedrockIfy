package me.juancarloscp52.bedrockify.client.features.paperDoll;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Arrays;

public class PaperDoll {
    private final MinecraftClient client;
    private final int size = 20;
    private int posY = 60;
    private long lastTimeShown = 0;
    private BedrockifyClientSettings settings;
    /**
     * Uses in method <code>drawPaperDoll</code>; the custom shading vectors.
     *
     * @see net.minecraft.client.render.DiffuseLighting#disableGuiDepthLighting
     * @see RenderSystem#setupGuiFlatDiffuseLighting
     */
    private static final Vector3f FLAT_LIT_VEC1 = (new Vector3f(0.2F, 0.5F, -0.7F)).normalize();
    private static final Vector3f FLAT_LIT_VEC2 = (new Vector3f(-0.2F, 0.5F, 0.7F)).normalize();
    public PaperDoll(MinecraftClient client) {
        this.client = client;
    }

    private static final boolean supportsCrawling = Arrays.stream(EntityPose.values()).anyMatch(pose -> pose.name().equals("CRAWLING"));
    /**
     * Render the player at the top left of the screen.
     * The player will be rendered only when the player is not riding another entity, and it is sneaking, running, using elytra, using an item, underwater, or using a shield.
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
            if (client.player.isSneaking() || client.player.isSubmergedInWater() || client.player.getPose().equals(EntityPose.SWIMMING) || client.player.isSprinting() || client.player.getAbilities().flying || client.player.isFallFlying() || client.player.isBlocking() || client.player.isUsingItem() || (supportsCrawling && client.player.getPose() == EntityPose.valueOf("CRAWLING")))
                lastTimeShown = System.currentTimeMillis();

            // If the difference between the current game ticks and showTicks is less than 100 ticks, draw the player entity.
            if ((!client.player.isRiding() && !client.player.isSleeping() && System.currentTimeMillis() - lastTimeShown < 2000))
                drawPaperDoll(matrixStack);
        }
    }

    /**
     * Draw the player entity in the specified position on screen.
     */
    private void drawPaperDoll(MatrixStack matrixStack) {
        ClientPlayerEntity player = client.player;
        if (player == null)
            return;

        matrixStack.push();

        int renderPosY = posY;
        // If the player is elytra flying, the entity must be manually centered depending on the pitch.
        if (player.isFallFlying())
            renderPosY = posY - MathHelper.ceil(size * 2 * toMaxAngleRatio(player.getPitch()));
        // If the player is swimming, the entity must also be centered in the Y axis.
        else if (player.isSwimming()) {
            renderPosY = posY - size;
        }

        // Position the entity on screen.
        int posX = 30;
        int safeArea = settings.overlayIgnoresSafeArea? 0 : settings.getScreenSafeArea();
        matrixStack.translate(posX + safeArea, renderPosY + safeArea, 0);
        matrixStack.scale((float) size, (float) size, -(float) size);
        Quaternionf quaternion = new Quaternionf().rotateZ((float)Math.PI);
        matrixStack.multiply(quaternion);

        // Store previous entity rotations.
        float bodyYaw = player.bodyYaw;
        float yaw = player.getYaw();
        float headYaw = player.headYaw;


        // Set the entity desired rotation for drawing.
        float angle = 145;
        if (!player.isFallFlying()) {
            player.setYaw(headYaw - bodyYaw + angle);
            player.headYaw = player.getYaw();
        } else {
            player.headYaw = headYaw - bodyYaw + angle;
        }
        player.bodyYaw = angle;

        // Set up shading.
        RenderSystem.setupGuiFlatDiffuseLighting(FLAT_LIT_VEC1, FLAT_LIT_VEC2);

        // Draw the entity.
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(player, 0, 0, 0, 0.0F, 1.0F, matrixStack, immediate, 0xF000F0));
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);

        // Restore previous entity rotations.
        player.bodyYaw = bodyYaw;
        player.setYaw(yaw);
        player.headYaw = headYaw;

        matrixStack.pop();

        // Restore shading.
        DiffuseLighting.enableGuiDepthLighting();
    }

    private float toMaxAngleRatio(float angle) {
        return (90 + angle) / 180;
    }

}
