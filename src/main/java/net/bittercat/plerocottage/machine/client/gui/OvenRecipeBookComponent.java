package net.bittercat.plerocottage.machine.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.bittercat.plerocottage.machine.menus.OvenMenu;
import net.bittercat.plerocottage.machine.recipes.OvenRecipe;

import java.util.Iterator;
import java.util.List;

public class OvenRecipeBookComponent extends VerticalFilterRecipeBookComponent {
    private final ResourceLocation filterButtonTexture;
    private final int filterButtonTextureX;

    public OvenRecipeBookComponent(ResourceLocation filterButtonTexture, int filterButtonTextureX) {
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
        if (!(this.menu instanceof OvenMenu)) {
            return;
        }
        this.ghostRecipe.clear();
        this.ghostRecipe.setRecipe(recipe);
        Slot output = slots.get(OvenMenu.OUTPUT_MENU_SLOT);
        if (output.getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(
                    Ingredient.of(recipe.getResultItem(this.minecraft.level.registryAccess())),
                    output.x,
                    output.y
            );
        }
        Iterator<Ingredient> ingredients;
        if (recipe instanceof OvenRecipe ovenRecipe) {
            if (ovenRecipe.hasContainer() && slots.get(OvenMenu.CONTAINER_MENU_SLOT).getItem().isEmpty()) {
                Slot container = slots.get(OvenMenu.CONTAINER_MENU_SLOT);
                this.ghostRecipe.addIngredient(ovenRecipe.getContainer(), container.x, container.y);
            }
            ingredients = ovenRecipe.input.iterator();
        } else {
            ingredients = recipe.getIngredients().iterator();
        }
        this.placeRecipe(
                4,
                2,
                OvenMenu.OUTPUT_MENU_SLOT,
                recipe,
                ingredients,
                0
        );
    }
}

