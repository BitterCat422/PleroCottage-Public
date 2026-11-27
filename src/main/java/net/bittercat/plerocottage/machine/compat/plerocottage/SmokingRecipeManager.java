package net.bittercat.plerocottage.machine.compat.plerocottage;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.bittercat.plerocottage.machine.recipes.FastFryRecipe;
import net.bittercat.plerocottage.machine.recipes.OvenRecipe;

import java.util.List;

import static net.bittercat.plerocottage.machine.blockentities.OvenBlockEntity.INPUT_SLOTS;

public class SmokingRecipeManager {
    // 服务端和客户端线程都会重建：整表替换，读的一方不会看到清空到一半的表
    public static volatile List<FastFryRecipe> fastFryRecipes = List.of();

    public static volatile List<OvenRecipe> ovenRecipes = List.of();

    public static void reset(RecipeManager recipeManager) {
        List<SmokingRecipe> smokingRecipes = recipeManager.getAllRecipesFor(RecipeType.SMOKING);
        fastFryRecipes = smokingRecipes.stream().map(SmokingRecipeManager::toFastFryRecipe).toList();
        ovenRecipes = smokingRecipes.stream().map(SmokingRecipeManager::toOvenRecipe).toList();
    }

    public static void init(RecipeManager recipeManager) {
        reset(recipeManager);
    }

    public static FastFryRecipe toFastFryRecipe(SmokingRecipe recipe) {
        return new FastFryRecipe(recipe.getId(), recipe.getIngredients().get(0), recipe.getResultItem(null));
    }

    public static OvenRecipe toOvenRecipe(SmokingRecipe recipe) {
        NonNullList<Ingredient> input = NonNullList.withSize(INPUT_SLOTS.length, Ingredient.EMPTY);
        input.set(0, recipe.getIngredients().get(0));
        return new OvenRecipe(recipe.getId(), 200, 5 * 20, Ingredient.EMPTY, input, recipe.getResultItem(null));
    }
}
