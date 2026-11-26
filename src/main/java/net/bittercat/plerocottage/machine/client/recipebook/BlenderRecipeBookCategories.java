package net.bittercat.plerocottage.machine.client.recipebook;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.RecipeRegistry;

import java.util.function.Supplier;

public final class BlenderRecipeBookCategories {
    public static final Supplier<RecipeBookCategories> SEARCH = Suppliers.memoize(
            () -> RecipeBookCategories.create("BLENDER_SEARCH", new ItemStack(net.minecraft.world.item.Items.COMPASS))
    );
    public static final Supplier<RecipeBookCategories> RECIPES = Suppliers.memoize(
            () -> RecipeBookCategories.create("BLENDER_RECIPES", new ItemStack(ItemRegistry.BLENDER.get()))
    );

    private BlenderRecipeBookCategories() {
    }

    public static void register(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(
                PleroCottageMachine.RECIPE_TYPE_BLENDER,
                ImmutableList.of(SEARCH.get(), RECIPES.get())
        );
        event.registerAggregateCategory(SEARCH.get(), ImmutableList.of(RECIPES.get()));
        event.registerRecipeCategoryFinder(RecipeRegistry.BLENDER_RECIPE_TYPE.get(), recipe -> RECIPES.get());
    }
}
