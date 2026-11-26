package net.bittercat.plerocottage.machine.compat.jei.category;

import net.bittercat.plerocottage.machine.recipes.BaseRecipe;
import net.bittercat.plerocottage.machine.recipes.CuttingBoardMultipleRecipe;
import net.bittercat.plerocottage.machine.recipes.CuttingBoardSingleRecipe;

import java.util.Optional;

import static net.bittercat.plerocottage.machine.blockentities.CuttingBoardBlockEntity.CuttingBoardCraftContainer;

public class CuttingBoardRecipeWrapper {
    public BaseRecipe<CuttingBoardCraftContainer> recipe;

    public CuttingBoardRecipeWrapper(BaseRecipe<CuttingBoardCraftContainer> recipe) {
        this.recipe = recipe;
    }

    public boolean isSingleRecipe() {
        return this.recipe instanceof CuttingBoardSingleRecipe;
    }

    public Optional<CuttingBoardSingleRecipe> getSingleRecipe() {
        return this.isSingleRecipe() ? Optional.of((CuttingBoardSingleRecipe) this.recipe) : Optional.empty();
    }

    public boolean isMultipleRecipe() {
        return this.recipe instanceof CuttingBoardMultipleRecipe;
    }

    public Optional<CuttingBoardMultipleRecipe> getMultipleRecipe() {
        return this.isMultipleRecipe() ? Optional.of((CuttingBoardMultipleRecipe) this.recipe) : Optional.empty();
    }
}
