package net.bittercat.plerocottage.blocks;

import java.util.List;

import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.bittercat.plerocottage.blockentities.ColoredShulkerBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.BlockGetter;

public final class ColoredShulkerBoxBlock extends ShulkerBoxBlock {
    private final String color;

    public ColoredShulkerBoxBlock(String color) {
        super(null, BlockBehaviour.Properties.of()
            .mapColor(mapColor(color))
            .strength(2.0F)
            .sound(net.minecraft.world.level.block.SoundType.STONE)
            .noOcclusion());
        this.color = color;
    }

    public String colorName() {
        return color;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ColoredShulkerBoxBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityContent.COLORED_SHULKER_BOX.get(),
            net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity::tick);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ColoredShulkerBoxBlockEntity colored) {
            colored.saveToItem(stack);
        }
        return stack;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        BlockEntity blockEntity = builder.getOptionalParameter(
            net.minecraft.world.level.storage.loot.parameters.LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof ColoredShulkerBoxBlockEntity shulker && !drops.isEmpty()) {
            shulker.saveToItem(drops.get(0));
        }
        return drops;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!level.isClientSide && player.isCreative()
            && blockEntity instanceof ColoredShulkerBoxBlockEntity shulker && !shulker.isEmpty()) {
            ItemStack stack = new ItemStack(this);
            shulker.saveToItem(stack);
            if (shulker.hasCustomName()) {
                stack.setHoverName(shulker.getCustomName());
            }
            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D,
                pos.getZ() + 0.5D, stack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
            shulker.clearContent();
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    private static MapColor mapColor(String color) {
        return switch (color) {
            case "lavender" -> MapColor.COLOR_PURPLE;
            case "turquoise" -> MapColor.DIAMOND;
            default -> MapColor.SNOW;
        };
    }
}
