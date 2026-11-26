package net.bittercat.plerocottage.machine.compat.farmersdelight;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.bittercat.plerocottage.machine.recipes.CuttingBoardSingleRecipe;
import net.bittercat.plerocottage.init.RecipeRegistry;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuttingBoardRecipeCompat {
    private static volatile List<CuttingBoardSingleRecipe> serverRecipes = List.of();
    private static volatile List<CuttingBoardSingleRecipe> clientRecipes = List.of();

    public static List<CuttingBoardSingleRecipe> recipes() {
        return isServerThread() ? serverRecipes : clientRecipes;
    }

    private static boolean isServerThread() {
        return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER;
    }

    private static void update(List<CuttingBoardSingleRecipe> recipes) {
        if (isServerThread()) {
            serverRecipes = List.copyOf(recipes);
        } else {
            clientRecipes = List.copyOf(recipes);
        }
    }

    public static void reset(RecipeManager recipeManager) {
        List<CuttingBoardSingleRecipe> recipes = new ArrayList<>();
        List<CuttingBoardRecipe> cuttingRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.CUTTING.get());
        List<CuttingBoardSingleRecipe> singleRecipes = recipeManager.getAllRecipesFor(RecipeRegistry.CUTTING_BOARD_SINGLE_RECIPE_TYPE.get());
        cuttingRecipes.forEach(recipe -> {
            if (recipe.getResults().size() <= 6
                    && singleRecipes.stream().noneMatch(singleRecipe -> singleRecipe.input.equals(recipe.getIngredients().get(0)))) {
                recipes.add(toCuttingBoardRecipe(recipe));
            }
        });
        update(recipes);
    }

    public static void init(RecipeManager recipeManager) {
        if (!FarmersDelightCompat.canCuttingBoardCompat() || !FarmersDelightCompat.isLoad()) {
            update(List.of());
            return;
        }
        reset(recipeManager);
    }

    public static void clearClient() {
        clientRecipes = List.of();
    }

    public static CuttingBoardSingleRecipe toCuttingBoardRecipe(CuttingBoardRecipe recipe) {
        Map<ItemStack, Float> outgrowths = new HashMap<>();
        for (int i = 1; i < recipe.getRollableResults().size(); i++) {
            ChanceResult chanceResult = recipe.getRollableResults().get(i);
            outgrowths.put(chanceResult.getStack(), chanceResult.getChance());
        }
        return new CuttingBoardSingleRecipe(
                recipe.getId(),
                recipe.getTool(),
                recipe.getIngredients().get(0),
                recipe.getResultItem(null),
                outgrowths
        );
    }
}
