package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.builder;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets.entryList.BedrockifyEntry;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BedrockifyWidgetBuilder<TValue, TWidget extends BedrockifyEntry>{

    private Text title;
    private Supplier<TValue> defaultValue;
    private Consumer<TValue> applyValue;
    private TValue value;



}
