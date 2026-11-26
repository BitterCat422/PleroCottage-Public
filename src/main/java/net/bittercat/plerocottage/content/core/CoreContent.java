package net.bittercat.plerocottage.content.core;

import net.bittercat.plerocottage.blocks.EggIncubatorBlock;
import net.bittercat.plerocottage.blocks.HayNestBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.items.CinderItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public final class CoreContent {
    public static final RegistryObject<Block> HAY_NEST = BlockRegistry.register("hay_nest", HayNestBlock::new);
    public static final RegistryObject<Block> EGG_INCUBATOR =
        BlockRegistry.register("egg_incubator", EggIncubatorBlock::new);

    public static final RegistryObject<Item> MAIN_ICON = ItemRegistry.register("main_icon_item");
    public static final RegistryObject<Item> BUILDING_ICON = ItemRegistry.register("building_icon_item");
    public static final RegistryObject<Item> MACHINE_ICON =
        ItemRegistry.register("plerocottage_machine_icon");
    public static final RegistryObject<Item> NATURAL_PRODUCTS_ICON = ItemRegistry.register("natural_products_icon");
    public static final RegistryObject<Item> PROCESSED_PRODUCTS_ICON =
        ItemRegistry.register("processed_products_icon");
    public static final RegistryObject<Item> COP_STRAIGHT_OUTTA_USA_ICON =
        ItemRegistry.register("cop_straight_outta_usa_icon");
    public static final RegistryObject<Item> FRENCH_HONEYMOON_ICON =
        ItemRegistry.register("french_honeymoon_icon");
    public static final RegistryObject<Item> MILK_TEA_ALLIANCE_ICON =
        ItemRegistry.register("milk_tea_alliance_icon");
    public static final RegistryObject<Item> YUTU_PROGRAM_ICON = ItemRegistry.register("yutu_program_icon");
    public static final RegistryObject<Item> CINDER =
        ItemRegistry.register("cinder", () -> new CinderItem(new Item.Properties()));
    public static final RegistryObject<Item> HAY_NEST_ITEM =
        ItemRegistry.registerBlockItem("hay_nest", HAY_NEST);
    public static final RegistryObject<Item> EGG_INCUBATOR_ITEM =
        ItemRegistry.registerBlockItem("egg_incubator", EGG_INCUBATOR);

    private CoreContent() {
    }

    public static void bootstrap() {
    }
}
