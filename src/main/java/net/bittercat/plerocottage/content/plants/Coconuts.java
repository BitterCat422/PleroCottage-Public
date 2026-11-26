package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;
import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.PROCESSED;

import java.util.function.Supplier;

import net.bittercat.plerocottage.blocks.FallenFruitBlock;
import net.bittercat.plerocottage.blocks.HalfOfCoconutBlock;
import net.bittercat.plerocottage.blocks.CupBlock;
import net.bittercat.plerocottage.blocks.YoungCoconutsBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.bittercat.plerocottage.items.BlockFoodItem;
import net.bittercat.plerocottage.items.ConsumableItem;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public final class Coconuts {

    public static final RegistryObject<Block> COCONUT = BlockRegistry.register("coconut",
        () -> new FallenFruitBlock(MapColor.PLANT, Block.box(3.351, 0.001, 3.351, 12.649, 9.299, 12.649)));

    public static final RegistryObject<Block> YOUNG_COCONUT = BlockRegistry.register("young_coconut",
        () -> new FallenFruitBlock(MapColor.PLANT, Block.box(3.25, 0, 3.25, 12.75, 9.5, 12.75)));

    public static final RegistryObject<Block> YOUNG_COCONUTS = BlockRegistry.register("young_coconuts",
        YoungCoconutsBlock::new);

    public static final RegistryObject<Block> HALF_OF_COCONUT_BLOCK =
        BlockRegistry.register("half_of_coconut", HalfOfCoconutBlock::new);

    public static final RegistryObject<Block> CUP_BLOCK =
        BlockRegistry.register("reactive_glaze_mug_block", CupBlock::new);

    public static final RegistryObject<Item> COCONUT_ITEM = flatIcon("coconut",
        () -> new BlockItem(COCONUT.get(), new Item.Properties()));

    public static final RegistryObject<Item> YOUNG_COCONUT_ITEM = flatIcon("young_coconut",
        () -> new BlockItem(YOUNG_COCONUT.get(), new Item.Properties()));

    public static final RegistryObject<Item> YOUNG_COCONUTS_ITEM = flatIcon("young_coconuts",
        () -> new BlockItem(YOUNG_COCONUTS.get(), new Item.Properties()));

    public static final RegistryObject<Item> HALF_OF_COCONUT_ITEM = flatIcon("half_of_coconut",
        () -> new BlockFoodItem(HALF_OF_COCONUT_BLOCK.get(),
            FoodDefinition.get("half_of_coconut").itemProperties(),
            FoodDefinition.get("half_of_coconut").useDuration(), true,
            FoodDefinition.get("half_of_coconut").useAnim(), null, true));

    public static final RegistryObject<Item> COCONUT_BLOSSOM = ItemRegistry.register("coconut_blossoms",
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COCONUT_MEAT = ItemRegistry.register("coconut_meat",
        () -> FoodDefinition.get("coconut_meat").consumable());

    public static final RegistryObject<Item> GRATED_COCONUT = ItemRegistry.register("grated_coconut",
        () -> FoodDefinition.get("grated_coconut").consumable());

    public static final RegistryObject<Item> COCONUT_BUN = ItemRegistry.register("coconut_bun",
        () -> FoodDefinition.get("coconut_bun").item());

    public static final RegistryObject<Item> COCONUT_SUGAR_CARAMEL =
        ItemRegistry.register("coconut_sugar_caramels",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CUP = ItemRegistry.register("cup",
        () -> new BlockItem(CUP_BLOCK.get(), new Item.Properties()));

    private Coconuts() {
    }

    public static void bootstrap() {

        PlantCatalog.addCompostable(YOUNG_COCONUT_ITEM, 0.65F);
        PlantCatalog.addCompostable(COCONUT_ITEM, 0.65F);
        PlantCatalog.addCompostable(COCONUT_BLOSSOM, 0.65F);
    }

    private static RegistryObject<Item> flatIcon(String name, Supplier<? extends Item> item) {
        RegistryObject<Item> registered = ItemRegistry.register(name, item);
        PlantCatalog.markFlatIcon(registered);
        return registered;
    }

}

