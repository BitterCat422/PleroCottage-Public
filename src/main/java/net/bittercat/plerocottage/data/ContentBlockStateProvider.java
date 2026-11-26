package net.bittercat.plerocottage.data;

import java.util.HashMap;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blocks.AbstractFruitLeavesBlock;
import net.bittercat.plerocottage.blocks.BananaBunchBlock;
import net.bittercat.plerocottage.blocks.BananaFrondBlock;
import net.bittercat.plerocottage.blocks.BerryBushBlock;
import net.bittercat.plerocottage.blocks.DoubleCropBlock;
import net.bittercat.plerocottage.blocks.ElderberryFlowerBlock;
import net.bittercat.plerocottage.blocks.YoungCoconutsBlock;
import net.bittercat.plerocottage.blocks.VineArborBlock;
import net.bittercat.plerocottage.blocks.VineCropBlock;
import net.bittercat.plerocottage.blocks.VineSpecies;
import net.bittercat.plerocottage.blocks.VineStakeBlock;
import net.bittercat.plerocottage.blocks.WildVineBlock;
import net.bittercat.plerocottage.content.plants.BlackDirts;
import net.bittercat.plerocottage.content.plants.DragonFruits;
import net.bittercat.plerocottage.content.plants.Melons;
import net.bittercat.plerocottage.content.plants.Corns;
import net.bittercat.plerocottage.content.plants.PottedPlantContent;
import net.bittercat.plerocottage.content.core.CoreContent;
import net.bittercat.plerocottage.content.fluids.FluidContent;
import net.bittercat.plerocottage.content.plants.Bananas;
import net.bittercat.plerocottage.content.plants.Bushes;
import net.bittercat.plerocottage.content.plants.Coconuts;
import net.bittercat.plerocottage.content.plants.Crops;
import net.bittercat.plerocottage.content.plants.PurpleSugarCanes;
import net.bittercat.plerocottage.content.plants.DoubleCrops;
import net.bittercat.plerocottage.content.plants.Trees;
import net.bittercat.plerocottage.content.plants.Vines;
import net.bittercat.plerocottage.content.plants.Flowers;
import net.bittercat.plerocottage.content.plants.WildRice;
import net.bittercat.plerocottage.content.plants.FrigidGrass;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
import net.bittercat.plerocottage.content.blocks.OreBlockContent;
import net.bittercat.plerocottage.content.blocks.MarbleContent;
import net.bittercat.plerocottage.content.blocks.GemExcavationContent;
import net.bittercat.plerocottage.content.building.BuildingBlockContent;
import net.bittercat.plerocottage.blocks.DragonFruitCactusBlock;
import net.bittercat.plerocottage.blocks.DragonFruitCactusFlowerBlock;
import net.bittercat.plerocottage.blocks.DragonFruitCropBlock;
import net.bittercat.plerocottage.blocks.HamimelonStemBlock;
import net.bittercat.plerocottage.blocks.HamimelonAttachedStemBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.content.furniture.FurnitureContent;
import net.bittercat.plerocottage.wood.FurnitureSet;
import net.bittercat.plerocottage.blocks.FurnitureBlock;
import net.minecraft.core.Direction;
import net.bittercat.plerocottage.content.food.DisplayFoodContent;
import net.bittercat.plerocottage.content.food.DisplayModelResources;
import net.bittercat.plerocottage.content.food.GlassFoodModels;
import net.bittercat.plerocottage.content.food.FoodBlockContent;
import net.bittercat.plerocottage.content.food.StackableFoodContent;
import net.bittercat.plerocottage.blocks.StackableFoodBlock;
import net.bittercat.plerocottage.blocks.StackableFoodDisplayBlock;
import net.bittercat.plerocottage.blocks.FoodShapeBlock;
import net.bittercat.plerocottage.blocks.StaticFoodDisplayBlock;
import net.bittercat.plerocottage.blocks.PortionFoodBlock;
import net.bittercat.plerocottage.content.building.ColoredDecorationContent;
import net.bittercat.plerocottage.blocks.ColoredBedBlock;
import net.bittercat.plerocottage.blocks.ColoredShulkerBoxBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.bittercat.plerocottage.wood.WoodSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class ContentBlockStateProvider extends BlockStateProvider {
    public ContentBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PleroCottage.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (WoodSet wood : WoodSetContent.all()) {
            String name = wood.name();
            ResourceLocation planks = modLoc("block/" + name + "_planks");

            logBlock(wood.log().get());
            logBlock(wood.strippedLog().get());
            if (!wood.stalkStyle()) {
                axisBlock(wood.wood().get(), modLoc("block/" + name + "_log"), modLoc("block/" + name + "_log"));
                axisBlock(wood.strippedWood().get(),
                    modLoc("block/stripped_" + name + "_log"), modLoc("block/stripped_" + name + "_log"));
            }

            simpleBlockWithItem(wood.planks().get(), cubeAll(wood.planks().get()));
            stairsBlock(wood.stairs().get(), planks);
            slabBlock(wood.slab().get(), planks, planks);
            fenceBlock(wood.fence().get(), planks);
            fenceGateBlock(wood.fenceGate().get(), planks);
            
            doorBlockWithRenderType(wood.door().get(),
                modLoc("block/" + name + "_door_bottom"), modLoc("block/" + name + "_door_top"), "cutout_mipped");
            trapdoorBlockWithRenderType(wood.trapdoor().get(), modLoc("block/" + name + "_trapdoor"), true,
                "cutout_mipped");
            pressurePlateBlock(wood.pressurePlate().get(), planks);
            buttonBlock(wood.button().get(), planks);

            signBlock(wood.sign().get(), wood.wallSign().get(), planks);
            hangingSignBlock(wood, modLoc("block/stripped_" + name
                + (wood.stalkStyle() ? "_stalk" : "_log")));
            woodItemModels(wood, planks);
        }

        furnitureBlocks();
        plateBlocks();
        cupBlock();
        displayContainerBlocks();

        simpleBlockWithItem(OreBlockContent.PEARL_ROCK_BLOCK.get(),
            cubeAll(OreBlockContent.PEARL_ROCK_BLOCK.get()));
        simpleBlockWithItem(OreBlockContent.GARNET_BLOCK.get(),
            cubeAll(OreBlockContent.GARNET_BLOCK.get()));
        simpleBlockWithItem(OreBlockContent.TOPAZ_BLOCK.get(),
            cubeAll(OreBlockContent.TOPAZ_BLOCK.get()));
        simpleBlockWithItem(OreBlockContent.PINK_CRYSTAL_BLOCK.get(),
            cubeAll(OreBlockContent.PINK_CRYSTAL_BLOCK.get()));
        simpleBlockWithItem(OreBlockContent.BRILLIANT_DIAMOND_BLOCK.get(),
            cubeAll(OreBlockContent.BRILLIANT_DIAMOND_BLOCK.get()));
        oreBlocks();
        marbleBlocks();
        buildingBlocks();
        pottedPlants();
        flowerBlocks();
        dragonFruitBlocks();
        melonBlocks();
        cornBlocks();
        treeHarvestBlocks();
        dragonFruitSlices();
        ModelFile blender = models().getBuilder("blender").texture("particle", modLoc("block/blender"));
        getVariantBuilder(BlockRegistry.BLENDER.get()).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(blender)
            .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
            .build());
        ModelFile saucepanAndWhisk = models().getBuilder("saucepan_and_whisk")
            .texture("particle", modLoc("block/saucepan_and_whisk"));
        horizontalBlock(BlockRegistry.SAUCEPAN_AND_WHISK.get(), saucepanAndWhisk, 180);
        fluidBlocks();

        for (Bushes.Entry bush : Bushes.entries()) {
            bushBlock(bush);
        }

        for (Crops.Crop crop : Crops.entries()) {
            cropBlock(crop);
        }

        for (DoubleCrops.Entry crop : DoubleCrops.entries()) {
            doubleCropBlock(crop);
        }

        for (Trees.Entry tree : Trees.entries()) {
            treeBlocks(tree);
        }

        for (Trees.PlainEntry tree : Trees.plainTrees()) {
            plainTreeBlocks(tree);
        }

        elderberryFlower();
        coconutBlocks();
        bananaTree();
        blackDirtBlocks();
        wildPeanut();
        pineappleFruit();
        vineStructures();
        for (Vines.Entry crop : Vines.entries()) {
            vineCropBlock(crop);
        }
        wildVines();
        for (String name : Flowers.generatedNames()) {
            simpleBlock(Flowers.block(name).get(),
                models().cross(name, modLoc("block/" + name)).renderType("cutout"));
            if (name.equals("hyacinth") || name.equals("coffea_arabica")
                || name.equals("jasmine") || name.equals("orange_blossom")) {
                itemModels().getBuilder(name)
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc((name.equals("hyacinth") ? "block/" : "item/") + name));
            } else {
                itemModels().withExistingParent(name, modLoc("block/" + name));
            }
        }
        for (String name : Flowers.petalNames()) {
            petalFlowerbed(name);
        }
        wildRice();
        wildMint();
        simpleBlock(FrigidGrass.BLOCK.get(),
            models().cross("mugwort", modLoc("block/mugwort")).renderType("cutout"));
        displayFoodBlocks();
        stackableFoodBlocks();
        purpleSugarCaneBlock();
        foodBlocks();
        coloredDecorationBlocks();
        // 花草资源使用原工作区的手工 blockstate/model；不能在数据生成时用统一模板覆盖。
    }

    private void petalFlowerbed(String name) {
        var multipart = getMultipartBuilder(Flowers.block(name).get());
        for (int amount = PinkPetalsBlock.MIN_FLOWERS; amount <= PinkPetalsBlock.MAX_FLOWERS; amount++) {
            ModelFile model = models().getBuilder(name + "_" + amount)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("block/flowerbed_" + amount)))
                .texture("flowerbed", modLoc("block/" + name))
                .texture("stem", modLoc("block/" + Flowers.petalStemTexture(name)))
                .renderType("cutout");
            Integer[] amounts = new Integer[PinkPetalsBlock.MAX_FLOWERS - amount + 1];
            for (int index = 0; index < amounts.length; index++) {
                amounts[index] = amount + index;
            }
            Direction facing = Direction.NORTH;
            for (int rotation = 0; rotation < 360; rotation += 90) {
                multipart.part().modelFile(model).rotationY(rotation).addModel()
                    .condition(PinkPetalsBlock.FACING, facing)
                    .condition(PinkPetalsBlock.AMOUNT, amounts).end();
                facing = facing.getClockWise();
            }
        }
        itemModels().getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + name));
    }

    private void coloredDecorationBlocks() {
        for (String color : ColoredDecorationContent.COLORS) {
            CandleBlock candle = (CandleBlock) ColoredDecorationContent.candle(color).get();
            for (int count = 1; count <= CandleBlock.MAX_CANDLES; count++) {
                for (boolean lit : new boolean[] {false, true}) {
                    String suffix = switch (count) {
                        case 1 -> "one";
                        case 2 -> "two";
                        case 3 -> "three";
                        default -> "four";
                    };
                    ResourceLocation texture = modLoc("block/" + color + "_candle" + (lit ? "_lit" : ""));
                    String modelSuffix = count == 1 ? "_candle" : "_candles";
                    ModelFile model = models().withExistingParent(color + "_candle_" + count + (lit ? "_lit" : ""),
                            mcLoc("block/candle_" + suffix + modelSuffix + (lit ? "_lit" : "")))
                        .texture("all", texture).texture("particle", texture);
                    for (boolean waterlogged : new boolean[] {false, true}) {
                        getVariantBuilder(candle).partialState()
                            .with(CandleBlock.CANDLES, count).with(CandleBlock.LIT, lit)
                            .with(CandleBlock.WATERLOGGED, waterlogged)
                            .modelForState().modelFile(model).addModel();
                    }
                }
            }
            CandleCakeBlock candleCake = (CandleCakeBlock) ColoredDecorationContent.candleCake(color).get();
            for (boolean lit : new boolean[] {false, true}) {
                ResourceLocation candleTexture = modLoc("block/" + color + "_candle" + (lit ? "_lit" : ""));
                ModelFile cakeModel = models().withExistingParent(color + "_candle_cake" + (lit ? "_lit" : ""),
                        mcLoc("block/template_cake_with_candle"))
                    .texture("bottom", mcLoc("block/cake_bottom"))
                    .texture("candle", candleTexture)
                    .texture("particle", mcLoc("block/cake_side"))
                    .texture("side", mcLoc("block/cake_side"))
                    .texture("top", mcLoc("block/cake_top"));
                getVariantBuilder(candleCake).partialState().with(CandleCakeBlock.LIT, lit)
                    .modelForState().modelFile(cakeModel).addModel();
            }
            ColoredBedBlock bed = (ColoredBedBlock) ColoredDecorationContent.bed(color).get();
            ModelFile bedModel = models().withExistingParent(color + "_bed", mcLoc("block/bed"))
                .texture("particle", modLoc("block/" + color + "_wool"));
            getVariantBuilder(bed).forAllStates(state -> ConfiguredModel.builder().modelFile(bedModel).build());
            ColoredShulkerBoxBlock shulker =
                (ColoredShulkerBoxBlock) ColoredDecorationContent.shulkerBox(color).get();
            ModelFile shulkerModel = models().withExistingParent(color + "_shulker_box", mcLoc("block/shulker_box"))
                .texture("particle", modLoc("block/" + color + "_shulker_box"));
            getVariantBuilder(shulker)
                .forAllStates(state -> ConfiguredModel.builder().modelFile(shulkerModel).build());
        }
    }

    private void oreBlocks() {
        simpleBlockWithItem(OreBlockContent.DEBRISHROOM.get(),
            models().cubeAll("debrishroom", modLoc("block/debrishroom")));
        simpleBlockWithItem(OreBlockContent.PEARL_ROCK_ORE.get(),
            models().cubeAll("pearl_rock_ore", modLoc("block/pearl_rock_ore")));
        simpleBlockWithItem(OreBlockContent.DEEPSLATE_PEARL_ROCK_ORE.get(),
            models().cubeAll("deepslate_pearl_rock_ore", modLoc("block/pearl_rock_ore_deep")));
        simpleBlockWithItem(OreBlockContent.SUSPICIOUS_ORE.get(),
            models().cubeAll("suspicious_ore", modLoc("block/suspicious_ore_1")));
        simpleBlockWithItem(OreBlockContent.DEEPSLATE_SUSPICIOUS_ORE.get(),
            models().cubeAll("deepslate_suspicious_ore", modLoc("block/deepslate_suspicious_ore_1")));

        simpleBlock(GemExcavationContent.ORE_DISPLAY.get(),
            models().cubeAll("gem_excavation_ore_display", modLoc("block/gem_excavation_ore_display"))
                .renderType("translucent"));

        ModelFile eggDisplay = models().getBuilder("egg_display")
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/egg_display")));
        simpleBlock(net.bittercat.plerocottage.content.entities.PoultryContent.EGG_DISPLAY_BLOCK.get(), eggDisplay);
        ModelFile mallardEgg = models().getBuilder("mallard_duck_egg_block")
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/mallard_duck_egg_block")));
        ModelFile turkeyEgg = models().getBuilder("turkey_egg_block")
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/turkey_egg_block")));
        simpleBlock(net.bittercat.plerocottage.content.entities.PoultryContent.MALLARD_EGG_BLOCK.get(), mallardEgg);
        simpleBlock(net.bittercat.plerocottage.content.entities.PoultryContent.TURKEY_EGG_BLOCK.get(), turkeyEgg);
        itemModels().withExistingParent("mallard_duck_egg_block", modLoc("block/mallard_duck_egg_block"));
        itemModels().withExistingParent("turkey_egg_block", modLoc("block/turkey_egg_block"));
    }

    private void marbleBlocks() {
        ResourceLocation marble = modLoc("block/marble");
        simpleBlockWithItem(MarbleContent.MARBLE.get(), models().cubeAll("marble", marble));
        stairsBlock((net.minecraft.world.level.block.StairBlock) MarbleContent.MARBLE_STAIRS.get(), marble);
        slabBlock((net.minecraft.world.level.block.SlabBlock) MarbleContent.MARBLE_SLAB.get(), marble, marble);
        wallBlock((net.minecraft.world.level.block.WallBlock) MarbleContent.MARBLE_WALL.get(), marble);

        ResourceLocation polished = modLoc("block/polished_marble");
        simpleBlockWithItem(MarbleContent.POLISHED_MARBLE.get(), models().cubeAll("polished_marble", polished));
        stairsBlock((net.minecraft.world.level.block.StairBlock) MarbleContent.POLISHED_MARBLE_STAIRS.get(), polished);
        slabBlock((net.minecraft.world.level.block.SlabBlock) MarbleContent.POLISHED_MARBLE_SLAB.get(),
            polished, polished);
        wallBlock((net.minecraft.world.level.block.WallBlock) MarbleContent.POLISHED_MARBLE_WALL.get(), polished);

        ResourceLocation brick = modLoc("block/marble_brick");
        ModelFile brickModel = models().getBuilder("polished_marble_brick")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("block/cube_all")))
            .texture("all", modLoc("block/marble_brick"))
            .texture("particle", modLoc("block/marble_brick"));
        simpleBlockWithItem(MarbleContent.POLISHED_MARBLE_BRICK.get(), brickModel);
        stairsBlock((net.minecraft.world.level.block.StairBlock) MarbleContent.POLISHED_MARBLE_BRICK_STAIRS.get(),
            brick);
        slabBlock((net.minecraft.world.level.block.SlabBlock) MarbleContent.POLISHED_MARBLE_BRICK_SLAB.get(),
            modLoc("block/polished_marble_brick"), brick);
        wallBlock((net.minecraft.world.level.block.WallBlock) MarbleContent.POLISHED_MARBLE_BRICK_WALL.get(), brick);
        for (String name : List.of("marble_slab", "marble_stairs", "polished_marble_slab",
            "polished_marble_stairs", "polished_marble_brick_slab", "polished_marble_brick_stairs")) {
            itemModels().withExistingParent(name, modLoc("block/" + name));
        }
        simpleBlockItem(MarbleContent.MARBLE_WALL.get(),
            models().wallInventory("marble_wall_inventory", marble).texture("particle", marble).renderType("solid"));
        simpleBlockItem(MarbleContent.POLISHED_MARBLE_WALL.get(),
            models().wallInventory("polished_marble_wall_inventory", polished)
                .texture("particle", polished).renderType("solid"));
        simpleBlockItem(MarbleContent.POLISHED_MARBLE_BRICK_WALL.get(),
            models().wallInventory("polished_marble_brick_wall_inventory", brick)
                .texture("particle", brick).renderType("solid"));

        simpleBlock(MarbleContent.BOWL.get(), models().getBuilder("bowl_particle")
            .texture("particle", modLoc("block/bowl")));
        itemModels().getBuilder("bowl")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/bowl"));
    }

    private void buildingBlocks() {
        for (Map<String, net.minecraftforge.registries.RegistryObject<net.minecraft.world.level.block.Block>> group
            : BuildingBlockContent.all().values()) {
            for (Map.Entry<String, net.minecraftforge.registries.RegistryObject<net.minecraft.world.level.block.Block>>
                entry : group.entrySet()) {
                String name = entry.getKey();
                if (name.equals("panelled_wallpaper_block") || name.endsWith("_panelled_wallpaper_block")) {
                    ResourceLocation panelledTexture = modLoc("block/" + name);
                    ResourceLocation wallpaperTexture = modLoc("block/"
                        + name.replace("panelled_wallpaper_block", "wallpaper_block"));
                    simpleBlockWithItem(entry.getValue().get(),
                        models().cubeBottomTop(name, panelledTexture, wallpaperTexture, wallpaperTexture));
                } else if (name.equals("wallpaper_block") || name.endsWith("_wallpaper_block")) {
                    simpleBlockWithItem(entry.getValue().get(), models().cubeAll(name, modLoc("block/" + name)));
                }
            }
        }
        for (String color : List.of("snow", "lavender", "turquoise")) {
            String paneName = color + (color.equals("turquoise") ? "_glasspane" : "_glass_pane");
            simpleBlockWithItem(BuildingBlockContent.block(color + "_terracotta").get(),
                models().cubeAll(color + "_terracotta", modLoc("block/" + color + "_terracotta")));
            var glazedTerracotta = BuildingBlockContent.block(color + "_glazed_terracotta").get();
            ResourceLocation glazedTexture = blockTexture(glazedTerracotta);
            ModelFile glazedModel = models().cubeAll(color + "_glazed_terracotta", glazedTexture)
                .texture("particle", glazedTexture).renderType("solid");
            getVariantBuilder(glazedTerracotta).forAllStates(state -> {
                AttachFace face = state.getValue(BlockStateProperties.ATTACH_FACE);
                int y = (int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
                int x = switch (face) {
                    case FLOOR -> 0;
                    case WALL -> 90;
                    case CEILING -> 180;
                };
                return ConfiguredModel.builder().modelFile(glazedModel).rotationX(x)
                    .rotationY((y + (face == AttachFace.CEILING ? 0 : 180)) % 360).build();
            });
            simpleBlockItem(glazedTerracotta, glazedModel);
            var concrete = BuildingBlockContent.block(color + "_concrete").get();
            ResourceLocation concreteTexture = blockTexture(concrete);
            ModelFile concreteModel = models().cubeAll(color + "_concrete", concreteTexture)
                .texture("particle", concreteTexture)
                .renderType("solid");
            horizontalBlock(concrete, concreteModel, 180);
            simpleBlockItem(concrete, concreteModel);
            simpleBlockWithItem(BuildingBlockContent.block(color + "_concrete_powder").get(),
                models().cubeAll(color + "_concrete_powder", modLoc("block/" + color + "_concrete_powder")));
            simpleBlockWithItem(BuildingBlockContent.block(color + "_glass").get(),
                models().cubeAll(color + "_glass", modLoc("block/" + color + "_glass")).renderType("translucent"));
            ResourceLocation pane = modLoc("block/" + color + "_glass");
            ResourceLocation edge = modLoc(color.equals("turquoise") ? "block/turquoise_glass_pine_top"
                : "block/" + paneName + "_top");
            paneBlock((IronBarsBlock) BuildingBlockContent.block(paneName).get(),
                models().panePost(paneName + "_post", pane, edge).texture("particle", pane).renderType("translucent"),
                models().paneSide(paneName + "_side", pane, edge).texture("particle", pane).renderType("translucent"),
                models().paneSideAlt(paneName + "_side_alt", pane, edge).texture("particle", pane)
                    .renderType("translucent"),
                models().paneNoSide(paneName + "_noside", pane).texture("particle", pane).renderType("translucent"),
                models().paneNoSideAlt(paneName + "_noside_alt", pane).texture("particle", pane)
                    .renderType("translucent"));
            itemModels().singleTexture(paneName, mcLoc("item/generated"), "layer0", pane).renderType("translucent");
            simpleBlockWithItem(BuildingBlockContent.block(color + "_wool").get(),
                models().cubeAll(color + "_wool", modLoc("block/" + color + "_wool")));
            simpleBlockWithItem(BuildingBlockContent.block(color + "_carpet").get(),
                models().withExistingParent(color + "_carpet", mcLoc("block/carpet"))
                    .texture("wool", modLoc("block/" + color + "_wool")));
        }
    }

    private void pottedPlants() {
        for (PottedPlantContent.Entry entry : PottedPlantContent.entries()) {
            String name = entry.name();
            String geometry = switch (name) {
                case "potted_banana_flower", "potted_banana_frond_sapling",
                    "potted_dragon_fruit_cactus", "potted_dragon_fruit_cactus_flower",
                    "potted_lavender", "potted_licorice", "potted_purple_sugar_cane",
                    "potted_tea_tree" -> name;
                case "potted_chamomiles", "potted_dianthuses", "potted_lucky_clovers",
                    "potted_unlucky_clovers", "potted_wild_roses" -> "potted_clover";
                default -> null;
            };
            var model = geometry == null
                ? models().withExistingParent(name, mcLoc("block/flower_pot_cross"))
                    .texture("plant", modLoc("block/" + entry.texture()))
                    .texture("particle", modLoc("block/" + entry.texture())).renderType("cutout")
                : models().withExistingParent(name, modLoc("custom/" + geometry))
                    .texture("particle", modLoc("block/" + entry.texture())).renderType("cutout");
            if ("potted_clover".equals(geometry)) {
                model.texture("0", modLoc("block/" + entry.texture()))
                    .texture("flower_pot", mcLoc("block/flower_pot"))
                    .texture("dirt", mcLoc("block/dirt"));
            } else if (name.equals("potted_lavender")) {
                model.texture("all", modLoc("block/lavender"))
                    .texture("0", modLoc("block/lavender_big"))
                    .texture("1", modLoc("block/lavender_small"))
                    .texture("flowerpot", mcLoc("block/flower_pot")).texture("dirt", mcLoc("block/dirt"));
            } else if (name.equals("potted_licorice")) {
                model.texture("all", modLoc("block/licorice"))
                    .texture("0", modLoc("block/licorice_flower"))
                    .texture("1", modLoc("block/licorice_leaves"))
                    .texture("flower_pot", mcLoc("block/flower_pot")).texture("dirt", mcLoc("block/dirt"));
            } else {
                model.texture("all", modLoc("block/" + entry.texture()))
                    .texture("0", modLoc("block/" + entry.texture()))
                    .texture("flower_pot", mcLoc("block/flower_pot"))
                    .texture("dirt", mcLoc("block/dirt"));
            }
            simpleBlock(entry.block().get(), model);
        }
    }

    private void flowerBlocks() {
        for (Map.Entry<String, net.minecraftforge.registries.RegistryObject<net.minecraft.world.level.block.Block>>
            entry : Flowers.blocks().entrySet()) {
            String name = entry.getKey();
            if (Flowers.petalNames().contains(name) || Flowers.generatedNames().contains(name)) {
                continue;
            }
            if (name.equals("lavender")) {
                ModelFile[] stages = new ModelFile[9];
                String[] textures = {"lavender", "lavender_cool", "lavender_warm"};
                for (int color = 0; color < 3; color++) {
                    for (int stage = 0; stage < 3; stage++) {
                        int index = color * 3 + stage;
                        String modelName = index == 0 ? name : name + "_state_blockstate_" + (index - 1);
                        String geometry = stage == 0 ? "lavender" : "lavender_" + (stage + 1);
                        ResourceLocation texture = modLoc("block/" + textures[color]);
                        stages[index] = models().withExistingParent(modelName, modLoc("custom/" + geometry))
                            .texture("all", texture)
                            .texture("0", texture)
                            .texture("particle", texture).renderType("cutout");
                    }
                }
                getVariantBuilder(entry.getValue().get()).forAllStates(state -> {
                    int index = state.getValue(net.bittercat.plerocottage.blocks.LavenderBlock.COLOR) * 3
                        + state.getValue(net.bittercat.plerocottage.blocks.LavenderBlock.STAGE);
                    return ConfiguredModel.builder().modelFile(stages[index])
                        .rotationY(((int) state.getValue(net.bittercat.plerocottage.blocks.LavenderBlock.FACING)
                            .toYRot() + 180) % 360).build();
                });
                itemModels().getBuilder(name)
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/" + name));
            } else if (name.equals("licorice")) {
                ModelFile model = models().withExistingParent(name, modLoc("custom/licorice"))
                    .texture("0", modLoc("block/licorice"))
                    .texture("particle", modLoc("block/licorice")).renderType("cutout");
                simpleBlock(entry.getValue().get(), model);
                itemModels().getBuilder(name)
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/" + name));
            } else {
                ModelFile model = models().cross(name, modLoc("block/" + name)).renderType("cutout");
                simpleBlock(entry.getValue().get(), model);
                itemModels().getBuilder(name)
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/" + name));
            }
        }
    }

    private void dragonFruitBlocks() {
        ModelFile fruit = models().getBuilder("dragon_fruit")
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/dragon_fruit")))
            .texture("0", modLoc("block/dragon_fruit"))
            .texture("particle", modLoc("block/dragon_fruit")).renderType("cutout_mipped");
        simpleBlock(DragonFruits.FRUIT_BLOCK.get(), fruit);

        ModelFile cactus = models().getBuilder("dragon_fruit_cactus")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("block/cactus")))
            .texture("side", modLoc("block/dragon_fruit_cactus_side"))
            .texture("top", modLoc("block/dragon_fruit_cactus_top"))
            .texture("bottom", modLoc("block/dragon_fruit_cactus_bottom"))
            .texture("particle", modLoc("block/dragon_fruit_cactus_side"));
        getVariantBuilder(DragonFruits.CACTUS.get()).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(cactus).build());

        ResourceLocation flowerTexture = modLoc("block/dragon_fruit_cactus_flower");
        ModelFile cactusFlower = models().withExistingParent("dragon_fruit_cactus_flower",
                modLoc("custom/dragon_fruit_cactus_flower"))
            .texture("all", flowerTexture).texture("particle", flowerTexture).texture("0", flowerTexture)
            .renderType("cutout_mipped");
        simpleBlock(DragonFruits.CACTUS_FLOWER.get(), cactusFlower);
        itemModels().withExistingParent("dragon_fruit_cactus", modLoc("block/dragon_fruit_cactus"));
        itemModels().getBuilder("dragon_fruit_cactus_flower")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/dragon_fruit_cactus_flower")).renderType("translucent");

        ModelFile[] cropStages = new ModelFile[3];
        for (int age = 0; age < cropStages.length; age++) {
            cropStages[age] = models().withExistingParent("dragon_fruit_crop" + (age == 0 ? "" : "_blockstate_"
                    + (age - 1)), modLoc("custom/dragon_fruit_crop_" + (age + 1)))
                .texture("all", modLoc("block/dragon_fruit_crop_" + (age + 1)))
                .texture("0", modLoc("block/dragon_fruit_crop_" + (age + 1)))
                .texture("particle", modLoc("block/dragon_fruit_crop_" + (age + 1)))
                .renderType("cutout_mipped");
        }
        getVariantBuilder(DragonFruits.CROP.get()).forAllStates(state ->
            ConfiguredModel.builder().modelFile(cropStages[state.getValue(DragonFruitCropBlock.AGE)]).build());
        itemModels().getBuilder("dragon_fruit_crop")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/dragon_fruit")).renderType("translucent");
        itemModels().getBuilder("dragon_fruit")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/dragon_fruit"));
        itemModels().getBuilder("dragon_fruit_slices")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/dragon_fruit_slices"));
    }

    private void melonBlocks() {
        ResourceLocation side = modLoc("block/muskmelon_side");
        simpleBlock(Melons.HAMIMELON.get(), models().cube("muskmelon", side,
            modLoc("block/muskmelon_top"), side, side, side, side).texture("particle", side));
        ModelFile[] stemStages = new ModelFile[8];
        for (int age = 0; age < stemStages.length; age++) {
            int textureStage = age < 2 ? age : switch (age) {
                case 2 -> 3;
                case 3, 4 -> 4;
                default -> 5;
            };
            String modelName = "muskmelon_stem" + (age < 2 ? "" : "_state_blockstate_" + (textureStage - 1));
            stemStages[age] = models().cross(modelName, modLoc("block/muskmelon_crop_" + textureStage))
                .renderType("cutout_mipped");
        }
        getVariantBuilder(Melons.HAMIMELON_STEM.get()).forAllStates(state ->
            ConfiguredModel.builder().modelFile(stemStages[state.getValue(HamimelonStemBlock.AGE)]).build());
        ModelFile attached = models().withExistingParent("muskmelon_attached_stem",
            modLoc("custom/muskmelon_crop_6"))
            .texture("all", modLoc("block/muskmelon_crop_6"))
            .texture("0", modLoc("block/muskmelon_crop_6"))
            .texture("particle", modLoc("block/muskmelon_crop_6")).renderType("cutout");
        getVariantBuilder(Melons.HAMIMELON_ATTACHED_STEM.get()).forAllStates(state ->
            ConfiguredModel.builder().modelFile(attached)
                .rotationY(((int) state.getValue(HamimelonAttachedStemBlock.FACING).toYRot() + 180) % 360)
                .build());
        itemModels().getBuilder("muskmelon")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/muskmelon"));
    }

    private void cornBlocks() {
        ModelFile stem = models().cross("corn_crop_stem_0", modLoc("block/corn_crop_2")).renderType("cutout");
        ModelFile top = models().cross("corn_crop_top_0", modLoc("block/corn_crop_1")).renderType("cutout");
        ModelFile bottom = models().withExistingParent("corn_bottom", modLoc("custom/corn_bottom"))
            .texture("cross", modLoc("block/corn_crop_3")).texture("corn", modLoc("block/corn"))
            .texture("particle", modLoc("block/corn_crop_3")).renderType("cutout");
        ModelFile upper = models().withExistingParent("corn_top", modLoc("custom/corn_top"))
            .texture("cross", modLoc("block/corn_crop_4")).texture("corn", modLoc("block/corn"))
            .texture("particle", modLoc("block/corn_crop_4")).renderType("cutout");
        getVariantBuilder(Corns.CORN_CROP_BLOCK.get()).forAllStates(state -> {
            boolean isTop = state.getValue(net.bittercat.plerocottage.blocks.StackableCropBlock.TOP);
            int age = state.getValue(net.bittercat.plerocottage.blocks.StackableCropBlock.AGE);
            ModelFile model = age == 0 ? (isTop ? top : stem) : (isTop ? upper : bottom);
            return ConfiguredModel.builder().modelFile(model).build();
        });
        itemModels().getBuilder("corn_crop")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/corn"));
    }

    private void treeHarvestBlocks() {
        for (Trees.Entry tree : Trees.entries()) {
            if (tree.hangingFruit() == null) {
                continue;
            }
            String name = tree.hangingFruit().getId().getPath();
            if (name.equals("durian_fruit") || name.equals("pomelo_fruit")) {
                boolean durian = name.equals("durian_fruit");
                String[] geometries = durian
                    ? new String[] {"durian_leaves_c", "durian_leaves_d", "durian_leaves_e"}
                    : new String[] {"pomelo_crop_1", "pomelo_crop_2", "pomelo_crop_3"};
                String[] textures = durian
                    ? new String[] {"durian_leaves_c", "durian_leaves_d", "durian_leaves_fruit"}
                    : new String[] {"pomelo_block_1", "pomelo_block_2", "pomelo_block_3"};
                ModelFile[] stages = new ModelFile[3];
                for (int stage = 0; stage < stages.length; stage++) {
                    stages[stage] = models().withExistingParent(name + (stage == 0 ? "" :
                        stage == 1 ? "_growing" : "_ripe"), modLoc("custom/" + geometries[stage]))
                        .texture("0", modLoc("block/" + textures[stage]))
                        .texture("particle", modLoc("block/" + textures[0]))
                        .renderType(durian ? "cutout_mipped" : "solid");
                }
                getVariantBuilder(tree.hangingFruit().get()).forAllStates(state ->
                    ConfiguredModel.builder().modelFile(stages[state.getValue(
                        net.bittercat.plerocottage.blocks.HangingFruitBlock.STAGE).ordinal()]).build());
            }
        }
        simpleBlock(Trees.durian().get(), models().withExistingParent("durian", modLoc("custom/durian"))
            .texture("all", modLoc("block/durian_block")).texture("0", modLoc("block/durian_block"))
            .texture("particle", modLoc("block/durian_block")).renderType("cutout_mipped"));
        itemModels().getBuilder("durian")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/durian"));
        simpleBlock(Trees.pomelo().get(), models().withExistingParent("pomelo", modLoc("custom/pomelo"))
            .texture("all", modLoc("block/pomelo_block")).texture("0", modLoc("block/pomelo_block"))
            .texture("particle", modLoc("block/pomelo_block")));
        itemModels().getBuilder("pomelo")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/pomelo"));
    }

    private void dragonFruitSlices() {
        ModelFile[] modelsByState = new ModelFile[6];
        for (int state = 0; state < modelsByState.length; state++) {
            String modelName = "dragon_fruit_slices_blockstate_" + state;
            String texture = "dragon_fruit_slices_" + (state + 1);
            modelsByState[state] = models().withExistingParent(modelName,
                modLoc("custom/" + texture))
                .texture("0", modLoc("block/" + texture))
                .texture("particle", modLoc("block/" + texture)).renderType("cutout_mipped");
        }
        getVariantBuilder(DragonFruits.SLICE_BLOCK.get()).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(modelsByState[state.getValue(
                net.bittercat.plerocottage.blocks.DragonFruitSliceBlock.BLOCKSTATE)])
            .rotationY(((int) state.getValue(
                net.bittercat.plerocottage.blocks.DragonFruitSliceBlock.FACING).toYRot() + 180) % 360)
            .build());
        itemModels().getBuilder("dragon_fruit_slices")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/dragon_fruit_slices"));
    }

    private void wildRice() {
        ModelFile upper = models().cross("wild_rice", modLoc("block/wild_rice_top")).renderType("cutout");
        ModelFile lower = models().cross("wild_rice_bottom", modLoc("block/wild_rice_bottom"))
            .texture("particle", modLoc("block/wild_rice_top"))
            .renderType("cutout");
        getVariantBuilder(WildRice.BLOCK.get())
            .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).modelForState()
            .modelFile(upper).addModel()
            .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).modelForState()
            .modelFile(lower).addModel();
        itemModels().getBuilder("wild_rice")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/wild_rice"));
    }

    private void wildMint() {
        ModelFile upper = models().cross("wild_mint_upper", modLoc("block/wild_mint_top"))
            .renderType("cutout");
        ModelFile lower = models().cross("wild_mint_lower", modLoc("block/wild_mint_bottom"))
            .texture("particle", modLoc("block/wild_mint_top"))
            .renderType("cutout");
        getVariantBuilder(Crops.WILD_MINT.get())
            .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).modelForState()
            .modelFile(upper).addModel()
            .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).modelForState()
            .modelFile(lower).addModel();
        itemModels().getBuilder("wild_mint")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/wild_mint"));
    }

    private void furnitureBlocks() {
        for (FurnitureSet furniture : FurnitureContent.all()) {
            furnitureBlock(furniture.name() + "_cabinet", furniture.cabinet().get(), "cabinet");
            furnitureBlock(furniture.name() + "_drawer", furniture.drawer().get(), "drawer");
        }
    }

    private void plateBlocks() {
        plateBlock("plate", net.bittercat.plerocottage.content.food.PlateContent.PLATE.get());
    }

    private void cupBlock() {
        ModelFile cup = models().withExistingParent("reactive_glaze_mug_block",
            modLoc("custom/reactive_glaze_mug"))
            .texture("all", modLoc("block/reactive_glaze_mug_block"))
            .texture("0", modLoc("block/reactive_glaze_mug_block"))
            .texture("particle", modLoc("block/reactive_glaze_mug_block"))
            .renderType("solid");
        getVariantBuilder(Coconuts.CUP_BLOCK.get()).forAllStates(state ->
            ConfiguredModel.builder().modelFile(cup)
                .rotationY(((int) state.getValue(
                    net.bittercat.plerocottage.blocks.CupBlock.FACING).toYRot() + 180) % 360)
                .build());
        itemModels().getBuilder("cup")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/cup"));
    }

    private void displayContainerBlocks() {
        ModelFile hayNest = models().withExistingParent("hay_nest", modLoc("custom/hay_nest"))
            .texture("all", modLoc("block/hay_nest"))
            .texture("0", modLoc("block/hay_nest"))
            .texture("particle", modLoc("block/hay_nest"))
            .renderType("cutout");
        simpleBlock(CoreContent.HAY_NEST.get(), hayNest);
        itemModels().getBuilder("hay_nest")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/hay_nest"));

        ModelFile incubator = models().withExistingParent("egg_incubator", modLoc("custom/egg_incubator"))
            .texture("all", modLoc("block/egg_incubator"))
            .texture("0", modLoc("block/egg_incubator"))
            .texture("1", mcLoc("block/redstone_lamp_on"))
            .texture("particle", modLoc("block/egg_incubator"))
            .renderType("cutout");
        getVariantBuilder(CoreContent.EGG_INCUBATOR.get()).forAllStates(state ->
            ConfiguredModel.builder().modelFile(incubator).build());
        itemModels().getBuilder("egg_incubator")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/egg_incubator"));
    }

    private void plateBlock(String name, net.minecraft.world.level.block.Block block) {
        ResourceLocation texture = modLoc("block/" + name);
        ModelFile model = models().withExistingParent(name, modLoc("custom/" + name))
            .texture("1", texture).texture("particle", texture);
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(model)
            .rotationY(((int) state.getValue(
                net.bittercat.plerocottage.blocks.PlateBlock.FACING).toYRot() + 180) % 360)
            .build());
        itemModels().getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + name));
    }

    private void furnitureBlock(String name, net.minecraft.world.level.block.Block block, String modelName) {
        ResourceLocation texture = modLoc("block/" + name);
        String textureSlot = "drawer".equals(modelName) ? "11" : "2";
        ModelFile closed = models().withExistingParent(name, modLoc("custom/" + modelName))
            .texture(textureSlot, texture).texture("particle", texture);
        ModelFile open = models().withExistingParent(name + "_open", modLoc("custom/" + modelName + "_open"))
            .texture(textureSlot, texture).texture("particle", texture);
        getVariantBuilder(block).forAllStates(state -> {
            boolean isOpen = state.getValue(FurnitureBlock.OPEN);
            Direction facing = state.getValue(FurnitureBlock.FACING);
            return ConfiguredModel.builder()
                .modelFile(isOpen ? open : closed)
                .rotationY(((int) facing.toYRot() + 180) % 360)
                .build();
        });
        itemModels().withExistingParent(name, modLoc("block/" + name));
    }

    private void displayFoodBlocks() {
        for (DisplayFoodContent.DisplayModel display : DisplayFoodContent.displayModels()) {
            String blockName = BuiltInRegistries.BLOCK.getKey(display.block().get()).getPath();
            if (GlassFoodModels.contains(blockName)) {
                glassFoodDisplayBlock(display, blockName);
                continue;
            }
            var composition = DisplayModelResources.compositionForBlock(blockName);
            boolean composed = composition != null;
            var model = composed
                ? models().getBuilder(blockName).parent(new ModelFile.UncheckedModelFile(
                    new ResourceLocation("minecraft", "block/block")))
                : models().withExistingParent(blockName, modLoc("custom/" + display.modelName()));
            ResourceLocation blockTexture = modLoc("block/" + DisplayModelResources.blockTextureName(blockName));
            ResourceLocation particleTexture = blockTexture;
            if (composed) {
                ResourceLocation texture = composition.additions().get(0).texture();
                String path = texture.getPath();
                particleTexture = new ResourceLocation(texture.getNamespace(),
                    path.substring("textures/".length(), path.length() - ".png".length()));
            }
            model.texture("particle", particleTexture);
            if (!"SOLID".equals(display.transparencyType())) {
                model.renderType(display.transparencyType().toLowerCase(java.util.Locale.ROOT));
            }
            if (!composed) {
                for (int textureSlot = 0; textureSlot <= 5; textureSlot++) {
                    model.texture(Integer.toString(textureSlot), blockTexture);
                }
            }
            if (display.rotatable()) {
                getVariantBuilder(display.block().get()).forAllStates(state -> {
                    ConfiguredModel.Builder builder = ConfiguredModel.builder().modelFile(model);
                    if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                        builder.rotationY(((int) state.getValue(
                            BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360);
                    } else if (state.hasProperty(FoodShapeBlock.FACING)) {
                        builder.rotationY(((int) state.getValue(FoodShapeBlock.FACING).toYRot() + 180) % 360);
                    }
                    return builder.build();
                });
            } else {
                getVariantBuilder(display.block().get()).forAllStates(state -> ConfiguredModel.builder()
                    .modelFile(model)
                    .build());
            }
            if (!DisplayFoodContent.hasSpecialItemModel(blockName)) {
                itemModels().getBuilder(display.modelName())
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/" + display.modelName()));
            }
        }
    }

    private void glassFoodDisplayBlock(DisplayFoodContent.DisplayModel display, String blockName) {
        ModelFile content = models().withExistingParent(blockName + "_content",
                modLoc("custom/" + blockName + "_content"))
            .texture("0", modLoc("block/" + blockName))
            .texture("particle", modLoc("block/" + blockName))
            .renderType("cutout");
        String glassModel = GlassFoodModels.glassModel(blockName);
        ModelFile glass = models().withExistingParent(blockName + "_glass",
                modLoc("custom/glass_parts/" + glassModel))
            .texture("glasses", modLoc("block/glasses"))
            .texture("klein_bottle", modLoc("block/klein_bottle"))
            .texture("particle", modLoc("block/glasses"))
            .renderType("translucent");

        var multipart = getMultipartBuilder(display.block().get());
        if (!display.rotatable()) {
            multipart.part().modelFile(content).addModel();
            multipart.part().modelFile(glass).addModel();
        } else if (display.block().get().defaultBlockState()
            .hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                int rotation = ((int) direction.toYRot() + 180) % 360;
                multipart.part().modelFile(content).rotationY(rotation)
                    .addModel().condition(BlockStateProperties.HORIZONTAL_FACING, direction).end();
                multipart.part().modelFile(glass).rotationY(rotation)
                    .addModel().condition(BlockStateProperties.HORIZONTAL_FACING, direction).end();
            }
        } else if (display.block().get().defaultBlockState().hasProperty(FoodShapeBlock.FACING)) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                int rotation = ((int) direction.toYRot() + 180) % 360;
                multipart.part().modelFile(content).rotationY(rotation)
                    .addModel().condition(FoodShapeBlock.FACING, direction).end();
                multipart.part().modelFile(glass).rotationY(rotation)
                    .addModel().condition(FoodShapeBlock.FACING, direction).end();
            }
        } else {
            multipart.part().modelFile(content).addModel();
            multipart.part().modelFile(glass).addModel();
        }

    }

    private void purpleSugarCaneBlock() {
        ResourceLocation texture = modLoc("block/purple_sugar_canes");
        ModelFile tip = models().getBuilder("purple_sugar_canes")
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/purple_sugar_cane_top")))
            .texture("all", texture)
            .texture("0", modLoc("block/purple_sugar_cane_top_block"))
            .texture("particle", texture)
            .renderType("cutout");
        ModelFile stem = models().getBuilder("purple_sugar_canes_state_blockstate_0")
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/purple_sugar_canes")))
            .texture("all", texture)
            .texture("0", texture)
            .texture("particle", texture)
            .renderType("cutout");
        getVariantBuilder(PurpleSugarCanes.CROP.get()).forAllStates(state ->
            ConfiguredModel.builder()
                .modelFile(state.getValue(net.bittercat.plerocottage.blocks.PurpleSugarCaneCropBlock.TOP)
                    ? tip : stem)
                .build());
    }

    private void foodBlocks() {
        for (FoodBlockContent.FoodBlock food : FoodBlockContent.entries()) {
            String name = food.name();
            if (food.block().get() instanceof PortionFoodBlock) {
                ModelFile baseModel = models().cubeAll(name, modLoc("block/" + name));
                for (int state = 0; state < 3; state++) {
                    models().cubeAll(name + "_blockstate_" + state, modLoc("block/" + name));
                }
                getVariantBuilder(food.block().get()).forAllStates(state -> {
                    int bites = state.getValue(PortionFoodBlock.BITES);
                    ModelFile model = bites == 0
                        ? baseModel
                        : new ModelFile.UncheckedModelFile(modLoc("block/" + name + "_blockstate_" + (bites - 1)));
                    return ConfiguredModel.builder().modelFile(model)
                        .rotationY(((int) state.getValue(PortionFoodBlock.FACING).toYRot() + 180) % 360)
                        .build();
                });
                continue;
            }
            ModelFile model = models().cubeAll(name, modLoc("block/" + name));
            getVariantBuilder(food.block().get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(model)
                .rotationY(state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)
                    ? ((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360 : 0)
                .build());
            itemModels().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/" + name));
        }
    }

    private void stackableFoodBlocks() {
        for (StackableFoodContent.Entry entry : StackableFoodContent.entries()) {
            String name = entry.name();
            ResourceLocation texture = modLoc("block/" + name);
            if (entry.block().get() instanceof StackableFoodDisplayBlock) {
                String geometry = name.equals("blueberry_muffin") ? "berry_muffin" : name;
                ModelFile[] models = new ModelFile[StackableFoodDisplayBlock.BLOCKSTATE.getPossibleValues().size()];
                for (int stackState = 0; stackState < models.length; stackState++) {
                    String stackModel = "muffin" + (stackState == 0 ? "" : "_" + (stackState + 1));
                    String modelName = stackState == 0 ? name : name + "_blockstate_" + (stackState - 1);
                    models[stackState] = models().withExistingParent(modelName,
                        modLoc("custom/muffin/" + stackModel + "/" + geometry))
                        .texture("muffin", texture)
                        .texture("particle", texture);
                }
                getVariantBuilder(entry.block().get()).forAllStates(state -> ConfiguredModel.builder()
                    .modelFile(models[state.getValue(StackableFoodDisplayBlock.BLOCKSTATE)])
                    .rotationY(((int) state.getValue(StackableFoodDisplayBlock.FACING).toYRot() + 180) % 360)
                    .build());
                itemModels().getBuilder(name)
                    .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                    .texture("layer0", modLoc("item/" + entry.name()));
                continue;
            }

            ModelFile[] models = new ModelFile[3];
            models[0] = new ModelFile.UncheckedModelFile(modLoc("block/" + name));
            models[1] = new ModelFile.UncheckedModelFile(modLoc("block/" + name + "_blockstate_0"));
            models[2] = new ModelFile.UncheckedModelFile(modLoc("block/" + name + "_blockstate_1"));
            getVariantBuilder(entry.block().get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models[state.getValue(StackableFoodBlock.BLOCKSTATE)])
                .rotationY(((int) state.getValue(StackableFoodBlock.FACING).toYRot() + 180) % 360)
                .build());
            models().withExistingParent(name, modLoc("custom/" + entry.name()))
                .texture("all", texture).texture("particle", texture).texture("0", texture)
                .renderType("cutout");
            models().withExistingParent(name + "_blockstate_0", modLoc("custom/" + entry.name() + "_2"))
                .texture("all", texture).texture("particle", texture).texture("0", texture)
                .renderType("cutout");
            models().withExistingParent(name + "_blockstate_1", modLoc("custom/" + entry.name() + "_3"))
                .texture("all", texture).texture("particle", texture).texture("0", texture)
                .renderType("cutout");
            itemModels().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/" + entry.name()));
        }
    }

    private void fluidBlocks() {
        for (String name : new String[] {
            "vanilla_ice_cream_fluid", "blueberry_ice_cream_fluid", "cherries_ice_cream_fluid",
            "starfruit_ice_cream_fluid", "chocolate_ice_cream_fluid", "jalapeno_ice_cream_fluid",
            "olive_oil", "cake_liquid"
        }) {
            ModelFile model = models().getBuilder(name)
                .texture("particle", modLoc("block/" + name + "_still"));
            simpleBlock(FluidContent.block(name).get(), model);
        }
    }

    private void woodItemModels(WoodSet wood, ResourceLocation planks) {
        String name = wood.name();

        models().fenceInventory(name + "_fence_inventory", planks);
        models().buttonInventory(name + "_button_inventory", planks);

        String pillar = wood.stalkStyle() ? "_stalk" : "_log";
        itemModels().withExistingParent(name + pillar, modLoc("block/" + name + pillar));
        itemModels().withExistingParent("stripped_" + name + pillar,
            modLoc("block/stripped_" + name + pillar));
        if (!wood.stalkStyle()) {
            itemModels().withExistingParent(name + "_wood", modLoc("block/" + name + "_wood"));
            itemModels().withExistingParent("stripped_" + name + "_wood",
                modLoc("block/stripped_" + name + "_wood"));
        }
        itemModels().withExistingParent(name + "_stairs", modLoc("block/" + name + "_stairs"));
        itemModels().withExistingParent(name + "_slab", modLoc("block/" + name + "_slab"));
        itemModels().withExistingParent(name + "_fence", modLoc("block/" + name + "_fence_inventory"));
        itemModels().withExistingParent(name + "_fence_gate", modLoc("block/" + name + "_fence_gate"));
        itemModels().withExistingParent(name + "_trapdoor", modLoc("block/" + name + "_trapdoor_bottom"));
        itemModels().withExistingParent(name + "_pressure_plate", modLoc("block/" + name + "_pressure_plate"));
        itemModels().withExistingParent(name + "_button", modLoc("block/" + name + "_button_inventory"));

        itemModels().getBuilder(name + "_door")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + name + "_door"));
    }

    private void treeBlocks(Trees.Entry tree) {
        AbstractFruitLeavesBlock<?> leaves = tree.leaves().get();
        String leafName = tree.leaves().getId().getPath();

        Map<String, ModelFile> modelsByStage = new HashMap<>();
        for (BlockState stageState : leaves.stageStates()) {
            String stage = leaves.stageName(stageState);
            ResourceLocation texture = tree.leafTextures().get(stage);
            if (texture == null) {
                throw new IllegalStateException("树 " + tree.name() + " 缺少树叶阶段 " + stage + " 的纹理");
            }
            modelsByStage.put(stage, models().withExistingParent(leafName + "_" + stage, mcLoc("block/leaves"))
                .texture("all", texture)
                .renderType("cutout_mipped"));
        }

        getVariantBuilder(leaves).forAllStatesExcept(
            state -> ConfiguredModel.builder()
                .modelFile(modelsByStage.get(leaves.stageName(state)))
                .build(),
            LeavesBlock.DISTANCE, LeavesBlock.PERSISTENT, LeavesBlock.WATERLOGGED);

        String saplingName = tree.sapling().getId().getPath();
        simpleBlock(tree.sapling().get(), models().cross(saplingName, tree.saplingTexture()).renderType("cutout"));
        
        itemModels().getBuilder(tree.saplingSeed().getId().getPath())
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", tree.saplingItemTexture());

        itemModels().getBuilder(leafName)
            .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + leafName + "_" +
                leaves.plainStageName())));
    }

    private void plainTreeBlocks(Trees.PlainEntry tree) {
        String leafName = tree.leaves().getId().getPath();
        simpleBlock(tree.leaves().get(), models().withExistingParent(leafName, mcLoc("block/leaves"))
            .texture("all", tree.leavesTexture())
            .renderType("cutout_mipped"));
        itemModels().getBuilder(leafName)
            .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + leafName)));

        String saplingName = tree.sapling().getId().getPath();
        simpleBlock(tree.sapling().get(), models().cross(saplingName, tree.saplingTexture()).renderType("cutout"));
        
        itemModels().getBuilder(tree.harvest().getId().getPath())
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + tree.harvest().getId().getPath()));
    }

    private void elderberryFlower() {
        ElderberryFlowerBlock flower = Trees.elderberryFlower().get();
        String name = Trees.elderberryFlower().getId().getPath();

        ModelFile buds = models().cross(name, modLoc("block/elderberry_tree_flower")).renderType("cutout");
        ModelFile ripe = models().cross("elderberry_flower_ripe", modLoc("block/elderberry_tree_fruit"))
            .renderType("cutout");
        getVariantBuilder(flower).forAllStatesExcept(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(ElderberryFlowerBlock.AGE) == ElderberryFlowerBlock.MAX_AGE ? ripe : buds)
            .build(), ElderberryFlowerBlock.PERSISTENT);

        itemModels().getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + name));
    }

    private void coconutBlocks() {
        simpleBlock(Coconuts.COCONUT.get(), models().getBuilder("coconut")
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/coconut")))
            .texture("particle", modLoc("block/coconut_block"))
            .texture("1", modLoc("block/coconut_block"))
            .renderType("cutout"));
        simpleBlock(Coconuts.YOUNG_COCONUT.get(), models().getBuilder("young_coconut")
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/young_coconut")))
            .texture("particle", modLoc("block/young_coconut_4"))
            .texture("0", modLoc("block/young_coconut_4")));

        ModelFile[] coconutStageModels = new ModelFile[YoungCoconutsBlock.RIPE + 1];
        for (int stage = 0; stage <= YoungCoconutsBlock.RIPE; stage++) {
            coconutStageModels[stage] = models().getBuilder("young_coconuts_stage_" + stage)
                .texture("particle", modLoc("block/young_coconut_" + (stage + 1)));
        }
        getVariantBuilder(Coconuts.YOUNG_COCONUTS.get()).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(coconutStageModels[state.getValue(YoungCoconutsBlock.BLOCKSTATE)])
            .rotationY(((int) state.getValue(YoungCoconutsBlock.FACING).toYRot() + 180) % 360)
            .build());
        simpleBlock(Coconuts.HALF_OF_COCONUT_BLOCK.get(), models()
            .getBuilder("half_of_coconut")
            .texture("particle", modLoc("block/half_of_coconut")));

    }

    private void bananaTree() {
        ModelFile frond = models().getBuilder("banana_frond")
            .texture("particle", modLoc("block/banana_frond"));
        getVariantBuilder(Bananas.FROND.get()).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(frond)
            .rotationY(((int) state.getValue(BananaFrondBlock.FACING).toYRot() + 180) % 360)
            .build());

        simpleBlock(Bananas.CROWN.get(), models().getBuilder("banana_frond_block")
            .texture("particle", modLoc("block/banana_frond_block_none")));
        simpleBlock(Bananas.SAPLING.get(), models().getBuilder("banana_frond_sapling")
            .texture("particle", modLoc("block/banana_frond_block_none")));
        simpleBlock(Bananas.FLOWER.get(), models().getBuilder("banana_tree_flower")
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/banana_tree_flower")))
            .texture("all", modLoc("block/banana_frond_block_none"))
            .texture("particle", modLoc("block/banana_frond_block_none"))
            .texture("0", modLoc("block/banana_frond_block_none"))
            .renderType("cutout"));
        
        itemModels().getBuilder("banana_tree_flower")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/banana_tree_flower"))
            .renderType("translucent");

        simpleBlock(Bananas.TREE_BUNCH.get(), models().getBuilder("banana_tree_bunch")
            .texture("particle", modLoc("block/banana_bunch_1")));

        ModelFile bunch = models().getBuilder("banana_bunch")
            .texture("particle", modLoc("block/banana_bunch"));
        getVariantBuilder(Bananas.BUNCH.get()).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(bunch)
            .rotationY(((int) state.getValue(BananaBunchBlock.FACING).toYRot() + 180) % 360)
            .build());
    }

    private void blackDirtBlocks() {
        simpleBlockWithItem(BlackDirts.BLACK_DIRT.get(),
            models().cubeAll("black_dirt", modLoc("block/snowy_black_dirt_bottom")));
        simpleBlockWithItem(BlackDirts.SNOWY_BLACK_DIRT.get(), models().cube("snowy_black_dirt",
            modLoc("block/snowy_black_dirt_bottom"), modLoc("block/snowy_black_dirt_top"),
            modLoc("block/snowy_black_dirt_side"), modLoc("block/snowy_black_dirt_side"),
            modLoc("block/snowy_black_dirt_side"), modLoc("block/snowy_black_dirt_side"))
            .texture("particle", modLoc("block/snowy_black_dirt_bottom")));
        simpleBlockWithItem(BlackDirts.BLACK_DIRT_TRUFFLE.get(),
            models().cubeAll("black_dirt_truffle", modLoc("block/black_dirt_truffle")));

        ModelFile farmland = models().getBuilder("rich_black_dirt_farmland")
            .parent(new ModelFile.UncheckedModelFile(mcLoc("block/template_farmland")))
            .texture("dirt", modLoc("block/rich_black_dirt"))
            .texture("top", modLoc("block/rich_black_dirt_farmland"));
        getVariantBuilder(BlackDirts.RICH_BLACK_DIRT_FARMLAND.get())
            .forAllStates(state -> ConfiguredModel.builder().modelFile(farmland).build());
        itemModels().getBuilder("rich_black_dirt_farmland")
           .parent(new ModelFile.UncheckedModelFile(modLoc("block/rich_black_dirt_farmland")));
    }

    private void cropBlock(Crops.Crop crop) {
        CropBlock block = crop.block().get();
        String name = crop.block().getId().getPath();
        List<Crops.Stage> stages = crop.stages();
        ResourceLocation particle = modLoc("block/" + crop.particleTexture());

        int stagesCount = CropBlock.MAX_AGE + 1;
        ModelFile[] modelsByAge = new ModelFile[stagesCount];
        for (int age = 0; age < stagesCount; age++) {
            modelsByAge[age] = cropStageModel(crop.shape(), name + "_stage" + age, stages.get(age), particle);
        }
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(modelsByAge[state.getValue(CropBlock.AGE)])
            .build());

        if (crop.blockItemIcon() != null) {
            itemModels().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/" + crop.blockItemIcon()));
        }
    }

    private ModelFile cropStageModel(Crops.Shape shape, String name, Crops.Stage stage, ResourceLocation particle) {
        return switch (shape) {
            case CROP -> models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("block/crop")))
                .texture("crop", stage.texture())
                .texture("particle", particle)
                .renderType("cutout");
            case CROSS -> models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("block/cross")))
                .texture("cross", stage.texture())
                .texture("particle", particle)
                .renderType("cutout");
            case GEOMETRY -> models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(
                    new ResourceLocation(stage.texture().getNamespace(), "custom/" + stage.geometry())))
                .texture("all", stage.texture())
                .texture(stage.geometryKey(), stage.texture())
                .texture("particle", particle)
                .renderType("cutout_mipped");
        };
    }

    private void doubleCropBlock(DoubleCrops.Entry crop) {
        DoubleCropBlock block = crop.block().get();
        String name = crop.block().getId().getPath();
        ResourceLocation particle = modLoc("block/" + crop.particleTexture());

        ModelFile[] lower = new ModelFile[crop.lowerStages().size()];
        for (int age = 0; age < lower.length; age++) {
            lower[age] = cropStageModel(Crops.Shape.CROSS, name + "_lower_stage" + age,
                crop.lowerStages().get(age), particle);
        }
        ModelFile[] upper = new ModelFile[crop.upperStages().size()];
        for (int stage = 0; stage < upper.length; stage++) {
            upper[stage] = cropStageModel(Crops.Shape.CROSS, name + "_upper_stage" + stage,
                crop.upperStages().get(stage), particle);
        }

        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER
                ? upper[Math.min(Math.max(0, state.getValue(DoubleCropBlock.AGE) - crop.doubleAge()),
                    upper.length - 1)]
                : lower[Math.min(state.getValue(DoubleCropBlock.AGE), lower.length - 1)])
            .build());

        itemModels().getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + crop.blockItemIcon()));
    }

    private void wildPeanut() {
        String name = Crops.WILD_PEANUT.getId().getPath();
        simpleBlock(Crops.WILD_PEANUT.get(),
            models().cross(name, modLoc("block/wild_peanut")).renderType("cutout"));
        itemModels().getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/wild_peanut_item"));
    }

    private void pineappleFruit() {
        simpleBlock(Crops.PINEAPPLE_FRUIT.get(), models().getBuilder("pineapple")
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/pineapple")))
            .texture("all", modLoc("block/pineapple_crop_6"))
            .texture("1", modLoc("block/pineapple_crop_6"))
            .texture("particle", modLoc("block/pineapple"))
            .renderType("cutout_mipped"));
    }

    private void vineStructures() {
        VineStakeBlock stake = Vines.VINE_STAKE.get();
        Map<String, ModelFile> stakeModels = new HashMap<>();
        ModelFile emptyStake = vineStageModel("vine_stake", Vines.stakeStage(VineSpecies.EMPTY, false),
            modLoc("block/" + Vines.STAKE_PARTICLE), "cutout");
        
        stakeModels.put(stakeKey(VineSpecies.EMPTY, false), emptyStake);
        stakeModels.put(stakeKey(VineSpecies.EMPTY, true), emptyStake);
        for (VineSpecies species : VineSpecies.values()) {
            if (species == VineSpecies.EMPTY) {
                continue;
            }
            for (boolean climbed : new boolean[] {false, true}) {
                String name = "vine_stake_" + species.getSerializedName() + (climbed ? "_climbed" : "_sown");
                stakeModels.put(stakeKey(species, climbed), vineStageModel(name,
                    Vines.stakeStage(species, climbed), modLoc("block/" + Vines.STAKE_PARTICLE), "cutout"));
            }
        }
        getVariantBuilder(stake).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(stakeModels.get(
                stakeKey(state.getValue(VineStakeBlock.SPECIES), state.getValue(VineStakeBlock.CLIMBED))))
            .build());
        itemModels().withExistingParent("vine_stake", modLoc("block/vine_stake"));

        VineArborBlock arbor = Vines.VINE_ARBOR.get();
        Map<VineSpecies, ModelFile> arborModels = new EnumMap<>(VineSpecies.class);
        for (VineSpecies species : VineSpecies.values()) {
            String name = species == VineSpecies.EMPTY
                ? "vine_arbor" : "vine_arbor_" + species.getSerializedName();
            arborModels.put(species, vineStageModel(name, Vines.arborStage(species),
                modLoc("block/" + Vines.ARBOR_PARTICLE), "cutout"));
        }
        getVariantBuilder(arbor).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(arborModels.get(state.getValue(VineArborBlock.SPECIES)))
            .build());
        itemModels().withExistingParent("vine_arbor", modLoc("block/vine_arbor"));
    }

    private void vineCropBlock(Vines.Entry crop) {
        VineCropBlock block = crop.cropBlock().get();
        String name = crop.name();
        ResourceLocation particle = crop.stages().get(0).texture();

        int ages = VineCropBlock.MAX_AGE + 1;
        ModelFile[] models = new ModelFile[ages * 2];
        for (int age = 0; age < ages; age++) {
            ModelFile model = vineStageModel(name + "_stage" + age, crop.stages().get(age), particle,
                "cutout_mipped");
            models[age * 2] = model;
            models[age * 2 + 1] = model;
        }
        if (crop.rareStage() != null) {
            models[VineCropBlock.MAX_AGE * 2 + 1] = vineStageModel(name + "_stage_rare", crop.rareStage(),
                particle, "cutout_mipped");
        }
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
            .modelFile(models[state.getValue(VineCropBlock.AGE) * 2
                + (state.getValue(VineCropBlock.RARE) ? 1 : 0)])
            .build());

        itemModels().getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", modLoc("item/" + crop.cropIcon()));
    }

    private ModelFile vineStageModel(String name, Crops.Stage stage, ResourceLocation particle, String renderType) {
        return models().getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(modLoc("custom/" + stage.geometry())))
            .texture("all", stage.texture())
            .texture(stage.geometryKey(), stage.texture())
            .texture("particle", particle)
            .renderType(renderType);
    }

    private static String stakeKey(VineSpecies species, boolean climbed) {
        return species.getSerializedName() + "_" + climbed;
    }

    private void wildVines() {
        for (Vines.Entry vine : Vines.entries()) {
            Vines.WildVine wild = vine.wild();
            String name = wild.block().getId().getPath();

            Map<String, ModelFile> models = new HashMap<>();
            for (boolean tip : new boolean[] {true, false}) {
                for (boolean fruit : new boolean[] {false, true}) {
                    String modelName = name + (tip ? "_tip" : "_middle") + (fruit ? "_fruit" : "");
                    models.put(tip + "_" + fruit, wildVineModel(modelName,
                        Vines.wildVineTexture(name, tip, fruit), name + "_bottom"));
                }
            }
            getVariantBuilder(wild.block().get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models.get(
                    state.getValue(WildVineBlock.TIP) + "_" + state.getValue(WildVineBlock.FRUIT)))
                .build());

            itemModels().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/" + wild.icon()));
        }
    }

    private ModelFile wildVineModel(String name, String texture, String particleTexture) {
        return models().cross(name, modLoc("block/" + texture))
            .texture("particle", modLoc("block/" + particleTexture))
            .renderType("cutout_mipped");
    }

    private void bushBlock(Bushes.Entry bush) {
        BerryBushBlock block = bush.block().get();
        String name = bush.block().getId().getPath();
        List<Bushes.Stage> stages = bush.stages();
        ResourceLocation particle = bush.model().particleTexture() == null
            ? stages.get(0).texture()
            : modLoc("block/" + bush.model().particleTexture());

        int ages = BerryBushBlock.AGE.getPossibleValues().size();
        ModelFile[] modelsByAge = new ModelFile[ages];
        for (int age = 0; age < ages; age++) {
            modelsByAge[age] = bushStageModel(name + "_stage" + age,
                stages.get(Math.min(age, stages.size() - 1)), particle);
        }
        ModelFile rareModel = bush.model().rareTexture() == null ? null
            : bushStageModel(name + "_stage_rare",
                rareStage(stages.get(stages.size() - 1), bush.model().rareTexture()), particle);

        int maxAge = block.settings().maxAge();
        getVariantBuilder(block).forAllStatesExcept(state -> {
            int age = state.getValue(BerryBushBlock.AGE);
            ModelFile model = rareModel != null && age >= maxAge && state.getValue(BerryBushBlock.RARE)
                ? rareModel
                : modelsByAge[age];
            return ConfiguredModel.builder().modelFile(model).build();
        }, BerryBushBlock.TRIMMED);

        if (bush.item() == null) {
            return;
        }
        
        ResourceLocation harvestId = BuiltInRegistries.ITEM.getKey(bush.fruitItem());
        itemModels().getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated")))
            .texture("layer0", new ResourceLocation(harvestId.getNamespace(), "item/" + harvestId.getPath()));
    }

    private static Bushes.Stage rareStage(Bushes.Stage matureStage, String rareTexture) {
        return new Bushes.Stage(new ResourceLocation("plerocottage", "block/" + rareTexture),
            matureStage.geometry() == null ? null : rareTexture);
    }

    private ModelFile bushStageModel(String name, Bushes.Stage stage, ResourceLocation particle) {
        if (stage.geometry() == null) {
            return models().cross(name, stage.texture()).renderType("cutout_mipped");
        }
        return models().getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(
                new ResourceLocation(stage.texture().getNamespace(), "custom/" + stage.geometry())))
            .texture("all", stage.texture())
            
            .texture("0", stage.texture())
            .texture("5", stage.texture())
            .texture("particle", particle)
            .renderType("cutout_mipped");
    }

    private void hangingSignBlock(WoodSet wood, ResourceLocation particleTexture) {
        String name = wood.name();
        ModelFile model = models().getBuilder(name + "_hanging_sign")
            .texture("particle", particleTexture);
        simpleBlock(wood.hangingSign().get(), model);
        simpleBlock(wood.wallHangingSign().get(), model);
    }
}

