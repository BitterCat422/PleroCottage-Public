package net.bittercat.plerocottage.machine.compat.farmersdelight;

import java.util.LinkedHashMap;
import java.util.Arrays;
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
            // 本模组的配方格式里主产物必出：对方主产物带概率的配方不转换，否则概率产物会变成必出
            if (recipe.getResults().size() <= 6
                    && !recipe.getRollableResults().isEmpty()
                    && recipe.getRollableResults().get(0).getChance() >= 1.0F
                    // Ingredient 没有按内容比较的 equals，只能看本模组配方的原料是否接受对方的任一物品
                    && singleRecipes.stream().noneMatch(singleRecipe -> Arrays.stream(
                            recipe.getIngredients().get(0).getItems()).anyMatch(singleRecipe.input::test))) {
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
        Map<ItemStack, Float> outgrowths = new LinkedHashMap<>();
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
