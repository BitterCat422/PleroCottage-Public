package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import net.bittercat.plerocottage.blocks.FrigidGrassBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class FrigidGrass {

    public static final RegistryObject<FrigidGrassBlock> BLOCK =
        BlockRegistry.register("mugwort", FrigidGrassBlock::new);

    public static final RegistryObject<Item> ITEM = ItemRegistry.register("mugwort",
        () -> new BlockItem(BLOCK.get(), new Item.Properties()));

    private FrigidGrass() {
    }

    public static void bootstrap() {
        
        PlantCatalog.addCompostable(ITEM, 0.3F);
        PlantCatalog.markFlatIcon(ITEM);
    }
}
