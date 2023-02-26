package me.juancarloscp52.bedrockify.mixin.client.features.chat;

import com.mojang.blaze3d.systems.RenderSystem;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifyClientSettings;
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

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;
    @Shadow protected abstract boolean isChatFocused();
    @Shadow public abstract double getChatScale();
    @Shadow public abstract int getWidth();
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scrolledLines;
    @Shadow private static double getMessageOpacityMultiplier(int age) {
        return 0;
    }
    @Shadow private boolean hasUnreadNewMessages;
    @Shadow public abstract int getVisibleLineCount();
    @Shadow protected abstract boolean isChatHidden();
    @Shadow public abstract void addMessage(Text message);

    @Shadow protected abstract int getLineHeight();

    @Shadow protected abstract int getIndicatorX(ChatHudLine.Visible line);

    @Shadow protected abstract void drawIndicatorIcon(MatrixStack matrices, int x, int y, MessageIndicator.Icon icon);

    @Shadow protected abstract int getMessageIndex(double chatLineX, double chatLineY);

    @Shadow protected abstract double toChatLineX(double x);

    @Shadow protected abstract double toChatLineY(double y);

    private int counter1 =0;
    BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;

        /**
     * Use bedrock-like chat if enabled.
     */
    @Inject(method = "render", at=@At("HEAD"),cancellable = true)
    private void render(MatrixStack matrices, int ticks, int mouseX, int mouseY, CallbackInfo info){

        if(!settings.isBedrockChatEnabled() || client.options.debugEnabled)
            return;

        if(this.isChatHidden())
            return;

        RenderSystem.setShaderColor(1,1,1,1);
        int visibleLines = Math.min(this.getVisibleLineCount(),this.getAvailableLines());
        int visibleMessagesCount = this.visibleMessages.size();

        if(visibleMessagesCount <= 0)
            return;

        int safeArea = settings.overlayIgnoresSafeArea? 0: settings.getScreenSafeArea();
        boolean isChatFocused = this.isChatFocused();

        int posY = 2+settings.getPositionHUDHeight() + (settings.getPositionHUDHeight()<50? 50:0) + (settings.isShowPositionHUDEnabled() ? 10 : 0) + (settings.getFPSHUDoption()==2 ? 10 : 0) + safeArea;
        float chatScale = (float) this.getChatScale();
        int scaledChatWidth = MathHelper.ceil((double)this.getWidth() / chatScale);

        matrices.push();
        matrices.scale(chatScale, chatScale, 1.0F);
        matrices.translate(safeArea, (float) (48-MinecraftClient.getInstance().getWindow().getScaledHeight() + (counter1*(9.0D * chatScale* (this.client.options.getChatLineSpacing().getValue() + 1.0D))))+ posY, 0.0F);

        double x;

        int m = MathHelper.floor((float)(this.client.getWindow().getScaledHeight() - 48) / chatScale);
        int messageIndex = this.getMessageIndex(this.toChatLineX(mouseX), this.toChatLineY(mouseY));
        double textOpacity = this.client.options.getChatOpacity().getValue() * 0.9D + 0.1D;// d
        double backgroundOpacity = this.client.options.getTextBackgroundOpacity().getValue(); //e
        //double g = this.client.options.getChatLineSpacing().getValue();
        double chatLineSpacing1 = this.getLineHeight(); //o
        double chatLineSpacing2 = -8.0D * (this.client.options.getChatLineSpacing().getValue() + 1.0D) + 4.0D * this.client.options.getChatLineSpacing().getValue(); //p
        counter1 = 0; //Shown messages   q
        for (int i = 0; i + this.scrolledLines < this.visibleMessages.size() && i < visibleLines; ++i) {
            int s = i + this.scrolledLines;
            ChatHudLine.Visible visible = this.visibleMessages.get(s);
            int ticksSinceCreation = ticks - visible.addedTime();
            if (visible == null || ticksSinceCreation >= 200 && !isChatFocused) continue;
            double opacityMultiplayer = isChatFocused ? 1.0D : getMessageOpacityMultiplier(ticksSinceCreation);
            int finalTextOpacity = (int)((255.0D * opacityMultiplayer * textOpacity)*BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false)); //u
            int finalBackgroundOpacity = (int)((255.0D * opacityMultiplayer * backgroundOpacity)*BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));//v
            ++counter1;
            if (finalTextOpacity <= 3) continue;
            double currentMessageHeight = (double)(-i) * chatLineSpacing1;

            //x = m + currentMessageHeight;
            x = m + currentMessageHeight;
            int currentMessageHeight2 = (int) (x + chatLineSpacing2);
            matrices.push();
            matrices.translate(0.0f, 0.0f, 50.0f);
            ChatHud.fill(matrices, 0, (int) x, scaledChatWidth+6, (int)(x-chatLineSpacing1), finalBackgroundOpacity << 24);
            MessageIndicator messageIndicator = visible.indicator();
            if (messageIndicator != null) {
                int z = messageIndicator.indicatorColor() | finalTextOpacity << 24;
                ChatHud.fill(matrices, 0, (int)(x - chatLineSpacing1), 2, (int)x, z);
                if (s == messageIndex && messageIndicator.icon() != null) {
                    int aa = this.getIndicatorX(visible);
                    int ab = currentMessageHeight2 + this.client.textRenderer.fontHeight;
                    this.drawIndicatorIcon(matrices, aa, ab, messageIndicator.icon());
                }
            }
            RenderSystem.enableBlend();
            matrices.translate(0.0f, 0.0f, 50.0f);
            this.client.textRenderer.drawWithShadow(matrices, visible.content(), 4.0f, (float)currentMessageHeight2, 0xFFFFFF + (finalTextOpacity << 24));
            RenderSystem.disableBlend();
            matrices.pop();
        }
        long ac = this.client.getMessageHandler().getUnprocessedMessageCount();
        if (ac > 0L) {
            int textOpacityFinal = (int)((128.0 * textOpacity)*BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
            int backgroundOpacityFinal = (int)(255.0D * backgroundOpacity*BedrockifyClient.getInstance().hudOpacity.getHudOpacity(false));
            matrices.push();
            matrices.translate(0.0f, m, 50.0f);
            ChatHud.fill(matrices, -2, 0, scaledChatWidth + 4, 9, backgroundOpacityFinal << 24);
            RenderSystem.enableBlend();
            matrices.translate(0.0f, 0.0f, 50.0f);
            this.client.textRenderer.drawWithShadow(matrices, Text.translatable("chat.queue", ac), 2.0f, 1.0f, 0xFFFFFF + (textOpacityFinal << 24));
            matrices.pop();
            RenderSystem.disableBlend();
        }
        if (isChatFocused) {
            //int textSize = 9;
            int textSize = this.getLineHeight();
            int xx = visibleMessagesCount * textSize + visibleMessagesCount;
            int renderedMessages = counter1 * textSize + counter1;
            int z = this.scrolledLines * renderedMessages / visibleMessagesCount;
            int aa = renderedMessages * renderedMessages / xx;
            if (xx != renderedMessages) {
                int alpha = z > 0 ? 170 : 96;
                int color = this.hasUnreadNewMessages ? 13382451 : 3355562;
                matrices.translate(-3.0F, 0.0F, 0.0F);
                fill(matrices, 2, -z, 2, -z - aa, color + (alpha << 24));
                fill(matrices, 4, -z, 1, -z - aa, 13421772 + (alpha << 24));
            }
        }
        matrices.pop();
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
                if(chatX<= MathHelper.floor(this.getWidth()/getChatScale())){
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
        BedrockifyClientSettings settings = BedrockifyClient.getInstance().settings;
        int posY = 2+settings.getPositionHUDHeight() + (settings.isShowPositionHUDEnabled() ? 10 : 0) + (settings.getFPSHUDoption()==2 ? 10 : 0) +  (settings.overlayIgnoresSafeArea? 0: settings.getScreenSafeArea());
        return MathHelper.ceil((client.getWindow().getScaledHeight()-posY)/((this.client.options.getChatLineSpacing().getValue() + 1.0D)*9)) -2;
    }
}
