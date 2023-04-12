package me.juancarloscp52.bedrockify.mixin.client.compat.fastload;

import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import me.juancarloscp52.bedrockify.client.features.loadingScreens.LoadingScreenWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
@Pseudo
@Mixin(io.github.bumblesoftware.fastload.client.BuildingTerrainScreen.class)
public abstract class BuildingTerrainScreen {

    @Shadow protected abstract Integer getLoadedChunkCount();

    @Shadow protected abstract Integer getBuiltChunkCount();

    @Shadow @Final private Text screenName;

    @Shadow @Final private Text screenTemplate;

    @Shadow @Final private Text preparingChunks;

    @Shadow @Final private Text buildingChunks;

    @Shadow @Final public int loadingAreaGoal;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lio/github/bumblesoftware/fastload/client/BuildingTerrainScreen;getLoadedChunkCount()Ljava/lang/Integer;",ordinal = 3),locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci, String loadedChunksString, String builtChunksString){
        MinecraftClient client = MinecraftClient.getInstance();
        if(!BedrockifyClient.getInstance().settings.isLoadingScreenEnabled()){
            return;
        }


        if (this.getLoadedChunkCount() == 0 && this.getBuiltChunkCount() == 0) {
            LoadingScreenWidget.getInstance().render(matrices, client.getWindow().getScaledWidth() / 2, client.getWindow().getScaledHeight() / 2, Text.translatable("fastload.buildingTerrain.starting"), null, -1);
        }else{
            Text content = screenTemplate.copyContentOnly().append("\n").append(this.preparingChunks.getString() + ": " + loadedChunksString).append("\n").append(this.buildingChunks.getString() + ": " + builtChunksString);
            int progress = (int)Math.ceil((this.getBuiltChunkCount()/this.loadingAreaGoal)*100);
            LoadingScreenWidget.getInstance().render(matrices, client.getWindow().getScaledWidth() / 2, client.getWindow().getScaledHeight() / 2, this.screenName, content, progress);
        }

        ci.cancel();

    }

}
