package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface GrowthChance {

    float chance(BlockState state, Level level, BlockPos pos, RandomSource random);

    static GrowthChance constant(float value) {
        return (state, level, pos, random) -> value;
    }

    static GrowthChance byLight(float base) {
        return (state, level, pos, random) -> level.getRawBrightness(pos, 0) >= 9 ? base : base * 0.4F;
    }

    static GrowthChance bySoil(float base, float bare) {
        return (state, level, pos, random) -> {
            BlockState below = level.getBlockState(pos.below());
            return below.isFertile(level, pos.below()) ? base : bare;
        };
    }
}
