package net.bittercat.plerocottage.machine.menus;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.NonNullList;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.recipes.BaseRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class BoundedRecipeBookPlacement {
    private BoundedRecipeBookPlacement() {
    }

    public static void place(RecipeBookMenu menu, ServerPlayer player, Recipe recipe,
                             boolean useMaxItems, int containerSlot, int containersPerBatch) {
        Placer placer = new Placer(menu, recipe, containerSlot, containersPerBatch);
        placer.recipeClicked(player, placer.inputs, useMaxItems);
    }

    private static final class Placer extends ServerPlaceRecipe {
        private final Recipe original;
        private final Inputs inputs;

        private Placer(RecipeBookMenu menu, Recipe original, int containerSlot, int containersPerBatch) {
            super(menu);
            this.original = original;
            this.inputs = new Inputs(original, containerSlot, containersPerBatch);
        }

        @Override
        protected void handleRecipeClicked(Recipe recipe, boolean useMaxItems) {
            boolean recipeMatches = this.menu.recipeMatches(this.original);
            int maxPossible = this.stackedContents.getBiggestCraftableStack(this.inputs, null);
            int batches = getStackSize(useMaxItems, maxPossible, recipeMatches);
            IntArrayList chosen = new IntArrayList();
            if (batches <= 0 || !this.stackedContents.canCraft(this.inputs, chosen, batches)
                    || !fitsTargets(chosen, batches)) {
                return;
            }

            clearGrid();
            placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(),
                    this.menu.getResultSlotIndex(), this.inputs, chosen.iterator(), batches);
        }

        @Override
        protected int getStackSize(boolean useMaxItems, int maxPossible, boolean recipeMatches) {
            int requested = useMaxItems ? 64 : 1;
            if (!useMaxItems && this.menu.recipeMatches(this.original)) {
                int current = currentBatches();
                if (current > 0) {
                    requested = current + 1;
                }
            }

            int limit = Math.min(requested,
                    this.stackedContents.getBiggestCraftableStack(this.inputs, requested, null));
            for (int batches = limit; batches > 0; batches--) {
                IntArrayList chosen = new IntArrayList();
                if (!this.stackedContents.canCraft(this.inputs, chosen, batches)) {
                    continue;
                }
                if (fitsTargets(chosen, batches)) {
                    return batches;
                }
            }
            return 0;
        }

        @Override
        public void placeRecipe(int width, int height, int result, Recipe recipe,
                                Iterator iterator, int batches) {
            for (int target : this.inputs.targets) {
                int stackingIndex = (Integer) iterator.next();
                ItemStack stack = StackedContents.fromStackingIndex(stackingIndex);
                Slot slot = this.menu.getSlot(target);
                if (PleroCottageCompat.isDamageableMaterial(stack)) {
                    moveDamageableToGrid(slot, this.inputs.originalIngredients.get(target), batches);
                } else {
                    super.addItemToSlot(new SingleIndexIterator(stackingIndex), target, batches, 0, 0);
                }
            }
        }

        private void moveDamageableToGrid(Slot slot, Ingredient ingredient, int batches) {
            if (slot.hasItem()) {
                return;
            }
            for (int index = 0; index < this.inventory.items.size(); index++) {
                ItemStack source = this.inventory.items.get(index);
                if (!PleroCottageCompat.isDamageableMaterial(source)
                        || !ingredient.test(source)
                        || source.getMaxDamage() - source.getDamageValue() < batches
                        || !slot.mayPlace(source)) {
                    continue;
                }
                ItemStack placed = this.inventory.removeItem(index, 1);
                if (!placed.isEmpty()) {
                    slot.set(placed);
                }
                return;
            }
        }

        private int currentBatches() {
            int batches = Integer.MAX_VALUE;
            for (int target = 0; target < this.inputs.amounts.length; target++) {
                int amount = this.inputs.amounts[target];
                if (amount <= 0) {
                    continue;
                }
                ItemStack stack = this.menu.getSlot(target).getItem();
                Ingredient ingredient = this.inputs.originalIngredients.get(target);
                if (stack.isEmpty() || !ingredient.test(stack)) {
                    return 0;
                }
                int available = PleroCottageCompat.isDamageableMaterial(stack)
                        ? stack.getMaxDamage() - stack.getDamageValue()
                        : stack.getCount();
                batches = Math.min(batches, available / amount);
            }
            return batches == Integer.MAX_VALUE ? 0 : batches;
        }

        private boolean fitsTargets(IntArrayList chosen, int batches) {
            int index = 0;
            boolean[] usedInventory = new boolean[this.inventory.items.size()];
            boolean[] usedMachineSlots = new boolean[this.menu.getSize()];
            for (int target : this.inputs.targets) {
                ItemStack stack = StackedContents.fromStackingIndex(chosen.getInt(index++));
                Slot slot = this.menu.getSlot(target);
                if (PleroCottageCompat.isDamageableMaterial(stack)) {
                    ItemStack source = findDamageableSource(
                            this.inputs.originalIngredients.get(target), batches, usedInventory, usedMachineSlots);
                    if (source == null || !slot.mayPlace(source)) {
                        return false;
                    }
                    continue;
                }
                if (!slot.mayPlace(stack)) {
                    return false;
                }
                int required = batches * this.inputs.amounts[target];
                if (required > Math.min(slot.getMaxStackSize(stack), stack.getMaxStackSize())) {
                    return false;
                }
            }
            return true;
        }

        private ItemStack findDamageableSource(Ingredient ingredient, int batches,
                                               boolean[] usedInventory, boolean[] usedMachineSlots) {
            for (int index = 0; index < this.menu.getSize(); index++) {
                if (usedMachineSlots[index] || !this.menu.shouldMoveToInventory(index)) {
                    continue;
                }
                ItemStack stack = this.menu.getSlot(index).getItem();
                if (isUsableDamageable(ingredient, stack, batches)) {
                    usedMachineSlots[index] = true;
                    return stack;
                }
            }
            for (int index = 0; index < this.inventory.items.size(); index++) {
                if (usedInventory[index]) {
                    continue;
                }
                ItemStack stack = this.inventory.items.get(index);
                if (isUsableDamageable(ingredient, stack, batches)) {
                    usedInventory[index] = true;
                    return stack;
                }
            }
            return null;
        }

        private boolean isUsableDamageable(Ingredient ingredient, ItemStack stack, int batches) {
            return !stack.isEmpty()
                    && PleroCottageCompat.isDamageableMaterial(stack)
                    && ingredient.test(stack)
                    && stack.getMaxDamage() - stack.getDamageValue() >= batches;
        }
    }

    private static final class SingleIndexIterator implements Iterator<Integer> {
        private final int index;
        private boolean consumed;

        private SingleIndexIterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return !this.consumed;
        }

        @Override
        public Integer next() {
            this.consumed = true;
            return this.index;
        }
    }

    private static final class Inputs extends BaseRecipe<Container> {
        private final Recipe original;
        private final NonNullList<Ingredient> originalIngredients;
        private final NonNullList<Ingredient> ingredients = NonNullList.create();
        private final List<Integer> targets = new ArrayList<>();
        private final int[] amounts;

        private Inputs(Recipe original, int containerSlot, int containersPerBatch) {
            super(original.getId(), original.getResultItem(null));
            this.original = original;
            this.originalIngredients = original.getIngredients();
            this.amounts = new int[this.originalIngredients.size()];
            for (int slot = 0; slot < this.originalIngredients.size(); slot++) {
                Ingredient ingredient = this.originalIngredients.get(slot);
                int amount = slot == containerSlot ? Math.max(1, containersPerBatch) : 1;
                this.amounts[slot] = ingredient.isEmpty() ? 0 : amount;
                for (int index = 0; index < this.amounts[slot]; index++) {
                    this.ingredients.add(ingredient);
                    this.targets.add(slot);
                }
            }
        }

        @Override
        public NonNullList<Ingredient> getIngredients() {
            return this.ingredients;
        }

        @Override
        public boolean matches(Container container, Level level) {
            return this.original.matches(container, level);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return this.original.getSerializer();
        }

        @Override
        public RecipeType<?> getType() {
            return this.original.getType();
        }
    }
}
