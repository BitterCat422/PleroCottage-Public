package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import net.bittercat.plerocottage.blocks.DragonFruitBlock;
import net.bittercat.plerocottage.blocks.DragonFruitCactusBlock;
import net.bittercat.plerocottage.blocks.DragonFruitCactusFlowerBlock;
import net.bittercat.plerocottage.blocks.DragonFruitCropBlock;
import net.bittercat.plerocottage.blocks.DragonFruitSliceBlock;
import net.bittercat.plerocottage.items.DragonFruitSliceItem;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.registries.RegistryObject;

public final class DragonFruits {
    public static final RegistryObject<DragonFruitCactusBlock> CACTUS =
        BlockRegistry.register("dragon_fruit_cactus", DragonFruitCactusBlock::new);
    public static final RegistryObject<DragonFruitCactusFlowerBlock> CACTUS_FLOWER =
        BlockRegistry.register("dragon_fruit_cactus_flower", DragonFruitCactusFlowerBlock::new);
    public static final RegistryObject<DragonFruitCropBlock> CROP =
        BlockRegistry.register("dragon_fruit_crop", DragonFruitCropBlock::new);
    public static final RegistryObject<DragonFruitBlock> FRUIT_BLOCK =
        BlockRegistry.register("dragon_fruit", DragonFruitBlock::new);
    public static final RegistryObject<DragonFruitSliceBlock> SLICE_BLOCK =
        BlockRegistry.register("dragon_fruit_slices", DragonFruitSliceBlock::new);

    public static final RegistryObject<Item> FRUIT = ItemRegistry.register("dragon_fruit",
        () -> FoodDefinition.get("dragon_fruit").blockItem(FRUIT_BLOCK.get(), true));
    public static final RegistryObject<Item> CACTUS_FLOWER_ITEM = ItemRegistry.register("dragon_fruit_cactus_flower",
        () -> new BlockItem(CACTUS_FLOWER.get(), new Item.Properties()));
    public static final RegistryObject<Item> CACTUS_ITEM = ItemRegistry.register("dragon_fruit_cactus",
        () -> new BlockItem(CACTUS.get(), new Item.Properties()));
    public static final RegistryObject<Item> CROP_ITEM = ItemRegistry.register("dragon_fruit_crop",
        () -> new ItemNameBlockItem(CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> SLICE = ItemRegistry.register("dragon_fruit_slices",
        () -> new DragonFruitSliceItem(SLICE_BLOCK.get(), FoodDefinition.get("dragon_fruit_slices")));

    private DragonFruits() {}

    public static void bootstrap() {
        PlantCatalog.addCompostable(FRUIT, 0.65F);
        PlantCatalog.addCompostable(CACTUS_ITEM, 0.5F);
        PlantCatalog.addCompostable(CACTUS_FLOWER_ITEM, 0.65F);
    }
}
