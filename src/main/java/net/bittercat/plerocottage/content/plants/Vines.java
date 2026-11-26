package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.BUILDING;
import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.blocks.VineArborBlock;
import net.bittercat.plerocottage.blocks.VineCropBlock;
import net.bittercat.plerocottage.blocks.VineSpecies;
import net.bittercat.plerocottage.blocks.VineStakeBlock;
import net.bittercat.plerocottage.blocks.WildVineBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.bittercat.plerocottage.items.ConsumableItem;
import net.bittercat.plerocottage.items.VinePlantingItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public final class Vines {

    private static final int EAT_TICKS = 32;

    public static final String STAKE_PARTICLE = "vine_stake_none";
    public static final String ARBOR_PARTICLE = "vine_arbor";

    public static final int WILD_HARVEST_MIN = 1;
    public static final int WILD_HARVEST_MAX = 2;

    private static final TagKey<Item> FORGE_SEEDS = ItemTags.create(new ResourceLocation("forge", "seeds"));
    private static final ResourceLocation FRUITS = new ResourceLocation("forge", "fruits");
    private static final ResourceLocation VEGETABLES = new ResourceLocation("forge", "vegetables");

    private static final VoxelShape[] CUCUMBER_SHAPES = {
        Block.box(2.5, 11, 2.5, 13.5, 16, 13.5),
        Block.box(5, 8, 5, 11, 14.5, 11),
        Block.box(4.5, 5, 4.5, 11.5, 14, 11.5)
    };
    private static final VoxelShape[] GRAPE_SHAPES = {
        Block.box(4, 5, 4, 12, 16, 12),
        Block.box(4, 2, 4, 12, 16, 12),
        Block.box(2, 0, 2, 14, 16, 14)
    };
    private static final VoxelShape[] PASSION_FRUIT_SHAPES = {
        Block.box(2.5, 11, 2.5, 13.5, 16, 13.5),
        Block.box(4.5, 10, 4.5, 11.5, 14.5, 11.5),
        Block.box(4, 9, 4, 12, 14, 12)
    };

    private static final List<Entry> ENTRIES = new ArrayList<>();

    public static final RegistryObject<Item> CUCUMBER = food("cucumber");
    public static final RegistryObject<Item> GRAPE = food("grapes");
    public static final RegistryObject<Item> PASSION_FRUIT = food("passion_fruit");

    public static final RegistryObject<Item> MUSCAT_GRAPE = ItemRegistry.register("muscat_grapes",
        () -> FoodDefinition.get("muscat_grapes").consumable());

    public static final RegistryObject<Item> CUCUMBER_SEED =
        plantingItem("cucumber_seeds", VineSpecies.CUCUMBER, new Item.Properties(), EAT_TICKS);
    public static final RegistryObject<Item> GRAPE_SEED =
        plantingItem("grape_seeds", VineSpecies.GRAPE, new Item.Properties(), EAT_TICKS);

    public static final RegistryObject<Item> PASSION_FRUIT_SLICE = plantingItem("half_of_passion_fruit",
        VineSpecies.PASSION_FRUIT, FoodDefinition.get("half_of_passion_fruit").itemProperties(),
        FoodDefinition.get("half_of_passion_fruit").useDuration());

    public static final RegistryObject<VineStakeBlock> VINE_STAKE = stake();
    public static final RegistryObject<VineArborBlock> VINE_ARBOR = arbor();

    public static final RegistryObject<Item> VINE_STAKE_ITEM = blockItem("vine_stake", VINE_STAKE);
    public static final RegistryObject<Item> VINE_ARBOR_ITEM = blockItem("vine_arbor", VINE_ARBOR);

    public static final RegistryObject<VineCropBlock> CUCUMBER_CROP = crop("cucumber_crop",
        new VineCropBlock.Settings(VineSpecies.CUCUMBER, CUCUMBER, null, 3, CUCUMBER_SHAPES));

    public static final RegistryObject<VineCropBlock> GRAPE_CROP = crop("grape_crop",
        new VineCropBlock.Settings(VineSpecies.GRAPE, GRAPE, MUSCAT_GRAPE, 1, GRAPE_SHAPES));

    public static final RegistryObject<VineCropBlock> PASSION_FRUIT_CROP = crop("passion_fruit_crop",
        new VineCropBlock.Settings(VineSpecies.PASSION_FRUIT, PASSION_FRUIT, null, 3, PASSION_FRUIT_SHAPES));

    public static final RegistryObject<Item> CUCUMBER_CROP_ITEM = blockItem("cucumber_crop", CUCUMBER_CROP);
    public static final RegistryObject<Item> GRAPE_CROP_ITEM = blockItem("grape_crop", GRAPE_CROP);
    public static final RegistryObject<Item> PASSION_FRUIT_CROP_ITEM =
        blockItem("passion_fruit_crop", PASSION_FRUIT_CROP);

    public static final WildVine GRAPE_WILD = wildVine("wild_grape_vine", "grape_seeds", GRAPE_SEED);
    public static final WildVine PASSION_FRUIT_WILD =
        wildVine("wild_passion_fruit_vine", "half_of_passion_fruit", PASSION_FRUIT_SLICE);
    public static final WildVine CUCUMBER_WILD = wildVine("wild_cucumber_vine", "cucumber", CUCUMBER_SEED);

    public static final Entry GRAPE_VINE = finish(new Entry(VineSpecies.GRAPE, GRAPE_CROP, "grapes",
        stages("grape_crop", "3"), geometry("grape_crop_4", "grape_crop_spe", "3"),
        GRAPE_SEED, GRAPE, MUSCAT_GRAPE, 1,
        List.of(FRUITS, fruits("grapes")), List.of(FRUITS, fruits("green_grape")),
        List.of(FORGE_SEEDS), compost(GRAPE, 0.65F, MUSCAT_GRAPE, 1.0F, GRAPE_SEED, 0.3F), GRAPE_WILD));

    public static final Entry PASSION_FRUIT_VINE = finish(new Entry(VineSpecies.PASSION_FRUIT, PASSION_FRUIT_CROP,
        "passion_fruit", stages("passion_fruit_crop", "1"), null,
        PASSION_FRUIT_SLICE, PASSION_FRUIT, null, 3,
        List.of(FRUITS, fruits("passion_fruit")), List.of(), List.of(),
        compost(PASSION_FRUIT, 0.65F), PASSION_FRUIT_WILD));

    public static final Entry CUCUMBER_VINE = finish(new Entry(VineSpecies.CUCUMBER, CUCUMBER_CROP, "cucumber",
        stages("cucumber_crop", "1"), null,
        CUCUMBER_SEED, CUCUMBER, null, 3,
        List.of(VEGETABLES, vegetables("cucumber")), List.of(), List.of(FORGE_SEEDS),
        compost(CUCUMBER, 0.65F, CUCUMBER_SEED, 0.3F), CUCUMBER_WILD));

    private Vines() {
    }

    public static void bootstrap() {

    }

    public static List<Entry> entries() {
        return List.copyOf(ENTRIES);
    }

    public static Crops.Stage stakeStage(VineSpecies species, boolean climbed) {
        if (species == VineSpecies.EMPTY) {
            return geometry("vine_stake", "vine_stake_none", "0");
        }
        if (!climbed) {
            return geometry("vine_stake_seed", "vine_stake_seed", "0");
        }
        return switch (species) {
            case GRAPE -> geometry("vine_stake_leaves", "vine_stake_leaves", "0");
            case PASSION_FRUIT ->
                geometry("vine_stake_passion_fruit_leaves", "vine_stake_passion_fruit_leaves", "0");
            case CUCUMBER -> geometry("vine_stake_cucumber_leaves", "vine_stake_cucumber_leaves", "2");
            case EMPTY -> throw new IllegalStateException("空杆没有爬上去了的形态");
        };
    }

    public static Crops.Stage arborStage(VineSpecies species) {
        return switch (species) {
            case EMPTY -> geometry("vine_arbor", "vine_arbor", "0");
            case GRAPE -> geometry("vine_arbor_leaves", "vine_arbor_leaves", "1");
            case PASSION_FRUIT ->
                geometry("vine_arbor_passion_fruit_leaves", "vine_arbor_passion_fruit_leaves", "1");
            case CUCUMBER -> geometry("vine_arbor_cucumber_leaves", "vine_arbor_cucumber_leaves", "2");
        };
    }

    public static String wildVineTexture(String name, boolean tip, boolean fruit) {
        return name + (fruit ? "_fruit" : "") + (tip ? "_bottom" : "");
    }

    private static RegistryObject<VineStakeBlock> stake() {
        return BlockRegistry.register("vine_stake", () -> new VineStakeBlock(VINE_ARBOR));
    }

    private static RegistryObject<VineArborBlock> arbor() {
        
        return BlockRegistry.register("vine_arbor", () -> {
            Map<VineSpecies, Supplier<? extends Block>> crops = new LinkedHashMap<>();
            crops.put(VineSpecies.GRAPE, GRAPE_CROP);
            crops.put(VineSpecies.PASSION_FRUIT, PASSION_FRUIT_CROP);
            crops.put(VineSpecies.CUCUMBER, CUCUMBER_CROP);
            return new VineArborBlock(VINE_STAKE, crops);
        });
    }

    private static RegistryObject<VineCropBlock> crop(String name, VineCropBlock.Settings settings) {
        return BlockRegistry.register(name, () -> new VineCropBlock(settings));
    }

    private static RegistryObject<Item> blockItem(String name, Supplier<? extends Block> block) {
        return ItemRegistry.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static WildVine wildVine(String name, String icon, Supplier<Item> cloneItem) {
        RegistryObject<WildVineBlock> block =
            BlockRegistry.register(name, () -> new WildVineBlock(cloneItem));
        RegistryObject<Item> item =
            ItemRegistry.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return new WildVine(block, item, icon, cloneItem);
    }

    private static Supplier<? extends Block> wildVineOf(VineSpecies species) {
        return switch (species) {
            case GRAPE -> GRAPE_WILD.block();
            case PASSION_FRUIT -> PASSION_FRUIT_WILD.block();
            case CUCUMBER -> CUCUMBER_WILD.block();
            case EMPTY -> throw new IllegalArgumentException("空种类没有野生藤蔓");
        };
    }

    private static RegistryObject<Item> food(String name) {
        RegistryObject<Item> item = ItemRegistry.register(name,
            () -> FoodDefinition.get(name).consumable());
        PlantCatalog.markFlatIcon(item);
        return item;
    }

    private static RegistryObject<Item> plantingItem(String name, VineSpecies species, Item.Properties properties,
                                                     int useDuration) {
        RegistryObject<Item> item = ItemRegistry.register(name,
            () -> new VinePlantingItem(species, VINE_STAKE, wildVineOf(species), properties, useDuration));
        PlantCatalog.markFlatIcon(item);
        return item;
    }

    private static Entry finish(Entry entry) {
        ENTRIES.add(entry);
        PlantCatalog.add(entry);
        return entry;
    }

    private static ResourceLocation fruits(String name) {
        return new ResourceLocation("forge", "fruits/" + name);
    }

    private static ResourceLocation vegetables(String name) {
        return new ResourceLocation("forge", "vegetables/" + name);
    }

    private static Crops.Stage geometry(String geometry, String texture, String textureKey) {
        return new Crops.Stage(new ResourceLocation("plerocottage", "block/" + texture), geometry, textureKey);
    }

    private static List<Crops.Stage> stages(String prefix, String textureKey) {
        List<Crops.Stage> stages = new ArrayList<>(VineCropBlock.MAX_AGE + 1);
        for (int stage = 1; stage <= VineCropBlock.MAX_AGE + 1; stage++) {
            stages.add(geometry(prefix + "_" + stage, prefix + "_" + stage, textureKey));
        }
        return List.copyOf(stages);
    }

    private static Map<Supplier<? extends Item>, Float> compost(Supplier<? extends Item> item, float chance) {
        return Map.of(item, chance);
    }

    private static Map<Supplier<? extends Item>, Float> compost(Supplier<? extends Item> item, float chance,
                                                                Supplier<? extends Item> other,
                                                                float otherChance) {
        return Map.of(item, chance, other, otherChance);
    }

    private static Map<Supplier<? extends Item>, Float> compost(Supplier<? extends Item> item, float chance,
                                                                Supplier<? extends Item> other, float otherChance,
                                                                Supplier<? extends Item> third, float thirdChance) {
        return Map.of(item, chance, other, otherChance, third, thirdChance);
    }

    public record Entry(VineSpecies species, RegistryObject<VineCropBlock> cropBlock, String cropIcon,
                        List<Crops.Stage> stages, @Nullable Crops.Stage rareStage, RegistryObject<Item> seed,
                        RegistryObject<Item> harvest, @Nullable RegistryObject<Item> rareHarvest, int harvestCount,
                        List<ResourceLocation> harvestTags, List<ResourceLocation> rareHarvestTags,
                        List<TagKey<Item>> seedTags,
                        Map<Supplier<? extends Item>, Float> compost, WildVine wild) implements PlantEntry {

        @Override
        public String name() {
            return cropBlock.getId().getPath();
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

    public record WildVine(RegistryObject<WildVineBlock> block, RegistryObject<Item> item, String icon,
                           Supplier<Item> pickItem) {
    }
}

