package net.bittercat.plerocottage.machine.recipes.builder;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public abstract class BaseFermentationRecipeBuilder extends BaseRecipeBuilder {
    public final int maxCookingTime;
    public final Ingredient container;
    public final NonNullList<Ingredient> input;
    public final Item output;

    public BaseFermentationRecipeBuilder(int maxCookingTime, NonNullList<Ingredient> input, Item output) {
        this(maxCookingTime, Ingredient.EMPTY, input, output);
    }

    public BaseFermentationRecipeBuilder(int maxCookingTime, Ingredient container, NonNullList<Ingredient> input, Item output) {
        this.maxCookingTime = maxCookingTime;
        this.container = container;
        this.input = input;
        this.output = output;
    }

    @Override
    public FinishedRecipe createResult(ResourceLocation path, RecipeSerializer<?> recipeSerializer) {
        return new Result(
                path,
                recipeSerializer,
                this.maxCookingTime,
                this.container,
                this.input,
                this.output
        );
    }

    public static class Result extends BaseResult {
        public final int maxCookingTime;
        public final Ingredient container;
        public final NonNullList<Ingredient> input;
        public final Item output;

        public Result(ResourceLocation id, RecipeSerializer<?> recipeSerializer, int maxCookingTime, Ingredient container, NonNullList<Ingredient> input, Item output) {
            super(id, recipeSerializer);
            this.maxCookingTime = maxCookingTime;
            this.container = container;
            this.input = input;
            this.output = output;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            this.addMaxCookingTime(pJson, this.maxCookingTime);
            this.addContainer(pJson, this.container);
            this.addInput(pJson, this.input);
            this.addOutput(pJson, this.output);
        }
    }
}
