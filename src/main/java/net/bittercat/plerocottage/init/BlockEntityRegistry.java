package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.bittercat.plerocottage.machine.blockentities.*;

public final class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PleroCottage.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = BLOCK_ENTITIES;
    public static final RegistryObject<BlockEntityType<IceCreamMachineBlockEntity>> ICE_CREAM_MACHINE =
        BLOCK_ENTITIES.register("ice_cream_machine", () -> BlockEntityType.Builder.of(IceCreamMachineBlockEntity::new, BlockRegistry.ICE_CREAM_MACHINE.get()).build(null));
    public static final RegistryObject<BlockEntityType<FryerBlockEntity>> FRYER =
        BLOCK_ENTITIES.register("fryer", () -> BlockEntityType.Builder.of(FryerBlockEntity::new, BlockRegistry.FRYER.get()).build(null));
    public static final RegistryObject<BlockEntityType<OvenBlockEntity>> OVEN =
        BLOCK_ENTITIES.register("oven", () -> BlockEntityType.Builder.of(OvenBlockEntity::new, BlockRegistry.OVEN.get()).build(null));
    public static final RegistryObject<BlockEntityType<TeapotBlockEntity>> TEAPOT =
        BLOCK_ENTITIES.register("teapot", () -> BlockEntityType.Builder.of(TeapotBlockEntity::new, BlockRegistry.TEAPOT.get()).build(null));
    public static final RegistryObject<BlockEntityType<FermenterBlockEntity>> FERMENTER =
        BLOCK_ENTITIES.register("fermenter", () -> BlockEntityType.Builder.of(FermenterBlockEntity::new, BlockRegistry.FERMENTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlenderBlockEntity>> BLENDER =
        BLOCK_ENTITIES.register("blender", () -> BlockEntityType.Builder.of(BlenderBlockEntity::new, BlockRegistry.BLENDER.get()).build(null));
    public static final RegistryObject<BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD =
        BLOCK_ENTITIES.register("cutting_board", () -> BlockEntityType.Builder.of(CuttingBoardBlockEntity::new, BlockRegistry.CUTTING_BOARD.get()).build(null));
    public static final RegistryObject<BlockEntityType<StockPotBlockEntity>> STOCK_POT =
        BLOCK_ENTITIES.register("stock_pot", () -> BlockEntityType.Builder.of(StockPotBlockEntity::new, BlockRegistry.STOCK_POT.get()).build(null));
    public static final RegistryObject<BlockEntityType<SaucepanAndWhiskBlockEntity>> SAUCEPAN_AND_WHISK =
        BLOCK_ENTITIES.register("saucepan_and_whisk", () -> BlockEntityType.Builder.of(SaucepanAndWhiskBlockEntity::new, BlockRegistry.SAUCEPAN_AND_WHISK.get()).build(null));
    public static final RegistryObject<BlockEntityType<CooktopBlockEntity>> COOKTOP =
        BLOCK_ENTITIES.register("cooktop", () -> BlockEntityType.Builder.of(CooktopBlockEntity::new, BlockRegistry.COOKTOP.get()).build(null));

    private BlockEntityRegistry() {
    }
}
