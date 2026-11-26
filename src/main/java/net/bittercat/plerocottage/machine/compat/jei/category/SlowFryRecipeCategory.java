package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.resources.ResourceLocation;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;
import net.bittercat.plerocottage.machine.recipes.SlowFryRecipe;
import net.bittercat.plerocottage.init.BlockRegistry;
import org.jetbrains.annotations.Nullable;

public class SlowFryRecipeCategory extends BaseRecipeCategory<SlowFryRecipe> {
    public static final ResourceLocation SLOW_FRY_RECIPE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/jei/slow_fry_recipe_background.png");
    public static final String TRANSLATABLE = "gui.jei.category.recipe.slow_fry";

    public SlowFryRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, PleroCottageMachineJeiPlugin.SLOW_FRY, SLOW_FRY_RECIPE_BACKGROUND, TRANSLATABLE);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.guiHelper.createDrawableItemLike(BlockRegistry.FRYER.get());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SlowFryRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(40, 27)
                .addIngredients(recipe.input);
        builder.addOutputSlot(108, 27)
                .addItemStack(recipe.output);
    }
}
