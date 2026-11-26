package net.bittercat.plerocottage.content.blockentities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.bittercat.plerocottage.init.BlockEntityRegistry;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.content.core.CoreContent;
import net.bittercat.plerocottage.content.blocks.MarbleContent;
import net.bittercat.plerocottage.content.food.PlateContent;
import net.bittercat.plerocottage.content.furniture.FurnitureContent;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
import net.bittercat.plerocottage.content.food.DisplayFoodContent;
import net.bittercat.plerocottage.content.food.FoodBlockContent;
import net.bittercat.plerocottage.blockentities.BananaBlockEntity;
import net.bittercat.plerocottage.blockentities.ModHangingSignBlockEntity;
import net.bittercat.plerocottage.blockentities.ModSignBlockEntity;
import net.bittercat.plerocottage.blockentities.HalfOfCoconutBlockEntity;
import net.bittercat.plerocottage.blockentities.YoungCoconutsBlockEntity;
import net.bittercat.plerocottage.blockentities.StaticFoodDisplayBlockEntity;
import net.bittercat.plerocottage.blockentities.PlateBlockEntity;
import net.bittercat.plerocottage.blockentities.FurnitureBlockEntity;
import net.bittercat.plerocottage.blockentities.EggIncubatorBlockEntity;
import net.bittercat.plerocottage.blockentities.HayNestBlockEntity;
import net.bittercat.plerocottage.blockentities.BowlBlockEntity;
import net.bittercat.plerocottage.blockentities.ColoredBedBlockEntity;
import net.bittercat.plerocottage.blockentities.ColoredShulkerBoxBlockEntity;
import net.bittercat.plerocottage.content.building.ColoredDecorationContent;
import net.bittercat.plerocottage.content.plants.Bananas;
import net.bittercat.plerocottage.content.plants.Coconuts;
import net.bittercat.plerocottage.wood.WoodSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public final class BlockEntityContent {
    public static void bootstrap() {
    }

    public static final RegistryObject<BlockEntityType<ColoredShulkerBoxBlockEntity>> COLORED_SHULKER_BOX =
        BlockEntityRegistry.BLOCK_ENTITIES.register("colored_shulker_box", () -> BlockEntityType.Builder
            .of(ColoredShulkerBoxBlockEntity::new, ColoredDecorationContent.shulkerBoxes()).build(null));

    public static final RegistryObject<BlockEntityType<ColoredBedBlockEntity>> COLORED_BED =
        BlockEntityRegistry.BLOCK_ENTITIES.register("colored_bed", () -> BlockEntityType.Builder
            .of(ColoredBedBlockEntity::new, ColoredDecorationContent.beds()).build(null));

    public static final RegistryObject<BlockEntityType<StaticFoodDisplayBlockEntity>> STATIC_FOOD_DISPLAY =
        BlockEntityRegistry.BLOCK_ENTITIES.register("static_food_display", () -> BlockEntityType.Builder
            .of(StaticFoodDisplayBlockEntity::new, allStaticFoodBlocks())
            .build(null));

    public static final RegistryObject<BlockEntityType<BowlBlockEntity>> BOWL =
        BlockEntityRegistry.BLOCK_ENTITIES.register("bowl", () -> BlockEntityType.Builder
            .of(BowlBlockEntity::new, MarbleContent.BOWL.get()).build(null));

    public static final RegistryObject<BlockEntityType<PlateBlockEntity>> PLATE =
        BlockEntityRegistry.BLOCK_ENTITIES.register("plate", () -> BlockEntityType.Builder
            .of(PlateBlockEntity::new, PlateContent.PLATE.get()).build(null));

    public static final RegistryObject<BlockEntityType<HayNestBlockEntity>> HAY_NEST =
        BlockEntityRegistry.BLOCK_ENTITIES.register("hay_nest", () -> BlockEntityType.Builder
            .of(HayNestBlockEntity::new, CoreContent.HAY_NEST.get()).build(null));

    public static final RegistryObject<BlockEntityType<EggIncubatorBlockEntity>> EGG_INCUBATOR =
        BlockEntityRegistry.BLOCK_ENTITIES.register("egg_incubator", () -> BlockEntityType.Builder
            .of(EggIncubatorBlockEntity::new, CoreContent.EGG_INCUBATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<FurnitureBlockEntity.Cabinet>> CABINET =
        BlockEntityRegistry.BLOCK_ENTITIES.register("cabinet", () -> BlockEntityType.Builder
            .of(FurnitureBlockEntity.Cabinet::new, FurnitureContent.cabinets()).build(null));
    public static final RegistryObject<BlockEntityType<FurnitureBlockEntity.Drawer>> DRAWER =
        BlockEntityRegistry.BLOCK_ENTITIES.register("drawer", () -> BlockEntityType.Builder
            .of(FurnitureBlockEntity.Drawer::new, FurnitureContent.drawers()).build(null));

    public static final RegistryObject<BlockEntityType<ModSignBlockEntity>> SIGN =
        BlockEntityRegistry.BLOCK_ENTITIES.register("sign", () -> BlockEntityType.Builder
            .of(ModSignBlockEntity::new, signBlocks(false))
            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntity>> HANGING_SIGN =
        BlockEntityRegistry.BLOCK_ENTITIES.register("hanging_sign", () -> BlockEntityType.Builder
            .of(ModHangingSignBlockEntity::new, signBlocks(true))
            .build(null));

    public static final RegistryObject<BlockEntityType<YoungCoconutsBlockEntity>> YOUNG_COCONUTS =
        BlockEntityRegistry.BLOCK_ENTITIES.register("young_coconuts", () -> BlockEntityType.Builder
            .of(YoungCoconutsBlockEntity::new, Coconuts.YOUNG_COCONUTS.get())
            .build(null));

    public static final RegistryObject<BlockEntityType<HalfOfCoconutBlockEntity>> HALF_OF_COCONUT_BLOCK =
        BlockEntityRegistry.BLOCK_ENTITIES.register("half_of_coconut_block", () -> BlockEntityType.Builder
            .of(HalfOfCoconutBlockEntity::new, Coconuts.HALF_OF_COCONUT_BLOCK.get())
            .build(null));

    private static final Map<Block, RegistryObject<BlockEntityType<BananaBlockEntity>>> BANANA_TYPES =
        new LinkedHashMap<>();

    public static final RegistryObject<BlockEntityType<BananaBlockEntity>> BANANA_FROND =
        banana("banana_frond", Bananas.FROND);

    public static final RegistryObject<BlockEntityType<BananaBlockEntity>> BANANA_CROWN =
        banana("banana_crown", Bananas.CROWN);

    public static final RegistryObject<BlockEntityType<BananaBlockEntity>> BANANA_SAPLING =
        banana("banana_frond_sapling", Bananas.SAPLING);

    public static final RegistryObject<BlockEntityType<BananaBlockEntity>> BANANA_TREE_BUNCH =
        banana("banana_tree_bunch", Bananas.TREE_BUNCH);

    public static final RegistryObject<BlockEntityType<BananaBlockEntity>> BANANA_BUNCH =
        banana("banana_bunch", Bananas.BUNCH);

    private BlockEntityContent() {
    }

    public static BlockEntityType<? extends BananaBlockEntity> bananaType(Block block) {
        RegistryObject<BlockEntityType<BananaBlockEntity>> type = BANANA_TYPES.get(block);
        if (type == null) {
            throw new IllegalStateException("没有登记方块实体类型的香蕉方块：" + block);
        }
        return type.get();
    }

    private static RegistryObject<BlockEntityType<BananaBlockEntity>> banana(String name,
                                                                             Supplier<? extends Block> block) {

        List<RegistryObject<BlockEntityType<BananaBlockEntity>>> holder = new ArrayList<>(1);
        holder.add(BlockEntityRegistry.BLOCK_ENTITIES.register(name, () -> {
            Block value = block.get();
            BANANA_TYPES.put(value, holder.get(0));
            return BlockEntityType.Builder.of(BananaBlockEntity::new, value).build(null);
        }));
        return holder.get(0);
    }

    private static Block[] signBlocks(boolean hanging) {
        List<Block> blocks = new ArrayList<>();
        for (WoodSet wood : WoodSetContent.all()) {
            if (hanging) {
                blocks.add(wood.hangingSign().get());
                blocks.add(wood.wallHangingSign().get());
            } else {
                blocks.add(wood.sign().get());
                blocks.add(wood.wallSign().get());
            }
        }
        return blocks.toArray(Block[]::new);
    }

    private static Block[] allStaticFoodBlocks() {
        List<Block> blocks = new ArrayList<>(List.of(DisplayFoodContent.allBlocks()));
        FoodBlockContent.entries().forEach(food -> blocks.add(food.block().get()));
        return blocks.toArray(Block[]::new);
    }
}
