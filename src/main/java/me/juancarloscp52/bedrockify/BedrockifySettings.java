package me.juancarloscp52.bedrockify;

public class BedrockifySettings {

    public boolean bedrockRecipes = true;
    public boolean dyingTrees = true;
    public boolean fireAspectLight = true;
    public boolean pickupAnimations = true;
    public boolean isPickupAnimationsEnabled() {
        return pickupAnimations;
    }


    public boolean isDyingTreesEnabled() {
        return this.dyingTrees;
    }
    public boolean isBedrockRecipesEnabled() {
        return bedrockRecipes;
    }


}
