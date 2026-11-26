package net.bittercat.plerocottage.blockentities;

import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public final class HayNestBlockEntity extends DisplayContainerBlockEntity {
    public HayNestBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityContent.HAY_NEST.get(), pos, state, 6);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.plerocottage.hay_nest");
    }

}
