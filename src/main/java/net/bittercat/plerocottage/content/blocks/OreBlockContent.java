package net.bittercat.plerocottage.content.blocks;

import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.util.valueproviders.UniformInt;
import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.BUILDING;
import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;

import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public final class OreBlockContent {
    public static final RegistryObject<Block> DEBRISHROOM = registerBlock("debrishroom", MapColor.COLOR_BROWN);
    public static final RegistryObject<Block> PEARL_ROCK_ORE = registerOre("pearl_rock_ore", MapColor.STONE, 2, 5);
    public static final RegistryObject<Block> DEEPSLATE_PEARL_ROCK_ORE =
        registerOre("deepslate_pearl_rock_ore", MapColor.DEEPSLATE, 2, 5);
    public static final RegistryObject<Block> SUSPICIOUS_ORE = registerOre("suspicious_ore", MapColor.STONE, 3, 7);
    public static final RegistryObject<Block> DEEPSLATE_SUSPICIOUS_ORE =
        registerOre("deepslate_suspicious_ore", MapColor.DEEPSLATE, 3, 7);
    public static final RegistryObject<Block> PEARL_ROCK_BLOCK = registerBlock("pearl_rock_block", MapColor.STONE);
    public static final RegistryObject<Block> GARNET_BLOCK = registerBlock("garnet_block", MapColor.COLOR_RED);
    public static final RegistryObject<Block> TOPAZ_BLOCK = registerBlock("topaz_block", MapColor.COLOR_YELLOW);
    public static final RegistryObject<Block> PINK_CRYSTAL_BLOCK =
        registerBlock("pink_crystal_block", MapColor.COLOR_PINK);
    public static final RegistryObject<Block> BRILLIANT_DIAMOND_BLOCK =
        registerBlock("brilliant_diamond_block", MapColor.DIAMOND);

    private OreBlockContent() {
    }

    public static void bootstrap() {

    }

    /** 矿石和原版一样在被挖掉时掉经验（精准采集时不掉）：珍珠岩矿同青金石，可疑矿石同钻石。 */
    private static RegistryObject<Block> registerOre(String name, MapColor color, int minXp, int maxXp) {
        RegistryObject<Block> block = BlockRegistry.register(name, () -> new DropExperienceBlock(
            BlockBehaviour.Properties.of().mapColor(color).sound(SoundType.STONE).strength(3.0F, 3.0F)
                .requiresCorrectToolForDrops(), UniformInt.of(minXp, maxXp)));
        ItemRegistry.registerBlockItem(name, block);
        return block;
    }

    private static RegistryObject<Block> registerBlock(String name, MapColor color) {
        RegistryObject<Block> block = BlockRegistry.register(name, () -> new Block(
            BlockBehaviour.Properties.of().mapColor(color).sound(SoundType.STONE).strength(3.0F, 3.0F)
                .requiresCorrectToolForDrops()));
        ItemRegistry.registerBlockItem(name, block);
        return block;
    }
}
