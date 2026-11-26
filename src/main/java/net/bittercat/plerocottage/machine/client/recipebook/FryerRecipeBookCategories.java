package net.bittercat.plerocottage.machine.client.recipebook;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraft.nbt.CompoundTag;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.RecipeRegistry;

import java.util.function.Supplier;

public final class FryerRecipeBookCategories {
    public static final Supplier<RecipeBookCategories> SEARCH = Suppliers.memoize(
            () -> RecipeBookCategories.create("FRYER_SEARCH", new ItemStack(Items.COMPASS))
    );
    public static final Supplier<RecipeBookCategories> FAST_RECIPES = Suppliers.memoize(
            () -> RecipeBookCategories.create("FRYER_FAST_RECIPES", createIcon("fast"))
    );
    public static final Supplier<RecipeBookCategories> SLOW_RECIPES = Suppliers.memoize(
            () -> RecipeBookCategories.create("FRYER_SLOW_RECIPES", createIcon("slow"))
    );
    public static final Supplier<RecipeBookCategories> SMOKING_RECIPES = Suppliers.memoize(
            () -> RecipeBookCategories.create("FRYER_SMOKING_RECIPES", new ItemStack(Items.SMOKER))
    );

    private FryerRecipeBookCategories() {
    }

    private static ItemStack createIcon(String icon) {
        ItemStack stack = new ItemStack(ItemRegistry.FRYER.get());
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(RecipeBookIconModelProperties.NBT_KEY, icon);
        return stack;
    }

    public static void register(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(
                PleroCottageMachine.RECIPE_TYPE_FRYER,
                ImmutableList.of(SEARCH.get(), FAST_RECIPES.get(), SLOW_RECIPES.get(), SMOKING_RECIPES.get())
        );
        event.registerAggregateCategory(
                SEARCH.get(),
                ImmutableList.of(FAST_RECIPES.get(), SLOW_RECIPES.get(), RecipeBookCategories.SMOKER_FOOD)
        );
        event.registerRecipeCategoryFinder(
                RecipeRegistry.FAST_FRY_RECIPE_TYPE.get(),
                recipe -> FAST_RECIPES.get()
        );
        event.registerRecipeCategoryFinder(
                RecipeRegistry.SLOW_FRY_RECIPE_TYPE.get(),
                recipe -> SLOW_RECIPES.get()
        );
        event.registerAggregateCategory(
                SMOKING_RECIPES.get(),
                ImmutableList.of(RecipeBookCategories.SMOKER_FOOD)
        );
    }
}
