package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.bittercat.plerocottage.blocks.DoubleCropBlock;
import net.bittercat.plerocottage.blocks.BlockRef;
import net.bittercat.plerocottage.blocks.RiceCropBlock;
import net.bittercat.plerocottage.data.ItemTagProvider;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.bittercat.plerocottage.items.BlockFoodItem;
import net.bittercat.plerocottage.items.ConsumableItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public final class DoubleCrops {

    private static final int MAX_AGE = 7;
    private static final int DOUBLE_AGE = 3;

    private static final TagKey<Item> FORGE_SEEDS =
        ItemTags.create(new ResourceLocation("forge", "seeds"));
    private static final TagKey<Item> FORGE_VEGETABLES =
        ItemTags.create(new ResourceLocation("forge", "vegetables"));
    private static final TagKey<Item> FORGE_CROPS_COFFEE =
        ItemTags.create(new ResourceLocation("forge", "crops/coffee"));
    private static final TagKey<Item> FORGE_VEGETABLES_PEPPER =
        ItemTags.create(new ResourceLocation("forge", "vegetables/pepper"));
    private static final TagKey<Item> FORGE_VEGETABLES_TOMATO =
        ItemTags.create(new ResourceLocation("forge", "vegetables/tomato"));

    private static final List<Entry> ENTRIES = new ArrayList<>();

    private static final VoxelShape[] COFFEE_LOWER_SHAPES = {
        Block.box(4, 0, 4, 12, 9, 12),
        Block.box(2, 0, 2, 14, 11, 14),
        Block.box(1, 0, 1, 15, 15, 15),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(1, 0, 1, 15, 16, 15)
    };
    private static final VoxelShape[] COFFEE_UPPER_SHAPES = {
        Block.box(1, 0, 1, 15, 10, 15),
        Block.box(1, 0, 1, 15, 13, 15),
        Block.box(1, 0, 1, 15, 15, 15),
        Block.box(1, 0, 1, 15, 15, 15),
        Block.box(1, 0, 1, 15, 15, 15)
    };

    private static final Drops COFFEE_DROPS = new Drops(3, 4);

    private static final BlockRef COFFEE_BLOCK = new BlockRef();

    public static final RegistryObject<Item> COFFEE_BERRY = ItemRegistry.register("coffee_berries",
        () -> new BlockFoodItem(COFFEE_BLOCK.get(), FoodDefinition.get("coffee_berries").itemProperties(),
            FoodDefinition.get("coffee_berries").useDuration(), true,
            FoodDefinition.get("coffee_berries").useAnim(), null, false));

    public static final RegistryObject<DoubleCropBlock> COFFEE_CROP_BLOCK =
        BlockRegistry.register("coffee_berry_crop",
            () -> new DoubleCropBlock(settings(COFFEE_DROPS, COFFEE_BERRY, COFFEE_BERRY,
                COFFEE_LOWER_SHAPES, COFFEE_UPPER_SHAPES)));

    public static final Entry COFFEE_CROP = finish(new Entry("coffee_berry_crop", COFFEE_CROP_BLOCK,
        "coffee_berries", COFFEE_BERRY, COFFEE_BERRY, lowerStages("coffee_berry_crop"),
        upperStages("coffee_berry_crop"), "coffee_berry_crop_1", List.of(FORGE_CROPS_COFFEE),
        List.of(ItemTagProvider.NORMAL_FARMLAND_SEEDS), COFFEE_DROPS,
        compost(COFFEE_BERRY, 0.65F)));

    private static final VoxelShape[] JALAPENO_LOWER_SHAPES = {
        Block.box(3, 0, 3, 13, 10, 13),
        Block.box(2, 0, 2, 14, 12, 14),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(0, 0, 0, 16, 16, 16)
    };
    private static final VoxelShape[] JALAPENO_UPPER_SHAPES = {
        Block.box(1, 0, 1, 15, 6, 15),
        Block.box(1, 0, 1, 15, 11, 15),
        Block.box(0, 0, 0, 16, 14, 16),
        Block.box(0, 0, 0, 16, 14, 16),
        Block.box(0, 0, 0, 16, 14, 16)
    };

    private static final Drops JALAPENO_DROPS = new Drops(4, 5);

    private static final BlockRef JALAPENO_BLOCK = new BlockRef();

    public static final RegistryObject<Item> JALAPENO_SEED = plantingItem("jalapeno_seeds", JALAPENO_BLOCK);

    public static final RegistryObject<Item> JALAPENO = ItemRegistry.register("jalapeno",
        () -> FoodDefinition.get("jalapeno").consumable());

    public static final RegistryObject<DoubleCropBlock> JALAPENO_CROP_BLOCK =
        BlockRegistry.register("jalapeno_crop",
            () -> new DoubleCropBlock(settings(JALAPENO_DROPS, JALAPENO_SEED, JALAPENO,
                JALAPENO_LOWER_SHAPES, JALAPENO_UPPER_SHAPES)));

    public static final Entry JALAPENO_CROP = finish(new Entry("jalapeno_crop", JALAPENO_CROP_BLOCK,
        "jalapeno", JALAPENO_SEED, JALAPENO, lowerStages("jalapeno_crop"), upperStages("jalapeno_crop"),
        "jalapeno_crop_1", List.of(FORGE_VEGETABLES, FORGE_VEGETABLES_PEPPER),
        List.of(FORGE_SEEDS, ItemTagProvider.NORMAL_FARMLAND_SEEDS), JALAPENO_DROPS,
        compost(JALAPENO, 0.65F, JALAPENO_SEED, 0.3F)));

    private static final VoxelShape[] RICE_LOWER_SHAPES = {
        Block.box(2, 0, 2, 14, 12, 14),
        Block.box(2, 0, 2, 14, 13, 14),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(0, 0, 0, 16, 16, 16)
    };
    private static final VoxelShape[] RICE_UPPER_SHAPES = {
        Block.box(1, 0, 1, 15, 7, 15),
        Block.box(1, 0, 1, 15, 10, 15),
        Block.box(0, 0, 0, 16, 12, 16),
        Block.box(0, 0, 0, 16, 15, 16),
        Block.box(0, 0, 0, 16, 16, 16)
    };

    private static final Drops RICE_DROPS = new Drops(3, 4);

    private static final BlockRef RICE_BLOCK = new BlockRef();

    public static final RegistryObject<Item> RICE_PANICLE = plantingItem("rice_ear", RICE_BLOCK);

    public static final RegistryObject<RiceCropBlock> RICE_CROP_BLOCK =
        BlockRegistry.register("rice_crop",
            () -> new RiceCropBlock(settings(RICE_DROPS, RICE_PANICLE, RICE_PANICLE,
                RICE_LOWER_SHAPES, RICE_UPPER_SHAPES)));

    public static final Entry RICE_CROP = finish(new Entry("rice_crop", RICE_CROP_BLOCK, "rice_ear",
        RICE_PANICLE, RICE_PANICLE, lowerStages("rice_crop"), upperStages("rice_crop"), "rice_crop_1",
        List.of(), List.of(ItemTagProvider.WATER_DIRT_SEEDS), RICE_DROPS,
        compost(RICE_PANICLE, 0.65F)));

    private static final VoxelShape[] TOMATO_LOWER_SHAPES = {
        Block.box(3, 0, 3, 13, 10, 13),
        Block.box(2, 0, 2, 14, 15, 14),
        Block.box(1, 0, 1, 15, 15, 15),
        Block.box(1, 0, 1, 15, 15, 15),
        Block.box(1, 0, 1, 15, 16, 15),
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(0, 0, 0, 16, 16, 16)
    };
    private static final VoxelShape[] TOMATO_UPPER_SHAPES = {
        Block.box(1, 0, 1, 15, 15, 15),
        Block.box(1, 0, 1, 15, 15, 15),
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(0, 0, 0, 16, 16, 16),
        Block.box(0, 0, 0, 16, 16, 16)
    };

    private static final Drops TOMATO_DROPS = new Drops(4, 5);

    private static final BlockRef TOMATO_BLOCK = new BlockRef();

    public static final RegistryObject<Item> TOMATO_SEED = plantingItem("tomato_seeds", TOMATO_BLOCK);

    public static final RegistryObject<Item> TOMATO = ItemRegistry.register("tomato",
        () -> FoodDefinition.get("tomato").consumable());

    public static final RegistryObject<DoubleCropBlock> TOMATO_CROP_BLOCK =
        BlockRegistry.register("tomato_crop",
            () -> new DoubleCropBlock(settings(TOMATO_DROPS, TOMATO_SEED, TOMATO,
                TOMATO_LOWER_SHAPES, TOMATO_UPPER_SHAPES)));

    public static final Entry TOMATO_CROP = finish(new Entry("tomato_crop", TOMATO_CROP_BLOCK, "tomato",
        TOMATO_SEED, TOMATO, lowerStages("tomato_crop"), upperStages("tomato_crop"), "tomato_crop_1",
        List.of(FORGE_VEGETABLES, FORGE_VEGETABLES_TOMATO),
        List.of(FORGE_SEEDS, ItemTagProvider.NORMAL_FARMLAND_SEEDS), TOMATO_DROPS,
        compost(TOMATO, 0.65F, TOMATO_SEED, 0.3F)));

    public static final RegistryObject<Item> COFFEE_CROP_ITEM = blockItem("coffee_berry_crop", COFFEE_CROP_BLOCK);
    public static final RegistryObject<Item> JALAPENO_CROP_ITEM = blockItem("jalapeno_crop", JALAPENO_CROP_BLOCK);
    public static final RegistryObject<Item> RICE_CROP_ITEM = blockItem("rice_crop", RICE_CROP_BLOCK);
    public static final RegistryObject<Item> TOMATO_CROP_ITEM = blockItem("tomato_crop", TOMATO_CROP_BLOCK);

    static {
        
        PlantCatalog.markFlatIcon(COFFEE_BERRY);
    }

    private DoubleCrops() {
    }

    public static void bootstrap() {
        
        COFFEE_BLOCK.fill(COFFEE_CROP_BLOCK);
        JALAPENO_BLOCK.fill(JALAPENO_CROP_BLOCK);
        RICE_BLOCK.fill(RICE_CROP_BLOCK);
        TOMATO_BLOCK.fill(TOMATO_CROP_BLOCK);

    }

    public static List<Entry> entries() {
        return List.copyOf(ENTRIES);
    }

    private static RegistryObject<Item> plantingItem(String name, Supplier<Block> block) {
        RegistryObject<Item> item = ItemRegistry.register(name,
            () -> new ItemNameBlockItem(block.get(), new Item.Properties()));
        PlantCatalog.markFlatIcon(item);
        return item;
    }

    private static RegistryObject<Item> blockItem(String name, Supplier<? extends Block> block) {
        return ItemRegistry.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static DoubleCropBlock.Settings settings(Drops drops, Supplier<Item> seed, Supplier<Item> harvest,
                                                     VoxelShape[] lower, VoxelShape[] upper) {
        return new DoubleCropBlock.Settings(lower, upper, seed, harvest,
            drops.harvestMin(), drops.harvestMax(), MAX_AGE, DOUBLE_AGE);
    }

    private static Entry finish(Entry crop) {
        ENTRIES.add(crop);
        PlantCatalog.add(crop);
        return crop;
    }

    private static List<Crops.Stage> lowerStages(String prefix) {
        List<Crops.Stage> stages = new ArrayList<>(MAX_AGE + 1);
        for (int stage = 1; stage <= 3; stage++) {
            stages.add(stage(prefix + "_" + stage));
        }
        for (int stage = 4; stage <= 8; stage++) {
            stages.add(stage(prefix + "_" + stage + "_bottom"));
        }
        return List.copyOf(stages);
    }

    private static List<Crops.Stage> upperStages(String prefix) {
        List<Crops.Stage> stages = new ArrayList<>(MAX_AGE - DOUBLE_AGE + 1);
        for (int stage = 4; stage <= 8; stage++) {
            stages.add(stage(prefix + "_" + stage + "_top"));
        }
        return List.copyOf(stages);
    }

    private static Crops.Stage stage(String texture) {
        return new Crops.Stage(new ResourceLocation("plerocottage", "block/" + texture), null, null);
    }

    private static Map<Supplier<? extends Item>, Float> compost(Supplier<? extends Item> item, float chance) {
        return Map.of(item, chance);
    }

    private static Map<Supplier<? extends Item>, Float> compost(Supplier<? extends Item> item, float chance,
                                                                Supplier<? extends Item> other,
                                                                float otherChance) {
        Map<Supplier<? extends Item>, Float> compost = new LinkedHashMap<>();
        compost.put(item, chance);
        compost.put(other, otherChance);
        return Map.copyOf(compost);
    }

    public record Drops(int harvestMin, int harvestMax) {
    }

    public record Entry(String name, RegistryObject<? extends DoubleCropBlock> block, String blockItemIcon,
                        RegistryObject<Item> seed, RegistryObject<Item> harvest, List<Crops.Stage> lowerStages,
                        List<Crops.Stage> upperStages, String particleTexture, List<TagKey<Item>> harvestTags,
                        List<TagKey<Item>> seedTags, Drops drops,
                        Map<Supplier<? extends Item>, Float> compost) implements PlantEntry {

        public int doubleAge() {
            return DOUBLE_AGE;
        }

        public int maxAge() {
            return MAX_AGE;
        }

        @Override
        public Item fruitItem() {
            return harvest.get();
        }

        @Override
        public Item saplingItem() {
            return null;
        }

        @Override
        public String fruitTagName() {
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

