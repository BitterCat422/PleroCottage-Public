package net.bittercat.plerocottage.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blocks.BerryBushBlock;
import net.bittercat.plerocottage.blocks.LavenderBlock;
import net.bittercat.plerocottage.data.BlockTagProvider;
import net.bittercat.plerocottage.content.worldgen.FeatureContent;
import net.bittercat.plerocottage.content.plants.Bushes;
import net.bittercat.plerocottage.content.plants.Crops;
import net.bittercat.plerocottage.content.plants.Flowers;
import net.bittercat.plerocottage.content.plants.FrigidGrass;
import net.bittercat.plerocottage.content.plants.Melons;
import net.bittercat.plerocottage.content.plants.Trees;
import net.bittercat.plerocottage.content.plants.Vines;
import net.bittercat.plerocottage.content.plants.DragonFruits;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

public final class WorldGenRegistry {

    private static final HolderSet<Block> STRUCTURE_IGNORED_BLOCKS = HolderSet.direct(
        BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.AIR),
        BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.STRUCTURE_BLOCK));

    private static final List<StructureTree> ELDERBERRY_TREES = List.of(
        new StructureTree("elderberry_tree_1", -1, 0, -1),
        new StructureTree("elderberry_tree_2", -2, 0, -2),
        new StructureTree("elderberry_tree_3", -2, 0, -2));

    private static final List<StructureTree> ALPINE_TREES = List.of(
        new StructureTree("juniper_1", -3, 0, -3),
        new StructureTree("juniper_2", -3, 0, -3),
        new StructureTree("juniper_3", -3, 0, -3),
        new StructureTree("juniper_4", -3, 0, -3));

    private static final List<StructureTree> SCOTS_PINE_TREES = List.of(
        new StructureTree("scots_pine_1", -2, 0, -2, false, false),
        new StructureTree("scots_pine_2", -2, 0, -2, false, false),
        new StructureTree("scots_pine_3", -2, 0, -2, false, false),
        new StructureTree("scots_pine_4", -2, -1, -2, false, false));

    private static final List<StructureTree> MYRICLAVE_TREES = List.of(
        new StructureTree("myriclave_tree_1", -2, 0, -2),
        new StructureTree("myriclave_tree_2", -2, 0, -2),
        new StructureTree("myriclave_tree_3", -2, 0, -2));

    private static final List<StructureTree> COCONUT_TREES = List.of(
        new StructureTree("coconut_tree", -1, 0, -2));

    private static final List<StructureTree> WILD_COCONUT_TREES = List.of(
        new StructureTree("wild_coconut_tree", -1, 0, -2));

    private static final List<StructureTree> BANANA_TREES = List.of(
        new StructureTree("banana_tree_4block", 0, 0, 0, false, false),
        new StructureTree("banana_tree_4block", 0, 0, 0, false, false),
        new StructureTree("banana_tree_5block", 0, 0, 0, false, false),
        new StructureTree("banana_tree_5block", 0, 0, 0, false, false),
        new StructureTree("banana_tree_6block", 0, 0, 0, false, false));

    private static final List<StructureTree> DRAGON_FRUIT_CACTUS = List.of(
        new StructureTree("dragon_fruit_cactus_1", 0, 0, 0, false, false),
        new StructureTree("dragon_fruit_cactus_2", 0, 0, 0, false, false),
        new StructureTree("dragon_fruit_cactus_3", 0, 0, 0, false, false),
        new StructureTree("dragon_fruit_cactus_4", 0, 0, 0, false, false));

    private static final List<StructureTree> DURIAN_TREES = List.of(
        new StructureTree("durian_tree_1", -3, 0, -3),
        new StructureTree("durian_tree_2", -3, 0, -3),
        new StructureTree("durian_tree_3", -5, 0, -5));

    private static final List<StructureTree> STARFRUIT_TREES = List.of(
        new StructureTree("starfruit_tree_1", -2, 0, -2),
        new StructureTree("starfruit_tree_2", -2, 0, -2),
        new StructureTree("starfruit_tree_3", -2, 0, -2));

    private static final Map<String, WildSpawn> EXTRA_WILD_TREES = Map.of(
        
        "banana", WildSpawn.dirt(6));

    private static final List<String> UNUSED_WILD_TREE_FEATURES = List.of(
        "almonds", "alpine", "apricots", "elderberries", "hawthorns", "hazelnuts", "pistachios", "walpecans");

    private static final Map<String, List<StructureTree>> SAPLING_STRUCTURES = Map.of(
        "elderberries", ELDERBERRY_TREES,
        "alpine", ALPINE_TREES,
        "scots_pine", SCOTS_PINE_TREES,
        "myriclave", MYRICLAVE_TREES,
        "coconut", COCONUT_TREES,
        "durian", DURIAN_TREES,
        "starfruit", STARFRUIT_TREES);

    private static final Map<String, List<StructureTree>> WILD_STRUCTURES = Map.of(
        "elderberries", List.of(
            new StructureTree("wild_elderberry_tree_1", -4, -3, -4),
            new StructureTree("wild_elderberry_tree_2", -5, -4, -5),
            new StructureTree("wild_elderberry_tree_3", -4, -2, -4),
            new StructureTree("wild_elderberry_tree_4", -4, -3, -4),
            new StructureTree("wild_elderberry_tree_5", -3, -1, -3)),
        "alpine", ALPINE_TREES,
        "scots_pine", SCOTS_PINE_TREES,
        "myriclave", MYRICLAVE_TREES,
        "durian", DURIAN_TREES,
        "starfruit", STARFRUIT_TREES,
        "coconut", WILD_COCONUT_TREES,
        "banana", BANANA_TREES);

    private static final int WILD_BUSH_TRIES = 96;
    private static final int WILD_BUSH_XZ_SPREAD = 5;
    private static final int WILD_BUSH_Y_SPREAD = 2;

    private static final IntProvider WILD_BUSH_AGE = UniformInt.of(2, 4);

    private WorldGenRegistry() {
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> configuredKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, id(name));
    }

    public static ResourceKey<PlacedFeature> placedKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, id(name));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> treeKey(String species) {
        return configuredKey(species + "_tree");
    }

    public static ResourceKey<PlacedFeature> wildTreeKey(String species) {
        return placedKey("wild_" + species + "_tree");
    }

    public static void bootstrapConfigured(BootstapContext<ConfiguredFeature<?, ?>> context) {
        context.register(configuredKey("mugwort"), wildFlowerPatch(FrigidGrass.BLOCK.get(), 8));
        context.register(configuredKey("hyacinth"), wildFlowerPatch(Flowers.block("hyacinth").get(), 12));
        context.register(configuredKey("mediterranean_plain_bonemeal_flowers"),
            mediterraneanPlainBonemealFlowers());

        for (Trees.TreeEntry tree : Trees.allTrees()) {
            if (tree.species().shape() == null) {
                
                continue;
            }
            context.register(configuredKey(tree.name() + "_tree"),
                new ConfiguredFeature<>(Feature.TREE, tree.species().buildTree()));
            if (tree.species().wildSpawn() == null) {
                
                continue;
            }
            context.register(configuredKey("wild_" + tree.name() + "_tree"),
                new ConfiguredFeature<>(Feature.TREE, tree.species().buildTree(wildTreeDecorators(tree.name()))));
        }

        SAPLING_STRUCTURES.forEach((species, trees) ->
            context.register(configuredKey(species + "_tree"), randomStructureTree(trees)));
        WILD_STRUCTURES.forEach((species, trees) ->
            context.register(configuredKey("wild_" + species + "_tree"), randomStructureTree(trees,
                species.equals("myriclave") ? Optional.of(petals("unlucky_clovers")) : Optional.empty())));
        context.register(configuredKey("wild_dragon_fruit_cactus"), randomStructureTree(DRAGON_FRUIT_CACTUS));

        for (Bushes.Entry bush : Bushes.entries()) {
            if (bush.wild() != null) {
                context.register(configuredKey(wildBushName(bush)), wildBushPatch(bush));
            }
        }
        for (WildCrop wild : allWildCrops()) {
            context.register(configuredKey(wild.name()), wildCropPatch(wild));
        }
    }

    private static List<TreeDecorator> wildTreeDecorators(String species) {
        return switch (species) {
            case "fig" -> List.of(new HangingWildVineDecorator(Vines.GRAPE_WILD.block().get()));
            case "avocado" -> List.of(new HangingWildVineDecorator(Vines.PASSION_FRUIT_WILD.block().get()));
            case "mango" -> List.of(new HangingWildVineDecorator(Vines.CUCUMBER_WILD.block().get()));
            case "pear" -> List.of(new GroundPetalsDecorator(petals("chamomiles")));
            case "peach" -> List.of(new GroundPetalsDecorator(petals("wild_roses")));
            case "apricots" -> List.of(new GroundPetalsDecorator(petals("dianthuses")));
            default -> List.of();
        };
    }

    private static PinkPetalsBlock petals(String name) {
        return (PinkPetalsBlock) Flowers.block(name).get();
    }

    private static ConfiguredFeature<?, ?> wildFlowerPatch(Block flower, int tries) {
        PlacedFeature patch = new PlacedFeature(
            Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(flower)))),
            List.of(BlockPredicateFilter.forPredicate(
                BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR))));
        return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
            new RandomPatchConfiguration(tries, 7, 3, Holder.direct(patch)));
    }

    private static ConfiguredFeature<?, ?> mediterraneanPlainBonemealFlowers() {
        List<Holder<PlacedFeature>> flowers = List.of(
            BlockStateProvider.simple(Blocks.ORANGE_TULIP),
            BlockStateProvider.simple(Blocks.PINK_TULIP),
            BlockStateProvider.simple(Blocks.RED_TULIP),
            BlockStateProvider.simple(Blocks.WHITE_TULIP),
            BlockStateProvider.simple(Flowers.block("blue_tulip").get()),
            BlockStateProvider.simple(Flowers.block("hyacinth").get()),
            lavenderFlower()
        ).stream()
            .map(block -> Holder.direct(new PlacedFeature(
                Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(block))),
                List.of())))
            .toList();
        PlacedFeature randomFlower = new PlacedFeature(
            Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_RANDOM_SELECTOR,
                new SimpleRandomFeatureConfiguration(HolderSet.direct(flowers)))),
            List.of(BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR))));
        return new ConfiguredFeature<>(Feature.FLOWER,
            new RandomPatchConfiguration(64, 7, 3, Holder.direct(randomFlower)));
    }

    private static BlockStateProvider lavenderFlower() {
        BlockStateProvider lavender = BlockStateProvider.simple(Flowers.block("lavender").get());
        lavender = new RandomizedIntStateProvider(lavender, LavenderBlock.COLOR, UniformInt.of(0, 2));
        lavender = new RandomizedIntStateProvider(lavender, LavenderBlock.STAGE, UniformInt.of(0, 2));
        return lavender;
    }

    private static ConfiguredFeature<?, ?> randomStructureTree(List<StructureTree> trees) {
        return randomStructureTree(trees, Optional.empty());
    }

    private static ConfiguredFeature<?, ?> randomStructureTree(List<StructureTree> trees,
                                                               Optional<PinkPetalsBlock> groundPetals) {
        List<Holder<PlacedFeature>> options = new ArrayList<>();
        for (StructureTree tree : trees) {
            StructureTemplateFeatureConfig config = new StructureTemplateFeatureConfig(
                id(tree.name()), tree.randomRotation(), tree.randomMirror(), STRUCTURE_IGNORED_BLOCKS, tree.offset(),
                groundPetals);
            ConfiguredFeature<?, ?> configured =
                new ConfiguredFeature<>(FeatureContent.STRUCTURE_FEATURE.get(), config);
            options.add(Holder.direct(new PlacedFeature(Holder.direct(configured), List.of())));
        }
        return new ConfiguredFeature<>(Feature.SIMPLE_RANDOM_SELECTOR,
            new SimpleRandomFeatureConfiguration(HolderSet.direct(options)));
    }

    public static void bootstrapPlaced(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(placedKey("mediterranean_plain_bonemeal_flowers"),
            new PlacedFeature(features.getOrThrow(configuredKey("mediterranean_plain_bonemeal_flowers")),
                List.of(RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome())));
        context.register(placedKey("wild_dragon_fruit_cactus"),
            new PlacedFeature(features.getOrThrow(configuredKey("wild_dragon_fruit_cactus")),
                List.of(RarityFilter.onAverageOnceEvery(12), InSquarePlacement.spread(),
                    HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                    BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR)),
                    BiomeFilter.biome())));
        for (Trees.TreeEntry tree : Trees.allTrees()) {
            WildSpawn wild = tree.species().wildSpawn();
            if (wild == null || UNUSED_WILD_TREE_FEATURES.contains(tree.name())) {
                continue;
            }
            
            context.register(placedKey("wild_" + tree.name() + "_tree"), new PlacedFeature(
                features.getOrThrow(configuredKey("wild_" + tree.name() + "_tree")),
                placement(wild, tree.sapling().get())));
        }
        EXTRA_WILD_TREES.forEach((species, wild) -> context.register(placedKey("wild_" + species + "_tree"),
            new PlacedFeature(features.getOrThrow(configuredKey("wild_" + species + "_tree")),
                placement(wild, null))));

        for (Bushes.Entry bush : Bushes.entries()) {
            if (bush.wild() == null) {
                continue;
            }
            String name = wildBushName(bush);
            context.register(placedKey(name),
                new PlacedFeature(features.getOrThrow(configuredKey(name)), wildBushPlacement(bush.wild())));
        }
        for (WildCrop wild : allWildCrops()) {
            context.register(placedKey(wild.name()),
                new PlacedFeature(features.getOrThrow(configuredKey(wild.name())), wildCropPlacement(wild)));
        }
    }

    public static void registerAdditionalPlacements(BiConsumer<String, List<PlacementModifier>> register) {
        register.accept("camelia", rareSurfacePlacement(4));
        register.accept("coffea_arabica", rareSurfacePlacement(8));
        register.accept("hyacinth", rareSurfacePlacement(3));
        register.accept("jasmine", rareSurfacePlacement(4));
        register.accept("licorice", rareSurfacePlacement(8));
        register.accept("lucky_clovers", rareSurfacePlacement(4));
        register.accept("saffron", rareSurfacePlacement(4));
        register.accept("semper_augustus", rareSurfacePlacement(3));

        register.accept("debrishroom_spawn", uniformHeightPlacement(32, -64, 64));
        register.accept("debrishroom", uniformHeightPlacement(1, -64, 64));
        register.accept("lavender_spawn", List.of(RarityFilter.onAverageOnceEvery(2),
            CountPlacement.of(UniformInt.of(10, 12)), InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
            BlockPredicateFilter.forPredicate(BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT)),
            BiomeFilter.biome()));
        register.accept("marble_ore_more", uniformHeightPlacement(8, 0, 160));
        register.accept("marble_ore", uniformHeightPlacement(6, 0, 160));
        register.accept("pearl_rock_ore_spawn", uniformHeightPlacement(56, -64, 128));
        register.accept("suspicious_ore_spawn", List.of(CountPlacement.of(24),
            InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64),
                VerticalAnchor.absolute(16)), BiomeFilter.biome()));
        register.accept("mediterranean_plain_lake", List.of(
            RarityFilter.onAverageOnceEvery(120), InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome()));
        register.accept("mediterranean_plain_oak", List.of(
            RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
            BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT),
                BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR))),
            BiomeFilter.biome()));
        register.accept("mugwort", List.of(RarityFilter.onAverageOnceEvery(12),
            InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
            BiomeFilter.biome()));
        register.accept("tulips_spawn", List.of(InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
            BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT),
                BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR))),
            BiomeFilter.biome()));
        register.accept("wild_marble", List.of(RarityFilter.onAverageOnceEvery(2),
            InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
            BiomeFilter.biome()));
        register.accept("wild_purple_sugar_cane", List.of(CountPlacement.of(4),
            InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
            BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT),
                BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR))),
            BiomeFilter.biome()));
        register.accept("wild_rice_crop", List.of(RarityFilter.onAverageOnceEvery(6),
            InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(1),
            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG), BiomeFilter.biome()));

        register.accept("wild_almond_tree", placement(WildSpawn.soil(12), Trees.ALMOND_TREE.sapling().get()));
        register.accept("wild_apricot_tree", placement(WildSpawn.soil(4), Trees.APRICOT_TREE.sapling().get()));
        register.accept("wild_hazelnut_tree", placement(WildSpawn.soil(12), Trees.HAZELNUT_TREE.sapling().get()));
        register.accept("wild_juniper", placement(WildSpawn.soil(6), Trees.ALPINE_TREE.sapling().get()));
        register.accept("wild_pistachio_tree", placement(WildSpawn.soil(12), Trees.PISTACHIO_TREE.sapling().get()));
        register.accept("wild_walpecan_tree", placement(WildSpawn.soil(12), Trees.WALPECAN_TREE.sapling().get()));
        register.accept("wild_elderberry_tree", placement(WildSpawn.endAir(32), null));
        register.accept("wild_hawthorn_tree", placement(WildSpawn.nether(12), null));
    }

    private static List<PlacementModifier> rareSurfacePlacement(int count) {
        return List.of(CountPlacement.of(count), RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome());
    }

    private static List<PlacementModifier> uniformHeightPlacement(int count, int min, int max) {
        return List.of(CountPlacement.of(count), InSquarePlacement.spread(),
            HeightRangePlacement.uniform(VerticalAnchor.absolute(min), VerticalAnchor.absolute(max)),
            BiomeFilter.biome());
    }

    private static String wildBushName(Bushes.Entry bush) {
        return "wild_" + bush.name();
    }

    private static ConfiguredFeature<?, ?> wildBushPatch(Bushes.Entry bush) {
        BlockStateProvider age = new RandomizedIntStateProvider(
            BlockStateProvider.simple(bush.block().get().defaultBlockState()), BerryBushBlock.AGE, WILD_BUSH_AGE);
        PlacedFeature patch = new PlacedFeature(
            Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(age))),
            List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR),
                groundBelow(bush.wild().ground())))));
        return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
            new RandomPatchConfiguration(WILD_BUSH_TRIES, WILD_BUSH_XZ_SPREAD, WILD_BUSH_Y_SPREAD,
                Holder.direct(patch)));
    }

    private static ConfiguredFeature<?, ?> wildCropPatch(WildCrop wild) {
        BlockStateProvider source = BlockStateProvider.simple(wild.block().get().defaultBlockState());
        BlockStateProvider state = wild.age() == null
            ? source
            : new RandomizedIntStateProvider(source, CropBlock.AGE, wild.age());

        List<BlockPredicate> predicates = new ArrayList<>();
        predicates.add(BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR));
        if (wild.ground() != null) {
            predicates.add(groundBelow(wild.ground()));
        }
        PlacedFeature patch = new PlacedFeature(
            Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(state))),
            List.of(BlockPredicateFilter.forPredicate(BlockPredicate.allOf(predicates))));
        return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
            new RandomPatchConfiguration(wild.patchTries(), wild.xzSpread(), wild.ySpread(), Holder.direct(patch)));
    }

    private static List<WildCrop> allWildCrops() {
        List<WildCrop> result = new ArrayList<>(Crops.wildFeatures());
        result.addAll(Melons.wildFeatures());
        return result;
    }

    private static BlockPredicate groundBelow(WildGround ground) {
        return switch (ground) {
            case DIRT -> BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT);
            case SAND -> BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.SAND);
            case END_STONE -> BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), Blocks.END_STONE);
            default -> throw new IllegalStateException("野生植物不支持的地面：" + ground);
        };
    }

    private static List<PlacementModifier> wildCropPlacement(WildCrop wild) {
        List<PlacementModifier> modifiers = new ArrayList<>();
        if (wild.count() > 0) {
            modifiers.add(CountPlacement.of(wild.count()));
        }
        if (wild.rarity() > 1) {
            modifiers.add(RarityFilter.onAverageOnceEvery(wild.rarity()));
        }
        modifiers.add(InSquarePlacement.spread());
        modifiers.add(HeightmapPlacement.onHeightmap(wild.heightmap()));
        modifiers.add(BiomeFilter.biome());
        return List.copyOf(modifiers);
    }

    private static List<PlacementModifier> wildBushPlacement(WildBush wild) {
        return List.of(RarityFilter.onAverageOnceEvery(wild.chance()), InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG), BiomeFilter.biome());
    }

    private static List<PlacementModifier> placement(WildSpawn wild, @Nullable Block sapling) {
        WeightedListInt density = sparseCount(wild);
        return switch (wild.ground()) {
            case SOIL -> VegetationPlacements.treePlacement(CountPlacement.of(density), sapling);
            
            case DIRT -> List.of(CountPlacement.of(density), InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                    BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT),
                    BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR))),
                BiomeFilter.biome());
            
            case SAND -> List.of(CountPlacement.of(density), InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                    BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.SAND),
                    BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR),
                    BlockPredicate.replaceable(BlockPos.ZERO.east()),
                    BlockPredicate.replaceable(BlockPos.ZERO.west()),
                    BlockPredicate.replaceable(BlockPos.ZERO.south()),
                    BlockPredicate.replaceable(BlockPos.ZERO.north()))),
                BiomeFilter.biome());
            // 从天顶往下扫是不行的：EnvironmentScanPlacement 的 max_steps 被原版限制在 1..32，

            case NETHER -> List.of(CountOnEveryLayerPlacement.of(density),
                BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                    BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR),
                    BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTagProvider.NETHER_PLANTABLE))),
                BiomeFilter.biome());
            case END_STONE -> List.of(CountPlacement.of(density), InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                    BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR),
                    BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), Blocks.END_STONE))),
                BiomeFilter.biome());
            
            case END_AIR -> List.of(CountPlacement.of(density), InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(70)),
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(BlockPos.ZERO, Blocks.AIR)),
                BiomeFilter.biome());
        };
    }

    private record StructureTree(String name, Vec3i offset, boolean randomRotation, boolean randomMirror) {

        StructureTree(String name, int x, int y, int z) {
            this(name, new Vec3i(x, y, z), true, true);
        }

        StructureTree(String name, int x, int y, int z, boolean randomRotation, boolean randomMirror) {
            this(name, new Vec3i(x, y, z), randomRotation, randomMirror);
        }
    }

    private static WeightedListInt sparseCount(WildSpawn wild) {
        int weight = wild.spawn() - 1;
        if (weight < 1) {
            
            return new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder()
                .add(ConstantInt.of(1), 1)
                .build());
        }
        return new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder()
            .add(ConstantInt.of(0), weight)
            .add(ConstantInt.of(1), 1)
            .build());
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(PleroCottage.MODID, name);
    }
}

