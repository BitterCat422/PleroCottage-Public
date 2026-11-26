package net.bittercat.plerocottage.machine.recipes.builder;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.bittercat.plerocottage.init.RecipeRegistry;

public class BrightFermentationRecipeBuilder extends BaseFermentationRecipeBuilder {
    public BrightFermentationRecipeBuilder(int cookingTime, NonNullList<Ingredient> input, Item output) {
        super(cookingTime, input, output);
    }

    public BrightFermentationRecipeBuilder(int cookingTime, Ingredient container, NonNullList<Ingredient> input, Item output) {
        super(cookingTime, container, input, output);
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return RecipeRegistry.BRIGHT_FERMENTATION_RECIPE_SERIALIZER.get();
    }

    @Override
    public String getRecipePath() {
        return "bright_fermentation/";
    }
}
