package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.resources.ResourceLocation;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;
import net.bittercat.plerocottage.machine.recipes.FastFryRecipe;
import net.bittercat.plerocottage.init.BlockRegistry;
import org.jetbrains.annotations.Nullable;

public class FastFryRecipeCategory extends BaseRecipeCategory<FastFryRecipe> {
    public static final ResourceLocation FAST_FRY_RECIPE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/jei/fast_fry_recipe_background.png");
    public static final String TRANSLATABLE = "gui.jei.category.recipe.fast_fry";

    public FastFryRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, PleroCottageMachineJeiPlugin.FAST_FRY, FAST_FRY_RECIPE_BACKGROUND, TRANSLATABLE);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.guiHelper.createDrawableItemLike(BlockRegistry.FRYER.get());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FastFryRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(40, 27)
                .addIngredients(recipe.input);
        builder.addOutputSlot(108, 27)
                .addItemStack(recipe.output);
    }
}
