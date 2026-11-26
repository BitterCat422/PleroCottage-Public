package net.bittercat.plerocottage.machine.recipes.builder;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.bittercat.plerocottage.init.RecipeRegistry;

public class OvenRecipeBuilder extends BaseRecipeBuilder {
    public final int temperature;
    public final int cookingTime;
    public final Ingredient container;
    public final NonNullList<Ingredient> input;
    public final Item output;
    public final int outputCount;

    public OvenRecipeBuilder(int temperature, int cookingTime, NonNullList<Ingredient> input,
                             Item output, int outputCount) {
        this(temperature, cookingTime, Ingredient.EMPTY, input, output, outputCount);
    }

    public OvenRecipeBuilder(int temperature, int cookingTime, Ingredient container,
                             NonNullList<Ingredient> input, Item output, int outputCount) {
        this.temperature = temperature;
        this.cookingTime = cookingTime;
        this.container = container;
        this.input = input;
        this.output = output;
        this.outputCount = outputCount;
    }

    @Override
    public String getRecipePath() {
        return "oven/";
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return RecipeRegistry.OVEN_RECIPE_SERIALIZER.get();
    }

    @Override
    public FinishedRecipe createResult(ResourceLocation path, RecipeSerializer<?> recipeSerializer) {
        return new Result(
                path,
                recipeSerializer,
                this.temperature,
                this.cookingTime,
                this.container,
                this.input,
                this.output,
                this.outputCount
        );
    }

    public static class Result extends BaseResult {
        public final int temperature;
        public final int cookingTime;
        public final Ingredient container;
        public final NonNullList<Ingredient> input;
        public final Item output;
        public final int outputCount;

        public Result(ResourceLocation id, RecipeSerializer<?> recipeSerializer, int temperature, int cookingTime,
                      Ingredient container, NonNullList<Ingredient> input, Item output, int outputCount) {
            super(id, recipeSerializer);
            this.temperature = temperature;
            this.cookingTime = cookingTime;
            this.container = container;
            this.input = input;
            this.output = output;
            this.outputCount = outputCount;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            this.addNumber(pJson, "temperature", this.temperature);
            this.addMaxCookingTime(pJson, this.cookingTime);
            this.addContainer(pJson, this.container);
            this.addInput(pJson, this.input);
            this.addOutput(pJson, this.output, this.outputCount);
        }
    }
}
