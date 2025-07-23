package me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets.entryList;

import me.juancarloscp52.bedrockify.client.gui.bedrockMenus.widgets.BedrockifyDrawableWidget;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BedrockifyValueEntry<T> extends  BedrockifyEntry{
    protected final Consumer<T> saveConsumer;
    protected T value;
    protected final Supplier<T> defaultValueSupplier;

    public BedrockifyValueEntry(BedrockifyDrawableWidget widget, int height, Supplier<T> defaultValueSupplier, Consumer<T> saveConsumer, T currentValue) {
        super(widget, height);
        this.saveConsumer = saveConsumer;
        this.defaultValueSupplier = defaultValueSupplier;
        this.value = currentValue;
    }

    public T getDefaultValue(){
        return defaultValueSupplier.get();
    }

    public void saveValue(){
        this.saveConsumer.accept(getValue());
    }

    public T getValue(){
        return getValue();
    }
}
