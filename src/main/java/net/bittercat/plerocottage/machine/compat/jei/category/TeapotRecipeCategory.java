package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;
import net.bittercat.plerocottage.machine.recipes.TeapotRecipe;
import net.bittercat.plerocottage.init.BlockRegistry;
import org.jetbrains.annotations.Nullable;

public class TeapotRecipeCategory extends BaseRecipeCategory<TeapotRecipe> {
    public static final ResourceLocation TEAPOT_RECIPE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/jei/teapot_recipe_background.png");
    public static final String TRANSLATABLE = "gui.jei.category.recipe.teapot";

    public TeapotRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, PleroCottageMachineJeiPlugin.TEAPOT, TEAPOT_RECIPE_BACKGROUND, TRANSLATABLE);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.guiHelper.createDrawableItemLike(BlockRegistry.TEAPOT.get());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TeapotRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(110, 6)
                .addIngredients(recipe.container);
        int[][] slots = {
                {77, 52},
                {110, 52},
                {25, 19},
                {45, 19},
                {25, 39},
                {45, 39}
        };
        for (int i = 0; i < slots.length; i++) {
            Ingredient input = recipe.input.get(i);
            if (input.isEmpty()) continue;
            builder.addInputSlot(slots[i][0], slots[i][1])
                    .addIngredients(input);
        }
        builder.addOutputSlot(110, 29)
                .addItemStack(recipe.output);
    }
}
