package net.bittercat.plerocottage.machine.data;

import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fluids.FluidStack;
import net.bittercat.plerocottage.machine.blockentities.OvenBlockEntity;
import net.bittercat.plerocottage.machine.recipes.SaucepanAndWhiskRecipe;
import net.bittercat.plerocottage.machine.recipes.builder.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider implements IConditionBuilder {
    public RecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    public static NonNullList<Ingredient> toIngredientList(Item[] input) {
        Ingredient[] ingredients = new Ingredient[input.length];
        for (int i = 0; i < input.length; i++) {
            ingredients[i] = Ingredient.of(input[i]);
        }
        return NonNullList.of(Ingredient.EMPTY, ingredients);
    }

    public static void iceCreamRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, FluidStack fluidStack, Item container, Item output, Item[] input) {
        new IceCreamRecipeBuilder(fluidStack, Ingredient.of(container), toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void iceCreamRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, FluidStack fluidStack, Item output, Item[] input) {
        new IceCreamRecipeBuilder(fluidStack, toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void fastFryRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item input, Item output) {
        new FastFryRecipeBuilder(Ingredient.of(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void slowFryRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item input, Item output) {
        new SlowFryRecipeBuilder(Ingredient.of(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void ovenRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, OvenBlockEntity.Temperature temperature, OvenBlockEntity.MaxCookingTime maxCookingTime, Ingredient container, Item[] input, Item output, int count) {
        new OvenRecipeBuilder(temperature.temperature, maxCookingTime.cookingTime, container, toIngredientList(input), output, count).save(pFinishedRecipeConsumer, output);
    }

    public static void ovenRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, OvenBlockEntity.Temperature temperature, OvenBlockEntity.MaxCookingTime maxCookingTime, Item[] input, Item output, int count) {
        new OvenRecipeBuilder(temperature.temperature, maxCookingTime.cookingTime, toIngredientList(input), output, count).save(pFinishedRecipeConsumer, output);
    }

    public static void teapotRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item container, Item[] input, Item output) {
        new TeapotRecipeBuilder(Ingredient.of(container), toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void dimFermentationRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, int cookingTime, Item[] input, Item output) {
        new DimFermentationRecipeBuilder(cookingTime, toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void dimFermentationRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, int cookingTime, Item container, Item[] input, Item output) {
        new DimFermentationRecipeBuilder(cookingTime, Ingredient.of(container), toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void normalFermentationRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, int cookingTime, Item[] input, Item output) {
        new NormalFermentationRecipeBuilder(cookingTime, toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void normalFermentationRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, int cookingTime, Item container, Item[] input, Item output) {
        new NormalFermentationRecipeBuilder(cookingTime, Ingredient.of(container), toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void brightFermentationRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, int cookingTime, Item[] input, Item output) {
        new BrightFermentationRecipeBuilder(cookingTime, toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void brightFermentationRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, int cookingTime, Item container, Item[] input, Item output) {
        new BrightFermentationRecipeBuilder(cookingTime, Ingredient.of(container), toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void blenderRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item[] mainInput, Item[] secondaryInput, Item output) {
        new BlenderRecipeBuilder(toIngredientList(mainInput), toIngredientList(secondaryInput), output).save(pFinishedRecipeConsumer, output);
    }

    public static void blenderRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item[] mainInput, Item[] secondaryInput, Item output, Item outgrowth, float outgrowthChance) {
        new BlenderRecipeBuilder(toIngredientList(mainInput), toIngredientList(secondaryInput), output, outgrowth, outgrowthChance).save(pFinishedRecipeConsumer, output);
    }

    public static void blenderRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item container, Item[] mainInput, Item[] secondaryInput, Item output) {
        new BlenderRecipeBuilder(Ingredient.of(container), toIngredientList(mainInput), toIngredientList(secondaryInput), output).save(pFinishedRecipeConsumer, output);
    }

    public static void blenderRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item container, Item[] mainInput, Item[] secondaryInput, Item output, Item outgrowth, float outgrowthChance) {
        new BlenderRecipeBuilder(Ingredient.of(container), toIngredientList(mainInput), toIngredientList(secondaryInput), output, outgrowth, outgrowthChance).save(pFinishedRecipeConsumer, output);
    }

    public static void blenderRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item[] mainInput, Item[] secondaryInput, Item output, int outputCount) {
        new BlenderRecipeBuilder(toIngredientList(mainInput), toIngredientList(secondaryInput), output, outputCount).save(pFinishedRecipeConsumer, output);
    }

    public static void blenderRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item[] mainInput, Item[] secondaryInput, Item output, int outputCount, Item outgrowth, float outgrowthChance) {
        new BlenderRecipeBuilder(toIngredientList(mainInput), toIngredientList(secondaryInput), output, outputCount, outgrowth, outgrowthChance).save(pFinishedRecipeConsumer, output);
    }

    public static void blenderRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item container, Item[] mainInput, Item[] secondaryInput, Item output, int outputCount) {
        new BlenderRecipeBuilder(Ingredient.of(container), toIngredientList(mainInput), toIngredientList(secondaryInput), output, outputCount).save(pFinishedRecipeConsumer, output);
    }

    public static void blenderRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item container, Item[] mainInput, Item[] secondaryInput, Item output, int outputCount, Item outgrowth, float outgrowthChance) {
        new BlenderRecipeBuilder(Ingredient.of(container), toIngredientList(mainInput), toIngredientList(secondaryInput), output, outputCount, outgrowth, outgrowthChance).save(pFinishedRecipeConsumer, output);
    }

    public static void cuttingBoardSingleRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> tool, Item input, Item output) {
        new CuttingBoardSingleRecipeBuilder(Ingredient.of(tool), Ingredient.of(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void cuttingBoardSingleRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> tool, Item input, Item output, int outputCount) {
        new CuttingBoardSingleRecipeBuilder(Ingredient.of(tool), Ingredient.of(input), output, outputCount).save(pFinishedRecipeConsumer, output);
    }

    public static void cuttingBoardSingleRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, TagKey<Item> tool, Item input, Item output, int outputCount, Map<Item, Float> outgrowths) {
        new CuttingBoardSingleRecipeBuilder(Ingredient.of(tool), Ingredient.of(input), output, outputCount, outgrowths).save(pFinishedRecipeConsumer, output);
    }

    public static void cuttingBoardMultipleRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, boolean isShaped, TagKey<Item> tool, Item[] input, Item output) {
        new CuttingBoardMultipleRecipeBuilder(isShaped, Ingredient.of(tool), toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void cuttingBoardMultipleRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, boolean isShaped, TagKey<Item> tool, Item[] input, Item output, Map<Item, Float> outgrowths) {
        new CuttingBoardMultipleRecipeBuilder(isShaped, Ingredient.of(tool), toIngredientList(input), output, outgrowths).save(pFinishedRecipeConsumer, output);
    }

    public static void cuttingBoardMultipleRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, boolean isShaped, Item[] input, Item output) {
        new CuttingBoardMultipleRecipeBuilder(isShaped, toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    public static void stockPotRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, int cookingTime, Item container, Item[] mainInput, Item[] secondaryInput, Item output) {
        new StockPotRecipeBuilder(cookingTime, Ingredient.of(container), toIngredientList(mainInput), toIngredientList(secondaryInput), output).save(pFinishedRecipeConsumer, output);
    }

    public static void saucepanAndWhiskRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, SaucepanAndWhiskRecipe.HeatState heatState, Item container, Item[] mainInput, Item[] secondaryInput, Item output) {
        new SaucepanAndWhiskRecipeBuilder(heatState, Ingredient.of(container), toIngredientList(mainInput), toIngredientList(secondaryInput), output).save(pFinishedRecipeConsumer, output);
    }

    public static void shakerRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item container, Item[] input, Item output) {
        new ShakerRecipeBuilder(Ingredient.of(container), toIngredientList(input), output).save(pFinishedRecipeConsumer, output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

    }
}
