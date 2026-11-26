package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import net.bittercat.plerocottage.machine.items.ShakerItem;

public final class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, PleroCottage.MODID);
    public static final DeferredRegister<Item> ITEM = ITEMS;
    public static final RegistryObject<Item> ICE_CREAM_MACHINE =
        ITEMS.register("ice_cream_machine", () -> new BlockItem(BlockRegistry.ICE_CREAM_MACHINE.get(), new Item.Properties()));
    public static final RegistryObject<Item> FRYER =
        ITEMS.register("fryer", () -> new BlockItem(BlockRegistry.FRYER.get(), new Item.Properties()));
    public static final RegistryObject<Item> OVEN =
        ITEMS.register("oven", () -> new BlockItem(BlockRegistry.OVEN.get(), new Item.Properties()));
    public static final RegistryObject<Item> TEAPOT =
        ITEMS.register("teapot", () -> new BlockItem(BlockRegistry.TEAPOT.get(), new Item.Properties()));
    public static final RegistryObject<Item> FERMENTER =
        ITEMS.register("fermenter", () -> new BlockItem(BlockRegistry.FERMENTER.get(), new Item.Properties()));
    public static final RegistryObject<Item> BLENDER =
        ITEMS.register("blender", () -> new BlockItem(BlockRegistry.BLENDER.get(), new Item.Properties()));
    public static final RegistryObject<Item> CUTTING_BOARD =
        ITEMS.register("cutting_board", () -> new BlockItem(BlockRegistry.CUTTING_BOARD.get(), new Item.Properties()));
    public static final RegistryObject<Item> STOCK_POT =
        ITEMS.register("stock_pot", () -> new BlockItem(BlockRegistry.STOCK_POT.get(), new Item.Properties()));
    public static final RegistryObject<Item> SAUCEPAN_AND_WHISK =
        ITEMS.register("saucepan_and_whisk", () -> new BlockItem(BlockRegistry.SAUCEPAN_AND_WHISK.get(), new Item.Properties()));
    public static final RegistryObject<Item> SHAKER = ITEMS.register("shaker", ShakerItem::new);
    public static final RegistryObject<Item> COOKTOP =
        ITEMS.register("cooktop", () -> new BlockItem(BlockRegistry.COOKTOP.get(), new Item.Properties()));

    private ItemRegistry() {
    }

    public static RegistryObject<Item> register(String id, Supplier<? extends Item> factory) {
        return ITEMS.register(id, factory);
    }

    public static RegistryObject<Item> register(String id) {
        return register(id, () -> new Item(new Item.Properties()));
    }

    public static RegistryObject<Item> register(String id, Item.Properties properties) {
        return register(id, () -> new Item(properties));
    }

    public static RegistryObject<Item> registerBlockItem(String id, RegistryObject<? extends Block> block) {
        return register(id, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
