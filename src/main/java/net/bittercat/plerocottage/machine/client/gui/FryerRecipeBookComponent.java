package net.bittercat.plerocottage.machine.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.bittercat.plerocottage.machine.menus.FryerMenu;

import java.util.List;

public class FryerRecipeBookComponent extends VerticalFilterRecipeBookComponent {
    private final ResourceLocation filterButtonTexture;
    private final int filterButtonTextureX;

    public FryerRecipeBookComponent(ResourceLocation filterButtonTexture, int filterButtonTextureX) {
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
        if (!(this.menu instanceof FryerMenu)) {
            return;
        }
        this.ghostRecipe.clear();
        Ingredient input = FryerMenu.getRecipeInput(recipe);
        if (input.isEmpty()) return;

        for (int slotIndex : new int[]{1, 2, 3, 4}) {
            Slot slot = slots.get(slotIndex);
            if (slot.getItem().isEmpty()) {
                this.ghostRecipe.setRecipe(recipe);
                this.ghostRecipe.addIngredient(input, slot.x, slot.y);
                return;
            }
        }
    }
}

