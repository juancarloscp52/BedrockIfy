package me.juancarloscp52.bedrockify.client.mixin;

import com.google.gson.JsonElement;
import me.juancarloscp52.bedrockify.client.BedrockifyClient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Shadow public abstract Optional<? extends Recipe<?>> get(Identifier id);

    @Inject(method = "apply", at=@At("HEAD"))
    public void applyRecipes(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info){
        HashMap<Identifier, JsonElement> bedrockifyRecipes = new HashMap<>();
        Iterator<Map.Entry<Identifier, JsonElement>> mapIterator = map.entrySet().iterator();
        final List<String> exceptions = Arrays.asList("dark_prismarine_extra", "black_concrete_powder_ink_sac", "white_concrete_powder_bone_meal", "brown_concrete_powder_cocoa_beans", "blue_concrete_powder_lapis_lazuli",
                "white_wool_from_colored", "white_wool_bone_meal","black_wool_ink_sac", "blue_wool_lapis_lazuli", "brown_wool_cocoa_beans",
                "black_bed_from_white_bed_ink_sac","blue_bed_from_white_bed_lapis_lazuli", "brown_bed_from_white_bed_cocoa_beans", "white_bed_from_white_bed", "white_bed_from_white_bed_bone_meal",
                "black_carpet_from_white_carpet_ink_sac", "blue_carpet_from_white_carpet", "brown_carpet_from_white_carpet_cocoa_beans",
                "string_from_cobweb", "white_terracotta_bone_meal", "black_terracotta_ink_sac", "brown_terracotta_cocoa_beans", "blue_terracotta_lapis_lazuli",
                "white_stained_glass_bone_meal", "blue_stained_glass_lapis_lazuli", "black_stained_glass_ink_sac", "brown_stained_glass_cocoa_beans",
                "black_stained_glass_pane_from_glass_pane_ink_sac", "blue_stained_glass_pane_from_glass_pane_lapis_lazuli", "brown_stained_glass_pane_from_glass_pane_cocoa_beans", "white_stained_glass_pane_from_glass_pane_bone_meal",
                "lime_dye_with_bone_meal", "pink_dye_from_red_dye_bone_meal", "light_gray_dye_from_gray_dye_bone_meal", "light_gray_dye_from_black_dye_bone_meal", "purple_dye_from_lapis_lazuli", "cyan_dye_from_lapis_lazuli", "light_blue_dye_from_blue_dye_bone_meal", "light_blue_dye_from_lapis_lazuli_bone_meal", "light_blue_dye_from_lapis_lazuli_white_dye",
                "magenta_dye_from_lapis_lazuli_red_pink", "magenta_dye_from_blue_red_bone_meal_dye", "magenta_dye_from_lapis_lazuli_red_bone_meal_dye", "magenta_dye_from_lapis_lazuli_red_white_dye", "gray_dye_ink_sac","gray_dye_ink_sac_bone_meal","gray_dye_bone_meal");
        while (mapIterator.hasNext()){
            Map.Entry<Identifier, JsonElement> elem = mapIterator.next();
            if(elem.getKey().getNamespace().equals("bedrockify") && (!exceptions.contains(elem.getKey().getPath()) || !BedrockifyClient.getInstance().settings.isBedrockRecipesEnabled())){
                if(BedrockifyClient.getInstance().settings.isBedrockRecipesEnabled())
                    bedrockifyRecipes.put(new Identifier("minecraft", elem.getKey().getPath()), elem.getValue());
                mapIterator.remove();
            }
        }
        for(Map.Entry<Identifier,JsonElement> elem: bedrockifyRecipes.entrySet()){
            map.replace(elem.getKey(),elem.getValue());
        }
    }
}
