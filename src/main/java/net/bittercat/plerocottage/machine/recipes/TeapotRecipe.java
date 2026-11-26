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
import net.bittercat.plerocottage.machine.blockentities.TeapotBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.init.RecipeRegistry;
import org.jetbrains.annotations.Nullable;

import static net.bittercat.plerocottage.machine.blockentities.TeapotBlockEntity.*;

public class TeapotRecipe extends BaseRecipe<TeapotBlockEntity> implements HasContainerItem, OneInputRecipe {
    public final Ingredient container;
    public final NonNullList<Ingredient> input;

    public TeapotRecipe(ResourceLocation id, Ingredient container, NonNullList<Ingredient> input, ItemStack output) {
        super(id, output);
        this.container = container;
        this.input = input;
    }

    @Override
    public boolean matches(TeapotBlockEntity pContainer, Level pLevel) {
        if (!pContainer.getItem(RESULT).isEmpty()) return false;
        if (!this.isContainerMatch(pContainer.getItem(CONTAINER))) return false;
        if (pContainer.getItem(CONTAINER).getCount() < this.output.getCount()) return false;
        if (!this.input.get(0).test(pContainer.getItem(DRINK))) return false;
        if (!this.input.get(1).test(pContainer.getItem(MATERIAL))) return false;
        return this.isInputMatch(pContainer.getInput());
    }

    @Override
    public ItemStack assemble(TeapotBlockEntity pContainer, RegistryAccess pRegistryAccess) {
        ItemStack output = this.output.copy();
        int count = output.getCount();
        int multiple = Math.min(pContainer.getMaxStackSize(), output.getMaxStackSize()) / count;
        for (int slot : INPUT_SLOTS) {
            ItemStack input = pContainer.getItem(slot);
            if (input.isEmpty()) continue;
            if (PleroCottageCompat.isDamageableMaterial(input)) {
                multiple = Math.min(multiple, input.getMaxDamage() - input.getDamageValue());
            } else {
                multiple = Math.min(multiple, input.getCount());
            }
        }
        multiple = Math.min(multiple, pContainer.getItem(CONTAINER).getCount() / count);
        pContainer.outputMultiple = multiple;
        return this.output.copyWithCount(count * multiple);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.TEAPOT_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.TEAPOT_RECIPE_TYPE.get();
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
        NonNullList<Ingredient> ingredients = NonNullList.withSize(this.input.size() + 1, Ingredient.EMPTY);
        ingredients.set(0, this.container);
        for (int index = 0; index < this.input.size(); index++) {
            ingredients.set(index + 1, this.input.get(index));
        }
        return ingredients;
    }

    @Override
    public boolean isIncomplete() {
        return this.container.isEmpty() || this.input.stream().allMatch(Ingredient::isEmpty);
    }

    public static class Serializer extends BaseRecipeSerializer<TeapotRecipe> {
        @Override
        public TeapotRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return new TeapotRecipe(
                    pRecipeId,
                    this.getContainer(pSerializedRecipe),
                    this.getInput(pSerializedRecipe, INPUT_SLOTS.length),
                    this.getOutput(pSerializedRecipe)
            );
        }

        @Override
        public @Nullable TeapotRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new TeapotRecipe(
                    pRecipeId,
                    this.getContainer(pBuffer),
                    this.getInput(pBuffer, INPUT_SLOTS.length),
                    this.getOutput(pBuffer)
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, TeapotRecipe pRecipe) {
            this.toContainer(pBuffer, pRecipe.container);
            this.toInput(pBuffer, pRecipe.input);
            this.toOutput(pBuffer, pRecipe.output);
        }
    }
}
