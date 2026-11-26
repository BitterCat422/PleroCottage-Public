package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class RegisteredPinkPetalsBlock extends PinkPetalsBlock {
    private final boolean usesNetherOrDirtGround;

    public RegisteredPinkPetalsBlock() {
        this(false);
    }

    public RegisteredPinkPetalsBlock(boolean usesNetherOrDirtGround) {
        super(BlockBehaviour.Properties.copy(Blocks.PINK_PETALS));
        this.usesNetherOrDirtGround = usesNetherOrDirtGround;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return usesNetherOrDirtGround ? NetherOrDirtSaplingBlock.isPlantableGround(state)
            : super.mayPlaceOn(state, level, pos);
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 100;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }
}
