package net.bittercat.plerocottage.machine.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.bittercat.plerocottage.machine.blockentities.OvenBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.init.RecipeRegistry;
import org.jetbrains.annotations.Nullable;

import static net.bittercat.plerocottage.machine.blockentities.OvenBlockEntity.*;

public class OvenRecipe extends BaseRecipe<OvenBlockEntity> implements CookingTimeRecipe, HasContainerItem, OneInputRecipe {
    public final int temperature;
    public final int maxCookingTime;
    public final Ingredient container;
    public final NonNullList<Ingredient> input;

    public OvenRecipe(ResourceLocation id, int temperature, int maxCookingTime, Ingredient container,
                      NonNullList<Ingredient> input, ItemStack output) {
        super(id, output);
        this.temperature = temperature;
        this.maxCookingTime = maxCookingTime;
        this.container = container;
        this.input = input;
    }

    public boolean isTemperatureMatch(OvenBlockEntity pContainer) {
        return this.temperature == pContainer.temperature.temperature;
    }

    public boolean isStateMatch(OvenBlockEntity pContainer) {
        return this.isTemperatureMatch(pContainer) && this.maxCookingTime == pContainer.cookingTime;
    }

    @Override
    public boolean matches(OvenBlockEntity pContainer, Level pLevel) {
        if (pLevel.isClientSide()) return false;
        if (this.hasContainer()
                && (!this.isContainerMatch(pContainer.getItem(CONTAINER))
                || pContainer.getItem(CONTAINER).getCount() < this.output.getCount())) {
            return false;
        }
        return this.isInputMatch(pContainer.getInput());
    }

    @Override
    public ItemStack assemble(OvenBlockEntity pContainer, RegistryAccess pRegistryAccess) {
        ItemStack output = this.output.copy();
        int count = output.getCount();
        int multiple = Math.min(pContainer.getMaxStackSize(), output.getMaxStackSize()) / count;
        if (this.hasContainer()) {
            multiple = Math.min(multiple, pContainer.getItem(CONTAINER).getCount() / count);
        }
        for (int slot : INPUT_SLOTS) {
            ItemStack input = pContainer.getItem(slot);
            if (input.isEmpty()) continue;
            if (PleroCottageCompat.isDamageableMaterial(input)) {
                multiple = Math.min(multiple, input.getMaxDamage() - input.getDamageValue());
            } else {
                multiple = Math.min(multiple, input.getCount());
            }
        }
        pContainer.outputMultiple = multiple;
        return this.output.copyWithCount(count * multiple);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.OVEN_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.OVEN_RECIPE_TYPE.get();
    }

    @Override
    public int getMaxCookingTime() {
        return this.maxCookingTime;
    }

    @Override
    public Ingredient getContainer() {
        return this.container;
    }

    @Override
    public NonNullList<Ingredient> getInput() {
        return this.input;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(
                this.input.size() + (this.hasContainer() ? 1 : 0),
                Ingredient.EMPTY
        );
        for (int index = 0; index < this.input.size(); index++) {
            ingredients.set(index, this.input.get(index));
        }
        if (this.hasContainer()) {
            ingredients.set(this.input.size(), this.container);
        }
        return ingredients;
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    public static class Serializer extends BaseRecipeSerializer<OvenRecipe> {
        @Override
        public OvenRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return new OvenRecipe(
                    pRecipeId,
                    this.getInt(pSerializedRecipe, "temperature"),
                    this.getCookingTime(pSerializedRecipe),
                    this.getContainer(pSerializedRecipe),
                    this.getInput(pSerializedRecipe, INPUT_SLOTS.length),
                    this.getOutput(pSerializedRecipe)
            );
        }

        @Override
        public @Nullable OvenRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new OvenRecipe(
                    pRecipeId,
                    this.getInt(pBuffer),
                    this.getCookingTime(pBuffer),
                    this.getContainer(pBuffer),
                    this.getInput(pBuffer, INPUT_SLOTS.length),
                    this.getOutput(pBuffer)
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, OvenRecipe pRecipe) {
            this.toInt(pBuffer, pRecipe.temperature);
            this.toCookingTime(pBuffer, pRecipe.maxCookingTime);
            this.toContainer(pBuffer, pRecipe.container);
            NonNullList<Ingredient> input = NonNullList.withSize(INPUT_SLOTS.length, Ingredient.EMPTY);
            for (int index = 0; index < Math.min(input.size(), pRecipe.input.size()); index++) {
                input.set(index, pRecipe.input.get(index));
            }
            this.toInput(pBuffer, input);
            this.toOutput(pBuffer, pRecipe.output);
        }
    }
}
