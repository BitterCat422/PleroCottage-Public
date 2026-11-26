package net.bittercat.plerocottage.content.food;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.bittercat.plerocottage.blocks.CakeBlock;
import net.bittercat.plerocottage.blocks.CheeseWheelBlock;
import net.bittercat.plerocottage.blocks.MultiBlockPieBlock;
import net.bittercat.plerocottage.blocks.PizzaBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public final class FoodBlockContent {
    private static final List<FoodBlock> BLOCKS = new ArrayList<>();
    private static final Map<String, String> PORTION_ITEMS = Map.ofEntries(
        Map.entry("nether_wart_soul_cake", "nether_wart_soul_cake_slice"),
        Map.entry("caramel_chocolate_cake", "caramel_chocolate_cake_slice"),
        Map.entry("sweet_berry_cake", "sweet_berry_cake_slice"),
        Map.entry("chorus_flower_jelly_cake", "chorus_flower_jelly_cake_slice"),
        Map.entry("pavlova_cake", "pavlova_cake_slice"),
        Map.entry("strawberry_shortcake", "strawberry_shortcake_slice"),
        Map.entry("cherries_pie", "cherries_pie_slice"),
        Map.entry("truffle_pie", "truffle_pie_slice"),
        Map.entry("vanilla_pizza", "vanilla_pizza_slice"),
        Map.entry("pepperoni_pizza", "pepperoni_pizza_slice"),
        Map.entry("supreme_pizza", "supreme_pizza_slice"),
        Map.entry("seafood_pizza", "seafood_pizza_slice"),
        Map.entry("durian_pizza", "durian_pizza_slice"),
        Map.entry("hawaiian_pizza", "hawaiian_pizza_slice"),
        Map.entry("cheesy_mushroom_bacon_pizza", "cheesy_mushroom_bacon_pizza_slice"),
        Map.entry("emmental_cheese_wheel", "emmental_cheese_wedge"),
        Map.entry("cheddar_cheese_wheel", "cheddar_cheese_wedge"),
        Map.entry("mozzarella_cheese_wheel", "mozzarella_cheese_wedge"));

    public record FoodBlock(String name, RegistryObject<Block> block, RegistryObject<Item> item) {
    }

    private FoodBlockContent() {
    }

    public static void bootstrap() {
        cake("caramel_chocolate_cake");
        cake("nether_wart_soul_cake");
        cake("sweet_berry_cake");
        cake("chorus_flower_jelly_cake");
        cake("pavlova_cake");
        cake("strawberry_shortcake");
        pie("cherries_pie");
        pie("truffle_pie");
        pizza("vanilla_pizza");
        pizza("pepperoni_pizza");
        pizza("supreme_pizza");
        pizza("seafood_pizza");
        pizza("durian_pizza");
        pizza("hawaiian_pizza");
        pizza("cheesy_mushroom_bacon_pizza");
        cheeseWheel("emmental_cheese_wheel");
        cheeseWheel("cheddar_cheese_wheel");
        cheeseWheel("mozzarella_cheese_wheel");
    }

    private static void cake(String name) {
        register(name, () -> new CakeBlock(() -> portionItem(name)));
    }

    private static void pie(String name) {
        register(name, () -> new MultiBlockPieBlock(() -> portionItem(name)));
    }

    private static void pizza(String name) {
        register(name, () -> new PizzaBlock(() -> portionItem(name)));
    }

    private static void cheeseWheel(String name) {
        register(name, () -> new CheeseWheelBlock(() -> portionItem(name)));
    }

    private static Item portionItem(String blockName) {
        String itemName = PORTION_ITEMS.get(blockName);
        if (itemName == null) {
            throw new IllegalStateException("未配置食物方块对应的分食物品：" + blockName);
        }
        return FoodContent.item(itemName).get();
    }

    private static void register(String name, java.util.function.Supplier<? extends Block> factory) {
        RegistryObject<Block> block = BlockRegistry.register(name, factory);
        RegistryObject<Item> item = ItemRegistry.registerBlockItem(name, block);
        BLOCKS.add(new FoodBlock(name, block, item));
        
    }

    public static List<FoodBlock> entries() {
        return List.copyOf(BLOCKS);
    }
}
