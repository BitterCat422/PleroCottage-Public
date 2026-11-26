package net.bittercat.plerocottage.blocks;

import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.BlockState;

public class DragonFruitBlock extends FallingBlock {
    public DragonFruitBlock() { super(BlockBehaviour.Properties.of().sound(SoundType.BAMBOO).strength(0.2F).noOcclusion()
        .isRedstoneConductor((s, l, p) -> false)); }
    @Override public VoxelShape getShape(BlockState s, BlockGetter l, BlockPos p, CollisionContext c) {
        return Shapes.or(box(4, 0, 4, 12, 11, 12), box(8, 0, 8, 8.001, 16, 8.001));
    }
    @Override public VoxelShape getVisualShape(BlockState s, BlockGetter l, BlockPos p, CollisionContext c) {
        return Shapes.empty();
    }
    @Override public boolean propagatesSkylightDown(BlockState s, BlockGetter l, BlockPos p) { return true; }
    @Override public int getLightBlock(BlockState s, BlockGetter l, BlockPos p) { return 0; }
}
