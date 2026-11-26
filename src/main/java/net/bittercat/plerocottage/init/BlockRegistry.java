package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public final class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, PleroCottage.MODID);
    public static final DeferredRegister<Block> BLOCK = BLOCKS;

    public static <T extends Block> RegistryObject<T> register(String id, Supplier<? extends T> factory) {
        return BLOCKS.register(id, factory);
    }

    public static final RegistryObject<Block> ICE_CREAM_MACHINE = BLOCK.register("ice_cream_machine",
        net.bittercat.plerocottage.machine.blocks.IceCreamMachineBlock::new);
    public static final RegistryObject<Block> FRYER = BLOCK.register("fryer",
        net.bittercat.plerocottage.machine.blocks.FryerBlock::new);
    public static final RegistryObject<Block> OVEN = BLOCK.register("oven",
        net.bittercat.plerocottage.machine.blocks.OvenBlock::new);
    public static final RegistryObject<Block> TEAPOT = BLOCK.register("teapot",
        net.bittercat.plerocottage.machine.blocks.TeapotBlock::new);
    public static final RegistryObject<Block> FERMENTER = BLOCK.register("fermenter",
        net.bittercat.plerocottage.machine.blocks.FermenterBlock::new);
    public static final RegistryObject<Block> BLENDER = BLOCK.register("blender",
        net.bittercat.plerocottage.machine.blocks.BlenderBlock::new);
    public static final RegistryObject<Block> CUTTING_BOARD = BLOCK.register("cutting_board",
        net.bittercat.plerocottage.machine.blocks.CuttingBoardBlock::new);
    public static final RegistryObject<Block> STOCK_POT = BLOCK.register("stock_pot",
        net.bittercat.plerocottage.machine.blocks.StockPotBlock::new);
    public static final RegistryObject<Block> SAUCEPAN_AND_WHISK = BLOCK.register("saucepan_and_whisk",
        net.bittercat.plerocottage.machine.blocks.SaucepanAndWhiskBlock::new);
    public static final RegistryObject<Block> COOKTOP = BLOCK.register("cooktop",
        net.bittercat.plerocottage.machine.blocks.CooktopBlock::new);

    public static final List<RegistryObject<Block>> MACHINE_BLOCKS = List.of(
        ICE_CREAM_MACHINE, FRYER, OVEN, TEAPOT, FERMENTER, BLENDER,
        CUTTING_BOARD, STOCK_POT, SAUCEPAN_AND_WHISK, COOKTOP);

    public static BlockBehaviour.Properties getBlockProperties() {
        return BlockBehaviour.Properties.of().noOcclusion().strength(1);
    }

    private BlockRegistry() {
    }
}
