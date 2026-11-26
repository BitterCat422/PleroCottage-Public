package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class StrippablePillarBlock extends RotatedPillarBlock {

    private final Supplier<? extends Block> stripped;

    public StrippablePillarBlock(BlockBehaviour.Properties properties, Supplier<? extends Block> stripped) {
        super(properties);
        this.stripped = stripped;
    }

    @Override
    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction,
                                           boolean simulate) {
        
        if (toolAction == ToolActions.AXE_STRIP
            && context.getItemInHand().canPerformAction(ToolActions.AXE_STRIP)) {
            
            return stripped.get().defaultBlockState()
                .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}
