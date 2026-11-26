package net.bittercat.plerocottage.blocks;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.blockentities.PlateBlockEntity;
import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.bittercat.plerocottage.items.TieredSwordItem;
import net.bittercat.plerocottage.items.UtensilFoodInteraction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlateBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 2, 15);
    public PlateBlock() {
        this(BlockBehaviour.Properties.of()
            .mapColor(MapColor.SNOW)
            .sound(SoundType.GLASS)
            .strength(0.6F)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false));
    }

    protected PlateBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
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
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof PlateBlockEntity plate)) {
            return InteractionResult.PASS;
        }
        if (player.getItemInHand(hand).getItem() instanceof TieredSwordItem
            && hand == InteractionHand.MAIN_HAND && !player.isShiftKeyDown()
            && player.getAbilities().mayBuild && level.mayInteract(player, pos)) {
            InteractionResult eating = UtensilFoodInteraction.eatFromDisplay(level, plate, player);
            if (eating.consumesAction()) {
                return eating;
            }
        }
        ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty()) {
            for (int slot = 0; slot < plate.getContainerSize(); slot++) {
                if (plate.getItem(slot).isEmpty()) {
                    if (!level.isClientSide) {
                        ItemStack placed = held.copy();
                        placed.setCount(1);
                        plate.setItem(slot, placed);
                        if (!player.getAbilities().instabuild) {
                            held.shrink(1);
                        }
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1, 1);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            return InteractionResult.PASS;
        }
        if (held.isEmpty()) {
            for (int slot = plate.getContainerSize() - 1; slot >= 0; slot--) {
                ItemStack stored = plate.getItem(slot);
                if (!stored.isEmpty()) {
                    if (!level.isClientSide) {
                        player.getInventory().placeItemBackInInventory(plate.removeItem(slot, 1));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1, 1);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof PlateBlockEntity plate) {
            Containers.dropContents(level, pos, plate);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, moving);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof PlateBlockEntity plate
            ? AbstractContainerMenu.getRedstoneSignalFromContainer(plate) : 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntity(pos, state);
    }
}
