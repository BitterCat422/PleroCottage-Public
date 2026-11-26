package net.bittercat.plerocottage.machine.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.OvenBlockEntity;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;
import net.bittercat.plerocottage.machine.recipes.OvenRecipe;
import net.bittercat.plerocottage.init.BlockRegistry;
import org.jetbrains.annotations.Nullable;

public class OvenRecipeCategory extends BaseRecipeCategory<OvenRecipe> {
    public static final ResourceLocation OVEN_RECIPE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/gui/jei/oven_recipe_background.png");
    public static final String TRANSLATABLE = "gui.jei.category.recipe.oven";

    public static final int TEMPERATURE_X_OFFSET = 154;
    public static final int TEMPERATURE_Y_OFFSET = 0;
    public static final int TEMPERATURE_WIDTH = 19;
    public static final int TEMPERATURE_HEIGHT = 14;

    public static final int TIME_X_OFFSET = 154;
    public static final int TIME_Y_OFFSET = 15;
    public static final int TIME_WIDTH = 16;
    public static final int TIME_HEIGHT = 13;

    public static final Rect2i TEMPERATURE_RECT = new Rect2i(
            15,
            1,
            18,
            18
    );
    public static final Rect2i TIME_RECT = new Rect2i(
            121,
            1,
            18,
            18
    );

    public OvenRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, PleroCottageMachineJeiPlugin.OVEN, OVEN_RECIPE_BACKGROUND, TRANSLATABLE);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.guiHelper.createDrawableItemLike(BlockRegistry.OVEN.get());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, OvenRecipe recipe, IFocusGroup focuses) {
        int[][] slots = {
                {16, 27},
                {35, 27},
                {54, 27},
                {73, 27},
                {16, 46},
                {35, 46},
                {54, 46},
                {73, 46}
        };
        for (int i = 0; i < slots.length; i++) {
            Ingredient input = recipe.input.get(i);
            if (input.isEmpty()) continue;
            builder.addInputSlot(slots[i][0], slots[i][1])
                    .addIngredients(input);
        }
        if (recipe.hasContainer()) {
            builder.addInputSlot(123, 55)
                    .addIngredients(recipe.getContainer());
        }
        builder.addOutputSlot(123, 32)
                .addItemStack(recipe.output);
    }

    public int getTemperatureIndex(int value) {
        return switch (value) {
            case 150 -> OvenBlockEntity.Temperature.ONE_HUNDRED_FIFTY.state;
            case 200 -> OvenBlockEntity.Temperature.TWO_HUNDRED.state;
            case 250 -> OvenBlockEntity.Temperature.TWO_HUNDRED_FIFTY.state;
            default -> 0;
        };
    }

    public int getCookingTimeIndex(int value) {
        return switch (value) {
            case 100 -> OvenBlockEntity.MaxCookingTime.FIVE.state;
            case 200 -> OvenBlockEntity.MaxCookingTime.TEN.state;
            case 300 -> OvenBlockEntity.MaxCookingTime.FIFTEEN.state;
            default -> 0;
        };
    }

    @Override
    public void draw(OvenRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        int temperatureIndex = this.getTemperatureIndex(recipe.temperature) - 1;
        this.guiHelper.createDrawable(OVEN_RECIPE_BACKGROUND, TEMPERATURE_X_OFFSET + TEMPERATURE_WIDTH * temperatureIndex, TEMPERATURE_Y_OFFSET, TEMPERATURE_WIDTH, TEMPERATURE_HEIGHT).draw(guiGraphics, 14, 1);
        int cookingTimeIndex = this.getCookingTimeIndex(recipe.maxCookingTime) - 1;
        this.guiHelper.createDrawable(OVEN_RECIPE_BACKGROUND, TIME_X_OFFSET + TIME_WIDTH * cookingTimeIndex, TIME_Y_OFFSET, TIME_WIDTH, TIME_HEIGHT).draw(guiGraphics, 122, 5);
        Font font = Minecraft.getInstance().font;
        if (TEMPERATURE_RECT.contains((int) mouseX, (int) mouseY)) {
            guiGraphics.renderTooltip(font, Component.literal(recipe.temperature + "°"), (int) mouseX, (int) mouseY);
        }
        if (TIME_RECT.contains((int) mouseX, (int) mouseY)) {
            guiGraphics.renderTooltip(font, Component.literal(recipe.maxCookingTime / 20 + "s"), (int) mouseX, (int) mouseY);
        }
    }
}
