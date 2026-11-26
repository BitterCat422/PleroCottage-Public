package net.bittercat.plerocottage.blockentities;

import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public final class ColoredBedBlockEntity extends BedBlockEntity {
    public ColoredBedBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, DyeColor.WHITE);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BlockEntityContent.COLORED_BED.get();
    }
}
