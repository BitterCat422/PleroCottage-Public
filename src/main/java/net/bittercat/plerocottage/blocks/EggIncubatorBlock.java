package net.bittercat.plerocottage.blocks;

import java.util.List;

import net.bittercat.plerocottage.blockentities.EggIncubatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.server.level.ServerLevel;

public final class EggIncubatorBlock extends BaseEntityBlock {
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
    private static final VoxelShape SHAPE = Shapes.join(box(0, 0, 0, 16, 16, 16),
        box(4, 14, 4, 12, 16, 12), BooleanOp.ONLY_FIRST);

    public EggIncubatorBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD)
            .strength(1.6F).lightLevel(state -> 15).noOcclusion().pushReaction(PushReaction.BLOCK)
            .hasPostProcess((state, level, pos) -> true)
            .emissiveRendering((state, level, pos) -> true)
            .isRedstoneConductor((state, level, pos) -> false));
        registerDefaultState(stateDefinition.any().setValue(HATCH, 0));
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
        lines.add(Component.literal(" §9incubator.hatching_mappings*3"));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, level, pos, oldState, moving);
        level.scheduleTick(pos, this, 4150);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        EggIncubatorHatching.tick(level, pos, state);
        level.scheduleTick(pos, this, 4150);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof EggIncubatorBlockEntity incubator)) return InteractionResult.PASS;
        ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty()) {
            if (!EggIncubatorHatching.hasHatchingMapping(held.getItem())) {
                return InteractionResult.PASS;
            }
            for (int slot = 0; slot < incubator.getContainerSize(); slot++) {
                if (incubator.getItem(slot).isEmpty()) {
                    if (!level.isClientSide) {
                        ItemStack placed = held.copy();
                        placed.setCount(1);
                        incubator.setItem(slot, placed);
                        level.setBlock(pos, state.setValue(HATCH, 0), 3);
                        if (!player.getAbilities().instabuild) held.shrink(1);
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1, 1);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            return InteractionResult.PASS;
        }
        for (int slot = incubator.getContainerSize() - 1; slot >= 0; slot--) {
            if (!incubator.getItem(slot).isEmpty()) {
                if (!level.isClientSide) {
                    player.getInventory().placeItemBackInInventory(incubator.removeItem(slot, 1));
                    if (incubator.isEmpty()) level.setBlock(pos, state.setValue(HATCH, 0), 3);
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1, 1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof EggIncubatorBlockEntity incubator) {
            Containers.dropContents(level, pos, incubator);
        }
        super.onRemove(state, level, pos, newState, moving);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EggIncubatorBlockEntity(pos, state);
    }
}
