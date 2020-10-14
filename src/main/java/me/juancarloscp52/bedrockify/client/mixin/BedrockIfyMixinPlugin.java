package me.juancarloscp52.bedrockify.client.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class BedrockIfyMixinPlugin  implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if(mixinClassName.equals("me.juancarloscp52.bedrockify.client.mixin.TitleScreenMixin") && FabricLoader.getInstance().isModLoaded("slight-gui-modifications")){
            LogManager.getLogger().info("BedrockIfy compatibility with Slight gui modifications enabled.");
            return false;
        }
        if(mixinClassName.equals("me.juancarloscp52.bedrockify.client.mixin.ItemTooltipsMixin") && FabricLoader.getInstance().isModLoaded("held-item-info")){
            LogManager.getLogger().info("The mod \"Held Item Info\" has been detected. This mod is not totally compatible with BedrockIfy. BedrockIfy Held Item Tooltips has been disabled.");
            return false;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
