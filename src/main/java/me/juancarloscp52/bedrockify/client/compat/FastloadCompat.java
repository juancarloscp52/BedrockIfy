package me.juancarloscp52.bedrockify.client.compat;

import io.github.bumblesoftware.fastload.compat.bedrockify.BedrockifyCompat;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class FastloadCompat {
    public static void register() {
        BedrockifyCompat.BEDROCKIFY_COMPAT_EVENT.registerThreadUnsafe(1,
                event -> event.stableArgs((context, closer, eventArgs) -> {
                    MinecraftClient client = context.clientCalls().getClientInstance();
                    if (!BedrockifyClient.getInstance().settings.isLoadingScreenEnabled()) {
                        return;
                    }

                    if (context.getLoadedChunkCount().get() == 0 && context.getBuiltChunkCount().get() == 0) {
                        LoadingScreenWidget.getInstance().render(
                                context.matrices(),
                                client.getWindow().getScaledWidth() / 2,
                                client.getWindow().getScaledHeight() / 2,
                                Text.translatable("fastload.buildingTerrain.starting"),
                                null,
                                -1
                        );
                    } else {
                        Text content = context
                                .screenTemplate()
                                .copyContentOnly()
                                .append("\n")
                                .append(context.preparingChunks().getString() + ": " + context.loadedChunksString())
                                .append("\n")
                                .append(context.buildingChunks().getString() + ": " + context.builtChunksString());
                        int progress = (int)Math.ceil(
                                (context.getBuiltChunkCount().get().doubleValue() /context.loadingAreaGoal())*100
                        );
                        LoadingScreenWidget.getInstance().render(
                                context.matrices(),
                                client.getWindow().getScaledWidth() / 2,
                                client.getWindow().getScaledHeight() / 2,
                                context.screenName(),
                                content,
                                progress
                        );
                    }

                    context.shouldContinueMethodCall().heldObj = false;
                })
        );
    }
}
