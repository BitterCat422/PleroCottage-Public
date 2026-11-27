package net.bittercat.plerocottage.data;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.plants.Flowers;
import net.bittercat.plerocottage.content.plants.Bananas;
import net.bittercat.plerocottage.content.plants.Bushes;
import net.bittercat.plerocottage.content.plants.Coconuts;
import net.bittercat.plerocottage.content.plants.Crops;
import net.bittercat.plerocottage.content.plants.Corns;
import net.bittercat.plerocottage.content.plants.DragonFruits;
import net.bittercat.plerocottage.content.plants.Melons;
import net.bittercat.plerocottage.content.plants.DoubleCrops;
import net.bittercat.plerocottage.content.plants.PlantEntry;
import net.bittercat.plerocottage.content.plants.PlantCatalog;
import net.bittercat.plerocottage.content.plants.Trees;
import net.bittercat.plerocottage.content.plants.Vines;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
import net.bittercat.plerocottage.content.building.BuildingBlockContent;
import net.bittercat.plerocottage.content.building.ColoredDecorationContent;
import net.bittercat.plerocottage.content.equipment.EquipmentContent;
import net.bittercat.plerocottage.content.items.OrdinaryItemContent;
import net.bittercat.plerocottage.wood.WoodSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class ItemTagProvider extends ItemTagsProvider {

    public static final TagKey<Item> NORMAL_DIRT_SEEDS = seedTip("normal_dirt_seeds");
    public static final TagKey<Item> NORMAL_FARMLAND_SEEDS = seedTip("normal_farmland_seeds");
    public static final TagKey<Item> SPECIAL_SEEDS = seedTip("special_seeds");
    public static final TagKey<Item> VINE_SEEDS = seedTip("vine_seeds");
    public static final TagKey<Item> BLACK_FARMLAND_SEEDS = seedTip("black_farmland_seeds");
    public static final TagKey<Item> WATER_DIRT_SEEDS = seedTip("water_dirt_seeds");
    public static final TagKey<Item> END_SEEDS = seedTip("end_seeds");
    public static final TagKey<Item> SAND_SEEDS = seedTip("sand_seeds");

    public static final TagKey<Item> STRIPPED_WOOD =
        ItemTags.create(new ResourceLocation("forge", "stripped_wood"));

    public static final TagKey<Item> MOD_STRIPPED_WOOD =
        ItemTags.create(new ResourceLocation(PleroCottage.MODID, "stripped_wood"));
    public static final TagKey<Item> WALLPAPER_BLOCK =
        ItemTags.create(new ResourceLocation(PleroCottage.MODID, "wallpaper_block"));
    public static final TagKey<Item> PANELLED_WALLPAPER_BLOCK =
        ItemTags.create(new ResourceLocation(PleroCottage.MODID, "panelled_wallpaper_block"));
    public static final TagKey<Item> ALL_WALLPAPER_BLOCK =
        ItemTags.create(new ResourceLocation(PleroCottage.MODID, "all_wallpaper_block"));
    public static final TagKey<Item> SHULKER_BOXES =
        ItemTags.create(new ResourceLocation(PleroCottage.MODID, "shulker_boxes"));
    private static final TagKey<Item> SICKLES =
        ItemTags.create(new ResourceLocation(PleroCottage.MODID, "tools/sickles"));
    private static final TagKey<Item> SICKLES_3X =
        ItemTags.create(new ResourceLocation(PleroCottage.MODID, "tools/sickles_3x"));
    private static final TagKey<Item> SICKLES_5X =
        ItemTags.create(new ResourceLocation(PleroCottage.MODID, "tools/sickles_5x"));

    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
                               ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, PleroCottage.MODID, existingFileHelper);
    }

    private static TagKey<Item> seedTip(String name) {
        return ItemTags.create(new ResourceLocation(PleroCottage.MODID, "seeds/" + name));
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(BlockTags.FLOWERS, ItemTags.FLOWERS);
        // 原版的物品 flowers 标签包含 small_flowers，蜜蜂靠它识别花
        for (var flower : Flowers.blocks().values()) {
            if (!Flowers.petalNames().contains(flower.getId().getPath())) {
                tag(ItemTags.SMALL_FLOWERS).add(flower.get().asItem());
            }
        }
        var wallpaperTag = tag(WALLPAPER_BLOCK);
        var panelledTag = tag(PANELLED_WALLPAPER_BLOCK);
        for (var family : BuildingBlockContent.all().values()) {
            for (var block : family.entrySet()) {
                if (block.getKey().equals("panelled_wallpaper_block")
                    || block.getKey().endsWith("_panelled_wallpaper_block")) {
                    panelledTag.add(block.getValue().get().asItem());
                } else if (block.getKey().equals("wallpaper_block")
                    || block.getKey().endsWith("_wallpaper_block")) {
                    wallpaperTag.add(block.getValue().get().asItem());
                }
            }
        }
        tag(ALL_WALLPAPER_BLOCK).addTag(WALLPAPER_BLOCK).addTag(PANELLED_WALLPAPER_BLOCK);
        tag(SHULKER_BOXES).add(ColoredDecorationContent.shulkerBox("snow").get().asItem(),
            ColoredDecorationContent.shulkerBox("lavender").get().asItem(),
            ColoredDecorationContent.shulkerBox("turquoise").get().asItem());
        tag(ItemTags.CANDLES).add(ColoredDecorationContent.candle("snow").get().asItem(),
            ColoredDecorationContent.candle("lavender").get().asItem(),
            ColoredDecorationContent.candle("turquoise").get().asItem());

        copy(BlockTags.LOGS, ItemTags.LOGS);
        copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);

        copy(BlockTags.LEAVES, ItemTags.LEAVES);

        for (PlantEntry plant : PlantCatalog.all()) {
            Item fruit = plant.fruitItem();
            String fruitTag = plant.fruitTagName();
            if (fruitTag != null) {
                tag(ItemTags.create(new ResourceLocation("forge", "fruits"))).add(fruit);
                addClassifiedItem(new ResourceLocation("forge", "fruits/" + fruitTag), fruit);
            }
            Item sapling = plant.saplingItem();
            if (sapling != null && plant != Trees.ELDERBERRY_TREE) {
                tag(ItemTags.SAPLINGS).add(sapling);
            }
        }

        Set<Item> bushSeeds = new LinkedHashSet<>();
        for (Bushes.Entry bush : Bushes.entries()) {
            Item harvest = bush.fruitItem();
            for (ResourceLocation harvestTag : bush.harvestTags()) {
                addClassifiedItem(harvestTag, harvest);
            }
            
            if (bush.settings().rareDrop() != null) {
                Item rareFruit = bush.settings().rareDrop().get();
                ResourceLocation rareName = BuiltInRegistries.ITEM.getKey(rareFruit);
                tag(ItemTags.create(new ResourceLocation("forge", "fruits"))).add(rareFruit);
                addClassifiedItem(new ResourceLocation("forge", "fruits/" + rareName.getPath()), rareFruit);
            }
            
            Item planting = bush.plantingItem().get();
            if (planting != harvest) {
                bushSeeds.add(planting);
            }
        }
        if (!bushSeeds.isEmpty()) {
            tag(ItemTags.create(new ResourceLocation("forge", "seeds"))).add(bushSeeds.toArray(Item[]::new));
        }
        tag(ItemTags.create(new ResourceLocation("forge", "seeds"))).add(
            Trees.FIG_TREE.saplingItem(),
            Trees.APPLE_TREE.saplingItem(),
            Trees.ORANGE_TREE.saplingItem(),
            Trees.POMEGRANATE_TREE.saplingItem(),
            Trees.HAWTHORN_TREE.saplingItem(),
            Trees.OLIVE_TREE.saplingItem(),
            Trees.LEMON_TREE.saplingItem(),
            Trees.MANGO_TREE.saplingItem(),
            Trees.STARFRUIT_TREE.saplingItem(),
            Trees.AVOCADO_TREE.saplingItem(),
            Trees.CHERRIES_TREE.saplingItem(),
            Trees.PEAR_TREE.saplingItem(),
            Trees.YUZUMELO_TREE.saplingItem(),
            Trees.PEACH_TREE.saplingItem(),
            Trees.KIWIFRUIT_TREE.saplingItem(),
            Trees.APRICOT_TREE.saplingItem(),
            Trees.DURIAN_TREE.saplingItem());

        ResourceLocation bellPepper = new ResourceLocation("forge", "vegetables/bell_pepper");
        addClassifiedTag(bellPepper, new ResourceLocation("forge", "vegetables/green_bell_pepper"));
        addClassifiedTag(bellPepper, new ResourceLocation("forge", "vegetables/red_bell_pepper"));
        addClassifiedTag(bellPepper, new ResourceLocation("forge", "vegetables/yellow_bell_pepper"));

        for (Crops.Crop crop : Crops.entries()) {
            for (TagKey<Item> harvestTag : crop.harvestTags()) {
                addClassifiedItem(harvestTag.location(), crop.harvest().get());
            }
            for (TagKey<Item> seedTag : crop.seedTags()) {
                tag(seedTag).add(crop.seed().get());
            }
        }
        tag(ItemTags.create(new ResourceLocation("forge", "seeds"))).add(Corns.CORN_SEED.get());
        tag(ItemTags.create(new ResourceLocation("forge", "vegetables"))).add(Corns.CORN.get());
        addClassifiedItem(new ResourceLocation("forge", "vegetables/corn"), Corns.CORN.get());
        tag(ItemTags.create(new ResourceLocation("forge", "tea_leaves")))
            .add(Bushes.TEA_TREE.fruitItem(), OrdinaryItemContent.GREEN_TEA_LEAVES.get(),
                OrdinaryItemContent.BLACK_TEA_LEAVES.get());
        addClassifiedItem(Melons.FRUIT_TAG.location(), Melons.HAMIMELON_ITEM.get());
        tag(ItemTags.create(new ResourceLocation("forge", "fruits"))).add(Melons.HAMIMELON_ITEM.get());
        tag(ItemTags.create(new ResourceLocation("forge", "seeds"))).add(Melons.HAMIMELON_SEED.get());
        tag(Melons.SEED_TAG).add(Melons.HAMIMELON_SEED.get());

        for (DoubleCrops.Entry crop : DoubleCrops.entries()) {
            for (TagKey<Item> harvestTag : crop.harvestTags()) {
                addClassifiedItem(harvestTag.location(), crop.harvest().get());
            }
            for (TagKey<Item> seedTag : crop.seedTags()) {
                tag(seedTag).add(crop.seed().get());
            }
        }

        // 爬藤作物：果实与种子的标签逐种声明——黄瓜走蔬菜而不是水果，青提走原工作区
        // 那个 green_grape 名字，都不能按注册名推断。
        for (Vines.Entry vine : Vines.entries()) {
            Item harvest = vine.harvest().get();
            for (ResourceLocation harvestTag : vine.harvestTags()) {
                addClassifiedItem(harvestTag, harvest);
            }
            if (vine.rareHarvest() != null) {
                Item rareHarvest = vine.rareHarvest().get();
                for (ResourceLocation rareTag : vine.rareHarvestTags()) {
                    addClassifiedItem(rareTag, rareHarvest);
                }
            }
            for (TagKey<Item> seedTag : vine.seedTags()) {
                tag(seedTag).add(vine.seed().get());
            }
        }

        tag(VINE_SEEDS).add(Vines.CUCUMBER_SEED.get(), Vines.GRAPE_SEED.get(),
            Vines.PASSION_FRUIT_SLICE.get());

        for (WoodSet wood : WoodSetContent.all()) {
            var logs = tag(ItemTags.create(
                new ResourceLocation(PleroCottage.MODID, wood.name() + "_logs")));
            if (wood.stalkStyle()) {
                logs.add(wood.log().get().asItem(), wood.strippedLog().get().asItem());
                tag(MOD_STRIPPED_WOOD).add(wood.strippedLog().get().asItem());
            } else {
                logs.add(wood.log().get().asItem(), wood.strippedLog().get().asItem(),
                    wood.wood().get().asItem(), wood.strippedWood().get().asItem());
                tag(MOD_STRIPPED_WOOD).add(wood.strippedWood().get().asItem());
            }
            tag(ItemTags.SIGNS).add(wood.signItem().get());
            tag(ItemTags.HANGING_SIGNS).add(wood.hangingSignItem().get());
            tag(ItemTags.BOATS).add(wood.vehicleItem().get());
            tag(ItemTags.CHEST_BOATS).add(wood.chestVehicleItem().get());
        }
        tag(STRIPPED_WOOD).addTag(MOD_STRIPPED_WOOD);
        tag(ItemTags.create(new ResourceLocation("forge", "seeds")))
            .addTag(NORMAL_DIRT_SEEDS)
            .addTag(NORMAL_FARMLAND_SEEDS)
            .addTag(SPECIAL_SEEDS)
            .addTag(VINE_SEEDS)
            .addTag(BLACK_FARMLAND_SEEDS)
            .addTag(WATER_DIRT_SEEDS)
            .addTag(END_SEEDS)
            .addTag(SAND_SEEDS);

        tag(SICKLES).add(EquipmentContent.WOODEN_SICKLE.get(), EquipmentContent.STONE_SICKLE.get(),
            EquipmentContent.IRON_SICKLE.get(), EquipmentContent.GOLDEN_SICKLE.get(),
            EquipmentContent.DIAMOND_SICKLE.get(), EquipmentContent.NETHERITE_SICKLE.get());
        tag(SICKLES_3X).add(EquipmentContent.WOODEN_SICKLE.get(),
            EquipmentContent.STONE_SICKLE.get(), EquipmentContent.IRON_SICKLE.get());
        tag(SICKLES_5X).add(EquipmentContent.GOLDEN_SICKLE.get(),
            EquipmentContent.DIAMOND_SICKLE.get(), EquipmentContent.NETHERITE_SICKLE.get());

        addClassifiedItem(new ResourceLocation("forge", "fruits/green_apple"), Trees.GREEN_APPLE.get());
        addClassifiedTag(new ResourceLocation("forge", "fruits/apple"),
            new ResourceLocation("forge", "fruits/green_apple"));
        addClassifiedItem(new ResourceLocation("forge", "fruits/olive"),
            Trees.OLIVE_TREE.harvest().rareFruit().get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/black_olive"),
            Trees.OLIVE_TREE.harvest().rareFruit().get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/cherry"),
            Trees.CHERRIES_TREE.harvest().fruit().get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/dragonfruit"), DragonFruits.FRUIT.get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/gold_kiwi"),
            Trees.KIWIFRUIT_TREE.harvest().rareFruit().get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/grapefruit"),
            Trees.ORANGE_TREE.harvest().rareFruit().get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/green_kiwi"),
            Trees.KIWIFRUIT_TREE.harvest().fruit().get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/green_olive"),
            Trees.OLIVE_TREE.harvest().fruit().get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/green_plum"),
            Trees.APRICOT_TREE.harvest().rareFruit().get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/lime"),
            Trees.LEMON_TREE.harvest().rareFruit().get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/plum"),
            Trees.PEACH_TREE.harvest().rareFruit().get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/pomelo"),
            Trees.YUZUMELO_TREE.harvest().fruit().get());
        addClassifiedTag(new ResourceLocation("forge", "fruits/kiwi"),
            new ResourceLocation("forge", "fruits/gold_kiwi"));
        addClassifiedTag(new ResourceLocation("forge", "fruits/kiwi"),
            new ResourceLocation("forge", "fruits/green_kiwi"));

        tag(ItemTags.create(new ResourceLocation("forge", "fruits"))).add(Coconuts.COCONUT_ITEM.get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/coconut"), Coconuts.COCONUT_ITEM.get());

        tag(ItemTags.create(new ResourceLocation("forge", "fruits"))).add(Bananas.BANANA.get());
        addClassifiedItem(new ResourceLocation("forge", "fruits/banana"), Bananas.BANANA.get());
    }

    private void addClassifiedItem(ResourceLocation tagId, Item item) {
        tag(ItemTags.create(tagId)).add(item);
        ResourceLocation cropsTag = cropsTag(tagId);
        if (cropsTag != null) {
            tag(ItemTags.create(cropsTag)).add(item);
        }
    }

    private void addClassifiedTag(ResourceLocation tagId, ResourceLocation childTagId) {
        tag(ItemTags.create(tagId)).addTag(ItemTags.create(childTagId));
        ResourceLocation cropsTag = cropsTag(tagId);
        ResourceLocation cropsChildTag = cropsTag(childTagId);
        if (cropsTag != null && cropsChildTag != null) {
            tag(ItemTags.create(cropsTag)).addTag(ItemTags.create(cropsChildTag));
        }
    }

    private static ResourceLocation cropsTag(ResourceLocation tagId) {
        if (!tagId.getNamespace().equals("forge")) {
            return null;
        }
        String path = tagId.getPath();
        if (path.startsWith("fruits/") || path.startsWith("vegetables/")) {
            return new ResourceLocation("forge", "crops/" + path.substring(path.indexOf('/') + 1));
        }
        return null;
    }
}

