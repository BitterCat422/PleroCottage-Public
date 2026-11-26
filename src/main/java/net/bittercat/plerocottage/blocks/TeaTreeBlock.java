package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TeaTreeBlock extends BerryBushBlock {

    public TeaTreeBlock(Settings settings) {
        super(settings, BlockBehaviour.Properties.of()
            .sound(SoundType.AZALEA)
            .strength(2.0F)
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .ignitedByLava());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AGE) <= 1 ? Shapes.empty() : getShape(state, level, pos, context);
    }

    @Override
    protected void dropHarvest(Level level, BlockPos pos, ItemStack stack) {
        ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, stack);
        drop.setPickUpDelay(10);
        level.addFreshEntity(drop);
    }
}
