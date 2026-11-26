package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.blocks.AlfalfaCropBlock;
import net.bittercat.plerocottage.blocks.BlockRef;
import net.bittercat.plerocottage.blocks.PeanutCropBlock;
import net.bittercat.plerocottage.blocks.MintCropBlock;
import net.bittercat.plerocottage.blocks.PineappleBlock;
import net.bittercat.plerocottage.blocks.PineappleCropBlock;
import net.bittercat.plerocottage.blocks.WildPeanutBlock;
import net.bittercat.plerocottage.blocks.WildMintBlock;
import net.bittercat.plerocottage.data.BlockTagProvider;
import net.bittercat.plerocottage.data.ItemTagProvider;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.bittercat.plerocottage.items.AlfalfaItem;
import net.bittercat.plerocottage.items.ConsumableItem;
import net.bittercat.plerocottage.worldgen.WildCrop;
import net.bittercat.plerocottage.worldgen.WildGround;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.RegistryObject;

public final class Crops {

    private static final int STAGES = CropBlock.MAX_AGE + 1;

    private static final TagKey<Item> FORGE_SEEDS = ItemTags.create(new ResourceLocation("forge", "seeds"));
    private static final TagKey<Item> FORGE_CROPS_ALFALFA =
        ItemTags.create(new ResourceLocation("forge", "crops/alfalfa"));
    private static final TagKey<Item> FORGE_CROPS_PEANUT =
        ItemTags.create(new ResourceLocation("forge", "crops/peanuts"));
    private static final TagKey<Item> FORGE_CROPS_MINT =
        ItemTags.create(new ResourceLocation("forge", "crops/mint"));

    private static final List<Crop> ENTRIES = new ArrayList<>();

    private static final BlockRef ALFALFA_BLOCK = new BlockRef();

    public static final RegistryObject<Item> ALFALFA_SEED = plantingItem("alfalfa_seeds", ALFALFA_BLOCK);

    public static final RegistryObject<Item> ALFALFA = ItemRegistry.register("alfalfa", AlfalfaItem::new);

    public static final RegistryObject<CropBlock> ALFALFA_CROP_BLOCK = BlockRegistry.register("alfalfa_crop",
        () -> new AlfalfaCropBlock(BlockTagProvider.BLACK_FARMLAND, ALFALFA_SEED));

    public static final Crop ALFALFA_CROP = finish(new Crop("alfalfa_crop", Shape.CROP, ALFALFA_CROP_BLOCK,
        null, ALFALFA_SEED, ALFALFA, null, numberedStages("alfalfa_crop"), "alfalfa_crop_1",
        List.of(FORGE_CROPS_ALFALFA), List.of(FORGE_SEEDS, ItemTagProvider.BLACK_FARMLAND_SEEDS),
        new Drops(true, 1, 3, 4, 6), compost(ALFALFA, 0.65F, ALFALFA_SEED, 0.3F), null));

    private static final BlockRef PEANUT_BLOCK = new BlockRef();

    public static final RegistryObject<Item> PEANUT = plantingItem("peanuts", PEANUT_BLOCK);

    public static final RegistryObject<Item> COOKED_PEANUT = ItemRegistry.register("cooked_peanuts",
        () -> FoodDefinition.get("cooked_peanuts").consumable());

    public static final RegistryObject<CropBlock> PEANUT_CROP_BLOCK = BlockRegistry.register("peanut_crop",
        () -> new PeanutCropBlock(PEANUT));

    public static final RegistryObject<Item> PEANUT_CROP_ITEM = ItemRegistry.register("peanut_crop",
        () -> new BlockItem(PEANUT_CROP_BLOCK.get(), new Item.Properties()));

    public static final Crop PEANUT_CROP = finish(new Crop("peanut_crop", Shape.CROSS, PEANUT_CROP_BLOCK,
        "peanuts", PEANUT, PEANUT, null, numberedStages("peanut_crop"), "peanut_crop_1",
        List.of(FORGE_CROPS_PEANUT), List.of(ItemTagProvider.NORMAL_FARMLAND_SEEDS),
        new Drops(false, 0, 0, 3, 4), compost(PEANUT, 0.3F), null));

