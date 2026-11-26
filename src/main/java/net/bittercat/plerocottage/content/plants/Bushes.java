package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.blocks.BerryBushBlock;
import net.bittercat.plerocottage.blocks.BlockRef;
import net.bittercat.plerocottage.blocks.TeaTreeBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.bittercat.plerocottage.items.BellPepperSeedItem;
import net.bittercat.plerocottage.items.BlockFoodItem;
import net.bittercat.plerocottage.items.SoilBoundSeedItem;
import net.bittercat.plerocottage.worldgen.WildBush;
import net.minecraft.resources.ResourceLocation;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public final class Bushes {

    private static final float RARE_COMPOST_CHANCE = 1.0F;

    private static final float SEED_COMPOST_CHANCE = 0.3F;

    private static final ResourceLocation FRUITS = new ResourceLocation("forge", "fruits");
    private static final ResourceLocation VEGETABLES = new ResourceLocation("forge", "vegetables");

    private static final List<Entry> ENTRIES = new ArrayList<>();

    private static final Supplier<BlockBehaviour.Properties> BUSH_PROPERTIES = () -> BlockBehaviour.Properties.of()
        .mapColor(MapColor.PLANT)
        .sound(SoundType.SWEET_BERRY_BUSH)
        .instabreak()
        .noCollission()
        .speedFactor(0.3F)
        .noOcclusion()
        .randomTicks()
        .pushReaction(PushReaction.DESTROY)
        .ignitedByLava();

    public static final RegistryObject<Item> BELL_PEPPER_SEED =
        ItemRegistry.register("bell_pepper_seeds", BellPepperSeedItem::new);

    public static final RegistryObject<Item> GOOSEBERRY = food("gooseberries");
    public static final RegistryObject<Item> BLACKBERRY = food("blackberry");
    public static final RegistryObject<Item> PINEBERRY = food("pineberry");
    public static final RegistryObject<Item> STRAWBERRY = food("strawberry");
    public static final RegistryObject<Item> GREEN_BELL_PEPPER = food("green_bell_pepper");
    public static final RegistryObject<Item> PIMENTO = food("pimento");
    public static final RegistryObject<Item> END_PEPPER = food("end_pepper");

    public static final Entry BLUEBERRY_BUSH = register("blueberry_bush",
        bush -> plantingFood("blueberries", bush),
        fruit -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(3, 0, 3, 13, 10, 13),
                Block.box(1, 0, 1, 15, 13, 15),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16)
            },
            fruit, fruit, null, 3, 4, 2, 2, 3, 0.2F),
        0.3F, Model.CROSS, WildBush.soil(18), List.of(FRUITS, fruits("blueberries")),
        cross("blueberry_bush_1"), cross("blueberry_bush_2"), cross("blueberry_bush_3"),
        cross("blueberry_bush_4"), cross("blueberry_bush_5"));

    public static final Entry BLACKCURRANT_BUSH = register("blackcurrant_bush",
        bush -> plantingFood("blackcurrants", bush),
        fruit -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(2, 0, 2, 14, 9, 14),
                Block.box(2, 0, 2, 14, 14, 14),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16)
            },
            fruit, fruit, GOOSEBERRY, 3, 4, 2, 2, 3, 0.2F)
            .onSoil(BerryBushBlock.DIRT_OR_END_STONE_SOIL),
        0.3F, new Model("blackcurrant_crop_rare", null), WildBush.endStone(32),
        List.of(FRUITS, fruits("currant")),
        cross("blackcurrant_crop_1"), cross("blackcurrant_crop_2"), cross("blackcurrant_crop_3"),
        cross("blackcurrant_crop_4"), cross("blackcurrant_crop_5"));

    public static final Entry RASPBERRY_BUSH = register("raspberry_bush",
        bush -> plantingFood("raspberry", bush),
        fruit -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(3, 0, 3, 13, 10, 13),
                Block.box(2, 0, 2, 14, 13, 14),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16)
            },
            fruit, fruit, BLACKBERRY, 3, 4, 2, 2, 3, 0.2F),
        0.65F, new Model("raspberry_crop_rare", null), WildBush.soil(18), List.of(FRUITS, fruits("raspberry")),
        cross("raspberry_crop_1"), cross("raspberry_crop_2"), cross("raspberry_crop_3"),
        cross("raspberry_crop_4"), cross("raspberry_crop_5"));

    public static final Entry BLUE_RASPBERRY_BUSH = register("blue_raspberry_bush",
        bush -> plantingFood("blue_raspberry", bush),
        fruit -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(3, 0, 3, 13, 10, 13),
                Block.box(2, 0, 2, 14, 13, 14),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16)
            },
            fruit, fruit, BLACKBERRY, 3, 4, 2, 2, 3, 0.2F),
        0.65F, new Model("blue_raspberry_crop_rare", null), null,
        List.of(FRUITS),
        cross("blue_raspberry_crop_1"), cross("blue_raspberry_crop_2"), cross("blue_raspberry_crop_3"),
        cross("blue_raspberry_crop_4"), cross("blue_raspberry_crop_5"));

    public static final Entry STRAWBERRY_BUSH = register("strawberry_bush",
        bush -> plantingSeed("strawberry_seeds", bush),
        seed -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(4, 0, 4, 12, 3.5, 12),
                Block.box(3, 0, 3, 13, 4, 13),
                Block.box(2, 0, 2, 14, 4, 14),
                Block.box(0, 0, 0, 16, 5.5, 16),
                Block.box(0, 0, 0, 16, 5.5, 16)
            },
            STRAWBERRY, seed, PINEBERRY, 3, 4, 2, 3, 4, 0.2F).onSoil(BerryBushBlock.FARMLAND_SOIL),
        0.65F, new Model("strawberry_bush_new_7", null), null, List.of(FRUITS, fruits("strawberry")),
        geometry("strawberry_bush_new_1"), geometry("strawberry_bush_new_2"),
        geometry("strawberry_bush_new_3"), geometry("strawberry_bush_new_4"), geometry("strawberry_bush_new_6"));

    public static final Entry CRANBERRY_BUSH = register("cranberry_bush",
        bush -> plantingFood("cranberries", bush),
        fruit -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(2, 0, 2, 14, 8, 14),
                Block.box(1, 0, 1, 15, 13, 15),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16)
            },
            fruit, fruit, null, 3, 4, 2, 2, 3, 0.2F),
        0.3F, Model.CROSS, WildBush.soil(32), List.of(FRUITS, fruits("cranberries")),
        cross("cranberry_bush_1"), cross("cranberry_bush_2"), cross("cranberry_bush_3"),
        cross("cranberry_bush_4"), cross("cranberry_bush_5"));

    public static final Entry CHERRY_TOMATOES_BUSH = register("cherry_tomatoes_bush",
        bush -> plantingFood("cherry_tomatoes", bush),
        fruit -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(3, 0, 3, 13, 10, 13),
                Block.box(1, 0, 1, 15, 15, 15),
                Block.box(1, 0, 1, 15, 16, 15),
                Block.box(1, 0, 1, 15, 16, 15),
                Block.box(1, 0, 1, 15, 16, 15)
            },
            fruit, fruit, null, 3, 4, 2, 2, 3, 0.2F),
        0.65F, Model.CROSS, WildBush.soil(32), List.of(FRUITS, fruits("cherry_tomato")),
        cross("cherry_tomatoes_bush_1"), cross("cherry_tomatoes_bush_2"), cross("cherry_tomatoes_bush_3"),
        cross("cherry_tomatoes_bush_4"), cross("cherry_tomatoes_bush_5"));

    public static final Entry WILD_JALAPENO_BUSH = register("wild_jalapeno_bush",
        bush -> plantingFood("wild_jalapeno", bush),
        fruit -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(1, 0, 1, 15, 11, 15),
                Block.box(1, 0, 1, 15, 14, 15),
                Block.box(1, 0, 1, 15, 15, 15),
                Block.box(1, 0, 1, 15, 16, 15),
                Block.box(1, 0, 1, 15, 15, 15)
            },
            fruit, fruit, null, 3, 4, 2, 2, 3, 0.2F).alwaysDroppingSeed(),
        0.65F, Model.CROSS, WildBush.soil(18), List.of(VEGETABLES, vegetables("pepper")),
        cross("wild_jalapeno_bush_1"), cross("wild_jalapeno_bush_2"), cross("wild_jalapeno_bush_3"),
        cross("wild_jalapeno_bush_4"), cross("wild_jalapeno_bush_5"));

    public static final Entry GREEN_BELL_PEPPER_BUSH = register("green_bell_pepper_bush",
        bush -> BELL_PEPPER_SEED,
        seed -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(2, 0, 2, 14, 8, 14),
                Block.box(1, 0, 1, 15, 15, 15),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16)
            },
            GREEN_BELL_PEPPER, seed, null, 3, 4, 2, 2, 3, 0.2F).alwaysDroppingSeed(),
        0.65F, Model.CROSS, WildBush.soil(32), List.of(VEGETABLES, vegetables("green_bell_pepper")),
        cross("green_bell_pepper_crop_1"), cross("green_bell_pepper_crop_2"),
        cross("green_bell_pepper_crop_3"), cross("green_bell_pepper_crop_4"),
        cross("green_bell_pepper_crop_5"));

    public static final Entry PIMENTO_BUSH = register("pimento_bush",
        bush -> BELL_PEPPER_SEED,
        seed -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(2, 0, 2, 14, 8, 14),
                Block.box(1, 0, 1, 15, 15, 15),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16)
            },
            PIMENTO, seed, null, 3, 4, 2, 2, 3, 0.2F)
            .onSoil(BerryBushBlock.NETHER_SOIL).alwaysDroppingSeed(),
        0.65F, Model.CROSS, null, List.of(VEGETABLES, vegetables("red_bell_pepper")),
        cross("pimento_crop_1"), cross("pimento_crop_2"), cross("pimento_crop_3"), cross("pimento_crop_4"),
        cross("pimento_crop_5"));

    public static final Entry END_PEPPER_BUSH = register("end_pepper_bush",
        bush -> BELL_PEPPER_SEED,
        seed -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(2, 0, 2, 14, 8, 14),
                Block.box(1, 0, 1, 15, 15, 15),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16)
            },
            END_PEPPER, seed, null, 3, 4, 2, 2, 3, 0.2F)
            .onSoil(BerryBushBlock.END_STONE_SOIL).alwaysDroppingSeed(),
        0.65F, Model.CROSS, null, List.of(VEGETABLES, vegetables("yellow_bell_pepper")),
        cross("end_pepper_crop_1"), cross("end_pepper_crop_2"), cross("end_pepper_crop_3"),
        cross("end_pepper_crop_4"), cross("end_pepper_crop_5"));

    public static final Entry BABY_CORN_BUSH = register("baby_corn_bush",
        bush -> plantingFood("baby_corn", bush),
        fruit -> new BerryBushBlock.Settings(
            new VoxelShape[] {
                Block.box(1, 0, 1, 15, 10, 15),
                Block.box(0, 0, 0, 16, 13, 16),
                Block.box(0, 0, 0, 16, 15, 16),
                Block.box(0, 0, 0, 16, 16, 16),
                Block.box(0, 0, 0, 16, 16, 16)
            },
            fruit, fruit, null, 3, 4, 2, 2, 3, 0.2F).alwaysDroppingSeed(),
        0.65F, Model.CROSS, WildBush.soil(24), List.of(VEGETABLES, vegetables("baby_corn")),
        cross("baby_corn_bush_1"), cross("baby_corn_bush_2"), cross("baby_corn_bush_3"),
        cross("baby_corn_bush_4"), cross("baby_corn_bush_5"));

    private static final VoxelShape[] TEA_TREE_SHAPES = {
        Block.box(2, 0, 2, 14, 10, 14),
        Block.box(2, 0, 2, 14, 10, 14),
        Block.box(0, 1, 0, 16, 16, 16),
        Block.box(0, 1, 0, 16, 16, 16),
        Block.box(0, 1, 0, 16, 16, 16)
    };

    public static final Entry TEA_TREE = register("tea_tree",
        bush -> plantingSeed("tea_leaves", bush),
        seed -> new BerryBushBlock.Settings(TEA_TREE_SHAPES, seed, seed, null, 3, 4, 2, 3, 5, 0.2F),
        0.3F, new Model(null, "tea_tree"), null, List.of(new ResourceLocation("forge", "tea_leaves")),
        TeaTreeBlock::new, false,
        cross("tea_tree_sapling"), cross("tea_tree_sapling"), geometry("tea_tree"), geometry("tea_tree"),
        geometry("tea_tree", "tea_tree_grow"));

    private Bushes() {
    }

    public static void bootstrap() {
        
    }

    public static List<Entry> entries() {
        return List.copyOf(ENTRIES);
    }

    public static Block pepperFor(Level level) {
        if (level.dimension() == Level.END) {
            return END_PEPPER_BUSH.block().get();
        }
        if (level.dimension() == Level.NETHER) {
            return PIMENTO_BUSH.block().get();
        }
        return GREEN_BELL_PEPPER_BUSH.block().get();
    }

    private static BerryBushBlock defaultBush(BerryBushBlock.Settings settings) {
        return new BerryBushBlock(settings, BUSH_PROPERTIES.get());
    }

    private static Entry register(String bushName,
                                  Function<Supplier<Block>, RegistryObject<Item>> planting,
                                  Function<RegistryObject<Item>, BerryBushBlock.Settings> settings,
                                  float compostChance, Model model, @Nullable WildBush wild,
                                  List<ResourceLocation> harvestTags, Stage... stages) {
        return register(bushName, planting, settings, compostChance, model, wild, harvestTags,
            Bushes::defaultBush, true, stages);
    }

    private static Entry register(String bushName,
                                  Function<Supplier<Block>, RegistryObject<Item>> planting,
                                  Function<RegistryObject<Item>, BerryBushBlock.Settings> settings,
                                  float compostChance, Model model, @Nullable WildBush wild,
                                  List<ResourceLocation> harvestTags,
                                  Function<BerryBushBlock.Settings, BerryBushBlock> blockFactory,
                                  boolean blockItem, Stage... stages) {

        BlockRef bush = new BlockRef();
        RegistryObject<Item> plantingItem = planting.apply(bush);
        BerryBushBlock.Settings blockSettings = settings.apply(plantingItem);
        RegistryObject<BerryBushBlock> block = BlockRegistry.register(bushName,
            () -> blockFactory.apply(blockSettings));
        bush.fill(block);
        RegistryObject<Item> item = blockItem
            ? ItemRegistry.register(bushName, () -> new BlockItem(block.get(), new Item.Properties()))
            : null;
        Entry entry = new Entry(block, item, plantingItem, blockSettings, compostChance, model, wild,
            List.copyOf(harvestTags), List.of(stages));
        ENTRIES.add(entry);
        PlantCatalog.add(entry);

        if (plantingItem != blockSettings.drop()) {
            
        }
        if (blockSettings.rareDrop() instanceof RegistryObject<Item> rareDrop) {
            
        }
        return entry;
    }

    private static RegistryObject<Item> plantingFood(String name, Supplier<Block> bush) {
        FoodDefinition definition = FoodDefinition.get(name);
        RegistryObject<Item> item = ItemRegistry.register(name, () -> new BlockFoodItem(bush.get(),
            definition.itemProperties(), definition.useDuration(), true, definition.useAnim(), null, false));
        
        PlantCatalog.markFlatIcon(item);
        return item;
    }

    private static RegistryObject<Item> plantingSeed(String name, Supplier<Block> bush) {
        RegistryObject<Item> item = ItemRegistry.register(name,
            () -> new ItemNameBlockItem(bush.get(), new Item.Properties()));
        PlantCatalog.markFlatIcon(item);
        return item;
    }

    private static RegistryObject<Item> plantingSeed(String name, Supplier<Block> bush,
                                                     Predicate<BlockState> soil) {
        RegistryObject<Item> item = ItemRegistry.register(name,
            () -> new SoilBoundSeedItem(bush.get(), new Item.Properties(), soil));
        PlantCatalog.markFlatIcon(item);
        return item;
    }

    private static RegistryObject<Item> food(String name) {
        return ItemRegistry.register(name,
            () -> FoodDefinition.get(name).item());
    }

    private static ResourceLocation fruits(String name) {
        return new ResourceLocation("forge", "fruits/" + name);
    }

    private static ResourceLocation vegetables(String name) {
        return new ResourceLocation("forge", "vegetables/" + name);
    }

    public record Stage(ResourceLocation texture, @Nullable String geometry) {
    }

    private static Stage cross(String texture) {
        return new Stage(modBlockTexture(texture), null);
    }

    private static Stage geometry(String texture) {
        return geometry(texture, texture);
    }

    private static Stage geometry(String texture, String geometry) {
        return new Stage(modBlockTexture(texture), geometry);
    }

    private static ResourceLocation modBlockTexture(String name) {
        return new ResourceLocation("plerocottage", "block/" + name);
    }

    public record Model(@Nullable String rareTexture, @Nullable String particleTexture) {

        public static final Model CROSS = new Model(null, null);
    }

    public record Entry(RegistryObject<BerryBushBlock> block, @Nullable RegistryObject<Item> item,
                        RegistryObject<Item> plantingItem, BerryBushBlock.Settings settings, float compostChance,
                        Model model, @Nullable WildBush wild, List<ResourceLocation> harvestTags,
                        List<Stage> stages)
        implements PlantEntry {

        @Override
        public String name() {
            return block.getId().getPath();
        }

        @Override
        public Item fruitItem() {
            return settings.drop().get();
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
            compostables.put(fruitItem(), compostChance);
            if (settings.rareDrop() != null) {
                compostables.put(settings.rareDrop().get(), RARE_COMPOST_CHANCE);
            }
            if (settings.seed() != settings.drop()) {
                compostables.put(settings.seed().get(), SEED_COMPOST_CHANCE);
            }
            return compostables;
        }
    }
}

