package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public final class WildSpread {

    private static final int ATTEMPTS = 4;

    private WildSpread() {
    }

    public static void spread(ServerLevel level, BlockPos pos, BlockState state, RandomSource random, int limit) {
        int remaining = limit;
        for (BlockPos nearby : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
            if (level.getBlockState(nearby).is(state.getBlock())) {
                if (--remaining <= 0) {
                    return;
                }
            }
        }

        BlockPos target = pos;
        BlockPos candidate = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2),
            random.nextInt(3) - 1);
        for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
            if (level.isEmptyBlock(candidate) && state.canSurvive(level, candidate)) {
                target = candidate;
            }
            candidate = target.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2),
                random.nextInt(3) - 1);
        }

        if (level.isEmptyBlock(candidate) && state.canSurvive(level, candidate)) {
            target = candidate;
        }
        if (target != pos && level.isEmptyBlock(target) && state.canSurvive(level, target)) {
            level.setBlock(target, state, 2);
        }
    }
}
