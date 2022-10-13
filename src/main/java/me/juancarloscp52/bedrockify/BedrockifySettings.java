package me.juancarloscp52.bedrockify;

public class BedrockifySettings {

    public boolean bedrockRecipes = true;
    public boolean dyingTrees = true;
    public boolean fireAspectLight = true;
    public boolean pickupAnimations = true;
    public boolean fernBonemeal = true;
    public boolean fallenTrees = true;

    public boolean isPickupAnimationsEnabled() {
        return pickupAnimations;
    }

    public boolean isBedrockRecipesEnabled() {
        return bedrockRecipes;
    }


}
