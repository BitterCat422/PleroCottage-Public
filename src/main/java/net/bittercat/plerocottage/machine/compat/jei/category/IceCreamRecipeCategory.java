package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;
import net.bittercat.plerocottage.machine.recipes.IceCreamRecipe;
import net.bittercat.plerocottage.init.BlockRegistry;
import org.jetbrains.annotations.Nullable;

public class IceCreamRecipeCategory extends BaseRecipeCategory<IceCreamRecipe> {
    public static final ResourceLocation ICE_CREAM_RECIPE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/jei/ice_cream_recipe_background.png");
    public static final String TRANSLATABLE = "gui.jei.category.recipe.ice_cream";

    public IceCreamRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, PleroCottageMachineJeiPlugin.ICE_CREAM, ICE_CREAM_RECIPE_BACKGROUND, TRANSLATABLE);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return guiHelper.createDrawableItemLike(BlockRegistry.ICE_CREAM_MACHINE.get());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IceCreamRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(7, 28)
                .addIngredients(recipe.container);
        builder.addInputSlot(30, 28)
                .addItemLike(recipe.fluidStack.getFluid().getBucket());
        if (!recipe.input.get(0).isEmpty()) {
            builder.addInputSlot(54, 28)
                    .addIngredients(recipe.input.get(0));
        }
        if (recipe.isTowFlavor) {
            builder.addInputSlot(54, 50)
                    .addIngredients(recipe.input.get(1));
        }
        builder.addOutputSlot(109, 28)
                .addItemStack(recipe.output);
    }

    @Override
    public void draw(IceCreamRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        if (recipe.input.get(0).isEmpty()) {
            this.guiHelper.getSlotDrawable().draw(guiGraphics, 53, 27);
        }
        if (!recipe.isTowFlavor) {
            this.guiHelper.getSlotDrawable().draw(guiGraphics, 53, 49);
        }
    }
}