    private static final BlockRef MINT_BLOCK = new BlockRef();

    public static final RegistryObject<Item> MINT = plantingItem("mint", MINT_BLOCK);

    public static final RegistryObject<CropBlock> MINT_CROP_BLOCK = BlockRegistry.register("mint_crop",
        () -> new MintCropBlock(MINT));

    public static final Crop MINT_CROP = finish(new Crop("mint_crop", Shape.CROSS, MINT_CROP_BLOCK,
        null, MINT, MINT, null, numberedStages("mint_crop"), "mint_crop_1",
        List.of(FORGE_CROPS_MINT), List.of(FORGE_SEEDS, ItemTagProvider.NORMAL_FARMLAND_SEEDS),
        new Drops(false, 0, 0, 3, 5), compost(MINT, 0.65F), null));

    public static final RegistryObject<WildMintBlock> WILD_MINT =
        BlockRegistry.register("wild_mint", WildMintBlock::new);

    public static final RegistryObject<Item> WILD_MINT_ITEM = ItemRegistry.register("wild_mint",
        () -> new DoubleHighBlockItem(WILD_MINT.get(), new Item.Properties()));

    private static final WildCrop WILD_MINT_PATCH = WildCrop.plant("wild_mint", WILD_MINT, 12, 7, 3, 8, 32,
        Heightmap.Types.WORLD_SURFACE_WG);

    public static final RegistryObject<Block> WILD_PEANUT = BlockRegistry.register("wild_peanut",
        WildPeanutBlock::new);

    public static final RegistryObject<Item> WILD_PEANUT_ITEM = ItemRegistry.register("wild_peanut",
        () -> new BlockItem(WILD_PEANUT.get(), new Item.Properties()));

    private static final WildCrop WILD_PEANUT_PATCH = WildCrop.plant("wild_peanut", WILD_PEANUT, 12, 8, 32,
        Heightmap.Types.WORLD_SURFACE_WG);

    public static final RegistryObject<Block> PINEAPPLE_FRUIT = BlockRegistry.register("pineapple",
        PineappleBlock::new);

    public static final RegistryObject<Item> PINEAPPLE = ItemRegistry.register("pineapple",
        () -> new BlockItem(PINEAPPLE_FRUIT.get(), new Item.Properties()));

    public static final RegistryObject<CropBlock> PINEAPPLE_CROP_BLOCK = BlockRegistry.register("pineapple_crop",
        PineappleCropBlock::new);

    public static final RegistryObject<Item> PINEAPPLE_CROP_ITEM = ItemRegistry.register("pineapple_crop",
        () -> new BlockItem(PINEAPPLE_CROP_BLOCK.get(), new Item.Properties()));

    public static final Crop PINEAPPLE_CROP = finish(new Crop("pineapple_crop", Shape.GEOMETRY,
        PINEAPPLE_CROP_BLOCK, "pineapple_seedling", PINEAPPLE_CROP_ITEM, PINEAPPLE, "pineapple",
        pineappleStages(), "pineapple_crop_1",
        List.of(), List.of(ItemTagProvider.NORMAL_FARMLAND_SEEDS),
        new Drops(false, 0, 0, 1, 1), compost(PINEAPPLE_CROP_ITEM, 0.3F, PINEAPPLE, 0.65F),
        WildCrop.crop("wild_pineapple", PINEAPPLE_CROP_BLOCK, UniformInt.of(5, 7), WildGround.SAND,
            24, 2, 2, 6, Heightmap.Types.OCEAN_FLOOR_WG)));

    static {
        
        PlantCatalog.markFlatIcon(PINEAPPLE);
    }

    private Crops() {
    }

