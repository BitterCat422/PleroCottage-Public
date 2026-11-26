package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.content.plants.Melons;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class HamimelonBlock extends StemGrownBlock {
    public HamimelonBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).sound(SoundType.WOOD)
            .strength(1.0F).pushReaction(PushReaction.DESTROY));
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    @Override
    public StemBlock getStem() {
        return (StemBlock) Melons.HAMIMELON_STEM.get();
    }

    @Override
    public AttachedStemBlock getAttachedStem() {
        return (AttachedStemBlock) Melons.HAMIMELON_ATTACHED_STEM.get();
    }
}
