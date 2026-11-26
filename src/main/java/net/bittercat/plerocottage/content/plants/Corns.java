package net.bittercat.plerocottage.content.plants;

import java.util.Map;

import net.bittercat.plerocottage.blocks.BlockRef;
import net.bittercat.plerocottage.blocks.StackableCropBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public final class Corns {
    private static final TagKey<Item> FORGE_VEGETABLES =
        ItemTags.create(new ResourceLocation("forge", "vegetables"));
    private static final BlockRef CORN_BLOCK = new BlockRef();

    private static final VoxelShape FULL = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape SMALL = Block.box(2, 0, 2, 14, 15, 14);

    public static final RegistryObject<Item> CORN_SEED = ItemRegistry.register("corn_seeds",
        () -> new ItemNameBlockItem(CORN_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> CORN = ItemRegistry.register("corn",
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<StackableCropBlock> CORN_CROP_BLOCK =
        BlockRegistry.register("corn_crop", () -> new StackableCropBlock(
            new StackableCropBlock.Settings(CORN_SEED, 2, 0.15F,
                new VoxelShape[] {FULL, FULL, FULL},
                new VoxelShape[] {SMALL, FULL, FULL})));
    public static final RegistryObject<Item> CORN_CROP_ITEM = ItemRegistry.register("corn_crop",
        () -> new net.minecraft.world.item.BlockItem(CORN_CROP_BLOCK.get(), new Item.Properties()));

    public static final PlantEntry ENTRY = new PlantEntry() {
        @Override public String name() { return "corn_crop"; }
        @Override public Item fruitItem() { return CORN.get(); }
        @Override public boolean isFruit() { return false; }
        @Override public Map<Item, Float> compostables() { return Map.of(CORN.get(), 0.65F, CORN_SEED.get(), 0.3F); }
    };

    private Corns() {}

    public static void bootstrap() {
        CORN_BLOCK.fill(CORN_CROP_BLOCK);
        PlantCatalog.add(ENTRY);

    }
}

