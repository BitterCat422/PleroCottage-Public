package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class PlainLeavesBlock extends LeavesBlock {

    @Nullable
    private final Supplier<SimpleParticleType> leafParticle;

    public PlainLeavesBlock(MapColor mapColor, SoundType soundType,
                            @Nullable Supplier<SimpleParticleType> leafParticle) {
        super(BlockBehaviour.Properties.of()
            .mapColor(mapColor)
            .sound(soundType)
            .strength(0.2F)
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false)
            .ignitedByLava()
            .isSuffocating((state, level, pos) -> false)
            .isViewBlocking((state, level, pos) -> false));
        this.leafParticle = leafParticle;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (leafParticle == null || random.nextInt(10) != 0) {
            return;
        }
        BlockPos below = pos.below();
        if (isFaceFull(level.getBlockState(below).getCollisionShape(level, below), Direction.UP)) {
            return;
        }
        ParticleUtils.spawnParticleBelow(level, pos, random, leafParticle.get());
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }

}
