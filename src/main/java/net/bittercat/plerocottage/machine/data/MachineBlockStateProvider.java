package net.bittercat.plerocottage.machine.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blocks.FryerBlock;
import net.bittercat.plerocottage.machine.blocks.OvenBlock;
import net.bittercat.plerocottage.machine.blocks.StockPotBlock;
import net.bittercat.plerocottage.machine.blocks.TeapotBlock;
import net.bittercat.plerocottage.init.BlockRegistry;

public class MachineBlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public MachineBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PleroCottageMachine.MOD_ID, exFileHelper);
    }

    @Override
    public String getName() {
        return "Machine Block States: plerocottage";
    }

    @Override
    protected void registerStatesAndModels() {
        this.iceCreamMachine();
        this.fryer();
        this.oven();
        this.teapot();
        this.fermenter();
        this.cuttingBoard();
        this.stockPot();
        this.cooktop();
    }

    public void iceCreamMachine() {
        Block block = BlockRegistry.ICE_CREAM_MACHINE.get();
        ResourceLocation registerKey = ForgeRegistries.BLOCKS.getKey(block);
        String namespace = registerKey.getNamespace();
        String path = registerKey.getPath();
        String texture = namespace + ":block/" + path;
        ModelFile modelFile = modelFile(path, texture);
        this.horizontalBlock(
                block,
                modelFile
        );
    }

    public void fryer() {
        Block block = BlockRegistry.FRYER.get();
        ResourceLocation registerKey = ForgeRegistries.BLOCKS.getKey(block);
        String namespace = registerKey.getNamespace();
        String path = registerKey.getPath();
        String texture = namespace + ":block/" + path;
        ModelFile modelFile1 = modelFile(path, texture);
        ModelFile modelFile2 = modelFile(path + "_oil", texture, "translucent");
        this.getVariantBuilder(block)
                .forAllStatesExcept(
                        state -> ConfiguredModel.builder()
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .modelFile(state.getValue(FryerBlock.HAS_OIL) ? modelFile2 : modelFile1)
                                .build()
                );
    }

    public void oven() {
        Block block = BlockRegistry.OVEN.get();
        ResourceLocation registerKey = ForgeRegistries.BLOCKS.getKey(block);
        String namespace = registerKey.getNamespace();
        String path = registerKey.getPath();
        String texture1 = namespace + ":block/" + path;
        String texture2 = namespace + ":block/" + path + "_running";
        ModelFile modelFile1 = modelFile(path, texture1);
        ModelFile modelFile2 = modelFile(path + "_open", texture1);
        ModelFile modelFile3 = modelFile(path + "_running", texture2);
        this.getVariantBuilder(block)
                .forAllStatesExcept(
                        state -> ConfiguredModel.builder()
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .modelFile(
                                        state.getValue(OvenBlock.RUNNING) ? modelFile3
                                                : state.getValue(OvenBlock.OPEN) ? modelFile2 : modelFile1
                                )
                                .build()
                );
    }

    public void teapot() {
        Block block = BlockRegistry.TEAPOT.get();
        ResourceLocation registerKey = ForgeRegistries.BLOCKS.getKey(block);
        String namespace = registerKey.getNamespace();
        String path = registerKey.getPath();
        String texture = namespace + ":block/" + path;
        ModelFile modelFile1 = modelFile(path, texture);
        ModelFile modelFile2 = modelFile(path + "_open", texture);
        this.getVariantBuilder(block)
                .forAllStatesExcept(
                        state -> ConfiguredModel.builder()
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .modelFile(state.getValue(TeapotBlock.OPEN) ? modelFile2 : modelFile1)
                                .build()
                );
    }

    public void fermenter() {
        Block block = BlockRegistry.FERMENTER.get();
        ResourceLocation registerKey = ForgeRegistries.BLOCKS.getKey(block);
        String namespace = registerKey.getNamespace();
        String path = registerKey.getPath();
        String texture = namespace + ":block/" + path;
        ModelFile modelFile1 = modelFile(path, texture);
        ModelFile modelFile2 = modelFile(path + "_open", texture);
        this.getVariantBuilder(block)
                .forAllStatesExcept(
                        state -> ConfiguredModel.builder()
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .modelFile(state.getValue(TeapotBlock.OPEN) ? modelFile2 : modelFile1)
                                .build()
                );
    }

    public void cuttingBoard() {
        Block block = BlockRegistry.CUTTING_BOARD.get();
        ResourceLocation registerKey = ForgeRegistries.BLOCKS.getKey(block);
        String namespace = registerKey.getNamespace();
        String path = registerKey.getPath();
        String texture = namespace + ":block/" + path;
        ModelFile modelFile = modelFile(path, texture);
        this.horizontalBlock(
                block,
                modelFile
        );
    }

    public void stockPot() {
        Block block = BlockRegistry.STOCK_POT.get();
        ResourceLocation registerKey = ForgeRegistries.BLOCKS.getKey(block);
        String namespace = registerKey.getNamespace();
        String path = registerKey.getPath();
        String texture = namespace + ":block/" + path;
        ModelFile modelFile1 = modelFile(path, texture);
        ModelFile modelFile2 = modelFile(path + "_open", texture);
        this.getVariantBuilder(block)
                .forAllStatesExcept(
                        state -> ConfiguredModel.builder()
                                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .modelFile(state.getValue(StockPotBlock.OPEN) ? modelFile2 : modelFile1)
                                .build()
                );
    }

    public void cooktop() {
        Block block = BlockRegistry.COOKTOP.get();
        ResourceLocation registerKey = ForgeRegistries.BLOCKS.getKey(block);
        String namespace = registerKey.getNamespace();
        String path = registerKey.getPath();
        String texture = namespace + ":block/" + path;
        ModelFile modelFile = modelFile(path, texture);
        this.horizontalBlock(
                block,
                modelFile
        );
    }

    public ModelFile modelFile(String path, String texture) {
        return this.modelFile(path, texture, "cutout_mipped");
    }

    public ModelFile modelFile(String path, String texture, String renderType) {
        return models().withExistingParent(path, modLoc("custom/" + path))
                .texture("particle", texture)
                .texture("0", texture)
                .texture("all", texture)
                .renderType(renderType);
    }
}
