package net.bittercat.plerocottage.machine.compat.plerocottage;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.material.Fluid;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CookingPotRecipeCompat;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CuttingBoardRecipeCompat;
import net.bittercat.plerocottage.machine.recipes.BaseFermentationRecipe;
import net.bittercat.plerocottage.init.RecipeRegistry;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

import java.util.ArrayList;
import java.util.List;

public class SlotInputLimitManager {
    public static final List<Fluid> ICE_CREAM_MACHINE_FLUID_LIMIT = new ArrayList<>();
    public static final List<Ingredient> ICE_CREAM_MACHINE_INPUT_LIMIT = new ArrayList<>();
    public static final List<Ingredient> FRYER_INPUT_LIMIT = new ArrayList<>();
    public static final List<Ingredient> OVEN_INPUT_LIMIT = new ArrayList<>();
    public static final List<Ingredient> OVEN_CONTAINER_LIMIT = new ArrayList<>();
    public static final List<Ingredient> TEAPOT_CUP_LIMIT = new ArrayList<>();
    public static final List<Ingredient> TEAPOT_DRINK_LIMIT = new ArrayList<>();
    public static final List<Ingredient> TEAPOT_MATERIAL_LIMIT = new ArrayList<>();
    public static final List<Ingredient> FERMENTER_INPUT_LIMIT = new ArrayList<>();
    public static final List<Ingredient> BLENDER_INPUT_LIMIT = new ArrayList<>();
    private static final List<Ingredient> SERVER_CUTTING_BOARD_TOOL_LIMIT = new ArrayList<>();
    private static final List<Ingredient> CLIENT_CUTTING_BOARD_TOOL_LIMIT = new ArrayList<>();
    private static final List<Ingredient> SERVER_STOCK_POT_INPUT_LIMIT = new ArrayList<>();
    private static final List<Ingredient> CLIENT_STOCK_POT_INPUT_LIMIT = new ArrayList<>();
    public static final List<Ingredient> SAUCEPAN_AND_WHISK_INPUT_LIMIT = new ArrayList<>();

    public static List<Ingredient> cuttingBoardToolLimit() {
        return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER
            ? SERVER_CUTTING_BOARD_TOOL_LIMIT : CLIENT_CUTTING_BOARD_TOOL_LIMIT;
    }

    public static List<Ingredient> stockPotInputLimit() {
        return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER
            ? SERVER_STOCK_POT_INPUT_LIMIT : CLIENT_STOCK_POT_INPUT_LIMIT;
    }

    public static void reset(RecipeManager recipeManager) {
        ICE_CREAM_MACHINE_FLUID_LIMIT.clear();
        ICE_CREAM_MACHINE_INPUT_LIMIT.clear();
        FRYER_INPUT_LIMIT.clear();
        OVEN_INPUT_LIMIT.clear();
        OVEN_CONTAINER_LIMIT.clear();
        TEAPOT_CUP_LIMIT.clear();
        TEAPOT_DRINK_LIMIT.clear();
        TEAPOT_MATERIAL_LIMIT.clear();
        FERMENTER_INPUT_LIMIT.clear();
        BLENDER_INPUT_LIMIT.clear();
        cuttingBoardToolLimit().clear();
        stockPotInputLimit().clear();
        SAUCEPAN_AND_WHISK_INPUT_LIMIT.clear();

        initIceCreamMachineSlotLimit(recipeManager);
        initFryerSlotLimit(recipeManager);
        initOvenSlotLimit(recipeManager);
        initTeapotSlotLimit(recipeManager);
        initFermenterSlotLimit(recipeManager);
        initBlenderSlotLimit(recipeManager);
        initCuttingBoardToolLimit(recipeManager);
        initStockPotSlotLimit(recipeManager);
        initSaucepanAndWhiskSlotLimit(recipeManager);
    }

    public static void init(RecipeManager recipeManager) {
        reset(recipeManager);
    }

