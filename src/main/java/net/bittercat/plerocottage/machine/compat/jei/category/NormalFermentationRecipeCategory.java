package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;
import net.bittercat.plerocottage.machine.recipes.NormalFermentationRecipe;

public class NormalFermentationRecipeCategory extends BaseFermentationRecipeCategory<NormalFermentationRecipe> {
    public static final ResourceLocation NORMAL_FERMENTATION_RECIPE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/jei/normal_fermentation_recipe_background.png");
    public static final String TRANSLATABLE = "gui.jei.category.recipe.normal_fermentation";

    public NormalFermentationRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, PleroCottageMachineJeiPlugin.NORMAL_FERMENTATION, NORMAL_FERMENTATION_RECIPE_BACKGROUND, TRANSLATABLE);
    }
}
