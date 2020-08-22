package me.juancarloscp52.bedrockify.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifySettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Deque;
import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin extends DrawableHelper {

    @Shadow protected abstract void method_27149();
    @Shadow @Final private List<ChatHudLine<OrderedText>> visibleMessages;
    @Shadow protected abstract boolean isChatFocused();
    @Shadow public abstract double getChatScale();
    @Shadow public abstract int getWidth();
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scrolledLines;
    @Shadow private static double getMessageOpacityMultiplier(int age) {
        return 0;
    }
    @Shadow @Final private Deque<Text> field_23934;
    @Shadow private boolean hasUnreadNewMessages;
    @Shadow public abstract int getVisibleLineCount();

    @Shadow protected abstract boolean isChatHidden();

    private int counter1 =0;
    BedrockifySettings settings = BedrockifyClient.getInstance().settings;

    /**
     * Use bedrock-like chat if enabled.
     */
    @Inject(method = "render", at=@At("HEAD"),cancellable = true)
    private void render(MatrixStack matrixStack, int ticks, CallbackInfo info){
        if(!settings.isBedrockChatEnabled())
            return;

        if (!this.isChatHidden()) {
            this.method_27149();
            int visibleLines = Math.min(this.getVisibleLineCount(),this.getAvailableLines());
            int visibleMessagesCount = this.visibleMessages.size();
            if (visibleMessagesCount > 0) {
                boolean isChatFocused = this.isChatFocused();
                int posY = settings.getPositionHUDHeight() + (settings.getPositionHUDHeight()<50? 50:0) + (settings.isShowPositionHUDEnabled() ? 10 : 0) + (settings.getFPSHUDoption()==2 ? 10 : 0) +  settings.getScreenSafeArea();
                double chatScale = this.getChatScale();
                int scaledChatWidth = MathHelper.ceil((double)this.getWidth() / chatScale);
                RenderSystem.pushMatrix();
                RenderSystem.translatef(settings.getScreenSafeArea(), (float) (48-MinecraftClient.getInstance().getWindow().getScaledHeight() + (counter1*(9.0D * chatScale* (this.client.options.chatLineSpacing + 1.0D))))+ posY, 0.0F);
                RenderSystem.scaled(chatScale, chatScale, 1.0D);
                double textOpacity = this.client.options.chatOpacity * 0.9D + 0.1D;
                double backgroundOpacity = this.client.options.textBackgroundOpacity;
                double chatLineSpacing1 = 9.0D * (this.client.options.chatLineSpacing + 1.0D);
                double chatLineSpacing2 = -8.0D * (this.client.options.chatLineSpacing + 1.0D) + 4.0D * this.client.options.chatLineSpacing;
                counter1 = 0; //Shown messages

                for(int i = 0; i + this.scrolledLines < this.visibleMessages.size() && i < visibleLines; ++i) {
                    ChatHudLine<OrderedText> chatHudLine = this.visibleMessages.get(i + this.scrolledLines);
                    if (chatHudLine != null) {
                        int ticksSinceCreation;
                        ticksSinceCreation = ticks - chatHudLine.getCreationTick();
                        if (ticksSinceCreation < 200 || isChatFocused) {
                            double opacityMultiplayer = isChatFocused ? 1.0D : getMessageOpacityMultiplier(ticksSinceCreation);
                            int finalTextOpacity = (int)(255.0D * opacityMultiplayer * textOpacity);
                            int finalBackgroundOpacity = (int)(255.0D * opacityMultiplayer * backgroundOpacity);
                            ++counter1;
                            if (finalTextOpacity > 3) {
                                double currentMessageHeight = (double)(-i) * chatLineSpacing1;
                                matrixStack.push();
                                matrixStack.translate(0, 0, 0);
                                fill(matrixStack, 0, (int)currentMessageHeight, scaledChatWidth + 4, (int)(currentMessageHeight - chatLineSpacing1), finalBackgroundOpacity << 24);
                                RenderSystem.enableBlend();
                                matrixStack.translate(0, 0, 0);
                                this.client.textRenderer.drawWithShadow(matrixStack, chatHudLine.getText(), 2F, (float)((int)(currentMessageHeight + chatLineSpacing2)), 16777215 + (finalTextOpacity << 24));
                                RenderSystem.disableAlphaTest();
                                RenderSystem.disableBlend();
                                matrixStack.pop();
                            }
                        }
                    }
                }

                //Unread Messages:
                if (!this.field_23934.isEmpty()) {
                    int textOpacityFinal = (int)(128.0D * textOpacity);
                    int backgroundOpacityFinal = (int)(255.0D * backgroundOpacity);
                    matrixStack.push();
                    matrixStack.translate(0.0D, 0.0D, 50.0D);
                    fill(matrixStack, -2, 0, scaledChatWidth + 4, 9, backgroundOpacityFinal << 24);
                    RenderSystem.enableBlend();
                    matrixStack.translate(0.0D, 0.0D, 50.0D);
                    this.client.textRenderer.drawWithShadow(matrixStack, new TranslatableText("chat.queue", this.field_23934.size()), 2F, 1.0F, 16777215 + (textOpacityFinal << 24));
                    matrixStack.pop();
                    RenderSystem.disableAlphaTest();
                    RenderSystem.disableBlend();
                }

                if (isChatFocused) {
                    int textSize = 9;
                    RenderSystem.translatef(-3.0F, 0.0F, 0.0F);
                    int x = visibleMessagesCount * textSize + visibleMessagesCount;
                    int renderedMessages = counter1 * textSize + counter1;
                    int z = this.scrolledLines * renderedMessages / visibleMessagesCount;
                    int aa = renderedMessages * renderedMessages / x;
                    if (x != renderedMessages) {
                        int alpha = z > 0 ? 170 : 96;
                        int color = this.hasUnreadNewMessages ? 13382451 : 3355562;
                        fill(matrixStack, 2, -z, 2, -z - aa, color + (alpha << 24));
                        fill(matrixStack, 4, -z, 1, -z - aa, 13421772 + (alpha << 24));
                    }
                }

                RenderSystem.popMatrix();
            }
        }


        info.cancel();
    }

    private int getAvailableLines(){
        BedrockifySettings settings = BedrockifyClient.getInstance().settings;
        int posY = settings.getPositionHUDHeight() + (settings.isShowPositionHUDEnabled() ? 10 : 0) + (settings.getFPSHUDoption()==2 ? 10 : 0) +  settings.getScreenSafeArea();
        return MathHelper.ceil((client.getWindow().getScaledHeight()-posY)/((this.client.options.chatLineSpacing + 1.0D)*9)) -2;
    }
}
