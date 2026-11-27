package net.bittercat.plerocottage.data;

import net.bittercat.plerocottage.blocks.VineStakeBlock;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.bittercat.plerocottage.blocks.AbstractFruitLeavesBlock;
import net.bittercat.plerocottage.blocks.BananaTreeBunchBlock;
import net.bittercat.plerocottage.blocks.BerryBushBlock;
import net.bittercat.plerocottage.blocks.CheeseWheelBlock;
import net.bittercat.plerocottage.blocks.DoubleCropBlock;
import net.bittercat.plerocottage.blocks.DragonFruitCropBlock;
import net.bittercat.plerocottage.blocks.StackableCropBlock;
import net.bittercat.plerocottage.blocks.StackableFoodBlock;
import net.bittercat.plerocottage.blocks.StackableFoodDisplayBlock;
import net.bittercat.plerocottage.blocks.ElderberryFlowerBlock;
import net.bittercat.plerocottage.blocks.ElderberryLeavesBlock;
import net.bittercat.plerocottage.blocks.FruitLeavesBlock;
import net.bittercat.plerocottage.blocks.HangingFruitBlock;
import net.bittercat.plerocottage.blocks.HangingFruitLeavesBlock;
import net.bittercat.plerocottage.blocks.LavenderBlock;
import net.bittercat.plerocottage.blocks.PortionFoodBlock;
import net.bittercat.plerocottage.blocks.YoungCoconutsBlock;
import net.bittercat.plerocottage.blocks.VineCropBlock;
import net.bittercat.plerocottage.blocks.WildVineBlock;
import net.bittercat.plerocottage.content.plants.BlackDirts;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.content.core.CoreContent;
import net.bittercat.plerocottage.content.building.BuildingBlockContent;
import net.bittercat.plerocottage.content.building.ColoredDecorationContent;
import net.bittercat.plerocottage.content.blocks.MarbleContent;
import net.bittercat.plerocottage.content.blocks.OreBlockContent;
import net.bittercat.plerocottage.content.food.DisplayFoodContent;
import net.bittercat.plerocottage.content.food.FoodBlockContent;
import net.bittercat.plerocottage.content.food.StackableFoodContent;
import net.bittercat.plerocottage.content.food.FoodContent;
import net.bittercat.plerocottage.content.furniture.FurnitureContent;
import net.bittercat.plerocottage.content.items.OrdinaryItemContent;
import net.bittercat.plerocottage.content.food.PlateContent;
import net.bittercat.plerocottage.content.entities.PoultryContent;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
import net.bittercat.plerocottage.content.plants.Bananas;
import net.bittercat.plerocottage.content.plants.Bushes;
import net.bittercat.plerocottage.content.plants.Coconuts;
import net.bittercat.plerocottage.content.plants.DragonFruits;
import net.bittercat.plerocottage.content.plants.Crops;
import net.bittercat.plerocottage.content.plants.Corns;
import net.bittercat.plerocottage.content.plants.PurpleSugarCanes;
import net.bittercat.plerocottage.content.plants.Melons;
import net.bittercat.plerocottage.content.plants.DoubleCrops;
import net.bittercat.plerocottage.content.plants.Trees;
import net.bittercat.plerocottage.content.plants.Vines;
import net.bittercat.plerocottage.content.plants.Flowers;
import net.bittercat.plerocottage.content.plants.WildRice;
import net.bittercat.plerocottage.content.plants.FrigidGrass;
import net.bittercat.plerocottage.content.plants.PottedPlantContent;
import net.bittercat.plerocottage.wood.WoodSet;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

public final class BlockLootTables extends BlockLootSubProvider {

