package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.machine.recipes.ShakerRecipe;
import org.jetbrains.annotations.Nullable;

public class ShakerRecipeCategory extends BaseRecipeCategory<ShakerRecipe> {
    public static final ResourceLocation SHAKER_RECIPE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/jei/shaker_recipe_background.png");
    public static final String TRANSLATABLE = "gui.jei.category.recipe.shaker";
    public static final String CONTAINER_TOOLTIP = TRANSLATABLE + ".container_tooltip";

    public ShakerRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, PleroCottageMachineJeiPlugin.SHAKER, SHAKER_RECIPE_BACKGROUND, TRANSLATABLE);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.guiHelper.createDrawableItemLike(ItemRegistry.SHAKER.get());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ShakerRecipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < recipe.input.size(); i++) {
            int x = 6 + (i % 4) * 18;
            int y = 20 + (i / 4) * 18;
            builder.addInputSlot(x, y).addIngredients(recipe.input.get(i));
        }
        if (recipe.hasContainer()) {
            builder.addInputSlot(110, 51).addIngredients(recipe.container);
        }
        builder.addOutputSlot(110, 28).addItemStack(recipe.output);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, ShakerRecipe recipe, IRecipeSlotsView slots, double mouseX, double mouseY) {
        if (mouseX >= 128 && mouseX < 138 && mouseY >= 52 && mouseY < 66) {
            tooltip.add(Component.translatable(CONTAINER_TOOLTIP));
        }
    }
}
