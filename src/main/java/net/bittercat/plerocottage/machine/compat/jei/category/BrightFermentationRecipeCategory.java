package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;
import net.bittercat.plerocottage.machine.recipes.BrightFermentationRecipe;

public class BrightFermentationRecipeCategory extends BaseFermentationRecipeCategory<BrightFermentationRecipe> {
    public static final ResourceLocation BRIGHT_FERMENTATION_RECIPE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/jei/bright_fermentation_recipe_background.png");
    public static final String TRANSLATABLE = "gui.jei.category.recipe.bright_fermentation";

    public BrightFermentationRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, PleroCottageMachineJeiPlugin.BRIGHT_FERMENTATION, BRIGHT_FERMENTATION_RECIPE_BACKGROUND, TRANSLATABLE);
    }
}
