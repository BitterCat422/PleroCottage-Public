package net.bittercat.plerocottage.machine.recipes.builder;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.bittercat.plerocottage.init.RecipeRegistry;

public class DimFermentationRecipeBuilder extends BaseFermentationRecipeBuilder {
    public DimFermentationRecipeBuilder(int cookingTime, NonNullList<Ingredient> input, Item output) {
        super(cookingTime, input, output);
    }

    public DimFermentationRecipeBuilder(int cookingTime, Ingredient container, NonNullList<Ingredient> input, Item output) {
        super(cookingTime, container, input, output);
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return RecipeRegistry.DIM_FERMENTATION_RECIPE_SERIALIZER.get();
    }

    @Override
    public String getRecipePath() {
        return "dim_fermentation/";
    }
}