    public BlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        for (var furniture : FurnitureContent.all()) {
            dropSelf(furniture.cabinet().get());
            dropSelf(furniture.drawer().get());
        }
        for (var family : BuildingBlockContent.all().values()) {
            for (RegistryObject<Block> block : family.values()) {
                dropSelf(block.get());
            }
        }
        for (String color : ColoredDecorationContent.COLORS) {
            Block bed = ColoredDecorationContent.bed(color).get();
            add(bed, createSinglePropConditionTable(bed, BedBlock.PART, BedPart.HEAD));
            add(ColoredDecorationContent.candle(color).get(), this::createCandleDrops);
            add(ColoredDecorationContent.candleCake(color).get(),
                createCandleCakeDrops(ColoredDecorationContent.candle(color).get()));
            add(ColoredDecorationContent.shulkerBox(color).get(), createShulkerBoxDrop(
                ColoredDecorationContent.shulkerBox(color).get()));
        }
        dropSelf(MarbleContent.MARBLE.get());
        add(MarbleContent.MARBLE_SLAB.get(), this::createSlabItemTable);
        dropSelf(MarbleContent.MARBLE_STAIRS.get());
        dropSelf(MarbleContent.MARBLE_WALL.get());
        dropSelf(MarbleContent.POLISHED_MARBLE.get());
        add(MarbleContent.POLISHED_MARBLE_SLAB.get(), this::createSlabItemTable);
        dropSelf(MarbleContent.POLISHED_MARBLE_STAIRS.get());
        dropSelf(MarbleContent.POLISHED_MARBLE_WALL.get());
        dropSelf(MarbleContent.POLISHED_MARBLE_BRICK.get());
        add(MarbleContent.POLISHED_MARBLE_BRICK_SLAB.get(), this::createSlabItemTable);
        dropSelf(MarbleContent.POLISHED_MARBLE_BRICK_STAIRS.get());
        dropSelf(MarbleContent.POLISHED_MARBLE_BRICK_WALL.get());
        dropSelf(MarbleContent.BOWL.get());
        dropSelf(PlateContent.PLATE.get());
        add(Coconuts.CUP_BLOCK.get(), createSingleItemTable(Coconuts.CUP.get()));
        dropSelf(CoreContent.HAY_NEST.get());
        dropSelf(CoreContent.EGG_INCUBATOR.get());
        add(PoultryContent.EGG_DISPLAY_BLOCK.get(), noDrop());
        dropSelf(OreBlockContent.DEBRISHROOM.get());
        add(OreBlockContent.PEARL_ROCK_ORE.get(), pearlRockOreDrops(OreBlockContent.PEARL_ROCK_ORE.get()));
        add(OreBlockContent.DEEPSLATE_PEARL_ROCK_ORE.get(),
            pearlRockOreDrops(OreBlockContent.DEEPSLATE_PEARL_ROCK_ORE.get()));
        add(OreBlockContent.SUSPICIOUS_ORE.get(), suspiciousOreDrops(OreBlockContent.SUSPICIOUS_ORE.get()));
        add(OreBlockContent.DEEPSLATE_SUSPICIOUS_ORE.get(),
            suspiciousOreDrops(OreBlockContent.DEEPSLATE_SUSPICIOUS_ORE.get()));
        dropSelf(OreBlockContent.PEARL_ROCK_BLOCK.get());
        dropSelf(OreBlockContent.GARNET_BLOCK.get());
        dropSelf(OreBlockContent.TOPAZ_BLOCK.get());
        dropSelf(OreBlockContent.PINK_CRYSTAL_BLOCK.get());
        dropSelf(OreBlockContent.BRILLIANT_DIAMOND_BLOCK.get());
        for (WoodSet wood : WoodSetContent.all()) {
            dropSelf(wood.log().get());
            dropSelf(wood.strippedLog().get());
            if (!wood.stalkStyle()) {
                dropSelf(wood.wood().get());
                dropSelf(wood.strippedWood().get());
            }
            dropSelf(wood.planks().get());
            dropSelf(wood.stairs().get());
            dropSelf(wood.fence().get());
            dropSelf(wood.fenceGate().get());
            dropSelf(wood.trapdoor().get());
            dropSelf(wood.pressurePlate().get());
            dropSelf(wood.button().get());
            dropSelf(wood.sign().get());
            dropSelf(wood.hangingSign().get());

            add(wood.slab().get(), this::createSlabItemTable);
            add(wood.door().get(), this::createDoorTable);
            
        }
        for (DisplayFoodContent.DisplayLoot display : DisplayFoodContent.displayLoot()) {
            add(display.block().get(), createSingleItemTable(display.item().get()));
        }
        add(PurpleSugarCanes.CROP.get(), createSingleItemTable(PurpleSugarCanes.ITEM.get()));
        add(PoultryContent.MALLARD_EGG_BLOCK.get(), createSingleItemTable(PoultryContent.MALLARD_EGG.get()));
        add(PoultryContent.TURKEY_EGG_BLOCK.get(), createSingleItemTable(PoultryContent.TURKEY_EGG.get()));
        for (FoodBlockContent.FoodBlock food : FoodBlockContent.entries()) {
            Block block = food.block().get();
            if (food.name().equals("truffle_pie") || food.name().equals("cherries_pie")) {
                add(block, pieSliceDrops(food.name()));
            } else if (block instanceof CheeseWheelBlock) {
                Item wedge = FoodContent.item(food.name().replace("_wheel", "_wedge")).get();
                add(block, cheeseWheelDrops(block, wedge));
            } else if (block instanceof PortionFoodBlock portion) {
                add(block, portionFoodDrops(block, portion.portionItem()));
            } else {
                dropSelf(block);
            }
        }
        for (StackableFoodContent.Entry food : StackableFoodContent.entries()) {
            add(food.block().get(), stackableFoodDrops(food));
        }
        for (PottedPlantContent.Entry potted : PottedPlantContent.entries()) {
            add(potted.block().get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(Items.FLOWER_POT))
                    .when(ExplosionCondition.survivesExplosion()))
                .withPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(potted.dropItem().get()))
                    .when(ExplosionCondition.survivesExplosion())));
        }
        plantDrops();

        BlockRegistry.MACHINE_BLOCKS.forEach(entry -> dropSelf(entry.get()));
    }

    private void plantDrops() {
        for (Trees.Entry tree : Trees.entries()) {
            
            dropSelf(tree.sapling().get());
            AbstractFruitLeavesBlock<?> leaves = tree.leaves().get();
            if (leaves instanceof HangingFruitLeavesBlock hanging) {
                add(hanging, hangingLeavesDrops(hanging));
                add(tree.hangingFruit().get(), hangingFruitDrops(tree.hangingFruit().get(), leaves.settings()));
            } else if (leaves instanceof ElderberryLeavesBlock elderberries) {
                add(elderberries, elderberryLeavesDrops(elderberries));
            } else if (leaves instanceof FruitLeavesBlock fruit) {
                add(fruit, fruitLeavesDrops(fruit));
            } else {
                throw new IllegalStateException("未处理的树叶实现：" + leaves.getClass().getName());
            }
        }
        for (Trees.PlainEntry tree : Trees.plainTrees()) {
            
            dropSelf(tree.sapling().get());
            add(tree.leaves().get(), plainLeavesDrops(tree.leaves().get(), tree.harvest().get()));
        }

        coconutDrops();
        bananaDrops();
        blackDirtDrops();

        add(DragonFruits.CACTUS_FLOWER.get(), LootTable.lootTable()
            .withPool(LootPool.lootPool().add(LootItem.lootTableItem(DragonFruits.CACTUS_FLOWER_ITEM.get())
                .when(shearedOrSilkTouch()))));
        add(DragonFruits.CROP.get(), LootTable.lootTable()
            .withPool(LootPool.lootPool().add(LootItem.lootTableItem(DragonFruits.CACTUS_FLOWER_ITEM.get())
                .when(ageIs(DragonFruits.CROP.get(), DragonFruitCropBlock.AGE, 0)
                    .or(ageIs(DragonFruits.CROP.get(), DragonFruitCropBlock.AGE, 1)))))
            .withPool(LootPool.lootPool().add(LootItem.lootTableItem(DragonFruits.FRUIT.get())
                .when(ageIs(DragonFruits.CROP.get(), DragonFruitCropBlock.AGE, 2)))));
        add(DragonFruits.FRUIT_BLOCK.get(), createSingleItemTable(DragonFruits.FRUIT.get()));
        dropSelf(DragonFruits.SLICE_BLOCK.get());

        add(Trees.elderberryFlower().get(), elderberryFlowerDrops(Trees.elderberryFlower().get()));
        add(Trees.durian().get(), durianDrops(Trees.durian().get()));
        dropSelf(Trees.pomelo().get());
        for (Bushes.Entry bush : Bushes.entries()) {
            add(bush.block().get(), berryBushDrops(bush.block().get()));
        }
        for (Crops.Crop crop : Crops.entries()) {
            add(crop.block().get(), cropDrops(crop));
        }
        for (DoubleCrops.Entry crop : DoubleCrops.entries()) {
            add(crop.block().get(), doubleCropDrops(crop));
        }
        add(Corns.CORN_CROP_BLOCK.get(), cornDrops());
        add(Melons.HAMIMELON.get(), hamimelonDrops());
        add(Melons.HAMIMELON_STEM.get(),
            createStemDrops(Melons.HAMIMELON_STEM.get(), Melons.HAMIMELON_SEED.get()));
        add(Melons.HAMIMELON_ATTACHED_STEM.get(),
            attachedStemDrops(Melons.HAMIMELON_SEED.get()));
        for (Vines.Entry vine : Vines.entries()) {
            add(vine.cropBlock().get(), vineCropDrops(vine));
            add(vine.wild().block().get(), wildVineDrops(vine));
        }
        add(Vines.VINE_STAKE.get(), vineStakeDrops(Vines.VINE_STAKE.get()));
        dropSelf(Vines.VINE_ARBOR.get());

        dropSelf(Crops.PINEAPPLE_FRUIT.get());
        dropSelf(Crops.WILD_PEANUT.get());
        dropSelf(DragonFruits.CACTUS.get());
        for (var flower : Flowers.blocks().values()) {
            Block block = flower.get();
            if (block instanceof PinkPetalsBlock) {
                var item = LootItem.lootTableItem(block);
                for (int amount = PinkPetalsBlock.MIN_FLOWERS; amount <= PinkPetalsBlock.MAX_FLOWERS; amount++) {
                    item.apply(SetItemCountFunction.setCount(ConstantValue.exactly(amount))
                        .when(ageIs(block, PinkPetalsBlock.AMOUNT, amount)));
                }
                add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                    .add(applyExplosionDecay(block, item))));
            } else if (block instanceof LavenderBlock) {
                var item = LootItem.lootTableItem(block);
                for (int stage = 1; stage <= 2; stage++) {
                    item.apply(SetItemCountFunction.setCount(ConstantValue.exactly(stage + 1))
                        .when(ageIs(block, LavenderBlock.STAGE, stage)));
                }
                add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                    .add(applyExplosionDecay(block, item))));
            } else {
                dropSelf(block);
            }
        }
        Block wildRice = WildRice.BLOCK.get();
        add(wildRice, LootTable.lootTable().withPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(WildRice.ITEM.get())
                .when(stageIs(wildRice, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
            .when(ExplosionCondition.survivesExplosion())));
        Block wildMint = Crops.WILD_MINT.get();
        add(wildMint, LootTable.lootTable().withPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(Crops.WILD_MINT_ITEM.get())
                .when(stageIs(wildMint, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
            .when(ExplosionCondition.survivesExplosion())));
        add(FrigidGrass.BLOCK.get(), LootTable.lootTable().withPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(FrigidGrass.ITEM.get())
                .when(shearedOrSilkTouch()))
            .when(ExplosionCondition.survivesExplosion())));
    }

    private LootTable.Builder cornDrops() {
        Block block = Corns.CORN_CROP_BLOCK.get();
        LootItemCondition.Builder age0 = ageIs(block, StackableCropBlock.AGE, 0);
        LootItemCondition.Builder age1To2 = ageIs(block, StackableCropBlock.AGE, 1)
            .or(ageIs(block, StackableCropBlock.AGE, 2));
        return LootTable.lootTable()
            .withPool(LootPool.lootPool().add(LootItem.lootTableItem(Corns.CORN_SEED.get()))
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                .when(age0))
            .withPool(LootPool.lootPool().add(LootItem.lootTableItem(Corns.CORN.get()))
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)))
                .when(age1To2));
    }

    private LootTable.Builder pearlRockOreDrops(Block ore) {
        return createSilkTouchDispatchTable(ore, applyExplosionDecay(ore,
            LootItem.lootTableItem(OrdinaryItemContent.PEARL_ROCK.get())
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))));
    }

    /** 精准采集掉落矿石本身，否则按权重抽取两次宝石，每次受时运加成。 */
    private LootTable.Builder suspiciousOreDrops(Block ore) {
        LootPool.Builder gems = LootPool.lootPool().setRolls(ConstantValue.exactly(2.0F)).when(HAS_NO_SILK_TOUCH)
            .add(gemEntry(ore, OrdinaryItemContent.GARNET.get(), 6))
            .add(gemEntry(ore, OrdinaryItemContent.TOPAZ.get(), 15))
            .add(gemEntry(ore, Items.EMERALD, 10))
            .add(gemEntry(ore, Items.DIAMOND, 15))
            .add(gemEntry(ore, Items.LAPIS_LAZULI, 20))
            .add(gemEntry(ore, Items.AMETHYST_SHARD, 30))
            .add(gemEntry(ore, OrdinaryItemContent.PINK_CRYSTAL.get(), 3))
            .add(gemEntry(ore, OrdinaryItemContent.BRILLIANT_DIAMOND.get(), 1));
        return LootTable.lootTable()
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(ore).when(HAS_SILK_TOUCH)))
            .withPool(gems);
    }

    private LootPoolEntryContainer.Builder<?> gemEntry(Block ore, Item gem, int weight) {
        return applyExplosionDecay(ore, LootItem.lootTableItem(gem).setWeight(weight)
            .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)));
    }

    /** 写法同原版西瓜：精准采集掉落整瓜，否则掉落瓜片，受时运加成且不超过 9 片。 */
    private LootTable.Builder hamimelonDrops() {
        Block melon = Melons.HAMIMELON.get();
        return createSilkTouchDispatchTable(melon, applyExplosionDecay(melon,
            LootItem.lootTableItem(Melons.HAMIMELON_SLICE.get())
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F)))
                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
                .apply(LimitCount.limitCount(IntRange.upperBound(9)))));
    }

    private LootTable.Builder attachedStemDrops(Item seed) {
        return LootTable.lootTable().withPool(LootPool.lootPool()
            .add(applyExplosionDecay(Melons.HAMIMELON_ATTACHED_STEM.get(),
                LootItem.lootTableItem(seed)
                    .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 8.0F / 15.0F)))
                    .apply(ApplyBonusCount.addBonusBinomialDistributionCount(
                        Enchantments.BLOCK_FORTUNE, 0.5714286F, 1)))));
    }

    private LootTable.Builder cropDrops(Crops.Crop crop) {
        Block block = crop.block().get();
        Crops.Drops drops = crop.drops();

        LootPool.Builder seed = LootPool.lootPool().add(LootItem.lootTableItem(crop.seed().get()));
        if (!drops.seedAtAllAges()) {
            seed = seed.when(ageBelow(block, CropBlock.MAX_AGE));
        }

        LootTable.Builder table = LootTable.lootTable().withPool(seed);
        if (drops.matureSeedMax() > 0) {
            table.withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(crop.seed().get())
                    .apply(SetItemCountFunction.setCount(
                        UniformGenerator.between(drops.matureSeedMin(), drops.matureSeedMax()))))
                .when(ageIs(block, CropBlock.AGE, CropBlock.MAX_AGE)));
        }
        return table.withPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(crop.harvest().get())
                .apply(SetItemCountFunction.setCount(
                    UniformGenerator.between(drops.harvestMin(), drops.harvestMax()))))
            .when(ageIs(block, CropBlock.AGE, CropBlock.MAX_AGE)));
    }

    private LootTable.Builder doubleCropDrops(DoubleCrops.Entry crop) {
        Block block = crop.block().get();
        DoubleCrops.Drops drops = crop.drops();
        LootItemCondition.Builder lower = stageIs(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER);
        LootItemCondition.Builder immature = lower.and(ageBelow(block, crop.maxAge()));
        LootItemCondition.Builder ripe = lower.and(ageIs(block, DoubleCropBlock.AGE, crop.maxAge()));

        return LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(crop.seed().get()))
                .when(immature))
            .withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(crop.harvest().get())
                    .apply(SetItemCountFunction.setCount(
                        UniformGenerator.between(drops.harvestMin(), drops.harvestMax()))))
                .when(ripe));
    }

    private LootTable.Builder vineCropDrops(Vines.Entry crop) {
        Block block = crop.cropBlock().get();
        LootItemCondition.Builder ripe = ageIs(block, VineCropBlock.AGE, VineCropBlock.MAX_AGE);
        LootItemCondition.Builder normal = crop.rareHarvest() == null
            ? ripe
            : ripe.and(stateIs(block, VineCropBlock.RARE, false));

        LootTable.Builder table = LootTable.lootTable().withPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(crop.harvest().get())
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(crop.harvestCount()))))
            .when(normal));
        if (crop.rareHarvest() != null) {
            table.withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(crop.rareHarvest().get())
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(crop.harvestCount()))))
                .when(ripe)
                .when(stateIs(block, VineCropBlock.RARE, true)));
        }
        return table;
    }

    private LootTable.Builder wildVineDrops(Vines.Entry vine) {
        Block wild = vine.wild().block().get();
        return LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(vine.seed().get()))
                .when(stateIs(wild, WildVineBlock.FRUIT, false)))
            .withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(vine.harvest().get())
                    .apply(SetItemCountFunction.setCount(
                        UniformGenerator.between(Vines.WILD_HARVEST_MIN, Vines.WILD_HARVEST_MAX))))
                .when(stateIs(wild, WildVineBlock.FRUIT, true)));
    }

    private void coconutDrops() {
        dropSelf(Coconuts.COCONUT.get());
        dropSelf(Coconuts.YOUNG_COCONUT.get());
        dropSelf(Coconuts.HALF_OF_COCONUT_BLOCK.get());
        add(Coconuts.YOUNG_COCONUTS.get(), youngCoconutsDrops(Coconuts.YOUNG_COCONUTS.get()));
    }

    private void bananaDrops() {
        dropSelf(Bananas.FROND.get());
        add(Bananas.CROWN.get(), bananaCrownDrops());
        dropSelf(Bananas.SAPLING.get());
        dropSelf(Bananas.FLOWER.get());
        add(Bananas.TREE_BUNCH.get(), bananaTreeBunchDrops(Bananas.TREE_BUNCH.get()));
        dropSelf(Bananas.BUNCH.get());
    }

    private LootTable.Builder bananaCrownDrops() {
        return LootTable.lootTable()
            .withPool(LootPool.lootPool().add(LootItem.lootTableItem(Bananas.SAPLING.get())))
            .withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Bananas.FROND.get())
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))));
    }

    private LootTable.Builder bananaTreeBunchDrops(Block block) {
        return LootTable.lootTable().withPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(Bananas.BUNCH.get())
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F)))
                .when(ageIs(block, BananaTreeBunchBlock.BLOCKSTATE, BananaTreeBunchBlock.RIPE))));
    }

    private void blackDirtDrops() {
        dropSelf(BlackDirts.BLACK_DIRT.get());
        dropSelf(BlackDirts.SNOWY_BLACK_DIRT.get());
        dropSelf(BlackDirts.RICH_BLACK_DIRT_FARMLAND.get());
        add(BlackDirts.BLACK_DIRT_TRUFFLE.get(), blackDirtTruffleDrops(BlackDirts.BLACK_DIRT_TRUFFLE.get()));
    }

    private LootTable.Builder youngCoconutsDrops(Block block) {
        return LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Coconuts.COCONUT_BLOSSOM.get())
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                    .when(ageIs(block, YoungCoconutsBlock.BLOCKSTATE, 0))))
            .withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Coconuts.YOUNG_COCONUT_ITEM.get())
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                    .when(ageIs(block, YoungCoconutsBlock.BLOCKSTATE, YoungCoconutsBlock.RIPE))));
    }

    private LootTable.Builder blackDirtTruffleDrops(Block block) {
        // 松露黑土只认精准采集：原工作区没用剪刀的那一条，否则拿剪刀挖会白白丢掉松露。
        LootItemCondition.Builder silkTouch = silkTouch();
        return LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(block).when(silkTouch))
                .add(LootItem.lootTableItem(BlackDirts.BLACK_DIRT.get()).when(silkTouch.invert()))
                .when(ExplosionCondition.survivesExplosion()))
            .withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(BlackDirts.TRUFFLE.get())
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                .when(silkTouch.invert()));
    }

    private LootTable.Builder elderberryLeavesDrops(ElderberryLeavesBlock leaves) {
        LootItemCondition.Builder ripe = stageIs(leaves, ElderberryLeavesBlock.STAGE,
            ElderberryLeavesBlock.Stage.RIPE);
        return leavesDrops(leaves, ripe, ripe);
    }

    private LootTable.Builder elderberryFlowerDrops(ElderberryFlowerBlock flower) {
        return LootTable.lootTable().withPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(flower)
                .when(ageIs(flower, ElderberryFlowerBlock.AGE, 0)))
            .add(LootItem.lootTableItem(flower)
                .when(ageIs(flower, ElderberryFlowerBlock.AGE, 1)))
            .add(LootItem.lootTableItem(Trees.ELDERBERRY_TREE.harvest().fruit().get())
                .when(ageIs(flower, ElderberryFlowerBlock.AGE, ElderberryFlowerBlock.MAX_AGE)))
            .when(ExplosionCondition.survivesExplosion()));
    }

    private LootTable.Builder durianDrops(Block fruit) {
        LootItemCondition.Builder axe = MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.AXES));
        return LootTable.lootTable().withPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(Trees.HALF_OF_DURIAN.get())
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                .when(axe))
            .add(LootItem.lootTableItem(fruit)
                .when(axe.invert())));
    }

    private LootTable.Builder fruitLeavesDrops(FruitLeavesBlock leaves) {
        return leavesDrops(leaves,
            stageIs(leaves, FruitLeavesBlock.STAGE, FruitLeavesBlock.Stage.FRUITS),
            stageIs(leaves, FruitLeavesBlock.STAGE, FruitLeavesBlock.Stage.RARE_FRUITS));
    }

    private LootTable.Builder hangingLeavesDrops(HangingFruitLeavesBlock leaves) {
        AbstractFruitLeavesBlock.Settings settings = leaves.settings();
        List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
        entries.add(leavesSelf(leaves));
        if (settings.canBeRare()) {
            entries.add(singleFruitEntry(settings.rareFruit().get()).when(stageIs(leaves,
                HangingFruitLeavesBlock.STAGE, HangingFruitLeavesBlock.Stage.RARE_RIPE)));
        }
        if (settings.sapling() != null) {
            entries.add(saplingEntry(settings.sapling().get()));
        }
        return LootTable.lootTable()
            .withPool(pool(entries))
            .withPool(stickPool());
    }

    private LootTable.Builder leavesDrops(AbstractFruitLeavesBlock<?> leaves,
                                          LootItemCondition.Builder ripe,
                                          LootItemCondition.Builder rare) {
        LootItemCondition.Builder handPicked = handPicked();
        AbstractFruitLeavesBlock.Settings settings = leaves.settings();
        return LootTable.lootTable()
            .withPool(outcomePool(leaves, ripe, rare, settings.dropMin(), settings.dropMax())
                .when(handPicked.invert()))
            .withPool(outcomePool(leaves, ripe, rare, settings.pickMin(), settings.pickMax())
                .when(handPicked))
            .withPool(stickPool());
    }

    private LootTable.Builder hangingFruitDrops(HangingFruitBlock fruit, AbstractFruitLeavesBlock.Settings settings) {
        return LootTable.lootTable().withPool(LootPool.lootPool()
            .add(singleFruitEntry(settings.fruit().get())
                .when(stageIs(fruit, HangingFruitBlock.STAGE, HangingFruitBlock.Stage.RIPE)))
            .when(ExplosionCondition.survivesExplosion()));
    }

    private static LootPool.Builder stickPool() {
        return LootPool.lootPool()
            .add(LootItem.lootTableItem(Items.STICK)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE,
                    1 / 50F, 1 / 45F, 1 / 40F, 1 / 30F, 1 / 10F)))
            .when(shearedOrSilkTouch().invert())
            .when(ExplosionCondition.survivesExplosion());
    }

    private static LootItemCondition.Builder handPicked() {
        return LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
            EntityPredicate.Builder.entity().of(EntityType.PLAYER).build());
    }

    private LootPool.Builder outcomePool(AbstractFruitLeavesBlock<?> leaves,
                                         LootItemCondition.Builder ripe,
                                         LootItemCondition.Builder rare, int min, int max) {
        AbstractFruitLeavesBlock.Settings settings = leaves.settings();
        List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
        entries.add(leavesSelf(leaves));
        if (settings.canBeRare()) {
            entries.add(harvestEntry(settings.rareFruit().get(), min, max).when(rare));
        }
        entries.add(harvestEntry(settings.fruit().get(), min, max).when(ripe));
        if (settings.sapling() != null) {
            entries.add(saplingEntry(settings.sapling().get()));
        }
        return pool(entries);
    }

    private LootTable.Builder berryBushDrops(BerryBushBlock bush) {
        BerryBushBlock.Settings s = bush.settings();
        LootItemCondition.Builder ripe = ageIs(bush, bush.ageProperty(), s.maxAge());

        List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
        if (s.rareDrop() != null) {
            entries.add(LootItem.lootTableItem(s.rareDrop().get())
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(s.dropMin(), s.dropMax())))
                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
                .when(ripe)
                .when(stateIs(bush, BerryBushBlock.RARE, true)));
        }
        entries.add(LootItem.lootTableItem(s.drop().get())
            .apply(SetItemCountFunction.setCount(UniformGenerator.between(s.dropMin(), s.dropMax())))
            .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
            .when(ripe));
        LootTable.Builder table = LootTable.lootTable();
        if (s.alwaysDropsSeed()) {
            
            table.withPool(pool(List.of(LootItem.lootTableItem(s.seed().get()))));
        } else {
            entries.add(LootItem.lootTableItem(s.seed().get()).when(ripe.invert()));
        }
        return table.withPool(pool(entries));
    }

    private static LootPoolEntryContainer.Builder<?> harvestEntry(Item fruit, int min, int max) {
        return LootItem.lootTableItem(fruit)
            .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
            .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE));
    }

    private LootTable.Builder plainLeavesDrops(Block leaves, Item harvest) {
        List<LootPoolEntryContainer.Builder<?>> entries = new ArrayList<>();
        entries.add(leavesSelf(leaves));
        entries.add(LootItem.lootTableItem(harvest)
            .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE,
                1 / 20F, 1 / 16F, 1 / 12F, 1 / 10F)));
        return LootTable.lootTable()
            .withPool(pool(entries))
            .withPool(stickPool());
    }

    private static LootPoolEntryContainer.Builder<?> singleFruitEntry(Item fruit) {
        return LootItem.lootTableItem(fruit);
    }

    private LootPoolEntryContainer.Builder<?> leavesSelf(Block block) {
        return LootItem.lootTableItem(block).when(shearedOrSilkTouch());
    }

    private static LootItemCondition.Builder shearedOrSilkTouch() {
        return MatchTool.toolMatches(ItemPredicate.Builder.item()
            .hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))))
            .or(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS)));
    }

    private static LootItemCondition.Builder silkTouch() {
        return MatchTool.toolMatches(ItemPredicate.Builder.item()
            .hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
    }

    private LootPoolEntryContainer.Builder<?> saplingEntry(Item sapling) {
        return LootItem.lootTableItem(sapling)
            .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE,
                1 / 20F, 1 / 16F, 1 / 12F, 1 / 10F));
    }

    private static LootPool.Builder pool(List<LootPoolEntryContainer.Builder<?>> entries) {
        return LootPool.lootPool()
            .add(AlternativesEntry.alternatives(entries.toArray(new LootPoolEntryContainer.Builder<?>[0])))
            .when(ExplosionCondition.survivesExplosion());
    }

    private LootTable.Builder cheeseWheelDrops(Block block, Item wedge) {
        LootTable.Builder table = LootTable.lootTable();
        for (int bites = 0; bites < 4; bites++) {
            table.withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(wedge)
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4 - bites)))
                    .when(ageIs(block, PortionFoodBlock.BITES, bites))));
        }
        return table;
    }

    /** 未切过时掉落整块，切过后只掉落剩余的份数，否则切完再破坏能拿回整块。 */
    private LootTable.Builder portionFoodDrops(Block block, Item portion) {
        LootTable.Builder table = LootTable.lootTable().withPool(LootPool.lootPool()
            .add(LootItem.lootTableItem(block).when(ageIs(block, PortionFoodBlock.BITES, 0)))
            .when(ExplosionCondition.survivesExplosion()));
        for (int bites = 1; bites < 4; bites++) {
            table.withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(portion)
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4 - bites)))
                    .when(ageIs(block, PortionFoodBlock.BITES, bites))));
        }
        return table;
    }

    /** 派由四个方块组成，每个方块就是一份切片，被破坏时掉落这一份。 */
    private LootTable.Builder pieSliceDrops(String pieName) {
        return LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(FoodContent.item(pieName + "_slice").get()))
                .when(ExplosionCondition.survivesExplosion()));
    }

    /** 藤架连同种在上面的藤种一起返还。藤蔓爬上去的那几节 height 大于 0，不重复给种子。 */
    private LootTable.Builder vineStakeDrops(Block stake) {
        LootTable.Builder table = createSingleItemTable(stake);
        for (Vines.Entry vine : Vines.entries()) {
            table.withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(vine.seed().get()))
                .when(stageIs(stake, VineStakeBlock.SPECIES, vine.species()))
                .when(ageIs(stake, VineStakeBlock.HEIGHT, 0)));
        }
        return table;
    }

    private LootTable.Builder stackableFoodDrops(StackableFoodContent.Entry food) {
        Block block = food.block().get();
        IntegerProperty property = block instanceof StackableFoodDisplayBlock
            ? StackableFoodDisplayBlock.BLOCKSTATE : StackableFoodBlock.BLOCKSTATE;
        LootTable.Builder table = LootTable.lootTable();
        for (int state = 0; state <= food.maximumState(); state++) {
            table.withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(food.food().get())
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(state + 1)))
                    .when(ageIs(block, property, state)))
                .when(ExplosionCondition.survivesExplosion()));
        }
        return table;
    }

    private static LootItemCondition.Builder ageIs(Block block, IntegerProperty property, int age) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, age));
    }

    private static LootItemCondition.Builder ageBelow(Block block, int age) {
        LootItemCondition.Builder immature = ageIs(block, CropBlock.AGE, 0);
        for (int value = 1; value < age; value++) {
            immature = immature.or(ageIs(block, CropBlock.AGE, value));
        }
        return immature;
    }

    private static LootItemCondition.Builder stateIs(Block block, BooleanProperty property, boolean value) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, value));
    }

    private static <T extends Enum<T> & net.minecraft.util.StringRepresentable> LootItemCondition.Builder stageIs(
        Block block, net.minecraft.world.level.block.state.properties.EnumProperty<T> property, T value) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, value));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream()
            .map(RegistryObject::get).toList();
    }
}

