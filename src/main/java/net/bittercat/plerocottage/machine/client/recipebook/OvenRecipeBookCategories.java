package net.bittercat.plerocottage.machine.client.recipebook;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.RecipeRegistry;

import java.util.function.Supplier;

public final class OvenRecipeBookCategories {
    public static final Supplier<RecipeBookCategories> SEARCH = Suppliers.memoize(
            () -> RecipeBookCategories.create("OVEN_SEARCH", new ItemStack(Items.COMPASS))
    );
    public static final Supplier<RecipeBookCategories> NATIVE_RECIPES = Suppliers.memoize(
            () -> RecipeBookCategories.create("OVEN_NATIVE_RECIPES", new ItemStack(ItemRegistry.OVEN.get()))
    );
    public static final Supplier<RecipeBookCategories> SMOKING_RECIPES = Suppliers.memoize(
            () -> RecipeBookCategories.create("OVEN_SMOKING_RECIPES", new ItemStack(Items.SMOKER))
    );

    private OvenRecipeBookCategories() {
    }

    public static void register(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(
                PleroCottageMachine.RECIPE_TYPE_OVEN,
                ImmutableList.of(SEARCH.get(), NATIVE_RECIPES.get(), SMOKING_RECIPES.get())
        );
        event.registerAggregateCategory(
                SEARCH.get(),
                ImmutableList.of(NATIVE_RECIPES.get(), RecipeBookCategories.SMOKER_FOOD)
        );
        event.registerRecipeCategoryFinder(
                RecipeRegistry.OVEN_RECIPE_TYPE.get(),
                recipe -> NATIVE_RECIPES.get()
        );
        // 原版烟熏配方直接归入 SMOKER_FOOD，搜索页也必须聚合这个叶分类。
        event.registerAggregateCategory(SMOKING_RECIPES.get(), ImmutableList.of(RecipeBookCategories.SMOKER_FOOD));
    }
}
