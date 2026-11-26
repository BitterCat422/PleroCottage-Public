package net.bittercat.plerocottage.items;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.blocks.VineSpecies;
import net.bittercat.plerocottage.blocks.VineStakeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class VinePlantingItem extends Item {

    private final VineSpecies species;
    private final Supplier<? extends Block> stake;
    private final Supplier<? extends Block> wildVine;
    private final int useDuration;

    public VinePlantingItem(VineSpecies species, Supplier<? extends Block> stake,
                            Supplier<? extends Block> wildVine, Properties properties, int useDuration) {
        super(properties);
        this.species = species;
        this.stake = stake;
        this.wildVine = wildVine;
        this.useDuration = useDuration;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useDuration;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState clicked = level.getBlockState(pos);

        if (context.getClickedFace() == Direction.DOWN && !clicked.canBeReplaced()) {
            BlockPos below = pos.below();
            if (level.getBlockState(below).canBeReplaced()) {
                return plant(level, context, below, wildVine.get().defaultBlockState());
            }
        }

        BlockPos stakePos = stakePos(context, level, clicked);
        if (stakePos == null) {
            return InteractionResult.PASS;
        }
        BlockState stake = level.getBlockState(stakePos);
        if (stake.getValue(VineStakeBlock.SPECIES) != VineSpecies.EMPTY) {
            return InteractionResult.PASS;
        }
        return plant(level, context, stakePos, stake.setValue(VineStakeBlock.SPECIES, species));
    }

    private InteractionResult plant(Level level, UseOnContext context, BlockPos pos, BlockState state) {
        if (!level.isClientSide) {
            level.setBlock(pos, state, 3);
            SoundType sound = state.getSoundType();
            level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS,
                (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
            Player player = context.getPlayer();
            if (player != null && !player.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    private BlockPos stakePos(UseOnContext context, Level level, BlockState clicked) {
        BlockPos pos = context.getClickedPos();
        if (clicked.is(stake.get())) {
            return level.getBlockState(pos.below()).is(BlockTags.DIRT) ? pos : null;
        }
        if (context.getClickedFace() == Direction.UP && clicked.is(BlockTags.DIRT)) {
            BlockPos above = pos.above();
            return level.getBlockState(above).is(stake.get()) ? above : null;
        }
        return null;
    }
}
