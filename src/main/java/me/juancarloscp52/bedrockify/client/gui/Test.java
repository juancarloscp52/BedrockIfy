package me.juancarloscp52.bedrockify.client.gui;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.BedrockifyIconButtonTextures;
import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.GuiUtils;
import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets.*;
import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets.entryList.BedrockifyEntry;
import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets.entryList.BedrockifyEntryListWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class Test extends Screen {

    BedrockifyEntryListWidget test;
    BedrockifyEntryListWidget test2;
    BedrockifyIconButtonTextures video = new BedrockifyIconButtonTextures(Identifier.of("bedrockify", "icons/video"), Identifier.of("bedrockify", "icons/video_highlighted"));
    BedrockifyIconButtonTextures back = new BedrockifyIconButtonTextures(Identifier.of("bedrockify", "widgets/back_button"), Identifier.of("bedrockify", "widgets/back_button_highlighted"), 4, 7);


    int header = 23;
    int sidebarWidth = (int) (this.width*0.4f);
    Screen previous;
    public Test(Text title, Screen previous) {
        super(title);
    }

    @Override
    protected void init() {
        this.addDrawableChild(new BedrockifyIconButtonWidget.Builder(Text.literal("Back"), button -> client.setScreen(previous), back).dimensions(4,4,12,14).build());
        sidebarWidth = (int) (this.width*0.4f);
        test = this.addDrawableChild(new BedrockifyEntryListWidget(client, sidebarWidth, this.height-header, header, 30, 0));
        test.setX(0);
        test.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Video"), button -> System.out.println("PRESSED")).icon(video).position(220, 10).width(test.getRowWidth()).build()));
        test.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button 1"), button -> System.out.println("PRESSED")).position(220, 10).icon(video).width(test.getRowWidth()).build()));
        test.children().add(new BedrockifyEntry(new BedrockifyTextWidget(Text.literal("Category Separator Centered"))));
        test.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button 2"), button -> System.out.println("PRESSED")).position(220, 10).width(test.getRowWidth()).build()));
        test.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button 3"), button -> System.out.println("PRESSED")).position(220, 10).width(test.getRowWidth()).build()));
        test.children().add(new BedrockifyEntry(new BedrockifyTextWidget(Text.literal("Category Separator top"), GuiUtils.VerticalLocation.TOP)));
        test.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button 69"), button -> System.out.println("PRESSED")).position(220, 10).width(test.getRowWidth()).build()));
        test.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button 69"), button -> System.out.println("PRESSED")).position(220, 10).width(test.getRowWidth()).build()));
        test.children().add(new BedrockifyEntry(new BedrockifyTextWidget(Text.literal("Category Separator bottom"), GuiUtils.VerticalLocation.BOTTOM)));
        test.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button 69"), button -> System.out.println("PRESSED")).position(220, 10).width(test.getRowWidth()).build()));
        test.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button 69"), button -> System.out.println("PRESSED")).position(220, 10).width(test.getRowWidth()).build()));
        test.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button 69"), button -> System.out.println("PRESSED")).position(220, 10).width(test.getRowWidth()).build()));

        test2 = this.addDrawableChild(new BedrockifyEntryListWidget(client, this.width-sidebarWidth-4, this.height-header, header, 30, 0));
        var text = new BedrockifyTextFieldWidget(this.textRenderer,100,Text.literal("Example Text Field"), Text.literal(""));
        text.setPlaceholder(Text.literal("Escribe aqui..."));
        test2.setX(sidebarWidth+4);
        test2.children().add(new BedrockifyEntry(new BedrockifyToggleWidget.Builder(Text.literal("Toggle Option 1"), button -> System.out.println("PRESSED")).position(5, 16).build(),20));
        test2.children().add(new BedrockifyEntry(new BedrockifyToggleWidget.Builder(Text.literal("Toggle Option 2"), button -> System.out.println("PRESSED")).position(5, 16).build(), 20));
        test2.children().add(new BedrockifyEntry(new BedrockifySliderWidget(0,0,test2.getRowWidth(), Text.literal("Slider Option: "),0.5d),35));
        test2.children().add(new BedrockifyEntry(text));
        test2.children().add(new BedrockifyEntry(new BedrockifyColorFieldWidget(this.textRenderer,100,50,100,Text.literal("Example Color Field"), false, ColorHelper.getArgb(0,0,255))));
        test2.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button"), button -> System.out.println("PRESSED")).position(220, 10).width(test2.getRowWidth()).build()));
        test2.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button"), button -> System.out.println("PRESSED")).position(220, 10).width(test2.getRowWidth()).build()));
        test2.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button"), button -> System.out.println("PRESSED")).position(220, 10).width(test2.getRowWidth()).build()));
        test2.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button"), button -> System.out.println("PRESSED")).position(220, 10).title(Text.literal("TITLE TEST:")).width(test2.getRowWidth()).build()));
        test2.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button"), button -> System.out.println("PRESSED")).position(220, 10).width(test2.getRowWidth()).build()));
        test2.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button"), button -> System.out.println("PRESSED")).position(220, 10).width(test2.getRowWidth()).build(),50));
        test2.children().add(new BedrockifyEntry(new BedrockifyButtonWidget.Builder(Text.literal("Button"), button -> System.out.println("PRESSED")).position(220, 10).width(test2.getRowWidth()).build()));



    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.renderBackground(context, mouseX, mouseY, deltaTicks);
        context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.of("bedrockify", "header"), 0, 0, this.width, header);
        context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.of("bedrockify", "separator"), sidebarWidth, header-1, 4, this.height-header+1);
        context.drawText(textRenderer, this.title, 18, 7, ColorHelper.getArgb(76, 76, 76), false);
    }
}

