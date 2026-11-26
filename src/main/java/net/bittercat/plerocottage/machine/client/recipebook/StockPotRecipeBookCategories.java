package net.bittercat.plerocottage.machine.client.recipebook;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.farmersdelight.FarmersDelightCompat;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.RecipeRegistry;
import vectorwing.farmersdelight.client.recipebook.RecipeCategories;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;
import java.util.List;

public final class StockPotRecipeBookCategories {
    public static final Supplier<RecipeBookCategories> SEARCH = Suppliers.memoize(
            () -> RecipeBookCategories.create("STOCK_POT_SEARCH", new ItemStack(Items.COMPASS))
    );
    private static final Supplier<RecipeBookCategories> NATIVE_SEARCH = Suppliers.memoize(
            () -> RecipeBookCategories.create("STOCK_POT_NATIVE_SEARCH", new ItemStack(Items.COMPASS))
    );
    public static final Supplier<RecipeBookCategories> NATIVE_RECIPES = Suppliers.memoize(
            () -> RecipeBookCategories.create("STOCK_POT_NATIVE_RECIPES", new ItemStack(ItemRegistry.STOCK_POT.get()))
    );
    public static final Supplier<RecipeBookCategories> FARMERS_DELIGHT_RECIPES = Suppliers.memoize(
            () -> RecipeBookCategories.create(
                    "STOCK_POT_FARMERS_DELIGHT_RECIPES",
                    new ItemStack(ModItems.COOKING_POT.get())
            )
    );

    private StockPotRecipeBookCategories() {
    }

    public static List<RecipeBookCategories> categories() {
        if (FarmersDelightCompat.isLoad() && FarmersDelightCompat.canCookingPotCompat()) {
            return ImmutableList.of(SEARCH.get(), NATIVE_RECIPES.get(), FARMERS_DELIGHT_RECIPES.get());
        }
        return ImmutableList.of(NATIVE_SEARCH.get(), NATIVE_RECIPES.get());
    }

    public static void register(RegisterRecipeBookCategoriesEvent event) {
        
        event.registerAggregateCategory(NATIVE_SEARCH.get(), ImmutableList.of(NATIVE_RECIPES.get()));
        event.registerBookCategories(
                PleroCottageMachine.RECIPE_TYPE_STOCK_POT,
                FarmersDelightCompat.isLoad()
                        ? ImmutableList.of(SEARCH.get(), NATIVE_RECIPES.get(), FARMERS_DELIGHT_RECIPES.get())
                        : ImmutableList.of(SEARCH.get(), NATIVE_RECIPES.get())
        );
        event.registerAggregateCategory(SEARCH.get(), FarmersDelightCompat.isLoad()
                ? ImmutableList.of(
                        NATIVE_RECIPES.get(),
                        RecipeCategories.COOKING_MEALS.get(),
                        RecipeCategories.COOKING_DRINKS.get(),
                        RecipeCategories.COOKING_MISC.get()
                )
                : ImmutableList.of(NATIVE_RECIPES.get()));
        event.registerRecipeCategoryFinder(
                RecipeRegistry.STOCK_POT_RECIPE_TYPE.get(),
                recipe -> NATIVE_RECIPES.get()
        );
        if (FarmersDelightCompat.isLoad()) {
            
            event.registerAggregateCategory(
                    FARMERS_DELIGHT_RECIPES.get(),
                    ImmutableList.of(
                    RecipeCategories.COOKING_MEALS.get(),
                    RecipeCategories.COOKING_DRINKS.get(),
                    RecipeCategories.COOKING_MISC.get()
                    )
            );
        }
    }
}