    public static void refreshCompatLimits(RecipeManager recipeManager) {
        cuttingBoardToolLimit().clear();
        stockPotInputLimit().clear();
        initCuttingBoardToolLimit(recipeManager);
        initStockPotSlotLimit(recipeManager);
    }

    public static void clearClientCompatLimits() {
        CLIENT_CUTTING_BOARD_TOOL_LIMIT.clear();
        CLIENT_STOCK_POT_INPUT_LIMIT.clear();
    }

    public static void initIceCreamMachineSlotLimit(RecipeManager recipeManager) {
        recipeManager.getAllRecipesFor(RecipeRegistry.ICE_CREAM_RECIPE_TYPE.get()).forEach(recipe -> {
            Fluid fluid = recipe.fluidStack.getFluid();
            if (!ICE_CREAM_MACHINE_FLUID_LIMIT.contains(fluid)) {
                ICE_CREAM_MACHINE_FLUID_LIMIT.add(fluid);
            }
            Ingredient container = recipe.container;
            if (!container.isEmpty() && !ingredientOverlaps(ICE_CREAM_MACHINE_INPUT_LIMIT, container)) {
                ICE_CREAM_MACHINE_INPUT_LIMIT.add(container);
            }
        });
    }

    public static void initFryerSlotLimit(RecipeManager recipeManager) {
        recipeManager.getAllRecipesFor(RecipeRegistry.FAST_FRY_RECIPE_TYPE.get()).forEach(recipe -> {
            Ingredient input = recipe.input;
            if (!ingredientOverlaps(FRYER_INPUT_LIMIT, input)) {
                FRYER_INPUT_LIMIT.add(input);
            }
        });
        SmokingRecipeManager.fastFryRecipes.forEach(recipe -> {
            Ingredient input = recipe.input;
            if (!ingredientOverlaps(FRYER_INPUT_LIMIT, input)) {
                FRYER_INPUT_LIMIT.add(input);
            }
        });
        recipeManager.getAllRecipesFor(RecipeRegistry.SLOW_FRY_RECIPE_TYPE.get()).forEach(recipe -> {
            Ingredient input = recipe.input;
            if (!ingredientOverlaps(FRYER_INPUT_LIMIT, input)) {
                FRYER_INPUT_LIMIT.add(input);
            }
        });
    }

    public static void initOvenSlotLimit(RecipeManager recipeManager) {
        recipeManager.getAllRecipesFor(RecipeRegistry.OVEN_RECIPE_TYPE.get()).forEach(recipe -> {
            recipe.input.forEach(ingredient -> {
                if (!ingredientOverlaps(OVEN_INPUT_LIMIT, ingredient)) {
                    OVEN_INPUT_LIMIT.add(ingredient);
                }
            });
            if (recipe.hasContainer() && !ingredientOverlaps(OVEN_CONTAINER_LIMIT, recipe.container)) {
                OVEN_CONTAINER_LIMIT.add(recipe.container);
            }
        });
        SmokingRecipeManager.ovenRecipes.forEach(recipe -> {
            recipe.input.forEach(ingredient -> {
                if (!ingredientOverlaps(OVEN_INPUT_LIMIT, ingredient)) {
                    OVEN_INPUT_LIMIT.add(ingredient);
                }
            });
        });
    }

    public static void initTeapotSlotLimit(RecipeManager recipeManager) {
        recipeManager.getAllRecipesFor(RecipeRegistry.TEAPOT_RECIPE_TYPE.get()).forEach(recipe -> {
            Ingredient container = recipe.container;
            if (!ingredientOverlaps(TEAPOT_CUP_LIMIT, container)) {
                TEAPOT_CUP_LIMIT.add(container);
            }
            Ingredient drink = recipe.input.get(0);
            if (!ingredientOverlaps(TEAPOT_DRINK_LIMIT, drink)) {
                TEAPOT_DRINK_LIMIT.add(drink);
            }
            Ingredient material = recipe.input.get(1);
            if (!ingredientOverlaps(TEAPOT_MATERIAL_LIMIT, material)) {
                TEAPOT_MATERIAL_LIMIT.add(material);
            }
        });
    }

