package net.bittercat.plerocottage.content.food;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.PROCESSED;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blocks.PlateBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public final class PlateContent {
    public static final RegistryObject<Block> PLATE =
        BlockRegistry.register("plate", PlateBlock::new);
    public static final RegistryObject<Item> PLATE_ITEM = ItemRegistry.registerBlockItem("plate", PLATE);

    private PlateContent() {
    }

    public static void bootstrap() {

    }
}
