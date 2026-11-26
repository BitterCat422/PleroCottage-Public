package net.bittercat.plerocottage.items;

import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class SoilBoundSeedItem extends ItemNameBlockItem {

    private final Predicate<BlockState> soil;

    public SoilBoundSeedItem(Block block, Properties properties, Predicate<BlockState> soil) {
        super(block, properties);
        this.soil = soil;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        BlockPos soilPos = context.getClickedPos().below();
        return super.canPlace(context, state) && soil.test(context.getLevel().getBlockState(soilPos));
    }
}
