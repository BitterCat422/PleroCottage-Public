package net.bittercat.plerocottage.machine.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.init.RecipeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bittercat.plerocottage.machine.items.ShakerItem.ITEMS_SIZE;
import static net.bittercat.plerocottage.machine.items.ShakerItem.SLOT_LIMIT;

public class ShakerRecipe extends BaseRecipe<ShakerRecipe.Input> implements HasContainerItem {
    public final Ingredient container;
    public final NonNullList<Ingredient> input;

    public ShakerRecipe(ResourceLocation id, Ingredient container, NonNullList<Ingredient> input, ItemStack output) {
        super(id, output);
        this.container = container;
        this.input = NonNullList.withSize(ITEMS_SIZE, Ingredient.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            this.input.set(i, input.get(i));
        }
    }

    @Override
    public boolean matches(Input pContainer, Level pLevel) {
        return plan(pContainer, 1) != null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.SHAKER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.SHAKER_RECIPE_TYPE.get();
    }

    @Override
    public Ingredient getContainer() {
        return this.container;
    }

    public @Nullable CraftPlan plan(Input contents) {
        return plan(contents, SLOT_LIMIT);
    }

    private @Nullable CraftPlan plan(Input contents, int limit) {
        if (output.isEmpty()) return null;
        int maxBatches = Math.min(limit, output.getMaxStackSize() / output.getCount());
        if (hasContainer()) {
            if (!isContainerMatch(contents.offhand)) return null;
            maxBatches = Math.min(maxBatches, contents.offhand.getCount());
        }
        for (int batches = maxBatches; batches > 0; batches--) {
            int[] used = allocate(contents, batches);
            if (used != null) return new CraftPlan(batches, used);
        }
        return null;
    }

    private @Nullable int[] allocate(Input contents, int batches) {
        List<Ingredient> demands = new ArrayList<>();
        for (Ingredient ingredient : input) {
            if (ingredient.isEmpty()) continue;
            for (int batch = 0; batch < batches; batch++) demands.add(ingredient);
        }

        int slots = contents.getContainerSize();
        List<Integer> unitSlots = new ArrayList<>();
        for (int slot = 0; slot < slots; slot++) {
            ItemStack stack = contents.getItem(slot);
            if (stack.isEmpty()) continue;
            int available = PleroCottageCompat.isDamageableMaterial(stack)
                    ? stack.getMaxDamage() - stack.getDamageValue() : stack.getCount();
            for (int unit = 0; unit < Math.min(available, demands.size()); unit++) unitSlots.add(slot);
        }
        if (unitSlots.size() < demands.size()) return null;

        boolean[][] matches = new boolean[demands.size()][slots];
        for (int demand = 0; demand < demands.size(); demand++) {
            for (int slot = 0; slot < slots; slot++) {
                ItemStack stack = contents.getItem(slot);
                matches[demand][slot] = !stack.isEmpty() && demands.get(demand).test(stack);
            }
        }
        int[] owners = new int[unitSlots.size()];
        Arrays.fill(owners, -1);
        for (int demand = 0; demand < demands.size(); demand++) {
            if (!assign(demand, matches, unitSlots, owners, new boolean[owners.length])) return null;
        }
        int[] used = new int[slots];
        for (int unit = 0; unit < owners.length; unit++) {
            if (owners[unit] >= 0) used[unitSlots.get(unit)]++;
        }
        return used;
    }

    private static boolean assign(int demand, boolean[][] matches, List<Integer> unitSlots,
                                  int[] owners, boolean[] visited) {
        for (int unit = 0; unit < owners.length; unit++) {
            if (visited[unit] || !matches[demand][unitSlots.get(unit)]) continue;
            visited[unit] = true;
            if (owners[unit] < 0 || assign(owners[unit], matches, unitSlots, owners, visited)) {
                owners[unit] = demand;
                return true;
            }
        }
        return false;
    }

    public record CraftPlan(int batches, int[] used) {}

    public static final class Input extends RecipeWrapper {
        private final ItemStack offhand;

        public Input(IItemHandlerModifiable ingredients, ItemStack offhand) {
            super(ingredients);
            this.offhand = offhand;
        }
    }

    public static class Serializer extends BaseRecipeSerializer<ShakerRecipe> {
        @Override
        public ShakerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            JsonElement container = pSerializedRecipe.get("container");
            return new ShakerRecipe(
                    pRecipeId,
                    container == null || container.isJsonNull() ? Ingredient.EMPTY : Ingredient.fromJson(container),
                    this.getInput(pSerializedRecipe, ITEMS_SIZE),
                    this.getOutput(pSerializedRecipe)
            );
        }

        @Override
        public @Nullable ShakerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return new ShakerRecipe(
                    pRecipeId,
                    this.getContainer(pBuffer),
                    this.getInput(pBuffer, ITEMS_SIZE),
                    this.getOutput(pBuffer)
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ShakerRecipe pRecipe) {
            this.toContainer(pBuffer, pRecipe.container);
            this.toInput(pBuffer, pRecipe.input);
            this.toOutput(pBuffer, pRecipe.output);
        }
    }
}
