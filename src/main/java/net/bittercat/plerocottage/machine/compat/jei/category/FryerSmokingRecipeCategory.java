package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.helpers.IGuiHelper;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;

public class FryerSmokingRecipeCategory extends FastFryRecipeCategory {
    public static final String TRANSLATABLE = "gui.jei.category.recipe.fryer_smoking";

    public FryerSmokingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper);
        this.recipeType = PleroCottageMachineJeiPlugin.FRYER_SMOKING;
        this.translatable = TRANSLATABLE;
    }
}
