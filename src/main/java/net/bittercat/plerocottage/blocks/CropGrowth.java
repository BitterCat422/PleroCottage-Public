package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;

public final class CropGrowth {

    private CropGrowth() {
    }

    public static float speed(Block block, BlockGetter level, BlockPos pos) {
        float speed = 1.0F;
        BlockPos below = pos.below();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                float bonus = 0.0F;
                BlockPos soilPos = below.offset(x, 0, z);
                BlockState soil = level.getBlockState(soilPos);
                if (soil.canSustainPlant(level, soilPos, net.minecraft.core.Direction.UP, (IPlantable) block)) {
                    bonus = 1.0F;
                    if (soil.isFertile(level, pos.offset(x, 0, z))) {
                        bonus = 3.0F;
                    }
                }
                if (x != 0 || z != 0) {
                    bonus /= 4.0F;
                }
                speed += bonus;
            }
        }

        BlockPos north = pos.north();
        BlockPos south = pos.south();
        BlockPos west = pos.west();
        BlockPos east = pos.east();
        boolean eastWestRow = level.getBlockState(west).is(block) || level.getBlockState(east).is(block);
        boolean northSouthRow = level.getBlockState(north).is(block) || level.getBlockState(south).is(block);
        if (eastWestRow && northSouthRow) {
            speed /= 2.0F;
        } else {
            boolean diagonalRow = level.getBlockState(west.north()).is(block)
                || level.getBlockState(east.north()).is(block)
                || level.getBlockState(east.south()).is(block)
                || level.getBlockState(west.south()).is(block);
            if (diagonalRow) {
                speed /= 2.0F;
            }
        }

        return speed;
    }
}
