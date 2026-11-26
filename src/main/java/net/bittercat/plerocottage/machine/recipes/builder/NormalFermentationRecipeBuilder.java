package net.bittercat.plerocottage.machine.recipes.builder;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.bittercat.plerocottage.init.RecipeRegistry;

public class NormalFermentationRecipeBuilder extends BaseFermentationRecipeBuilder {
    public NormalFermentationRecipeBuilder(int cookingTime, NonNullList<Ingredient> input, Item output) {
        super(cookingTime, input, output);
    }

    public NormalFermentationRecipeBuilder(int cookingTime, Ingredient container, NonNullList<Ingredient> input, Item output) {
        super(cookingTime, container, input, output);
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return RecipeRegistry.NORMAL_FERMENTATION_RECIPE_SERIALIZER.get();
    }

    @Override
    public String getRecipePath() {
        return "normal_fermentation/";
    }
}
