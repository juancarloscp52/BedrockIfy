package me.juancarloscp52.bedrockify.common.features.cauldron;

import net.minecraft.state.property.IntProperty;

public final class BedrockCauldronProperties {
    private BedrockCauldronProperties() {
    }

    public static final int MAX_LEVEL_6 = 6;
    public static final int MAX_LEVEL_8 = 8;

    public static final IntProperty LEVEL_6 = IntProperty.of("c_level", 1, MAX_LEVEL_6);
    public static final IntProperty LEVEL_8 = IntProperty.of("c_level", 1, MAX_LEVEL_8);
}
