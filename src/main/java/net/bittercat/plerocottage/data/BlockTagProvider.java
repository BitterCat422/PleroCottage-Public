package net.bittercat.plerocottage.data;

import net.bittercat.plerocottage.init.BlockRegistry;

import java.util.concurrent.CompletableFuture;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.plants.Trees;
import net.bittercat.plerocottage.content.plants.BlackDirts;
import net.bittercat.plerocottage.content.plants.Bananas;
import net.bittercat.plerocottage.content.plants.Bushes;
import net.bittercat.plerocottage.content.plants.Coconuts;
import net.bittercat.plerocottage.content.plants.PurpleSugarCanes;
import net.bittercat.plerocottage.content.plants.Crops;
import net.bittercat.plerocottage.content.plants.Flowers;
import net.bittercat.plerocottage.content.plants.Melons;
import net.bittercat.plerocottage.content.plants.Vines;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
import net.bittercat.plerocottage.content.building.BuildingBlockContent;
import net.bittercat.plerocottage.content.building.ColoredDecorationContent;
import net.bittercat.plerocottage.content.blocks.MarbleContent;
import net.bittercat.plerocottage.content.blocks.OreBlockContent;
import net.bittercat.plerocottage.content.furniture.FurnitureContent;
import net.bittercat.plerocottage.content.food.PlateContent;
import net.bittercat.plerocottage.wood.WoodSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class BlockTagProvider extends BlockTagsProvider {
    private static final TagKey<Block> WILD_VINES =
        BlockTags.create(new ResourceLocation(PleroCottage.MODID, "wild_vines"));
    private static final TagKey<Block> VINE_FRUITS =
        BlockTags.create(new ResourceLocation(PleroCottage.MODID, "vine_fruits"));

    public static final TagKey<Block> VB_ORE_REPLACEABLES =
        BlockTags.create(new ResourceLocation(PleroCottage.MODID, "vb_ore_replaceables"));

    public static final TagKey<Block> RARE_FRUIT_LEAVES =
        BlockTags.create(new ResourceLocation(PleroCottage.MODID, "rare_fruit_leaves"));

    public static final TagKey<Block> NETHER_PLANTABLE =
        BlockTags.create(new ResourceLocation(PleroCottage.MODID, "nether_plantable_blocks"));

    private static final TagKey<Block> FARMLAND =
        BlockTags.create(new ResourceLocation("forge", "farmland"));

    public static final TagKey<Block> BLACK_FARMLAND =
        BlockTags.create(new ResourceLocation(PleroCottage.MODID, "black_farmland"));

    private static final TagKey<Block> SMALL_FLOWERS =
        BlockTags.create(new ResourceLocation(PleroCottage.MODID, "small_flowers"));

    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PleroCottage.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
            BlockRegistry.MACHINE_BLOCKS.stream()
                .map(net.minecraftforge.registries.RegistryObject::get)
                .filter(block -> block != BlockRegistry.CUTTING_BOARD.get())
                .toArray(Block[]::new));
        tag(BlockTags.MINEABLE_WITH_AXE).add(
            BlockRegistry.CUTTING_BOARD.get());

        tag(VB_ORE_REPLACEABLES).addTag(BlockTags.STONE_ORE_REPLACEABLES)
            .addTag(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
            OreBlockContent.DEBRISHROOM.get(), OreBlockContent.PEARL_ROCK_ORE.get(),
            OreBlockContent.DEEPSLATE_PEARL_ROCK_ORE.get(), OreBlockContent.SUSPICIOUS_ORE.get(),
            OreBlockContent.DEEPSLATE_SUSPICIOUS_ORE.get(),
            OreBlockContent.PEARL_ROCK_BLOCK.get(), OreBlockContent.GARNET_BLOCK.get(),
            OreBlockContent.TOPAZ_BLOCK.get(), OreBlockContent.PINK_CRYSTAL_BLOCK.get(),
            OreBlockContent.BRILLIANT_DIAMOND_BLOCK.get());
        marbleTags();
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(PlateContent.PLATE.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(Coconuts.CUP_BLOCK.get());
        for (var furniture : FurnitureContent.all()) {
            tag(BlockTags.MINEABLE_WITH_AXE).add(furniture.cabinet().get(), furniture.drawer().get());
        }
        for (var family : BuildingBlockContent.all().values()) {
            for (var entry : family.entrySet()) {
                if (entry.getKey().endsWith("wallpaper_block")
                    || entry.getKey().endsWith("terracotta")
                    || entry.getKey().endsWith("concrete")) {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(entry.getValue().get());
                } else if (entry.getKey().endsWith("concrete_powder")) {
                    tag(BlockTags.MINEABLE_WITH_SHOVEL).add(entry.getValue().get());
                }
            }
        }
        for (String color : ColoredDecorationContent.COLORS) {
            tag(BlockTags.BEDS).add(ColoredDecorationContent.bed(color).get());
            tag(BlockTags.CANDLES).add(ColoredDecorationContent.candle(color).get());
            tag(BlockTags.CANDLE_CAKES).add(ColoredDecorationContent.candleCake(color).get());
            tag(BlockTags.SHULKER_BOXES).add(ColoredDecorationContent.shulkerBox(color).get());
        }
        for (WoodSet wood : WoodSetContent.all()) {
            tag(BlockTags.LOGS).add(wood.log().get(), wood.strippedLog().get());
            tag(BlockTags.LOGS_THAT_BURN).add(wood.log().get(), wood.strippedLog().get());
            if (!wood.stalkStyle()) {
                tag(BlockTags.LOGS).add(wood.wood().get(), wood.strippedWood().get());
                tag(BlockTags.LOGS_THAT_BURN).add(wood.wood().get(), wood.strippedWood().get());
            }
            tag(BlockTags.PLANKS).add(wood.planks().get());
            tag(BlockTags.WOODEN_STAIRS).add(wood.stairs().get());
            tag(BlockTags.WOODEN_SLABS).add(wood.slab().get());
            tag(BlockTags.WOODEN_FENCES).add(wood.fence().get());
            tag(BlockTags.FENCE_GATES).add(wood.fenceGate().get());
            tag(BlockTags.WOODEN_DOORS).add(wood.door().get());
            tag(BlockTags.WOODEN_TRAPDOORS).add(wood.trapdoor().get());
            tag(BlockTags.WOODEN_PRESSURE_PLATES).add(wood.pressurePlate().get());
            tag(BlockTags.WOODEN_BUTTONS).add(wood.button().get());
            tag(BlockTags.STANDING_SIGNS).add(wood.sign().get());
            tag(BlockTags.WALL_SIGNS).add(wood.wallSign().get());
            tag(BlockTags.CEILING_HANGING_SIGNS).add(wood.hangingSign().get());
            tag(BlockTags.WALL_HANGING_SIGNS).add(wood.wallHangingSign().get());

            tag(BlockTags.MINEABLE_WITH_AXE).add(wood.log().get(), wood.strippedLog().get(),
                wood.planks().get(), wood.stairs().get(),
                wood.slab().get(), wood.fence().get(), wood.fenceGate().get(), wood.door().get(),
                wood.trapdoor().get(), wood.pressurePlate().get(), wood.button().get(), wood.sign().get(),
                wood.wallSign().get(), wood.hangingSign().get(), wood.wallHangingSign().get());
            if (!wood.stalkStyle()) {
                tag(BlockTags.MINEABLE_WITH_AXE).add(wood.wood().get(), wood.strippedWood().get());
            }
        }

        for (Trees.Entry tree : Trees.entries()) {
            tag(BlockTags.LEAVES).add(tree.leaves().get());
            tag(BlockTags.MINEABLE_WITH_HOE).add(tree.leaves().get());
            tag(BlockTags.SAPLINGS).add(tree.sapling().get());
            tag(BlockTags.MINEABLE_WITH_AXE).add(tree.sapling().get());
            if (tree.harvest().rareFruit() != null) {
                tag(RARE_FRUIT_LEAVES).add(tree.leaves().get());
            }
        }

        tag(BlockTags.MINEABLE_WITH_AXE).add(Bananas.CROWN.get(), Bananas.SAPLING.get(), Bananas.TREE_BUNCH.get(),
            Bananas.BUNCH.get());

        for (Trees.PlainEntry tree : Trees.plainTrees()) {
            tag(BlockTags.LEAVES).add(tree.leaves().get());
            tag(BlockTags.MINEABLE_WITH_HOE).add(tree.leaves().get());
            tag(BlockTags.SAPLINGS).add(tree.sapling().get());
            tag(BlockTags.MINEABLE_WITH_AXE).add(tree.sapling().get());
        }

        tag(BlockTags.DIRT).add(BlackDirts.BLACK_DIRT.get(), BlackDirts.SNOWY_BLACK_DIRT.get(),
            BlackDirts.RICH_BLACK_DIRT_FARMLAND.get());
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(BlackDirts.BLACK_DIRT.get(), BlackDirts.SNOWY_BLACK_DIRT.get(),
            BlackDirts.RICH_BLACK_DIRT_FARMLAND.get(), BlackDirts.BLACK_DIRT_TRUFFLE.get());
        tag(BlockTags.ANIMALS_SPAWNABLE_ON).add(BlackDirts.BLACK_DIRT.get(), BlackDirts.SNOWY_BLACK_DIRT.get());
        tag(BlockTags.RABBITS_SPAWNABLE_ON).add(BlackDirts.BLACK_DIRT.get(), BlackDirts.SNOWY_BLACK_DIRT.get());
        tag(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON).add(BlackDirts.BLACK_DIRT.get(),
            BlackDirts.SNOWY_BLACK_DIRT.get());
        tag(FARMLAND).add(Blocks.FARMLAND, BlackDirts.RICH_BLACK_DIRT_FARMLAND.get());
        tag(BLACK_FARMLAND).add(BlackDirts.RICH_BLACK_DIRT_FARMLAND.get());

        tag(BlockTags.MINEABLE_WITH_AXE).add(Coconuts.COCONUT.get(), Coconuts.YOUNG_COCONUT.get(),
            Coconuts.YOUNG_COCONUTS.get(), Coconuts.HALF_OF_COCONUT_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_AXE).add(Crops.PINEAPPLE_FRUIT.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(PurpleSugarCanes.CROP.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(Melons.HAMIMELON.get());
        tag(BlockTags.MINEABLE_WITH_HOE).add(Melons.HAMIMELON_STEM.get(), Melons.HAMIMELON_ATTACHED_STEM.get());

        tag(BlockTags.MINEABLE_WITH_AXE).add(Bushes.TEA_TREE.block().get());

        tag(BlockTags.MINEABLE_WITH_AXE).add(Vines.VINE_STAKE.get(), Vines.VINE_ARBOR.get());

        tag(BlockTags.CLIMBABLE).add(Vines.CUCUMBER_WILD.block().get(),
            Vines.GRAPE_WILD.block().get(), Vines.PASSION_FRUIT_WILD.block().get());
        tag(WILD_VINES).add(Vines.CUCUMBER_WILD.block().get(),
            Vines.GRAPE_WILD.block().get(), Vines.PASSION_FRUIT_WILD.block().get());
        for (Vines.Entry vine : Vines.entries()) {
            tag(VINE_FRUITS).add(vine.cropBlock().get());
        }

        tag(BlockTags.SMALL_FLOWERS).add(Trees.elderberryFlower().get());
        tag(SMALL_FLOWERS).add(Crops.WILD_PEANUT.get());
        for (var flower : Flowers.blocks().values()) {
            Block block = flower.get();
            if (Flowers.petalNames().contains(flower.getId().getPath())) {
                tag(BlockTags.FLOWERS).add(block);
                tag(BlockTags.MINEABLE_WITH_HOE).add(block);
                tag(BlockTags.SWORD_EFFICIENT).add(block);
                tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(block);
            } else {
                tag(BlockTags.SMALL_FLOWERS).add(block);
                tag(SMALL_FLOWERS).add(block);
            }
        }

        tag(NETHER_PLANTABLE)
            .add(Blocks.SOUL_SOIL, Blocks.SOUL_SAND, Blocks.WARPED_NYLIUM, Blocks.CRIMSON_NYLIUM)
            .addTag(BlockTags.BASE_STONE_NETHER);

    }

    private void marbleTags() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
            MarbleContent.MARBLE.get(), MarbleContent.MARBLE_SLAB.get(), MarbleContent.MARBLE_STAIRS.get(),
            MarbleContent.MARBLE_WALL.get(), MarbleContent.POLISHED_MARBLE.get(),
            MarbleContent.POLISHED_MARBLE_SLAB.get(), MarbleContent.POLISHED_MARBLE_STAIRS.get(),
            MarbleContent.POLISHED_MARBLE_WALL.get(), MarbleContent.POLISHED_MARBLE_BRICK.get(),
            MarbleContent.POLISHED_MARBLE_BRICK_SLAB.get(), MarbleContent.POLISHED_MARBLE_BRICK_STAIRS.get(),
            MarbleContent.POLISHED_MARBLE_BRICK_WALL.get(), MarbleContent.BOWL.get());
        tag(BlockTags.SLABS).add(MarbleContent.MARBLE_SLAB.get(),
            MarbleContent.POLISHED_MARBLE_SLAB.get(), MarbleContent.POLISHED_MARBLE_BRICK_SLAB.get());
        tag(BlockTags.STAIRS).add(MarbleContent.MARBLE_STAIRS.get(),
            MarbleContent.POLISHED_MARBLE_STAIRS.get(), MarbleContent.POLISHED_MARBLE_BRICK_STAIRS.get());
        tag(BlockTags.WALLS).add(MarbleContent.MARBLE_WALL.get(),
            MarbleContent.POLISHED_MARBLE_WALL.get(), MarbleContent.POLISHED_MARBLE_BRICK_WALL.get());
    }
}

