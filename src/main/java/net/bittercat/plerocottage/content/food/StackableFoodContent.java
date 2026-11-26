package net.bittercat.plerocottage.content.food;

import java.util.ArrayList;
import java.util.List;

import net.bittercat.plerocottage.blocks.StackableFoodBlock;
import net.bittercat.plerocottage.blocks.StackableFoodDisplayBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.items.StackableFoodItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class StackableFoodContent {
    private static final List<Entry> ENTRIES = new ArrayList<>();

    public record Entry(String name, RegistryObject<Block> block, RegistryObject<Item> food,
                        int maximumState) {
    }

    private StackableFoodContent() {
    }

    public static void bootstrap() {
        registerMuffin("muffin");
        registerMuffin("blueberry_muffin");
        registerMuffin("cranberry_muffin");
        registerMuffin("chocolate_muffin");
        registerMuffin("orange_muffin");
        registerMuffin("elderberry_muffin");
        register("baguette", 2, false);
    }

    private static void registerMuffin(String name) {
        register(name, 3, true);
    }

    private static void register(String name, int maximumState, boolean muffin) {
        RegistryObject<Block> block = BlockRegistry.register(name, () -> muffin
            ? new StackableFoodDisplayBlock(name)
            : new StackableFoodBlock(name, true));
        FoodDefinition definition = FoodDefinition.get(name);
        RegistryObject<Item> food = ItemRegistry.register(name, () -> new StackableFoodItem(
            definition.itemProperties(), definition.useDuration(), block::get, maximumState));
        ENTRIES.add(new Entry(name, block, food, maximumState));
    }

    public static List<Entry> entries() {
        return List.copyOf(ENTRIES);
    }
}
