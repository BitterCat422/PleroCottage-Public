package net.bittercat.plerocottage.machine.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.bittercat.plerocottage.init.RecipeRegistry;
import org.jetbrains.annotations.Nullable;

import static net.bittercat.plerocottage.machine.blockentities.FryerBlockEntity.*;

public class FastFryRecipe extends BaseRecipe<FryerCraftContainer> implements SingleInputRecipe {
    public final Ingredient input;

    public FastFryRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        super(id, output);
        this.input = input;
    }

    @Override
    public boolean matches(FryerCraftContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) return false;
        int cookingTime = pContainer.blockEntity.cookingTime;
        if (cookingTime < FAST_COOKING_TIME || cookingTime > SLOW_COOKING_TIME)
            return false;
        return this.isInputMatch(pContainer.getItem());
    }

    @Override
    public ItemStack assemble(FryerCraftContainer pContainer, RegistryAccess pRegistryAccess) {
        return this.output.copyWithCount(pContainer.getItem().getCount());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.FAST_FRY_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.FAST_FRY_RECIPE_TYPE.get();
    }

    @Override
    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, this.input);
    }

    @Override
    public boolean isIncomplete() {
        return this.input.isEmpty();
    }

    public static class Serializer extends BaseRecipeSerializer<FastFryRecipe> {
        @Override
        public FastFryRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return new FastFryRecipe(
                    pRecipeId,
                    this.getInput(pSerializedRecipe),
                    this.getOutput(pSerializedRecipe)
            );
        }

        @Override
        public @Nullable FastFryRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new FastFryRecipe(
                    pRecipeId,
                    this.getInput(pBuffer),
                    this.getOutput(pBuffer)
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, FastFryRecipe pRecipe) {
            this.toInput(pBuffer, pRecipe.input);
            this.toOutput(pBuffer, pRecipe.output);
        }
    }
}
