package net.bittercat.plerocottage.machine.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.bittercat.plerocottage.machine.blockentities.IceCreamMachineBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.init.RecipeRegistry;
import org.jetbrains.annotations.Nullable;

import static net.bittercat.plerocottage.machine.blockentities.IceCreamMachineBlockEntity.*;

public class IceCreamRecipe extends BaseRecipe<IceCreamMachineBlockEntity> implements HasFluidRecipe, HasContainerItem {
    public final boolean isTowFlavor;
    public final FluidStack fluidStack;
    public final Ingredient container;
    public final NonNullList<Ingredient> input;

    public IceCreamRecipe(ResourceLocation id, boolean isTowFlavor, FluidStack fluidStack, NonNullList<Ingredient> input, ItemStack output) {
        this(id, isTowFlavor, fluidStack, Ingredient.EMPTY, input, output);
    }

    public IceCreamRecipe(ResourceLocation id, boolean isTowFlavor, FluidStack fluidStack, Ingredient container, NonNullList<Ingredient> input, ItemStack output) {
        super(id, output);
        this.isTowFlavor = isTowFlavor;
        this.fluidStack = fluidStack;
        this.container = container;
        this.input = input;
    }

    @Override
    public boolean matches(IceCreamMachineBlockEntity pContainer, Level pLevel) {
        if (!this.isFluidStackMatch(pContainer.fluidTank)) return false;
        ItemStack container = pContainer.getItem(CONTAINER_SLOT);
        if (this.hasContainer() && !this.isContainerMatch(container)) return false;
        if (!this.hasContainer() && !PleroCottageCompat.isIceCreamCone(container)) return false;
        if (!pContainer.getItem(OUTPUT_SLOT).isEmpty()) return false;
        if (!this.input.get(0).test(pContainer.getItem(LEFT_INPUT_SLOT))) return false;
        boolean hasRightInput = !pContainer.getItem(RIGHT_INPUT_SLOT).isEmpty();
        if (this.isTowFlavor != hasRightInput) return false;
        return !hasRightInput || this.input.get(1).test(pContainer.getItem(RIGHT_INPUT_SLOT));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ICE_CREAM_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.ICE_CREAM_RECIPE_TYPE.get();
    }

    @Override
    public FluidStack getFluidStack() {
        return this.fluidStack;
    }

    @Override
    public Ingredient getContainer() {
        return this.container;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(1 + this.input.size(), Ingredient.EMPTY);
        ingredients.set(2, this.container.isEmpty()
                ? Ingredient.of(PleroCottageCompat.getPleroCottageItem("ice_cream_cone"))
                : this.container);
        ingredients.set(0, this.input.get(0));
        ingredients.set(1, this.input.get(1));
        return ingredients;
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    public static class Serializer extends BaseRecipeSerializer<IceCreamRecipe> {
        @Override
        public IceCreamRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            NonNullList<Ingredient> input = this.getInput(pSerializedRecipe, MATERIAL_SLOTS.length);
            return new IceCreamRecipe(
                    pRecipeId,
                    !input.get(1).isEmpty(),
                    this.getFluidStack(pSerializedRecipe),
                    this.getContainer(pSerializedRecipe),
                    input,
                    this.getOutput(pSerializedRecipe)
            );
        }

        @Override
        public @Nullable IceCreamRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new IceCreamRecipe(
                    pRecipeId,
                    this.getBoolean(pBuffer),
                    this.getFluidStack(pBuffer),
                    this.getContainer(pBuffer),
                    this.getInput(pBuffer, MATERIAL_SLOTS.length),
                    this.getOutput(pBuffer)
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, IceCreamRecipe pRecipe) {
            this.toBoolean(pBuffer, pRecipe.isTowFlavor);
            this.toFluidStack(pBuffer, pRecipe.fluidStack);
            this.toContainer(pBuffer, pRecipe.container);
            this.toInput(pBuffer, pRecipe.input);
            this.toOutput(pBuffer, pRecipe.output);
        }
    }
}
