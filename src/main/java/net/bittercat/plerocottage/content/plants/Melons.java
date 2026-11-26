package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import java.util.List;
import java.util.function.Supplier;

import net.bittercat.plerocottage.blocks.HamimelonAttachedStemBlock;
import net.bittercat.plerocottage.blocks.HamimelonBlock;
import net.bittercat.plerocottage.blocks.HamimelonStemBlock;
import net.bittercat.plerocottage.data.ItemTagProvider;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.bittercat.plerocottage.items.ConsumableItem;
import net.bittercat.plerocottage.worldgen.WildCrop;
import net.bittercat.plerocottage.worldgen.WildGround;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.RegistryObject;

public final class Melons {
    public static final RegistryObject<Block> HAMIMELON = BlockRegistry.register("muskmelon",
        HamimelonBlock::new);
    public static final RegistryObject<StemBlock> HAMIMELON_STEM = BlockRegistry.register("muskmelon_stem",
        HamimelonStemBlock::new);
    public static final RegistryObject<AttachedStemBlock> HAMIMELON_ATTACHED_STEM =
        BlockRegistry.register("muskmelon_attached_stem", HamimelonAttachedStemBlock::new);
    public static final RegistryObject<Item> HAMIMELON_SEED = ItemRegistry.register("muskmelon_seeds",
        () -> new ItemNameBlockItem(HAMIMELON_STEM.get(), new Item.Properties()));
    public static final RegistryObject<Item> HAMIMELON_SLICE = ItemRegistry.register("muskmelon_slice",
        () -> FoodDefinition.get("muskmelon_slice").consumable());
    public static final RegistryObject<Item> HAMIMELON_ITEM = ItemRegistry.register("muskmelon",
        () -> new BlockItem(HAMIMELON.get(), new Item.Properties()));

    public static final TagKey<Item> FRUIT_TAG =
        ItemTags.create(new ResourceLocation("forge", "fruits/muskmelon"));
    public static final TagKey<Item> SEED_TAG = ItemTagProvider.NORMAL_FARMLAND_SEEDS;

    public static final WildCrop WILD_HAMIMELON = WildCrop.fruit("wild_muskmelon", HAMIMELON, WildGround.SAND,
        96, 3, 3, 32, Heightmap.Types.OCEAN_FLOOR_WG);

    private Melons() {
    }

    public static void bootstrap() {
        PlantCatalog.markFlatIcon(HAMIMELON_ITEM);
        PlantCatalog.markFlatIcon(HAMIMELON_SEED);
        PlantCatalog.addCompostable(HAMIMELON_ITEM, 0.65F);
        PlantCatalog.addCompostable(HAMIMELON_SLICE, 0.5F);
    }

    public static List<WildCrop> wildFeatures() {
        return List.of(WILD_HAMIMELON);
    }
}

