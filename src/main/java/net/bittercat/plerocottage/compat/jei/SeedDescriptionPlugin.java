package net.bittercat.plerocottage.compat.jei;

import java.util.List;

import net.bittercat.plerocottage.data.ItemTagProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;

@JeiPlugin
public final class SeedDescriptionPlugin implements IModPlugin {

    private static final List<Description> DESCRIPTIONS = List.of(
        new Description(ItemTagProvider.NORMAL_DIRT_SEEDS, "normal_dirt_seed_tips"),
        new Description(ItemTagProvider.NORMAL_FARMLAND_SEEDS, "normal_farmland_seed_tips"),
        new Description(ItemTagProvider.SPECIAL_SEEDS, "special_seed_tips"),
        new Description(ItemTagProvider.VINE_SEEDS, "vine_seed_tips"),
        new Description(ItemTagProvider.BLACK_FARMLAND_SEEDS, "black_farmland_seed_tips"),
        new Description(ItemTagProvider.WATER_DIRT_SEEDS, "water_dirt_seed_tips"),
        new Description(ItemTagProvider.END_SEEDS, "end_seed_tips"),
        new Description(ItemTagProvider.SAND_SEEDS, "sand_seed_tips"));

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("plerocottage", "seed_descriptions");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (Description description : DESCRIPTIONS) {
            List<ItemStack> items = BuiltInRegistries.ITEM.stream()
                .filter(item -> item != Items.AIR && item.builtInRegistryHolder().is(description.tag()))
                .map(ItemStack::new)
                .toList();
            if (!items.isEmpty()) {
                registration.addItemStackInfo(items,
                    Component.translatable("tips.plerocottage." + description.translationKey()));
            }
        }
    }

    private record Description(TagKey<Item> tag, String translationKey) {
    }
}
