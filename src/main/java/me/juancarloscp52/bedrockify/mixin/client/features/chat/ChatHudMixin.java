package me.juancarloscp52.bedrockify.mixin.client.features.chat;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.Bedrockify;
import me.juancarloscp52.bedrockify.BedrockifySettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin extends DrawableHelper {

    //@Shadow protected abstract void processMessageQueue();
    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;
    @Shadow protected abstract boolean isChatFocused();
    @Shadow public abstract double getChatScale();
    @Shadow public abstract int getWidth();
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scrolledLines;
    @Shadow private static double getMessageOpacityMultiplier(int age) {
        return 0;
    }
    //@Shadow @Final private Deque<Text> messageQueue;
    @Shadow private boolean hasUnreadNewMessages;
    @Shadow public abstract int getVisibleLineCount();
    @Shadow protected abstract boolean isChatHidden();
    @Shadow public abstract void addMessage(Text message);
    //@Shadow private long lastMessageAddedTime;

    @Shadow protected abstract int getLineHeight();

    @Shadow protected abstract int getIndicatorX(ChatHudLine.Visible line);

    @Shadow protected abstract void drawIndicatorIcon(MatrixStack matrices, int x, int y, MessageIndicator.Icon icon);

    private int counter1 =0;
    BedrockifySettings settings = Bedrockify.getInstance().settings;

    /**
     * Use bedrock-like chat if enabled.
     */
    @Inject(method = "render", at=@At("HEAD"),cancellable = true)
    private void render(MatrixStack matrixStack, int ticks, CallbackInfo info){
        if(!settings.isBedrockChatEnabled() || client.options.debugEnabled)
            return;

        if (!this.isChatHidden()) {
            int visibleLines = Math.min(this.getVisibleLineCount(),this.getAvailableLines());
            int visibleMessagesCount = this.visibleMessages.size();
            if (visibleMessagesCount > 0) {
                int safeArea = settings.overlayIgnoresSafeArea? 0: settings.getScreenSafeArea();
                boolean isChatFocused = this.isChatFocused();
                int posY = 2+settings.getPositionHUDHeight() + (settings.getPositionHUDHeight()<50? 50:0) + (settings.isShowPositionHUDEnabled() ? 10 : 0) + (settings.getFPSHUDoption()==2 ? 10 : 0) + safeArea;
                float chatScale = (float) this.getChatScale();
                int scaledChatWidth = MathHelper.ceil((double)this.getWidth() / chatScale);
                matrixStack.push();
                matrixStack.translate(safeArea, (float) (48-MinecraftClient.getInstance().getWindow().getScaledHeight() + (counter1*(9.0D * chatScale* (this.client.options.getChatLineSpacing().getValue() + 1.0D))))+ posY, 0.0F);
                matrixStack.scale(chatScale, chatScale, 1.0F);
                double textOpacity = this.client.options.getChatOpacity().getValue() * 0.9D + 0.1D;
                double backgroundOpacity = this.client.options.getTextBackgroundOpacity().getValue();
                //double chatLineSpacing1 = 9.0D * (this.client.options.getChatLineSpacing().getValue() + 1.0D);
                double chatLineSpacing1 = this.getLineHeight();

                double chatLineSpacing2 = -8.0D * (this.client.options.getChatLineSpacing().getValue() + 1.0D) + 4.0D * this.client.options.getChatLineSpacing().getValue();
                counter1 = 0; //Shown messages

                for(int i = 0; i + this.scrolledLines < this.visibleMessages.size() && i < visibleLines; ++i) {
                    ChatHudLine.Visible chatHudLine = this.visibleMessages.get(i + this.scrolledLines);
                    if (chatHudLine != null) {
                        int ticksSinceCreation;
                        ticksSinceCreation = ticks - chatHudLine.addedTime();
                        if (ticksSinceCreation < 200 || isChatFocused) {
                            double opacityMultiplayer = isChatFocused ? 1.0D : getMessageOpacityMultiplier(ticksSinceCreation);
                            int finalTextOpacity = (int)(255.0D * opacityMultiplayer * textOpacity);
                            int finalBackgroundOpacity = (int)(255.0D * opacityMultiplayer * backgroundOpacity);
                            ++counter1;
                            if (finalTextOpacity > 3) {
                                double currentMessageHeight = (double)(-i) * chatLineSpacing1;
                                matrixStack.push();
                                matrixStack.translate(0, 0, 0);
                                ChatHud.fill(matrixStack, 0, (int)currentMessageHeight, scaledChatWidth + 6, (int)(currentMessageHeight - chatLineSpacing1), finalBackgroundOpacity << 24);

                                // Message security indicator
                                MessageIndicator messageIndicator = chatHudLine.indicator();
                                if (messageIndicator != null) {
                                    int indicatorColor = messageIndicator.indicatorColor() | finalTextOpacity << 24;
                                    ChatHud.fill(matrixStack, 0, (int)currentMessageHeight, 2, (int)(currentMessageHeight - chatLineSpacing1), indicatorColor);
                                    if (isChatFocused() && chatHudLine.endOfEntry() && messageIndicator.icon() != null) {
                                        int w = this.getIndicatorX(chatHudLine);
                                        int x = (int)currentMessageHeight + this.client.textRenderer.fontHeight;
                                        this.drawIndicatorIcon(matrixStack, w, x, messageIndicator.icon());
                                    }
                                }

                                RenderSystem.enableBlend();
                                matrixStack.translate(0, 0, 0);
                                this.client.textRenderer.drawWithShadow(matrixStack, chatHudLine.content(), 4F, (float)((int)(currentMessageHeight + chatLineSpacing2)), 16777215 + (finalTextOpacity << 24));
                                RenderSystem.disableBlend();
                                matrixStack.pop();
                            }
                        }
                    }
                }

                //Unread Messages:
                long unprocessedMessageCount = this.client.getMessageHandler().getUnprocessedMessageCount();
                if (unprocessedMessageCount>0) {
                    int textOpacityFinal = (int)(128.0D * textOpacity);
                    int backgroundOpacityFinal = (int)(255.0D * backgroundOpacity);
                    matrixStack.push();
                    matrixStack.translate(0.0D, 0.0D, 50.0D);
                    fill(matrixStack, -2, 0, scaledChatWidth + 4, 9, backgroundOpacityFinal << 24);
                    RenderSystem.enableBlend();
                    matrixStack.translate(0.0D, 0.0D, 50.0D);
                    this.client.textRenderer.drawWithShadow(matrixStack, Text.translatable("chat.queue", unprocessedMessageCount), 2F, 1.0F, 16777215 + (textOpacityFinal << 24));
                    matrixStack.pop();
                    RenderSystem.disableBlend();
                }

                if (isChatFocused) {
                    //int textSize = 9;
                    int textSize = this.getLineHeight();
                    int x = visibleMessagesCount * textSize + visibleMessagesCount;
                    int renderedMessages = counter1 * textSize + counter1;
                    int z = this.scrolledLines * renderedMessages / visibleMessagesCount;
                    int aa = renderedMessages * renderedMessages / x;
                    if (x != renderedMessages) {
                        int alpha = z > 0 ? 170 : 96;
                        int color = this.hasUnreadNewMessages ? 13382451 : 3355562;
                        matrixStack.translate(-3.0F, 0.0F, 0.0F);
                        fill(matrixStack, 2, -z, 2, -z - aa, color + (alpha << 24));
                        fill(matrixStack, 4, -z, 1, -z - aa, 13421772 + (alpha << 24));
                    }
                }

                matrixStack.pop();
            }
        }


        info.cancel();
    }

    @Inject(method = "mouseClicked",at=@At("HEAD"),cancellable = true)
    public void mouseClicked(double x, double y, CallbackInfoReturnable<Boolean> info){
        if(!settings.isBedrockChatEnabled() || client.options.debugEnabled)
            return;
        MessageHandler messageHandler = this.client.getMessageHandler();
        if(this.isChatFocused() && !this.client.options.hudHidden && !this.isChatHidden() && messageHandler.getUnprocessedMessageCount() != 0L){
            int safeArea = settings.overlayIgnoresSafeArea? 0: settings.getScreenSafeArea();
            int posY = 2+settings.getPositionHUDHeight() + (settings.getPositionHUDHeight()<50? 50:0) + (settings.isShowPositionHUDEnabled() ? 10 : 0) + (settings.getFPSHUDoption()==2 ? 10 : 0) + safeArea;
            double lineSize = 9.0D * (this.client.options.getChatLineSpacing().getValue() + 1.0D);
            double chatX= x - safeArea;
            double chatY = posY+(counter1*lineSize) - y;

            if(chatX<=MathHelper.floor(this.getWidth() / this.getChatScale()) && chatY < 0.0D && chatY > (double)MathHelper.floor(-9.0D * this.getChatScale())){
                messageHandler.process();
                info.setReturnValue(true);
            }else{
                info.setReturnValue(false);
            }
        }else{
            info.setReturnValue(false);
        }
    }

    @Inject(method = "getTextStyleAt",at=@At("HEAD"),cancellable = true)
    public void getText(double x, double y, CallbackInfoReturnable<Style> info){
        if(!settings.isBedrockChatEnabled() || client.options.debugEnabled)
            return;
        int safeArea = settings.overlayIgnoresSafeArea? 0: settings.getScreenSafeArea();
        int posY = 2+settings.getPositionHUDHeight() + (settings.getPositionHUDHeight()<50? 50:0) + (settings.isShowPositionHUDEnabled() ? 10 : 0) + (settings.getFPSHUDoption()==2 ? 10 : 0) +  safeArea;
        double lineSize = 9.0D * (this.client.options.getChatLineSpacing().getValue() + 1.0D);

        if(this.isChatFocused() && !client.options.hudHidden && !this.isChatHidden()){
            double chatX = x - safeArea;
            double chatY = posY+(counter1*lineSize) - y;
            chatX= MathHelper.floor(chatX/this.getChatScale());
            chatY = MathHelper.floor(chatY/ (this.getChatScale() * (this.client.options.getChatLineSpacing().getValue() + 1.0D)));
            if(chatX>=0.0D && chatY >= 0.0D){
                int lines = Math.min(this.getVisibleLineCount(), this.visibleMessages.size());
                if(chatX<= MathHelper.fastFloor(this.getWidth()/getChatScale())){
                    if(chatY < 9*lines+lines){
                        int line = (int)(chatY/9.0D + this.scrolledLines);
                        if(line>=0 && line<this.visibleMessages.size()){
                            ChatHudLine.Visible chatHudLine = this.visibleMessages.get(line);
                            info.setReturnValue(this.client.textRenderer.getTextHandler().getStyleAt(chatHudLine.content(), (int)chatX));
                            return;
                        }
                    }
                }
            }
        }
        info.setReturnValue(null);
    }

    private int getAvailableLines(){
        BedrockifySettings settings = Bedrockify.getInstance().settings;
        int posY = 2+settings.getPositionHUDHeight() + (settings.isShowPositionHUDEnabled() ? 10 : 0) + (settings.getFPSHUDoption()==2 ? 10 : 0) +  (settings.overlayIgnoresSafeArea? 0: settings.getScreenSafeArea());
        return MathHelper.ceil((client.getWindow().getScaledHeight()-posY)/((this.client.options.getChatLineSpacing().getValue() + 1.0D)*9)) -2;
    }
}
