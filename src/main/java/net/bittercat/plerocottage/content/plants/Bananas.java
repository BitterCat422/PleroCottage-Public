package net.bittercat.plerocottage.content.plants;

import java.util.function.Supplier;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blocks.BananaBunchBlock;
import net.bittercat.plerocottage.blocks.BananaCrownBlock;
import net.bittercat.plerocottage.blocks.BananaFlowerBlock;
import net.bittercat.plerocottage.blocks.BananaFrondBlock;
import net.bittercat.plerocottage.blocks.BananaFrondSaplingBlock;
import net.bittercat.plerocottage.blocks.BananaTreeBunchBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
import net.bittercat.plerocottage.wood.WoodSet;
import net.bittercat.plerocottage.items.DisplayBlockItem;
import net.minecraft.resources.ResourceLocation;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraftforge.registries.RegistryObject;

public final class Bananas {

    private static final WoodSet WOOD_SET =
        WoodSetContent.byName("banana_tree").orElseThrow();
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_STALK = WOOD_SET.strippedLog();
    public static final RegistryObject<RotatedPillarBlock> STALK = WOOD_SET.log();
    public static final RegistryObject<Block> PLANKS = WOOD_SET.planks();
    public static final RegistryObject<StairBlock> STAIRS = WOOD_SET.stairs();
    public static final RegistryObject<SlabBlock> SLAB = WOOD_SET.slab();
    public static final RegistryObject<FenceBlock> FENCE = WOOD_SET.fence();
    public static final RegistryObject<FenceGateBlock> FENCE_GATE = WOOD_SET.fenceGate();
    public static final RegistryObject<DoorBlock> DOOR = WOOD_SET.door();
    public static final RegistryObject<TrapDoorBlock> TRAPDOOR = WOOD_SET.trapdoor();
    public static final RegistryObject<PressurePlateBlock> PRESSURE_PLATE = WOOD_SET.pressurePlate();
    public static final RegistryObject<ButtonBlock> BUTTON = WOOD_SET.button();

    public static final RegistryObject<Block> FROND =
        BlockRegistry.register("banana_frond", BananaFrondBlock::new);

    public static final RegistryObject<Block> CROWN =
        BlockRegistry.register("banana_frond_block", BananaCrownBlock::new);

    public static final RegistryObject<Block> SAPLING =
        BlockRegistry.register("banana_frond_sapling", BananaFrondSaplingBlock::new);

    public static final RegistryObject<Block> FLOWER =
        BlockRegistry.register("banana_tree_flower", BananaFlowerBlock::new);

    public static final RegistryObject<Block> TREE_BUNCH =
        BlockRegistry.register("banana_tree_bunch", BananaTreeBunchBlock::new);

    public static final RegistryObject<Block> BUNCH =
        BlockRegistry.register("banana_bunch", BananaBunchBlock::new);

    public static final RegistryObject<Item> BANANA = ItemRegistry.register("banana",
        () -> FoodDefinition.get("banana").item());

    public static final RegistryObject<Item> BANANA_SLICE = ItemRegistry.register("banana_slices",
        () -> FoodDefinition.get("banana_slices").consumable());

    private static final RegistryObject<Item> FROND_ITEM = flatIcon("banana_frond", FROND);
    
    public static final RegistryObject<Item> SAPLING_ITEM = flatIcon("banana_frond_sapling", SAPLING);
    public static final RegistryObject<Item> BUNCH_ITEM = flatIcon("banana_bunch", BUNCH);
    private static final RegistryObject<Item> FLOWER_ITEM = blockItem("banana_tree_flower", FLOWER);

    private static final RegistryObject<Item> CROWN_ITEM = ItemRegistry.register("banana_frond_block",
        () -> new DisplayBlockItem(CROWN.get(), new Item.Properties(),
            resource("geo/banana_frond_block.geo.json"),
            resource("animations/banana_frond_block.animation.json"),
            resource("textures/block/banana_frond_block_none.png")));

    private static final RegistryObject<Item> TREE_BUNCH_ITEM = ItemRegistry.register("banana_tree_bunch",
        () -> new DisplayBlockItem(TREE_BUNCH.get(), new Item.Properties(),
            resource("geo/banana_bunch_1.geo.json"),
            resource("animations/banana_bunch_1.animation.json"),
            resource("textures/block/banana_bunch_1.png")));

    private Bananas() {
    }

    public static void bootstrap() {
        
        PlantCatalog.addCompostable(BANANA, 0.5F);
        PlantCatalog.addCompostable(FLOWER_ITEM, 0.65F);
        PlantCatalog.addCompostable(FROND_ITEM, 0.3F);
        PlantCatalog.addCompostable(SAPLING_ITEM, 0.3F);
    }

    private static RegistryObject<Item> blockItem(String name, Supplier<? extends Block> block) {
        return ItemRegistry.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static RegistryObject<Item> flatIcon(String name, Supplier<? extends Block> block) {
        RegistryObject<Item> registered = blockItem(name, block);
        PlantCatalog.markFlatIcon(registered);
        return registered;
    }

    private static ResourceLocation resource(String path) {
        return new ResourceLocation(PleroCottage.MODID, path);
    }
}

