package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class PoultryEggBlock extends Block {
    private static final VoxelShape STANDARD_SHAPE = Block.box(6, 0, 6, 10, 5, 10);
    private static final VoxelShape TURKEY_SHAPE = Block.box(5.75, 0, 5.75, 10.25, 5.5, 10.25);

    private final VoxelShape shape;

    public PoultryEggBlock(Properties properties, boolean turkeyShape) {
        super(properties);
        this.shape = turkeyShape ? TURKEY_SHAPE : STANDARD_SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
                                        CollisionContext context) {
        return shape;
    }
}
