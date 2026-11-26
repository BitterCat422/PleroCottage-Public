package net.bittercat.plerocottage.machine.recipes.builder;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.bittercat.plerocottage.init.RecipeRegistry;

public class ShakerRecipeBuilder extends BaseRecipeBuilder {
    public final Ingredient container;
    public final NonNullList<Ingredient> input;
    public final Item output;

    public ShakerRecipeBuilder(Ingredient container, NonNullList<Ingredient> input, Item output) {
        this.container = container;
        this.input = input;
        this.output = output;
    }

    @Override
    public String getRecipePath() {
        return "shaker/";
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return RecipeRegistry.SHAKER_RECIPE_SERIALIZER.get();
    }

    @Override
    public FinishedRecipe createResult(ResourceLocation path, RecipeSerializer<?> recipeSerializer) {
        return new Result(
                path,
                recipeSerializer,
                this.container,
                this.input,
                this.output
        );
    }

    public static class Result extends BaseResult {
        public final Ingredient container;
        public final NonNullList<Ingredient> input;
        public final Item output;

        public Result(ResourceLocation id, RecipeSerializer<?> recipeSerializer, Ingredient container, NonNullList<Ingredient> input, Item output) {
            super(id, recipeSerializer);
            this.container = container;
            this.input = input;
            this.output = output;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            this.addContainer(pJson, this.container);
            this.addInput(pJson, this.input);
            this.addOutput(pJson, this.output);
        }
    }
}
