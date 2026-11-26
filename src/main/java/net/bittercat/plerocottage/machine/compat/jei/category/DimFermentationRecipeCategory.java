package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;
import net.bittercat.plerocottage.machine.recipes.DimFermentationRecipe;

public class DimFermentationRecipeCategory extends BaseFermentationRecipeCategory<DimFermentationRecipe> {
    public static final ResourceLocation DIM_FERMENTATION_RECIPE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/jei/dim_fermentation_recipe_background.png");
    public static final String TRANSLATABLE = "gui.jei.category.recipe.dim_fermentation";

    public DimFermentationRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, PleroCottageMachineJeiPlugin.DIM_FERMENTATION, DIM_FERMENTATION_RECIPE_BACKGROUND, TRANSLATABLE);
    }
}
