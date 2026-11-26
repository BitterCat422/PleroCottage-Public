package net.bittercat.plerocottage.blockentities;

import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public final class EggIncubatorBlockEntity extends DisplayContainerBlockEntity {
    public EggIncubatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityContent.EGG_INCUBATOR.get(), pos, state, 3);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.plerocottage.egg_incubator");
    }
}
