package net.bittercat.plerocottage.blockentities;

import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class ColoredShulkerBoxBlockEntity extends ShulkerBoxBlockEntity {
    public ColoredShulkerBoxBlockEntity(BlockPos pos, BlockState state) {
        super(null, pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BlockEntityContent.COLORED_SHULKER_BOX.get();
    }
}
