package net.bittercat.plerocottage.machine.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.bittercat.plerocottage.machine.menus.IceCreamMachineMenu;
import net.bittercat.plerocottage.machine.recipes.IceCreamRecipe;

import java.util.List;

public class IceCreamMachineRecipeBookComponent extends VerticalFilterRecipeBookComponent {
    private final ResourceLocation filterButtonTexture;
    private final int filterButtonTextureX;

    public IceCreamMachineRecipeBookComponent(ResourceLocation texture, int textureX) {
        this.filterButtonTexture = texture;
        this.filterButtonTextureX = textureX;
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
        if (!(this.menu instanceof IceCreamMachineMenu)
                || !(recipe instanceof IceCreamRecipe iceCreamRecipe)) {
            return;
        }
        this.ghostRecipe.clear();
        this.ghostRecipe.setRecipe(recipe);
        if (slots.get(3).getItem().isEmpty()) {
            this.ghostRecipe.addIngredient(
                    Ingredient.of(iceCreamRecipe.output),
                    slots.get(3).x,
                    slots.get(3).y
            );
        }
        addIngredient(slots, iceCreamRecipe.input.get(0), 0);
        addIngredient(slots, iceCreamRecipe.input.get(1), 1);
        addIngredient(slots, iceCreamRecipe.getIngredients().get(2), 2);
    }

    private void addIngredient(List<Slot> slots, Ingredient ingredient, int slotIndex) {
        if (!ingredient.isEmpty()) {
            Slot slot = slots.get(slotIndex);
            this.ghostRecipe.addIngredient(ingredient, slot.x, slot.y);
        }
    }
}
