package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.helpers.IGuiHelper;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;

public class OvenSmokingRecipeCategory extends OvenRecipeCategory {
    public static final String TRANSLATABLE = "gui.jei.category.recipe.oven_smoking";

    public OvenSmokingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper);
        this.recipeType = PleroCottageMachineJeiPlugin.OVEN_SMOKING;
        this.translatable = TRANSLATABLE;
    }
}
