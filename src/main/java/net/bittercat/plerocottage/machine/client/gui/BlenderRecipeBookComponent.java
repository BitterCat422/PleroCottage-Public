package net.bittercat.plerocottage.machine.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.bittercat.plerocottage.machine.menus.BlenderMenu;
import net.bittercat.plerocottage.machine.recipes.BlenderRecipe;

import java.util.List;

public class BlenderRecipeBookComponent extends VerticalFilterRecipeBookComponent {
    private final ResourceLocation filterButtonTexture;
    private final int filterButtonTextureX;

    public BlenderRecipeBookComponent(ResourceLocation filterButtonTexture, int filterButtonTextureX) {
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
        if (!(this.menu instanceof BlenderMenu) || !(recipe instanceof BlenderRecipe blenderRecipe)) {
            return;
        }
        this.ghostRecipe.setRecipe(recipe);
        if (slots.get(12).getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(Ingredient.of(blenderRecipe.output), slots.get(12).x, slots.get(12).y);
        }
        this.placeRecipe(
                this.menu.getGridWidth(),
                this.menu.getGridHeight(),
                this.menu.getResultSlotIndex(),
                recipe,
                recipe.getIngredients().iterator(),
                0
        );
    }
}

