package me.juancarloscp52.bedrockify.client.gui;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.BedrockifySettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class BedrockifyOptionsScreen extends Screen {

    Screen parent;
    ButtonWidget multiplayerReachAroundButton;
    public BedrockifyOptionsScreen(Screen parent) {
        super(new TranslatableText("bedrockify.options.settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        BedrockifySettings settings = BedrockifyClient.getInstance().settings;
        this.addButton(new SliderWidget(this.width / 2 - 159, this.height/6 -12, 318, 20, new TranslatableText("bedrockify.options.screenSafeArea").append(" " + settings.getScreenSafeArea()), settings.getScreenSafeArea() / 30F) {
            @Override
            protected void updateMessage() {}
            @Override
            protected void applyValue() {
                settings.setScreenSafeArea(value);
                setMessage(new TranslatableText("bedrockify.options.screenSafeArea").append(" " + settings.getScreenSafeArea()));
            }
        });
        this.addButton(new SliderWidget(this.width / 2 - 159, this.height/6 -12 + 24, 318, 20, new TranslatableText("bedrockify.options.coordinatesPosition").append(" "+settings.getPositionHUDHeight()), settings.getPositionHUDHeight() / 100F) {
            @Override
            protected void updateMessage() {}
            @Override
            protected void applyValue() {
                settings.setPositionHUDHeight(value);
                setMessage(new TranslatableText("bedrockify.options.coordinatesPosition").append(" "+settings.getPositionHUDHeight()));
            }
        });
        this.addButton(new ButtonWidget(this.width / 2 - 159, this.height/6 -12 + 24 * 2, 158, 20, new TranslatableText("bedrockify.options.showCoordinates").append(" ").append(settings.isShowPositionHUDEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")), (button) -> {
            settings.togglePositionHUD();
            button.setMessage(new TranslatableText("bedrockify.options.showCoordinates").append(" ").append(settings.isShowPositionHUDEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")));
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 159 + 160, this.height/6 -12 + 24 * 2, 158, 20, new TranslatableText("bedrockify.options.showFPS:").append(" ").append(new TranslatableText(getFpsMode())), (button) -> {
            settings.toggleFPSHUD();
            button.setMessage(new TranslatableText("bedrockify.options.showFPS:").append(" ").append(new TranslatableText(getFpsMode())));
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 159, this.height/6 -12 + 24 * 3, 158, 20, new TranslatableText("bedrockify.options.showPaperDoll").append(" ").append(settings.isShowPaperDollEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")), (button) -> {
            settings.togglePaperDoll();
            button.setMessage(new TranslatableText("bedrockify.options.showPaperDoll").append(" ").append(settings.isShowPaperDollEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")));
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 159 + 160, this.height/6 -12 + 24 * 3, 158, 20, new TranslatableText("bedrockify.options.loadingScreenChunkMap").append(" ").append(settings.isShowChunkMapEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")), (button) -> {
            settings.toggleChunkMap();
            button.setMessage(new TranslatableText("bedrockify.options.loadingScreenChunkMap").append(" ").append(settings.isShowChunkMapEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")));
        },(buttonWidget,matrixStack,x,y)-> renderOrderedTooltip(matrixStack,this.textRenderer.wrapLines(new TranslatableText("bedrockify.options.loadingScreenChunkMap.tooltip"),Math.max(this.width/2 - 43,170)),x,y)));
        this.addButton(new ButtonWidget(this.width / 2 - 159, this.height/6 -12 + 24 * 4, 158, 20, new TranslatableText("bedrockify.options.reachAround").append(" ").append(settings.isReacharoundEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")), (button) -> {
            settings.toggleReacharound();
            button.setMessage(new TranslatableText("bedrockify.options.reachAround").append(" ").append(settings.isReacharoundEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")));
        }));
        multiplayerReachAroundButton = this.addButton(new ButtonWidget(this.width / 2 - 159 + 160, this.height/6 -12 + 24 * 4, 158, 20, new TranslatableText("bedrockify.options.reachAround.multiplayer").append(" ").append(settings.isReacharoundMultiplayerEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")), (button) -> {
            settings.toggleReacharoundMultiplayer();
            button.setMessage(new TranslatableText("bedrockify.options.reachAround.multiplayer").append(" ").append(settings.isReacharoundMultiplayerEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")));
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 159, this.height/6 -12 + 24 * 5, 158, 20, new TranslatableText("bedrockify.options.chatStyle").append(" ").append(settings.isBedrockChatEnabled() ? new TranslatableText("bedrockify.options.chatStyle.bedrock") : new TranslatableText("bedrockify.options.chatStyle.vanilla")), (button) -> {
            settings.toggleBedrockChat();
            button.setMessage(new TranslatableText("bedrockify.options.chatStyle").append(" ").append(settings.isBedrockChatEnabled() ? new TranslatableText("bedrockify.options.chatStyle.bedrock") : new TranslatableText("bedrockify.options.chatStyle.vanilla")));
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 159 + 160, this.height/6 -12 + 24 * 5, 158, 20, new TranslatableText("bedrockify.options.tooltips").append(" ").append(new TranslatableText(getTooltipMode())), (button) -> {
            settings.toggleTooltipBackground();
            button.setMessage(new TranslatableText("bedrockify.options.tooltips").append(" ").append(new TranslatableText(getTooltipMode())));
        }));

        this.addButton(new ButtonWidget(this.width / 2 - 159, this.height/6 -12 + 24 * 6, 318, 20, new TranslatableText("bedrockify.options.rotationalBackground").append(" ").append(settings.isCubemapBackgroundEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")), (button) -> {
            settings.toggleCubemapBackground();
            button.setMessage(new TranslatableText("bedrockify.options.rotationalBackground").append(" ").append(settings.isCubemapBackgroundEnabled() ? new TranslatableText("bedrockify.options.on") : new TranslatableText("bedrockify.options.off")));
        },(buttonWidget,matrixStack,x,y)-> renderOrderedTooltip(matrixStack,this.textRenderer.wrapLines(new TranslatableText("bedrockify.options.rotationalBackground.tooltip"),Math.max(this.width/2 - 43,170)),x,y)));

        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 168, 200, 20, ScreenTexts.DONE, (buttonWidget) -> {
            this.onClose();
            this.client.openScreen(this.parent);
        }));
    }

    @Override
    public void onClose() {
        BedrockifyClient.getInstance().saveSettings();
        super.onClose();
    }

    private String getFpsMode(){
        String fpsMode="";
        switch (BedrockifyClient.getInstance().settings.getFPSHUDoption()){
            case 0: fpsMode = "bedrockify.options.off";break;
            case 1: fpsMode = "bedrockify.options.withPosition"; break;
            case 2: fpsMode = "bedrockify.options.underPosition"; break;
        }
        return fpsMode;
    }

    private String getTooltipMode(){
        String tooltipMode="";
        switch (BedrockifyClient.getInstance().settings.getHeldItemTooltip()){
            case 0: tooltipMode = "bedrockify.options.off";break;
            case 1: tooltipMode = "bedrockify.options.on"; break;
            case 2: tooltipMode = "bedrockify.options.withBackground"; break;
        }
        return tooltipMode;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        multiplayerReachAroundButton.active = BedrockifyClient.getInstance().settings.isReacharoundEnabled();
        drawCenteredText(matrices,this.textRenderer,this.title,this.width/2, 15,16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
