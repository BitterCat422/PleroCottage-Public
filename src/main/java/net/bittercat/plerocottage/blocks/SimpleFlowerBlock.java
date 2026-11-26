package net.bittercat.plerocottage.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.effect.MobEffects;

public final class SimpleFlowerBlock extends FlowerBlock {
    private final VoxelShape shape;

    public SimpleFlowerBlock() {
        this(0, 0, 0, 16, 16, 16);
    }

    public SimpleFlowerBlock(double minX, double minY, double minZ,
                             double maxX, double maxY, double maxZ) {
        this(0, BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .instabreak()
            .sound(net.minecraft.world.level.block.SoundType.GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ), Block.box(minX, minY, minZ, maxX, maxY, maxZ));
    }

    public SimpleFlowerBlock(int effectDuration, BlockBehaviour.Properties properties, VoxelShape shape) {
        super(() -> MobEffects.MOVEMENT_SPEED, effectDuration, properties);
        this.shape = shape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
                               net.minecraft.world.phys.shapes.CollisionContext context) {
        return shape.move(state.getOffset(level, pos).x, state.getOffset(level, pos).y, state.getOffset(level, pos).z);
    }
}

