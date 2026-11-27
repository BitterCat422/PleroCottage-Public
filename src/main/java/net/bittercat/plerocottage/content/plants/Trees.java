package net.bittercat.plerocottage.content.plants;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.BUILDING;
import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blocks.AbstractFruitLeavesBlock;
import net.bittercat.plerocottage.blocks.ElderberryFlowerBlock;
import net.bittercat.plerocottage.blocks.ElderberryLeavesBlock;
import net.bittercat.plerocottage.blocks.ElderberrySaplingBlock;
import net.bittercat.plerocottage.blocks.FallenFruitBlock;
import net.bittercat.plerocottage.blocks.FruitLeavesBlock;
import net.bittercat.plerocottage.blocks.HangingFruitBlock;
import net.bittercat.plerocottage.blocks.HangingFruitLeavesBlock;
import net.bittercat.plerocottage.blocks.NetherOrDirtSaplingBlock;
import net.bittercat.plerocottage.blocks.PlainLeavesBlock;
import net.bittercat.plerocottage.blocks.SandSaplingBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.content.particles.ParticleContent;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
import net.bittercat.plerocottage.items.ConsumableItem;
import net.bittercat.plerocottage.items.BlockFoodItem;
import net.bittercat.plerocottage.worldgen.ModTreeGrower;
import net.bittercat.plerocottage.worldgen.TreeShape;
import net.bittercat.plerocottage.worldgen.TreeSpecies;
import net.bittercat.plerocottage.worldgen.WildSpawn;
import net.bittercat.plerocottage.worldgen.WorldGenRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public final class Trees {

    private static final List<Entry> ENTRIES = new ArrayList<>();

    private static final List<PlainEntry> PLAIN_ENTRIES = new ArrayList<>();

    private static RegistryObject<ElderberryFlowerBlock> elderberryFlower;

    private static RegistryObject<Item> elderberryFlowerItem;

    public static final RegistryObject<Item> APPLE_SLICE = food("apple_slice");
    public static final RegistryObject<Item> GREEN_APPLE = food("green_apple");
    public static final RegistryObject<Item> GREEN_APPLE_SLICE = food("green_apple_slice");

    private static final RegistryObject<Item> APRICOT = food("apricots");
    private static final RegistryObject<Item> UME = food("ume");
    private static final RegistryObject<Item> AVOCADO = ingredient("avocado");
    private static final RegistryObject<Item> AVOCADO_SLICE = food("avocado_slice");
    private static final RegistryObject<Item> CHERRIES = food("cherries");
    private static final RegistryObject<Item> FIG = food("fig");
    private static final RegistryObject<Item> FIG_SLICE = food("fig_slice");
    private static final RegistryObject<Item> HAWTHORN = food("hawthorns");
    private static final RegistryObject<Item> KIWIFRUIT = ingredient("kiwifruit");
    private static final RegistryObject<Item> KIWIFRUIT_SLICE = food("kiwifruit_slice");
    private static final RegistryObject<Item> GOLD_KIWIFRUIT = ingredient("gold_kiwifruit", Rarity.EPIC);
    private static final RegistryObject<Item> GOLD_KIWIFRUIT_SLICE = food("gold_kiwifruit_slice");
    private static final RegistryObject<Item> LEMON = food("lemon");
    private static final RegistryObject<Item> LEMON_SLICE = food("lemon_slice");
    private static final RegistryObject<Item> LIME = food("lime");
    private static final RegistryObject<Item> LIME_SLICE = food("lime_slice");
    private static final RegistryObject<Item> MANGO = food("mango");
    private static final RegistryObject<Item> MANGO_SLICE = food("mango_slice");
    private static final RegistryObject<Item> OLIVE_FRUIT = food("olive_fruits");
    private static final RegistryObject<Item> BLACK_OLIVE_FRUIT = food("black_olive_fruits");
    private static final RegistryObject<Item> ORANGE = food("orange");
    private static final RegistryObject<Item> ORANGE_SEGMENT = food("orange_segment");
    private static final RegistryObject<Item> GRAPEFRUIT = food("grapefruit");
    private static final RegistryObject<Item> GRAPEFRUIT_SLICE = food("grapefruit_slice");
    private static final RegistryObject<Item> PEACH = food("peach");
    private static final RegistryObject<Item> PEACH_SLICE = food("peach_slice");
    private static final RegistryObject<Item> PLUM = food("plum");
    private static final RegistryObject<Item> PLUM_SLICE = food("plum_slice");
    private static final RegistryObject<Item> PEAR = food("pear");
    private static final RegistryObject<Item> PEAR_SLICE = food("pear_slice");
    private static final RegistryObject<Item> POMEGRANATE = ingredient("pomegranate");
    private static final RegistryObject<Item> POMEGRANATE_WEDGE = food("pomegranate_wedges");
    private static final RegistryObject<Item> STARFRUIT = food("starfruit");
    private static final RegistryObject<Item> STARFRUIT_SLICE = food("starfruit_slice");
    private static final RegistryObject<Item> YUZUMELO_WEDGE = food("pomelo_wedge");

    public static final RegistryObject<Item> HALF_OF_DURIAN =
        food("half_of_durian");

    private static final RegistryObject<Block> DURIAN = harvestBlock("durian",
        MapColor.TERRACOTTA_LIGHT_GREEN,
        Shapes.or(Block.box(1, 0, 1, 15, 15, 15), Block.box(7, 14, 7, 9, 16, 9)));
    private static final RegistryObject<Block> YUZUMELO = harvestBlock("pomelo", MapColor.STONE,
        Shapes.or(Block.box(3, 0, 3, 13, 12, 13), Block.box(5, 12, 5, 11, 14, 11)));

    public static final Entry APPLE_TREE = register("apple", "apple_leaves",
        rosaceae(), MapColor.WARPED_WART_BLOCK, TreeShape.ORCHARD, WildSpawn.soil(6),
        new Harvest(() -> Items.APPLE, GREEN_APPLE, 1, 2, 2, 3),
        stages("apple_leaves", "apple_leaves_a", "apple_leaves_b", "apple_leaves_rare"),
        "apple_seeds", "apple_seeds", "apple_tree_sapling", "apple_seeds");

    public static final Entry APRICOT_TREE = register("apricots", "apricot_leaves",
        rosaceae(), MapColor.QUARTZ, TreeShape.ORCHARD, WildSpawn.soil(4), new Harvest(APRICOT, UME, 1, 2, 2, 3),
        stages("apricot_leaves", "apricot_leaves_a", "apricot_leaves_b", "apricot_leaves_rare"),
        "apricot_seed", "apricot_seed", "apricot_tree_sapling", "apricot_seed");

    public static final Entry AVOCADO_TREE = register("avocado", "avocado_tree_leaves",
        trunk("avocado_tree"), MapColor.TERRACOTTA_GREEN, TreeShape.FANCY_FRUIT, WildSpawn.soil(8),
        new Harvest(AVOCADO, null, 1, 1, 1, 2),
        stages("avocado_tree_leaves", "avocado_tree_leaves_a", "avocado_tree_leaves_b", null),
        "avocado_seed", "avocado_seed", "avocado_tree_sapling", "avocado_seed");

    public static final Entry CHERRIES_TREE = register("cherries", "cherries_tree_leaves",
        trunk("cherries_tree"), MapColor.WARPED_WART_BLOCK, TreeShape.CHERRY_LARGE, WildSpawn.soil(6),
        new Harvest(CHERRIES, null, 1, 2, 2, 3),
        stages("cherries_tree_leaves", "cherries_tree_leaves_a", "cherries_tree_leaves_b", null),
        "cherries_seeds", "cherries_seeds", "cherries_tree_sapling", "cherries_seeds");

    public static final Entry FIG_TREE = register("fig", "fig_tree_leaves",
        trunk("fig_tree"), MapColor.PLANT, TreeShape.BUSH_FRUIT, WildSpawn.soil(6), new Harvest(FIG, null, 1, 1, 1, 2),
        stages("fig_tree_leaves", "fig_tree_leaves_a", "fig_tree_leaves_b", null),
        "fig_seeds", "fig_seeds", "fig_tree_sapling", "fig_seeds");

    public static final Entry HAWTHORN_TREE = register("hawthorns", "hawthorn_tree_leaves",
        trunk("hawthorn_tree"), MapColor.PLANT, TreeShape.CHERRY_BROAD, WildSpawn.nether(12),
        new Harvest(HAWTHORN, null, 1, 1, 2, 3),
        stages("hawthorn_tree_leaves", "hawthorn_tree_leaves_a", "hawthorn_tree_leaves_b", null),
        "hawthorn_seeds", "hawthorn_seeds", "hawthorn_sapling", "hawthorn_seeds");

    public static final Entry KIWIFRUIT_TREE = register("kiwifruit", "kiwifruit_tree_leaves",
        trunk("kiwifruit_tree"), MapColor.EMERALD, TreeShape.FORKING, WildSpawn.soil(8),
        new Harvest(KIWIFRUIT, GOLD_KIWIFRUIT, 1, 2, 2, 3),
        stages("kiwifruit_tree_leaves", "kiwifruit_tree_leaves_a", "kiwifruit_tree_leaves_b",
            "kiwifruit_tree_leaves_rare"),
        "kiwifruit_seeds", "kiwifruit_seeds", "kiwifruit_tree_sapling", "kiwifruit_seeds");

    public static final Entry LEMON_TREE = register("lemon", "lemon_leaves",
        rutaceae(), MapColor.COLOR_GREEN, TreeShape.ORCHARD, null, new Harvest(LEMON, LIME, 1, 2, 2, 3),
        stages("lemon_leaves", "lemon_leaves_a", "lemon_leaves_b", "lemon_tree_leaves_rare"),
        "lemon_seeds", "lemon_seeds", "lemon_tree_sapling", "lemon_seeds");

    public static final Entry MANGO_TREE = register("mango", "mango_tree_leaves",
        trunk("mango_tree"), MapColor.COLOR_LIGHT_GREEN, TreeShape.FANCY_FRUIT, WildSpawn.soil(6),
        new Harvest(MANGO, null, 1, 2, 2, 3),
        stages("mango_tree_leaves", "mango_tree_leaves_a", "mango_tree_leaves_b", null),
        "mango_seed", "mango_seed", "mango_tree_sapling", "mango_seed");

    public static final Entry OLIVE_TREE = register("olive", "olive_tree_leaves",
        trunk("olive_tree"), MapColor.GRASS, TreeShape.CHERRY_ROUND, WildSpawn.soil(14),
        new Harvest(OLIVE_FRUIT, BLACK_OLIVE_FRUIT, 2, 3, 3, 5),
        stages("olive_tree_leaves", "olive_tree_leaves_a", "olive_tree_leaves_b",
            "olive_tree_leaves_rare"),
        "olive_fruit_seed", "olive_fruit_seed", "olive_tree_sapling", "olive_fruit_seed");

    public static final Entry ORANGE_TREE = register("orange", "orange_leaves",
        rutaceae(), MapColor.COLOR_GREEN, TreeShape.ORCHARD, WildSpawn.soil(6),
        new Harvest(ORANGE, GRAPEFRUIT, 1, 2, 2, 3),
        stages("orange_leaves", "orange_leaves_a", "orange_leaves_b", "orange_leaves_rare"),
        "orange_seeds", "orange_seeds", "orange_tree_sapling", "orange_seeds");

    public static final Entry PEACH_TREE = register("peach", "peach_leaves",
        rosaceae(), MapColor.COLOR_PINK, TreeShape.CHERRY_PEACH, WildSpawn.soil(12),
        new Harvest(PEACH, PLUM, 1, 2, 2, 3),
        stages("peach_leaves", "peach_leaves_a", "peach_leaves_b", "peach_leaves_rare"),
        "peach_seed", "peach_seed", "peach_tree_sapling", "peach_seed");

    public static final Entry PEAR_TREE = register("pear", "pear_leaves",
        rosaceae(), MapColor.QUARTZ, TreeShape.ORCHARD_SMALL, WildSpawn.soil(6), new Harvest(PEAR, null, 1, 2, 2, 3),
        stages("pear_leaves", "pear_leaves_a", "pear_leaves_b", null),
        "pear_seeds", "pear_seeds", "pear_tree_sapling", "pear_seeds");

    public static final Entry POMEGRANATE_TREE = register("pomegranate", "pomegranate_tree_leaves",
        trunk("pomegranate_tree"), MapColor.PLANT, TreeShape.ORCHARD_TALL, WildSpawn.nether(16),
        new Harvest(POMEGRANATE, null, 1, 1, 1, 2),
        stages("pomegranate_tree_leaves", "pomegranate_tree_leaves_a",
            "pomegranate_tree_leaves_b", null),
        "pomegranate_tree_sapling", "pomegranate_seeds", "pomegranate_tree_sapling", "pomegranate_seeds");

    public static final Entry STARFRUIT_TREE = register("starfruit", "starfruit_tree_leaves",
        trunk("starfruit_tree"), MapColor.TERRACOTTA_LIGHT_GREEN, null, WildSpawn.soil(6),
        new Harvest(STARFRUIT, null, 1, 1, 1, 2),
        stages("starfruit_tree_leaves", "starfruit_tree_leaves_a", "starfruit_tree_leaves_b", null),
        "starfruit_seeds", "starfruit_seeds", "starfruit_tree_sapling", "starfruit_seeds");

    public static final Entry DURIAN_TREE = registerHanging("durian", "durian_tree_leaves",
        trunk("durian_tree"), MapColor.TERRACOTTA_LIGHT_GREEN, null, WildSpawn.soil(8),
        new Harvest(() -> DURIAN.get().asItem(), null, 1, 1, 1, 1),
        hangingStages("durian_leaves", "durian_leaves_ab", "durian_leaves_cde"),
        "durian_fruit",
        new HangingFruitBlock.Settings(MapColor.TERRACOTTA_LIGHT_GREEN, SoundType.BAMBOO, 0.6F, 2.0F, 40,
            new VoxelShape[] {Block.box(5, 7, 5, 11, 14, 11), Block.box(3.5, 4, 3.5, 12.5, 14, 12.5),
                Shapes.or(Block.box(2, 1, 2, 14, 14, 14), Block.box(7, 14, 7, 9, 16, 9))}),
        "durian_seed", "durian_seed", "durian_tree_sapling", "durian_seed");

    public static final Entry YUZUMELO_TREE = registerHanging("pomelo", "pomelo_leaves",
        rutaceae(), MapColor.COLOR_GREEN, TreeShape.DARK_OAK, WildSpawn.soil(8),
        new Harvest(() -> YUZUMELO.get().asItem(), null, 1, 1, 1, 1),
        hangingStages("pomelo_leaves", "pomelo_leaves_b", "pomelo_leaves_a"),
        "pomelo_fruit",
        new HangingFruitBlock.Settings(MapColor.PLANT, SoundType.BAMBOO, 0.6F, 1.0F, 20,
            new VoxelShape[] {
                Shapes.or(Block.box(5, 6, 5, 11, 13, 11), Block.box(6, 13, 6, 10, 14, 10)),
                Shapes.or(Block.box(4, 2, 4, 12, 12, 12), Block.box(5.5, 12, 5.5, 10.5, 14, 10.5)),
                Shapes.or(Block.box(3, 0, 3, 13, 12, 13), Block.box(5, 12, 5, 11, 14, 11))}),
        "pomelo_seeds", "pomelo_seeds", "pomelo_tree_sapling", "pomelo_seeds");

    public static final Entry ELDERBERRY_TREE = registerElderberry("elderberries", "elderberry_tree_leaves",
        trunk("elderberry_tree"), WildSpawn.endAir(32),
        Map.of(
            ElderberryLeavesBlock.Stage.UNROLLED, "elderberry_tree_leaves",
            ElderberryLeavesBlock.Stage.FRUITLESS, "elderberry_tree_leaves",
            ElderberryLeavesBlock.Stage.LEAVES, "elderberry_tree_leaves",
            ElderberryLeavesBlock.Stage.FLOWERS, "elderberry_tree_leaves",
            ElderberryLeavesBlock.Stage.SMALL, "elderberry_tree_leaves",
            ElderberryLeavesBlock.Stage.RIPE, "elderberry_tree_leaves"));

    public static final PlainEntry ALMOND_TREE = registerPlain("almonds", "almond_leaves", null,
        MapColor.PLANT, trunk("nut_tree"), TreeShape.NUT_ORCHARD, WildSpawn.soil(12),
        new HarvestItem("almonds", null, false), "almond_tree_sapling", "almond_sapling",
        null, 0.35F, "cooked_almonds", Processed.COOKED_NUT);

    public static final PlainEntry PISTACHIO_TREE = registerPlain("pistachios", "pistachio_leaves", null,
        MapColor.PLANT, trunk("nut_tree"), TreeShape.NUT_ORCHARD, WildSpawn.soil(12),
        new HarvestItem("pistachios", null, false), "pistachio_tree_sapling", "pistachio_sapling",
        null, 0.35F, "cooked_pistachios", Processed.COOKED_NUT);

    public static final PlainEntry WALPECAN_TREE = registerPlain("walpecans", "walpecan_leaves", null,
        MapColor.PLANT, trunk("nut_tree"), TreeShape.NUT_ORCHARD, WildSpawn.soil(12),
        new HarvestItem("walpecans", null, false), "walpecan_tree_sapling", "walpecan_sapling",
        null, 0.35F, "cooked_walpecans", Processed.COOKED_NUT);

    public static final PlainEntry HAZELNUT_TREE = registerPlain("hazelnuts", "hazelnut_leaves", null,
        MapColor.PLANT, trunk("nut_tree"), TreeShape.ORCHARD_TALL, WildSpawn.soil(12),
        new HarvestItem("hazelnuts", null, false), "hazelnut_tree_sapling", "hazelnut_sapling",
        null, 0.35F, "cooked_hazelnuts", Processed.COOKED_NUT);

    public static final PlainEntry CHESTNUT_TREE = registerPlain("chestnut", "chestnut_leaves", null,
        MapColor.PLANT, trunk("nut_tree"), TreeShape.CHERRY_CHESTNUT, WildSpawn.soil(8),
        new HarvestItem("chestnut", null, false), "chestnut_tree_sapling", "chestnut_sapling",
        null, 0.35F, "cooked_chestnuts", Processed.COOKED_NUT);

    public static final PlainEntry CASHEW_TREE = registerPlain("cashew", "cashew_leaves", null,
        MapColor.PLANT, trunk("nut_tree"), TreeShape.BUSH_CASHEW, WildSpawn.soil(8),
        new HarvestItem("cashew", FoodDefinition.get("cashew"), false),
        "cashew_tree_sapling", "cashew_sapling", null, 0.35F, "cooked_cashew", Processed.COOKED_NUT);

    public static final PlainEntry ALPINE_TREE = registerPlain("alpine", "juniper_leaves", null,
        MapColor.TERRACOTTA_CYAN, trunk("juniper"), null, WildSpawn.soil(6),
        new HarvestItem("juniper_berries", FoodDefinition.get("juniper_berries"), false),
        "juniper_sapling", "juniper_sapling", "juniper_berries", 0.3F, null, Processed.NONE);

    public static final PlainEntry SCOTS_PINE_TREE = registerPlain("scots_pine", "scots_pine_leaves",
        "scots_pine_leave", MapColor.GLOW_LICHEN, trunk("scots_pine"), null, WildSpawn.soil(8),
        new HarvestItem("scots_pine_cone", null, false), "scots_pine_sapling", "scots_pine_sapling",
        null, 0.35F, "pine_nuts", Processed.EDIBLE_NUT);

    public static final PlainEntry HOLLY_TREE = registerPlain("holly", "holly_tree_leaves", null,
        MapColor.PLANT, trunk("holly_tree"), TreeShape.HOLLY, WildSpawn.soil(14),
        new HarvestItem("holly_berries", FoodDefinition.get("holly_berries"), true),
        "holly_tree_sapling", "holly_tree_sapling", "holly_berries", 0.3F, null, Processed.NONE);

    public static final PlainEntry MYRICLAVE_TREE = registerPlain("myriclave", "myriclave_tree_leaves", null,
        MapColor.TERRACOTTA_PINK, trunk("myriclave_tree"), null, WildSpawn.nether(6),
        new HarvestItem("nutmeg", FoodDefinition.get("nutmeg"), true),
        "myriclave_tree_sapling", "myriclave_tree_sapling", null, 0.3F, "dried_nutmeg", Processed.REGISTERED);

    public static final PlainEntry CINNISE_TREE = registerPlain("cinnise", "cinnise_tree_leaves", null,
        MapColor.PLANT, trunk("cinnise_tree"), TreeShape.NUT_ORCHARD, WildSpawn.soil(12),
        new HarvestItem("star_anise", null, false), "cinnise_tree_sapling", "cinnise_tree_sapling",
        null, 0.3F, "dried_star_anise", Processed.REGISTERED);

    public static final PlainEntry COCONUT_TREE = registerPlain("coconut", "coconut_tree_leaves", null,
        MapColor.PLANT, trunk("coconut_tree"), null, WildSpawn.sand(4),
        new HarvestItem("coconut_sprout", null, false), "coconut_sprout", "coconut_sapling",
        null, 0.85F, null, Processed.NONE);

    private Trees() {
    }

    public static void bootstrap() {

        PlantCatalog.addCompostable(elderberryFlowerItem, 0.65F);
        PlantCatalog.addCompostable(() -> DURIAN.get().asItem(), 0.65F);
        PlantCatalog.addCompostable(() -> YUZUMELO.get().asItem(), 0.65F);
    }

    public static List<Entry> entries() {
        return List.copyOf(ENTRIES);
    }

    public static List<PlainEntry> plainTrees() {
        return List.copyOf(PLAIN_ENTRIES);
    }

    public static List<TreeEntry> allTrees() {
        List<TreeEntry> all = new ArrayList<>(ENTRIES);
        all.addAll(PLAIN_ENTRIES);
        return List.copyOf(all);
    }

    public static RegistryObject<ElderberryFlowerBlock> elderberryFlower() {
        return elderberryFlower;
    }

    public static RegistryObject<Block> durian() {
        return DURIAN;
    }

    public static RegistryObject<Block> pomelo() {
        return YUZUMELO;
    }

    public static Entry register(String name, String leavesName, Supplier<Block> trunk, MapColor mapColor,
                                 TreeShape shape, @Nullable WildSpawn wild, Harvest harvest,
                                 Map<FruitLeavesBlock.Stage, String> stageTextures,
                                 String saplingName, String saplingItemName,
                                 String saplingTexture, String saplingItemTexture) {
        return register(name, leavesName, trunk, mapColor, shape, wild, harvest, textures(stageTextures),
            FruitLeavesBlock::new, null, saplingName, saplingItemName, saplingTexture, saplingItemTexture);
    }

    public static Entry registerHanging(String name, String leavesName, Supplier<Block> trunk, MapColor mapColor,
                                        TreeShape shape, @Nullable WildSpawn wild, Harvest harvest,
                                        Map<HangingFruitLeavesBlock.Stage, String> stageTextures,
                                        String fruitName, HangingFruitBlock.Settings fruitSettings,
                                        String saplingName, String saplingItemName,
                                        String saplingTexture, String saplingItemTexture) {
        RegistryObject<HangingFruitBlock> fruit = BlockRegistry.register(fruitName,
            () -> new HangingFruitBlock(fruitSettings));
        return register(name, leavesName, trunk, mapColor, shape, wild, harvest, textures(stageTextures),
            settings -> new HangingFruitLeavesBlock(
                new HangingFruitLeavesBlock.HangingSettings(settings, fruit)),
            fruit, saplingName, saplingItemName, saplingTexture, saplingItemTexture);
    }

    public static Entry registerElderberry(String name, String leavesName, Supplier<Block> trunk,
                                           @Nullable WildSpawn wild,
                                           Map<ElderberryLeavesBlock.Stage, String> stageTextures) {
        RegistryObject<ElderberryFlowerBlock> flower = BlockRegistry.register("elderberry_flowers",
            ElderberryFlowerBlock::new);
        elderberryFlower = flower;
        RegistryObject<Item> flowerItem = ItemRegistry.register("elderberry_flowers",
            () -> new BlockItem(flower.get(), new Item.Properties()));
        elderberryFlowerItem = flowerItem;

        RegistryObject<SaplingBlock> sapling = BlockRegistry.register("elderberry_tree_sapling",
            () -> new ElderberrySaplingBlock(new ModTreeGrower(WorldGenRegistry.treeKey(name)), saplingProperties()));
        RegistryObject<Item> berry = ItemRegistry.register(name,
            () -> FoodDefinition.get(name).blockItem(sapling.get(), true));
        PlantCatalog.markFlatIcon(berry);

        Harvest harvest = new Harvest(berry, null, 1, 1, 1, 1);
        
        return finish(name, leavesName, trunk, MapColor.ICE, null, wild, harvest, textures(stageTextures),
            settings -> new ElderberryLeavesBlock(settings, flower), null, sapling, berry,
            "elderberry_tree_sapling", "elderberries");
    }

    private static PlainEntry registerPlain(String name, String leavesName, @Nullable String leavesTexture,
                                            MapColor leavesColor, Supplier<Block> trunk, @Nullable TreeShape shape,
                                            @Nullable WildSpawn wild, HarvestItem harvest,
                                            String saplingName, String saplingTexture,
                                            @Nullable String fruitTag, float compostChance,
                                            @Nullable String processedName, Processed processedKind) {
        String texture = leavesTexture == null ? leavesName : leavesTexture;
        RegistryObject<PlainLeavesBlock> leaves = BlockRegistry.register(leavesName,
            () -> new PlainLeavesBlock(leavesColor, leafSoundType(name), ParticleContent.leafParticleFor(name)));
        RegistryObject<Item> leavesItem = ItemRegistry.register(leavesName,
            () -> new BlockItem(leaves.get(), new Item.Properties()));

        RegistryObject<SaplingBlock> sapling = BlockRegistry.register(saplingName,
            () -> sapling(wild, new ModTreeGrower(WorldGenRegistry.treeKey(name))));
        RegistryObject<Item> harvestItem = ItemRegistry.register(harvest.name(),
            () -> harvest.create(sapling.get(), !harvest.name().equals(saplingName)));
        
        PlantCatalog.markFlatIcon(harvestItem);

        RegistryObject<Item> processed = null;
        if (processedName != null) {
            processed = ItemRegistry.register(processedName,
                () -> FoodDefinition.get(processedName).consumable());
        }

        TreeSpecies species = new TreeSpecies(name, trunk, leaves::get, shape, wild);
        ResourceKey<PlacedFeature> wildFeature = wild == null ? null : WorldGenRegistry.wildTreeKey(name);
        PlainEntry entry = new PlainEntry(name, species, sapling, harvestItem, processed, processedKind, leaves,
            leavesItem, new ResourceLocation(PleroCottage.MODID, "block/" + texture), fruitTag, compostChance,
            new ResourceLocation(PleroCottage.MODID, "block/" + saplingTexture), wildFeature);
        PLAIN_ENTRIES.add(entry);
        PlantCatalog.add(entry);

        if (processed != null) {
            
        }
        return entry;
    }

    @FunctionalInterface
    private interface LeavesFactory {
        AbstractFruitLeavesBlock<?> create(AbstractFruitLeavesBlock.Settings settings);
    }

    private static Entry register(String name, String leavesName, Supplier<Block> trunk, MapColor mapColor,
                                  TreeShape shape, @Nullable WildSpawn wild, Harvest harvest,
                                  Map<String, ResourceLocation> stageTextures, LeavesFactory leavesFactory,
                                  @Nullable RegistryObject<HangingFruitBlock> hangingFruit,
                                  String saplingName, String saplingItemName,
                                  String saplingTexture, String saplingItemTexture) {
        RegistryObject<SaplingBlock> sapling = BlockRegistry.register(saplingName,
            () -> new SaplingBlock(new ModTreeGrower(WorldGenRegistry.treeKey(name)), saplingProperties()));
        // 种子形态的物品用自己的名字（"苹果种子"），种下去的方块叫"苹果树苗"，Jade 等显示方块名时才统一
        boolean seedItem = saplingItemName.endsWith("_seed") || saplingItemName.endsWith("_seeds");
        RegistryObject<Item> saplingItem = ItemRegistry.register(saplingItemName,
            () -> seedItem ? new ItemNameBlockItem(sapling.get(), new Item.Properties())
                : new BlockItem(sapling.get(), new Item.Properties()));
        if (!saplingItemName.equals(saplingName)) {
            
            PlantCatalog.markFlatIcon(saplingItem);
        }
        return finish(name, leavesName, trunk, mapColor, shape, wild, harvest, stageTextures, leavesFactory,
            hangingFruit, sapling, saplingItem, saplingTexture, saplingItemTexture);
    }

    private static Entry finish(String name, String leavesName, Supplier<Block> trunk, MapColor mapColor,
                                @Nullable TreeShape shape, @Nullable WildSpawn wild, Harvest harvest,
                                Map<String, ResourceLocation> stageTextures, LeavesFactory leavesFactory,
                                @Nullable RegistryObject<HangingFruitBlock> hangingFruit,
                                RegistryObject<SaplingBlock> sapling, RegistryObject<Item> saplingItem,
                                String saplingTexture, String saplingItemTexture) {
        ResourceKey<ConfiguredFeature<?, ?>> tree = WorldGenRegistry.treeKey(name);
        ResourceKey<PlacedFeature> wildFeature = wild == null ? null : WorldGenRegistry.wildTreeKey(name);

        RegistryObject<AbstractFruitLeavesBlock<?>> leaves = BlockRegistry.register(leavesName,
            () -> leavesFactory.create(new AbstractFruitLeavesBlock.Settings(
                mapColor, leafSoundType(name), harvest.fruit(), harvest.rareFruit(), saplingItem,
                harvest.dropMin(), harvest.dropMax(), harvest.pickMin(), harvest.pickMax(),
                ParticleContent.leafParticleFor(name))));

        RegistryObject<Item> leavesItem = ItemRegistry.register(leavesName,
            () -> new BlockItem(leaves.get(), new Item.Properties()));

        TreeSpecies species = new TreeSpecies(name, trunk, leaves::get, shape, wild);

        Entry entry = new Entry(name, species, sapling, saplingItem, leaves, leavesItem, harvest, stageTextures,
            hangingFruit, new ResourceLocation(PleroCottage.MODID, "block/" + saplingTexture),
            new ResourceLocation(PleroCottage.MODID, "item/" + saplingItemTexture), wildFeature);
        ENTRIES.add(entry);
        PlantCatalog.add(entry);

        if (harvest.fruit() instanceof RegistryObject<Item> fruitItem) {

        }
        if (harvest.rareFruit() instanceof RegistryObject<Item> rareItem) {
            
        }
        return entry;
    }

    private static BlockBehaviour.Properties saplingProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY);
    }

    private static SoundType leafSoundType(String name) {
        return switch (name) {
            case "peach", "pear", "apricots", "myriclave" -> SoundType.CHERRY_LEAVES;
            default -> SoundType.GRASS;
        };
    }

    private static SaplingBlock sapling(@Nullable WildSpawn wild, ModTreeGrower grower) {
        BlockBehaviour.Properties properties = saplingProperties();
        if (wild == null) {
            return new SaplingBlock(grower, properties);
        }
        return switch (wild.ground()) {
            case NETHER -> new NetherOrDirtSaplingBlock(grower, properties);
            case SAND -> new SandSaplingBlock(grower, properties);
            default -> new SaplingBlock(grower, properties);
        };
    }

    private static Map<FruitLeavesBlock.Stage, String> stages(String plain, String flowers, String fruits,
                                                              @Nullable String rare) {
        Map<FruitLeavesBlock.Stage, String> byStage = new LinkedHashMap<>();
        byStage.put(FruitLeavesBlock.Stage.UNROLLED, plain);
        byStage.put(FruitLeavesBlock.Stage.FRUITLESS, plain);
        byStage.put(FruitLeavesBlock.Stage.LEAVES, plain);
        byStage.put(FruitLeavesBlock.Stage.FLOWERS, flowers);
        byStage.put(FruitLeavesBlock.Stage.FRUITS, fruits);
        byStage.put(FruitLeavesBlock.Stage.RARE_FRUITS, rare == null ? fruits : rare);
        return byStage;
    }

    private static Map<HangingFruitLeavesBlock.Stage, String> hangingStages(String plain, String flowers,
                                                                           String fruits) {
        Map<HangingFruitLeavesBlock.Stage, String> byStage = new LinkedHashMap<>();
        byStage.put(HangingFruitLeavesBlock.Stage.UNROLLED, plain);
        byStage.put(HangingFruitLeavesBlock.Stage.FRUITLESS, plain);
        byStage.put(HangingFruitLeavesBlock.Stage.LEAVES, plain);
        byStage.put(HangingFruitLeavesBlock.Stage.FLOWERS, flowers);
        byStage.put(HangingFruitLeavesBlock.Stage.SMALL, fruits);
        byStage.put(HangingFruitLeavesBlock.Stage.GROWING, fruits);
        byStage.put(HangingFruitLeavesBlock.Stage.RIPE, fruits);
        byStage.put(HangingFruitLeavesBlock.Stage.RARE_RIPE, fruits);
        return byStage;
    }

    private static Supplier<Block> rosaceae() {
        return trunk("rosaceae_tree");
    }

    private static Supplier<Block> rutaceae() {
        return trunk("rutaceae_tree");
    }

    private static Supplier<Block> trunk(String woodName) {
        return () -> WoodSetContent.byName(woodName).orElseThrow().log().get();
    }

    private static <S extends Enum<S> & StringRepresentable> Map<String, ResourceLocation> textures(
            Map<S, String> byStage) {
        Map<String, ResourceLocation> textures = new LinkedHashMap<>();
        byStage.forEach((stage, path) ->
            textures.put(stage.getSerializedName(), new ResourceLocation(PleroCottage.MODID, "block/" + path)));
        return textures;
    }

    private static RegistryObject<Item> food(String name) {
        return ItemRegistry.register(name, () -> FoodDefinition.get(name).item());
    }

    private static RegistryObject<Item> ingredient(String name) {
        return ingredient(name, Rarity.COMMON);
    }

    private static RegistryObject<Item> ingredient(String name, Rarity rarity) {
        return ItemRegistry.register(name, () -> new Item(new Item.Properties().rarity(rarity)));
    }

    private static RegistryObject<Block> harvestBlock(String name, MapColor mapColor, VoxelShape shape) {
        RegistryObject<Block> block = BlockRegistry.register(name,
            () -> new FallenFruitBlock(mapColor, shape));
        RegistryObject<Item> item = ItemRegistry.register(name,
            () -> new BlockItem(block.get(), new Item.Properties()));
        
        PlantCatalog.markFlatIcon(item);
        return block;
    }

    public interface TreeEntry {

        String name();

        TreeSpecies species();

        RegistryObject<SaplingBlock> sapling();
    }

    public record Harvest(Supplier<Item> fruit, Supplier<Item> rareFruit, int dropMin, int dropMax,
                          int pickMin, int pickMax) {
    }

    public record Entry(String name, TreeSpecies species, RegistryObject<SaplingBlock> sapling,
                        RegistryObject<Item> saplingSeed, RegistryObject<AbstractFruitLeavesBlock<?>> leaves,
                        RegistryObject<Item> leavesItem, Harvest harvest,
                        Map<String, ResourceLocation> leafTextures,
                        @Nullable RegistryObject<HangingFruitBlock> hangingFruit,
                        ResourceLocation saplingTexture, ResourceLocation saplingItemTexture,
                        @Nullable ResourceKey<PlacedFeature> wildFeature) implements PlantEntry, TreeEntry {

        @Override
        public Item saplingItem() {
            return saplingSeed.get();
        }

        @Override
        public Item fruitItem() {
            return harvest.fruit().get();
        }

        @Override
        public Map<Item, Float> compostables() {
            Map<Item, Float> compost = new LinkedHashMap<>();
            compost.put(leavesItem.get(), 0.3F);
            compost.put(saplingSeed.get(), 0.3F);
            if (harvest.fruit() instanceof RegistryObject<Item> fruit) {
                compost.put(fruit.get(), 0.65F);
            }
            if (harvest.rareFruit() instanceof RegistryObject<Item> rare) {
                compost.put(rare.get(), 1.0F);
            }
            return compost;
        }
    }

    public record PlainEntry(String name, TreeSpecies species, RegistryObject<SaplingBlock> sapling,
                             RegistryObject<Item> harvest, @Nullable RegistryObject<Item> processed,
                             Processed processedKind, RegistryObject<PlainLeavesBlock> leaves,
                             RegistryObject<Item> leavesItem, ResourceLocation leavesTexture,
                             @Nullable String fruitTag, float compostChance, ResourceLocation saplingTexture,
                             @Nullable ResourceKey<PlacedFeature> wildFeature)
        implements PlantEntry, TreeEntry {

        @Override
        public Item fruitItem() {
            return harvest.get();
        }

        @Override
        public Item saplingItem() {
            return harvest.get();
        }

        @Override
        public boolean isFruit() {
            return fruitTag != null;
        }

        @Override
        @Nullable
        public String fruitTagName() {
            return fruitTag;
        }

        @Override
        public Map<Item, Float> compostables() {
            Map<Item, Float> compost = new LinkedHashMap<>();
            compost.put(leavesItem.get(), 0.3F);
            if (compostChance > 0F) {
                compost.put(harvest.get(), compostChance);
            }
            return compost;
        }
    }

    public enum Processed {

        NONE,

        COOKED_NUT,

        EDIBLE_NUT,

        REGISTERED
    }

    private record HarvestItem(String name, @Nullable FoodDefinition food, boolean plantingFood) {

        Item create(Block sapling, boolean itemName) {
            if (food != null) {
                if (plantingFood) {
                    return new BlockFoodItem(sapling, food.itemProperties(), food.useDuration(),
                        itemName, food.useAnim(), null, false);
                }
                return food.blockItem(sapling, itemName);
            }
            Item.Properties properties = new Item.Properties();
            
            return itemName ? new ItemNameBlockItem(sapling, properties) : new BlockItem(sapling, properties);
        }
    }
}

