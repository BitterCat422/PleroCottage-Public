package net.bittercat.plerocottage.machine.compat.farmersdelight;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.bittercat.plerocottage.machine.recipes.StockPotRecipe;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

import java.util.ArrayList;
import java.util.List;

import static net.bittercat.plerocottage.machine.blockentities.StockPotBlockEntity.MAIN_INPUT_SLOTS;
import static net.bittercat.plerocottage.machine.blockentities.StockPotBlockEntity.SECONDARY_INPUT_SLOTS;

public class CookingPotRecipeCompat {
    private static volatile List<StockPotRecipe> serverRecipes = List.of();
    private static volatile List<StockPotRecipe> clientRecipes = List.of();

    public static List<StockPotRecipe> recipes() {
        return isServerThread() ? serverRecipes : clientRecipes;
    }

    private static boolean isServerThread() {
        return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER;
    }

    private static void update(List<StockPotRecipe> recipes) {
        if (isServerThread()) {
            serverRecipes = List.copyOf(recipes);
        } else {
            clientRecipes = List.copyOf(recipes);
        }
    }

    public static void reset(RecipeManager recipeManager) {
        List<StockPotRecipe> recipes = new ArrayList<>();
        List<CookingPotRecipe> cookingPotRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.COOKING.get());
        cookingPotRecipes.forEach(recipe -> recipes.add(toStockPotRecipe(recipe)));
        update(recipes);
    }

    public static void init(RecipeManager recipeManager) {
        if (!FarmersDelightCompat.canCookingPotCompat() || !FarmersDelightCompat.isLoad()) {
            update(List.of());
            return;
        }
        reset(recipeManager);
    }

    public static void clearClient() {
        clientRecipes = List.of();
    }

    public static StockPotRecipe toStockPotRecipe(CookingPotRecipe recipe) {
        NonNullList<Ingredient> mainInput = NonNullList.withSize(MAIN_INPUT_SLOTS.length, Ingredient.EMPTY);
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            mainInput.set(i, recipe.getIngredients().get(i));
        }
        return new StockPotRecipe(
                recipe.getId(),
                recipe.getCookTime(),
                Ingredient.of(recipe.getOutputContainer()),
                mainInput,
                NonNullList.withSize(SECONDARY_INPUT_SLOTS.length, Ingredient.EMPTY),
                recipe.getResultItem(null)
        );
    }
}
