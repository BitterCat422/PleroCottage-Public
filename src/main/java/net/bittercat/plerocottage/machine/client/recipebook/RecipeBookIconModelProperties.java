package net.bittercat.plerocottage.machine.client.recipebook;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.init.ItemRegistry;

public final class RecipeBookIconModelProperties {
    public static final String NBT_KEY = "RecipeBookIcon";
    private static final ResourceLocation PROPERTY = ResourceLocation.fromNamespaceAndPath(
            PleroCottageMachine.MOD_ID,
            "recipe_book_icon"
    );

    private RecipeBookIconModelProperties() {
    }

    public static void register() {
        register(ItemRegistry.FRYER.get(), new String[]{"fast", "slow"});
        register(ItemRegistry.FERMENTER.get(), new String[]{"dim", "normal", "bright"});
    }

    private static void register(Item item, String[] icons) {
        ItemProperties.register(item, PROPERTY, (stack, level, entity, seed) -> {
            if (!stack.hasTag()) {
                return 0.0F;
            }
            CompoundTag tag = stack.getTag();
            String icon = tag.getString(NBT_KEY);
            for (int i = 0; i < icons.length; i++) {
                if (icons[i].equals(icon)) {
                    return i + 1.0F;
                }
            }
            return 0.0F;
        });
    }
}
