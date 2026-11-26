package net.bittercat.plerocottage.machine.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.bittercat.plerocottage.machine.menus.TeapotMenu;
import net.bittercat.plerocottage.machine.blockentities.TeapotBlockEntity;
import net.bittercat.plerocottage.machine.recipes.TeapotRecipe;

import java.util.List;

public class TeapotRecipeBookComponent extends VerticalFilterRecipeBookComponent {
    private final ResourceLocation filterButtonTexture;
    private final int filterButtonTextureX;

    public TeapotRecipeBookComponent(ResourceLocation filterButtonTexture, int filterButtonTextureX) {
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
        if (!(this.menu instanceof TeapotMenu) || !(recipe instanceof TeapotRecipe teapotRecipe)) {
            return;
        }
        this.ghostRecipe.clear();
        this.ghostRecipe.setRecipe(recipe);
        if (slots.get(TeapotBlockEntity.CONTAINER).getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(
                    teapotRecipe.getContainer(),
                    slots.get(TeapotBlockEntity.CONTAINER).x,
                    slots.get(TeapotBlockEntity.CONTAINER).y
            );
        }
        for (int index = 0; index < TeapotBlockEntity.INPUT_SLOTS.length; index++) {
            Ingredient ingredient = teapotRecipe.input.get(index);
            if (ingredient.isEmpty()) {
                continue;
            }
            Slot slot = slots.get(TeapotBlockEntity.INPUT_SLOTS[index]);
            if (slot.getItem().isEmpty()) {
                this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y);
            }
        }
    }
}

