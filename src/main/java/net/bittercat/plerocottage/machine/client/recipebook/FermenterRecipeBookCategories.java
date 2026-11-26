package net.bittercat.plerocottage.machine.client.recipebook;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.RecipeRegistry;

import java.util.function.Supplier;

public final class FermenterRecipeBookCategories {
    public static final Supplier<RecipeBookCategories> SEARCH = Suppliers.memoize(
            () -> RecipeBookCategories.create("FERMENTER_SEARCH", new ItemStack(Items.COMPASS))
    );
    public static final Supplier<RecipeBookCategories> DIM = Suppliers.memoize(
            () -> RecipeBookCategories.create("FERMENTER_DIM", createIcon("dim"))
    );
    public static final Supplier<RecipeBookCategories> NORMAL = Suppliers.memoize(
            () -> RecipeBookCategories.create("FERMENTER_NORMAL", createIcon("normal"))
    );
    public static final Supplier<RecipeBookCategories> BRIGHT = Suppliers.memoize(
            () -> RecipeBookCategories.create("FERMENTER_BRIGHT", createIcon("bright"))
    );

    private FermenterRecipeBookCategories() {
    }

    private static ItemStack createIcon(String icon) {
        ItemStack stack = new ItemStack(ItemRegistry.FERMENTER.get());
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(RecipeBookIconModelProperties.NBT_KEY, icon);
        return stack;
    }

    public static void register(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(
                PleroCottageMachine.RECIPE_TYPE_FERMENTER,
                ImmutableList.of(SEARCH.get(), DIM.get(), NORMAL.get(), BRIGHT.get())
        );
        event.registerAggregateCategory(
                SEARCH.get(),
                ImmutableList.of(DIM.get(), NORMAL.get(), BRIGHT.get())
        );
        event.registerRecipeCategoryFinder(
                RecipeRegistry.DIM_FERMENTATION_RECIPE_TYPE.get(),
                recipe -> DIM.get()
        );
        event.registerRecipeCategoryFinder(
                RecipeRegistry.NORMAL_FERMENTATION_RECIPE_TYPE.get(),
                recipe -> NORMAL.get()
        );
        event.registerRecipeCategoryFinder(
                RecipeRegistry.BRIGHT_FERMENTATION_RECIPE_TYPE.get(),
                recipe -> BRIGHT.get()
        );
    }
}
