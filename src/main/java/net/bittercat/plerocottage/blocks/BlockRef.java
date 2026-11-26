package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public final class BlockRef implements Supplier<Block> {

    private RegistryObject<? extends Block> block;

    public void fill(RegistryObject<? extends Block> block) {
        this.block = block;
    }

    @Override
    public Block get() {
        return block.get();
    }
}
