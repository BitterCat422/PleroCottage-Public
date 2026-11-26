package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.bittercat.plerocottage.blocks.WildRiceBlock;
import net.minecraftforge.registries.RegistryObject;

public final class WildRice {

    public static final RegistryObject<WildRiceBlock> BLOCK =
        BlockRegistry.register("wild_rice", WildRiceBlock::new);

    public static final RegistryObject<Item> ITEM = ItemRegistry.register("wild_rice",
        () -> new DoubleHighBlockItem(BLOCK.get(), new Item.Properties()));

    private WildRice() {
    }

    public static void bootstrap() {
        PlantCatalog.addCompostable(ITEM, 0.65F);
    }
}
