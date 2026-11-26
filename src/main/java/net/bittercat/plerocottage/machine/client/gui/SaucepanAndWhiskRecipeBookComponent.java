package net.bittercat.plerocottage.machine.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.bittercat.plerocottage.machine.menus.SaucepanAndWhiskMenu;
import net.bittercat.plerocottage.machine.recipes.SaucepanAndWhiskRecipe;

import java.util.List;

public class SaucepanAndWhiskRecipeBookComponent extends VerticalFilterRecipeBookComponent {
    private final ResourceLocation filterButtonTexture;
    private final int filterButtonTextureX;

    public SaucepanAndWhiskRecipeBookComponent(ResourceLocation filterButtonTexture, int filterButtonTextureX) {
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
        if (!(this.menu instanceof SaucepanAndWhiskMenu)
                || !(recipe instanceof SaucepanAndWhiskRecipe saucepanRecipe)) {
            return;
        }
        this.ghostRecipe.setRecipe(recipe);
        if (slots.get(11).getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(
                    Ingredient.of(saucepanRecipe.output),
                    slots.get(11).x,
                    slots.get(11).y
            );
        }
        addIngredient(slots, saucepanRecipe.getContainer(), 10);
        for (int index = 0; index < saucepanRecipe.getMainInput().size(); index++) {
            addIngredient(slots, saucepanRecipe.getMainInput().get(index), index);
        }
        for (int index = 0; index < saucepanRecipe.getSecondaryInput().size(); index++) {
            addIngredient(slots, saucepanRecipe.getSecondaryInput().get(index), 6 + index);
        }
    }

    private void addIngredient(List<Slot> slots, Ingredient ingredient, int slotIndex) {
        if (!ingredient.isEmpty()) {
            Slot slot = slots.get(slotIndex);
            this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y);
        }
    }
}

