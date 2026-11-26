package net.bittercat.plerocottage.content.blocks;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.BUILDING;

import java.util.List;
import java.util.function.Supplier;

import net.bittercat.plerocottage.blocks.BowlBlock;
import net.bittercat.plerocottage.content.food.DisplayModelResources;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.item.Item;
import net.bittercat.plerocottage.items.DisplayBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public final class MarbleContent {
    public static final RegistryObject<Block> MARBLE = block("marble", MarbleContent::solid);
    public static final RegistryObject<Block> MARBLE_SLAB = block("marble_slab", MarbleContent::slab);
    public static final RegistryObject<Block> MARBLE_STAIRS =
        block("marble_stairs", () -> stairs(MARBLE));
    public static final RegistryObject<Block> MARBLE_WALL = block("marble_wall", MarbleContent::wall);

    public static final RegistryObject<Block> POLISHED_MARBLE =
        block("polished_marble", MarbleContent::solid);
    public static final RegistryObject<Block> POLISHED_MARBLE_SLAB =
        block("polished_marble_slab", MarbleContent::slab);
    public static final RegistryObject<Block> POLISHED_MARBLE_STAIRS =
        block("polished_marble_stairs", () -> stairs(POLISHED_MARBLE));
    public static final RegistryObject<Block> POLISHED_MARBLE_WALL =
        block("polished_marble_wall", MarbleContent::wall);

    public static final RegistryObject<Block> POLISHED_MARBLE_BRICK =
        block("polished_marble_brick", MarbleContent::solid);
    public static final RegistryObject<Block> POLISHED_MARBLE_BRICK_SLAB =
        block("polished_marble_brick_slab", MarbleContent::slab);
    public static final RegistryObject<Block> POLISHED_MARBLE_BRICK_STAIRS =
        block("polished_marble_brick_stairs", () -> stairs(POLISHED_MARBLE_BRICK));
    public static final RegistryObject<Block> POLISHED_MARBLE_BRICK_WALL =
        block("polished_marble_brick_wall", MarbleContent::wall);

    public static final RegistryObject<Block> BOWL =
        BlockRegistry.register("bowl", BowlBlock::new);

    private static boolean bootstrapped;

    private MarbleContent() {
    }

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        List<RegistryObject<Block>> building = List.of(
            MARBLE, POLISHED_MARBLE, POLISHED_MARBLE_BRICK, MARBLE_WALL,
            POLISHED_MARBLE_WALL, POLISHED_MARBLE_BRICK_WALL, MARBLE_SLAB,
            POLISHED_MARBLE_SLAB, POLISHED_MARBLE_BRICK_SLAB, MARBLE_STAIRS,
            POLISHED_MARBLE_STAIRS, POLISHED_MARBLE_BRICK_STAIRS);
        for (RegistryObject<Block> block : building) {
            registerItem(block);
            
        }
        ItemRegistry.register("bowl", () -> new DisplayBlockItem(BOWL.get(),
            new Item.Properties(),
            DisplayModelResources.BOWL.model(),
            DisplayModelResources.BOWL.animation(),
            DisplayModelResources.BOWL.texture()));
        
    }

    private static RegistryObject<Block> block(String id, Supplier<Block> factory) {
        return BlockRegistry.register(id, factory);
    }

    private static void registerItem(RegistryObject<Block> block) {
        ItemRegistry.registerBlockItem(block.getId().getPath(), block);
    }

    private static BlockBehaviour.Properties properties() {
        return BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(1.5F, 6.0F)
            .requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM);
    }

    private static Block solid() {
        return new Block(properties());
    }

    private static Block slab() {
        return new SlabBlock(properties());
    }

    private static Block stairs(RegistryObject<Block> base) {
        return new StairBlock(() -> base.get().defaultBlockState(), properties());
    }

    private static Block wall() {
        return new WallBlock(properties().forceSolidOn());
    }
}
