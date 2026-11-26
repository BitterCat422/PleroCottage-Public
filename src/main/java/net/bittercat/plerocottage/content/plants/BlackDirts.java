package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import java.util.function.Supplier;

import net.bittercat.plerocottage.blocks.BlackDirtBlock;
import net.bittercat.plerocottage.blocks.BlackDirtTruffleBlock;
import net.bittercat.plerocottage.blocks.RichBlackDirtFarmlandBlock;
import net.bittercat.plerocottage.blocks.SnowyBlackDirtBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public final class BlackDirts {

    public static final RegistryObject<Block> BLACK_DIRT = registerBlock("black_dirt", BlackDirtBlock::new);

    public static final RegistryObject<Block> SNOWY_BLACK_DIRT =
        registerBlock("snowy_black_dirt", SnowyBlackDirtBlock::new);

    public static final RegistryObject<Block> RICH_BLACK_DIRT_FARMLAND =
        registerBlock("rich_black_dirt_farmland", RichBlackDirtFarmlandBlock::new);

    public static final RegistryObject<Block> BLACK_DIRT_TRUFFLE =
        registerBlock("black_dirt_truffle", BlackDirtTruffleBlock::new);

    public static final RegistryObject<Item> TRUFFLE = ItemRegistry.register("truffle",
        () -> new Item(new Item.Properties()));

    private BlackDirts() {
    }

    public static void bootstrap() {

    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<? extends Block> block) {
        RegistryObject<Block> registered = BlockRegistry.register(name, block);
        ItemRegistry.register(name, () -> new BlockItem(registered.get(), new Item.Properties()));
        return registered;
    }
}