    public static void initFermenterSlotLimit(RecipeManager recipeManager) {
        List<BaseFermentationRecipe> recipes = new ArrayList<>();
        recipes.addAll(recipeManager.getAllRecipesFor(RecipeRegistry.BRIGHT_FERMENTATION_RECIPE_TYPE.get()));
        recipes.addAll(recipeManager.getAllRecipesFor(RecipeRegistry.NORMAL_FERMENTATION_RECIPE_TYPE.get()));
        recipes.addAll(recipeManager.getAllRecipesFor(RecipeRegistry.DIM_FERMENTATION_RECIPE_TYPE.get()));
        recipes.forEach(recipe -> {
            Ingredient container = recipe.container;
            if (!ingredientOverlaps(FERMENTER_INPUT_LIMIT, container)) {
                FERMENTER_INPUT_LIMIT.add(container);
            }
        });
    }

    public static void initBlenderSlotLimit(RecipeManager recipeManager) {
        recipeManager.getAllRecipesFor(RecipeRegistry.BLENDER_RECIPE_TYPE.get()).forEach(recipe -> {
            Ingredient container = recipe.container;
            if (!ingredientOverlaps(BLENDER_INPUT_LIMIT, container)) {
                BLENDER_INPUT_LIMIT.add(container);
            }
        });
    }

    public static void initCuttingBoardToolLimit(RecipeManager recipeManager) {
        List<Ingredient> tools = cuttingBoardToolLimit();
        recipeManager.getAllRecipesFor(RecipeRegistry.CUTTING_BOARD_MULTIPLE_RECIPE_TYPE.get()).forEach(recipe -> {
            Ingredient tool = recipe.tool;
            if (!ingredientOverlaps(tools, tool)) {
                tools.add(tool);
            }
        });
        recipeManager.getAllRecipesFor(RecipeRegistry.CUTTING_BOARD_SINGLE_RECIPE_TYPE.get()).forEach(recipe -> {
            Ingredient tool = recipe.tool;
            if (!ingredientOverlaps(tools, tool)) {
                tools.add(tool);
            }
        });
        CuttingBoardRecipeCompat.recipes().forEach(recipe -> {
            Ingredient tool = recipe.tool;
            if (!ingredientOverlaps(tools, tool)) {
                tools.add(tool);
            }
        });
    }

    public static void initStockPotSlotLimit(RecipeManager recipeManager) {
        List<Ingredient> inputs = stockPotInputLimit();
        recipeManager.getAllRecipesFor(RecipeRegistry.STOCK_POT_RECIPE_TYPE.get()).forEach(recipe -> {
            Ingredient container = recipe.container;
            if (!ingredientOverlaps(inputs, container)) {
                inputs.add(container);
            }
        });
        CookingPotRecipeCompat.recipes().forEach(recipe -> {
            Ingredient container = recipe.container;
            if (!ingredientOverlaps(inputs, container)) {
                inputs.add(container);
            }
        });
    }

    public static void initSaucepanAndWhiskSlotLimit(RecipeManager recipeManager) {
        recipeManager.getAllRecipesFor(RecipeRegistry.SAUCEPAN_AND_WHISK_RECIPE_TYPE.get()).forEach(recipe -> {
            Ingredient container = recipe.container;
            if (!ingredientOverlaps(SAUCEPAN_AND_WHISK_INPUT_LIMIT, container)) {
                SAUCEPAN_AND_WHISK_INPUT_LIMIT.add(container);
            }
        });
    }

    private static boolean ingredientOverlaps(List<Ingredient> list, Ingredient newIngredient) {
        if (newIngredient.isEmpty()) return false;
        for (Ingredient existing : list) {
            if (existing.isEmpty()) continue;
            
            ItemStack[] existingItems = existing.getItems();
            for (ItemStack stack : existingItems) {
                if (!stack.isEmpty() && newIngredient.test(stack)) {
                    return true; 
                }
            }
        }
        return false;
    }
}
