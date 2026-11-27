package net.bittercat.plerocottage.machine.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.bittercat.plerocottage.machine.menus.StockPotMenu;
import net.bittercat.plerocottage.machine.recipes.StockPotRecipe;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CookingPotRecipeCompat;
import net.bittercat.plerocottage.machine.compat.farmersdelight.FarmersDelightCompat;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.List;

public class StockPotRecipeBookComponent extends VerticalFilterRecipeBookComponent {
    private final ResourceLocation filterButtonTexture;
    private final int filterButtonTextureX;

    public StockPotRecipeBookComponent(ResourceLocation filterButtonTexture, int filterButtonTextureX) {
        this.filterButtonTexture = filterButtonTexture;
        this.filterButtonTextureX = filterButtonTextureX;
    }

    @Override
    protected ResourceLocation getFilterButtonTexture() {
        return this.filterButtonTexture;
    }

    @Override
    protected int getFilterButtonTextureX() {
        return this.filterButtonTextureX;
    }

    @Override
    protected Component getRecipeFilterName() {
        return Component.translatable("recipe_book.cookable");
    }

    @Override
    public void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
        if (!(this.menu instanceof StockPotMenu)) {
            return;
        }
        StockPotRecipe stockPotRecipe;
        if (recipe instanceof StockPotRecipe nativeRecipe) {
            stockPotRecipe = nativeRecipe;
        } else if (FarmersDelightCompat.isLoad() && recipe instanceof CookingPotRecipe cookingRecipe) {
            if (!FarmersDelightCompat.canCookingPotCompat()) {
                return;
            }
            stockPotRecipe = CookingPotRecipeCompat.toStockPotRecipe(cookingRecipe);
        } else {
            return;
        }

        this.ghostRecipe.setRecipe(recipe);
        if (slots.get(13).getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(Ingredient.of(stockPotRecipe.output), slots.get(13).x, slots.get(13).y);
        }
        addIngredient(slots, stockPotRecipe.getContainer(), 12);

        int[] mainSlots = {0, 1, 2, 3, 4, 5, 6, 7};
        for (int index = 0; index < mainSlots.length; index++) {
            addIngredient(slots, stockPotRecipe.getMainInput().get(index), mainSlots[index]);
        }

        int[] secondarySlots = {8, 9, 10, 11};
        for (int index = 0; index < secondarySlots.length; index++) {
            addIngredient(slots, stockPotRecipe.getSecondaryInput().get(index), secondarySlots[index]);
        }
    }

    private void addIngredient(List<Slot> slots, Ingredient ingredient, int slotIndex) {
        if (!ingredient.isEmpty()) {
            Slot slot = slots.get(slotIndex);
            this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y);
        }
    }
}

