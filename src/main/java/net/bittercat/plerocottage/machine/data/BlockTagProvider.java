package net.bittercat.plerocottage.machine.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.init.BlockRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends BlockTagsProvider {
    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PleroCottageMachine.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        BlockRegistry.MACHINE_BLOCKS.stream()
                                .map(RegistryObject::get)
                                .filter(block -> block != BlockRegistry.CUTTING_BOARD.get())
                                .toArray(Block[]::new)
                );

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(
                        BlockRegistry.CUTTING_BOARD.get()
                );
    }
}
