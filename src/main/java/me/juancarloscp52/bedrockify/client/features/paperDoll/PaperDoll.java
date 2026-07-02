package me.juancarloscp52.bedrockify.client.features.paperDoll;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.include.com.google.common.collect.Sets;

import java.util.Set;

public class PaperDoll {
    private final Minecraft client;
    private final int size = 20;
    private long lastTimeShown = 0;
    private final BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;

    private static final Set<String> TARGET_POSE_NAMES = Sets.newHashSet(Pose.FALL_FLYING.name(), Pose.SWIMMING.name(), "CRAWLING");
    private static final float SCREEN_PIXEL_TO_GL_SCALE = 0.0625f;

    public PaperDoll(Minecraft client) {
        this.client = client;
    }

    /**
     * Render the player at the top left of the screen.
     * The player will be rendered only when the player is not riding another entity, and it is sneaking, running, using elytra, using an item, underwater, or using a shield.
     */
    public void renderPaperDoll(GuiGraphicsExtractor drawContext) {
        if (!settings.isShowPaperDollEnabled())
            return;

        if (this.client.gui.screen() instanceof InventoryScreen || this.client.gui.screen() instanceof CreativeModeInventoryScreen) {
            return;
        }

        if (client.player != null) {
            //If the player does an action that must show the player entity gui, set the counter to the current time.
            if (shouldShow(client.player))
                lastTimeShown = System.currentTimeMillis();

            // If the difference between the current game ticks and showTicks is less than 100 ticks, draw the player entity.
            if ((!client.player.isHandsBusy() && !client.player.isSleeping() && System.currentTimeMillis() - lastTimeShown < 2000))
                drawPaperDoll(drawContext);
        }
    }

    /**
     * Checks player's action.
     *
     * @param player An instance of a {@link LocalPlayer}.
     * @return {@code true} if condition matches.
     */
    private static boolean shouldShow(LocalPlayer player) {
        return player.isShiftKeyDown() ||
                player.isSprinting() ||
                player.isUnderWater() ||
                player.getAbilities().flying ||  // flying in Creative mode
                player.isBlocking() ||
                player.isUsingItem() ||
                TARGET_POSE_NAMES.contains(player.getPose().name());
    }

    /**
     * Draw the player entity in the specified position on screen.
     */
    private void drawPaperDoll(GuiGraphicsExtractor drawContext) {
        LocalPlayer player = client.player;
        if (player == null)
            return;

        // Position the entity on screen.
        int posX = 10;
        int offsetX = -7;
        int renderBottomPosY;
        int offsetY = 2;

        // Determine the position of the doll depending on the position of the overlay text.
        int textPosY = settings.getPositionHUDHeight();
        if (textPosY >= 2 * size + 10) {
            renderBottomPosY = textPosY;
        } else {
            renderBottomPosY = textPosY + size * 2 + 6 + BedrockifyClient.getInstance().overlay.getTextsTopOffset();
        }

        // If the player is elytra flying, the entity must be manually centered depending on the pitch.
        if (player.getPose().equals(Pose.FALL_FLYING)) {
            posX = 0;
            offsetY = 15 - Mth.ceil(size * 2 * toMaxAngleRatio(player.getXRot()));
        }
        // If the player is swimming, the entity must also be centered in the Y axis.
        else if (player.isSwimming()) {
            offsetY = -2;
        }
        int safeArea = settings.overlayIgnoresSafeArea ? 0 : settings.getScreenSafeArea();
        int x1 = posX + safeArea;
        int y2 = renderBottomPosY + safeArea;
        int x2 = x1 + Mth.ceil(size * 3.25f);
        int y1 = Math.max(safeArea, y2 - size * 3);
        drawContext.enableScissor(x1, y1, x2, y2);

        // Store previous entity rotations.
        float bodyYaw = player.yBodyRot;
        float yaw = player.getYRot();
        float headYaw = player.yHeadRot;


        // Set the entity desired rotation for drawing.
        float angle = 145;
        if (player.getPose().equals(Pose.FALL_FLYING) || player.isBlocking()) {
            player.yHeadRot = angle;
        } else {
            player.setYRot(headYaw - bodyYaw + angle);
            player.yHeadRot = player.getYRot();
        }
        player.yBodyRot = angle;

        Vector3f translation = new Vector3f(offsetX * SCREEN_PIXEL_TO_GL_SCALE, player.getBbHeight() * 0.5f + offsetY * SCREEN_PIXEL_TO_GL_SCALE, 0.0F);
        Quaternionf rotation = new Quaternionf().rotateZ((float) Math.PI);

        // Draw the entity.
        EntityRenderDispatcher entityRenderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> entityRenderer = entityRenderManager.getRenderer(player);
        EntityRenderState entityRenderState = entityRenderer.createRenderState(player, 1.0F);
        entityRenderState.lightCoords = 15728880;
        entityRenderState.shadowPieces.clear();
        entityRenderState.outlineColor = 0;

        drawContext.entity(entityRenderState, (float) size, translation, rotation, null, x1, y1, x2, y2);

        // Restore previous entity rotations.
        player.yBodyRot = bodyYaw;
        player.setYRot(yaw);
        player.yHeadRot = headYaw;

        drawContext.disableScissor();
    }

    private float toMaxAngleRatio(float angle) {
        return (90 + angle) / 180;
    }

}
