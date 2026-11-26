package net.bittercat.plerocottage.init;

import java.util.List;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.food.SeasoningContent;
import net.bittercat.plerocottage.content.plants.Flowers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class BrewingRecipesRegistry {

    private BrewingRecipesRegistry() {
    }

    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> recipes().forEach(BrewingRecipesRegistry::addRecipe));
    }

    public static List<Recipe> recipes() {
        return List.of(
            new Recipe(Potions.FIRE_RESISTANCE, Flowers.item("orange_blossom").get(),
                SeasoningContent.ORANGE_BLOSSOM_WATER.get()),
            new Recipe(Potions.INVISIBILITY, Flowers.item("rose").get(),
                SeasoningContent.ROSA_WATER.get()),
            new Recipe(Potions.REGENERATION, Flowers.item("lavender").get(),
                SeasoningContent.LAVENDER_ESSENTIAL_OIL.get()));
    }

    private static void addRecipe(Recipe recipe) {
        BrewingRecipeRegistry.addRecipe(new IBrewingRecipe() {
            @Override
            public boolean isInput(ItemStack input) {
                return input.is(Items.POTION) && PotionUtils.getPotion(input) == recipe.potion();
            }

            @Override
            public boolean isIngredient(ItemStack ingredient) {
                return ingredient.is(recipe.ingredient());
            }

            @Override
            public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
                return isInput(input) && isIngredient(ingredient)
                    ? new ItemStack(recipe.output())
                    : ItemStack.EMPTY;
            }
        });
    }

    public record Recipe(Potion potion, Item ingredient, Item output) {
        public ItemStack inputStack() {
            return PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
        }
    }
}
