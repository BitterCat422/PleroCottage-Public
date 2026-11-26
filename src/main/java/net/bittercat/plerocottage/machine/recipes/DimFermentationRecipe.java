package net.bittercat.plerocottage.machine.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.bittercat.plerocottage.machine.blockentities.FermenterBlockEntity;
import net.bittercat.plerocottage.init.RecipeRegistry;

public class DimFermentationRecipe extends BaseFermentationRecipe {
    public static final FermenterBlockEntity.LightState LIGHT_STATE = FermenterBlockEntity.LightState.DIM;

    public DimFermentationRecipe(ResourceLocation id, int cookingTime, Ingredient container, NonNullList<Ingredient> input, ItemStack output) {
        super(id, LIGHT_STATE, cookingTime, container, input, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.DIM_FERMENTATION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.DIM_FERMENTATION_RECIPE_TYPE.get();
    }

    public static class Serializer extends BaseFermentationRecipeSerializer<DimFermentationRecipe> {
        @Override
        public DimFermentationRecipe createRecipe(ResourceLocation id, int cookingTime, Ingredient container, NonNullList<Ingredient> input, ItemStack output) {
            return new DimFermentationRecipe(id, cookingTime, container, input, output);
        }
    }
}