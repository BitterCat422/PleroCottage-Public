package net.bittercat.plerocottage.machine.recipes.builder;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.bittercat.plerocottage.machine.PleroCottageMachine;

import java.util.function.Consumer;

public abstract class BaseRecipeBuilder {
    public abstract String getRecipePath();

    public abstract RecipeSerializer<?> getRecipeSerializer();

    public abstract FinishedRecipe createResult(ResourceLocation path, RecipeSerializer<?> recipeSerializer);

    public void save(Consumer<FinishedRecipe> recipeConsumer, Item item) {
        this.save(recipeConsumer, ForgeRegistries.ITEMS.getKey(item));
    }

    public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation location) {
        ResourceLocation recipePath = ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, location.withPrefix(this.getRecipePath()).getPath());
        recipeConsumer.accept(this.createResult(recipePath, this.getRecipeSerializer()));
    }
}
