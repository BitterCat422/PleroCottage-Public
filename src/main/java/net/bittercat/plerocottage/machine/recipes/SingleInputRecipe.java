package net.bittercat.plerocottage.machine.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface SingleInputRecipe {
    Ingredient getInput();

    default boolean isInputMatch(ItemStack input) {
        return this.getInput().test(input);
    }
}
