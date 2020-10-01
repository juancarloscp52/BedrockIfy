package me.juancarloscp52.bedrockify.client.mixin;


import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    MinecraftClient client = MinecraftClient.getInstance();
    @Inject(method = "onKey", at=@At("HEAD"))
    public void checkShortcut(long window, int key, int scancode, int i, int j, CallbackInfo info) {
        if (window == MinecraftClient.getInstance().getWindow().getHandle()) {
            if (InputUtil.isKeyPressed(client.getWindow().getHandle(), 342) && key == 66 && !(client.currentScreen != null && client.currentScreen.getTitle().equals(new TranslatableText("bedrockify.options.settings")))) {
                client.openScreen(BedrockifyClient.getInstance().settingsGUI.getConfigScreen(client.currentScreen,client.world != null));
            }
        }
    }
}
