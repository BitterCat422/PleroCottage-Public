package net.bittercat.plerocottage.machine.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.bittercat.plerocottage.machine.blockentities.FermenterBlockEntity;
import net.bittercat.plerocottage.machine.menus.FermenterMenu;
import net.bittercat.plerocottage.machine.recipes.BaseFermentationRecipe;

import java.util.List;

public class FermenterRecipeBookComponent extends VerticalFilterRecipeBookComponent {
    private final ResourceLocation filterButtonTexture;
    private final int filterButtonTextureX;

    public FermenterRecipeBookComponent(ResourceLocation filterButtonTexture, int filterButtonTextureX) {
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
        if (!(this.menu instanceof FermenterMenu) || !(recipe instanceof BaseFermentationRecipe fermentationRecipe)) {
            return;
        }
        this.ghostRecipe.clear();
        this.ghostRecipe.setRecipe(recipe);
        addIngredient(slots, fermentationRecipe.getContainer(), FermenterMenu.CONTAINER_MENU_SLOT);
        for (int index = 0; index < FermenterBlockEntity.MATERIAL_SLOTS.length; index++) {
            addIngredient(slots, fermentationRecipe.input.get(index), FermenterMenu.MATERIAL_MENU_SLOTS[index]);
        }
        if (slots.get(FermenterMenu.OUTPUT_MENU_SLOT).getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(
                    Ingredient.of(fermentationRecipe.output),
                    slots.get(FermenterMenu.OUTPUT_MENU_SLOT).x,
                    slots.get(FermenterMenu.OUTPUT_MENU_SLOT).y
            );
        }
    }

    private void addIngredient(List<Slot> slots, Ingredient ingredient, int slotIndex) {
        if (!ingredient.isEmpty() && slots.get(slotIndex).getItem().isEmpty()) {
            Slot slot = slots.get(slotIndex);
            this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y);
        }
    }
}

