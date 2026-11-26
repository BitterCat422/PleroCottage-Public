package net.bittercat.plerocottage.compat.jei;

import java.util.List;

import net.bittercat.plerocottage.init.BrewingRecipesRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.registration.IRecipeRegistration;

@JeiPlugin
public final class BrewingRecipePlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("plerocottage", "brewing_recipes");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
        List<IJeiBrewingRecipe> recipes = BrewingRecipesRegistry.recipes().stream()
            .map(recipe -> factory.createBrewingRecipe(
                List.of(new ItemStack(recipe.ingredient())),
                List.of(recipe.inputStack()),
                new ItemStack(recipe.output())))
            .toList();
        registration.addRecipes(RecipeTypes.BREWING, recipes);
    }
}