    public static void bootstrap() {
        
        ALFALFA_BLOCK.fill(ALFALFA_CROP_BLOCK);
        PEANUT_BLOCK.fill(PEANUT_CROP_BLOCK);
        MINT_BLOCK.fill(MINT_CROP_BLOCK);

        PlantCatalog.addCompostable(WILD_PEANUT_ITEM, 0.65F);

    }

    public static List<Crop> entries() {
        return List.copyOf(ENTRIES);
    }

    public static List<WildCrop> wildFeatures() {
        List<WildCrop> features = new ArrayList<>();
        for (Crop crop : ENTRIES) {
            if (crop.wild() != null) {
                features.add(crop.wild());
            }
        }
        features.add(WILD_PEANUT_PATCH);
        features.add(WILD_MINT_PATCH);
        return List.copyOf(features);
    }

    private static RegistryObject<Item> plantingItem(String name, Supplier<Block> block) {
        RegistryObject<Item> item = ItemRegistry.register(name,
            () -> new ItemNameBlockItem(block.get(), new Item.Properties()));
        PlantCatalog.markFlatIcon(item);
        return item;
    }

    private static Crop finish(Crop crop) {
        ENTRIES.add(crop);
        PlantCatalog.add(crop);
        return crop;
    }

    private static List<Stage> numberedStages(String prefix) {
        List<Stage> stages = new ArrayList<>(STAGES);
        for (int stage = 1; stage <= STAGES; stage++) {
            stages.add(new Stage(blockTexture(prefix + "_" + stage), null, null));
        }
        return List.copyOf(stages);
    }

    private static List<Stage> pineappleStages() {
        int[] geometries = {1, 2, 2, 3, 3, 4, 5, 6};
        List<Stage> stages = new ArrayList<>(STAGES);
        for (int geometry : geometries) {
            String name = "pineapple_crop_" + geometry;
            stages.add(new Stage(blockTexture(name), name, geometry == 6 ? "1" : "0"));
        }
        return List.copyOf(stages);
    }

    private static ResourceLocation blockTexture(String name) {
        return new ResourceLocation("plerocottage", "block/" + name);
    }

    private static Map<Supplier<? extends Item>, Float> compost(Supplier<? extends Item> item, float chance) {
        return Map.of(item, chance);
    }

    private static Map<Supplier<? extends Item>, Float> compost(Supplier<? extends Item> item, float chance,
                                                                Supplier<? extends Item> other, float otherChance) {
        Map<Supplier<? extends Item>, Float> compost = new LinkedHashMap<>();
        compost.put(item, chance);
        compost.put(other, otherChance);
        return Map.copyOf(compost);
    }

    public enum Shape {

        CROP,

        CROSS,

        GEOMETRY
    }

    public record Stage(ResourceLocation texture, @Nullable String geometry, @Nullable String geometryKey) {
    }

    public record Drops(boolean seedAtAllAges, int matureSeedMin, int matureSeedMax, int harvestMin,
                        int harvestMax) {
    }

    public record Crop(String name, Shape shape, RegistryObject<? extends CropBlock> block,
                       @Nullable String blockItemIcon, RegistryObject<Item> seed, RegistryObject<Item> harvest,
                       @Nullable String fruitTag, List<Stage> stages, String particleTexture,
                       List<TagKey<Item>> harvestTags, List<TagKey<Item>> seedTags, Drops drops,
                       Map<Supplier<? extends Item>, Float> compost, @Nullable WildCrop wild)
        implements PlantEntry {

        @Override
        public Item fruitItem() {
            return harvest.get();
        }

        @Override
        @Nullable
        public String fruitTagName() {
            return fruitTag;
        }

        @Override
        public Item saplingItem() {
            return null;
        }

        @Override
        public Map<Item, Float> compostables() {
            Map<Item, Float> compostables = new LinkedHashMap<>();
            compost.forEach((item, chance) -> compostables.put(item.get(), chance));
            return compostables;
        }
    }
}

