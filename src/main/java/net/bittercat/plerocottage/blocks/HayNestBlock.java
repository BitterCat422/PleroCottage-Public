package net.bittercat.plerocottage.blocks;

import java.util.List;

import net.bittercat.plerocottage.blockentities.HayNestBlockEntity;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class HayNestBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Shapes.join(
        Shapes.or(box(1, 0, 1, 15, 4, 15), box(0, 2, 0, 16, 6, 16)),
        box(4, 2, 4, 12, 6, 12), BooleanOp.ONLY_FIRST);

    public HayNestBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).sound(SoundType.GRASS)
            .strength(0.6F).noOcclusion().pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, level, lines, flag);
        lines.add(Component.translatable("tips.plerocottage.can_put_in_items"));
        lines.add(Component.literal(" §9#forge:eggs*3"));
        lines.add(Component.literal(" §9#forge:feathers*3"));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof HayNestBlockEntity nest)) return InteractionResult.PASS;
        ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty()) {
            boolean feather = held.is(ItemTags.create(new ResourceLocation("forge", "feathers")));
            boolean egg = held.is(ItemTags.create(new ResourceLocation("forge", "eggs")));
            boolean specialEgg = held.is(ItemTags.create(new ResourceLocation("plerocottage", "can_place_on_nest")));
            if (!specialEgg) return InteractionResult.PASS;
            int first = feather ? 3 : 0;
            int last = feather ? 6 : 3;
            for (int slot = first; slot < last; slot++) {
                if (nest.getItem(slot).isEmpty()) {
                    if (!level.isClientSide) {
                        ItemStack placed = held.copy();
                        placed.setCount(1);
                        nest.setItem(slot, placed);
                        if (!player.getAbilities().instabuild) held.shrink(1);
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1, 1);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            return InteractionResult.PASS;
        }
        for (int slot : new int[] {2, 1, 0, 5, 4, 3}) {
            if (!nest.getItem(slot).isEmpty()) {
                if (!level.isClientSide) {
                    player.getInventory().placeItemBackInInventory(nest.removeItem(slot, 1));
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1, 1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof HayNestBlockEntity nest
            ? AbstractContainerMenu.getRedstoneSignalFromContainer(nest) : 0;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof HayNestBlockEntity nest) {
            Containers.dropContents(level, pos, nest);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, moving);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HayNestBlockEntity(pos, state);
    }
}
