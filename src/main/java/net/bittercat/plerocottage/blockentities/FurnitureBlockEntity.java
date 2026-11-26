package net.bittercat.plerocottage.blockentities;

import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.blocks.FurnitureBlock;
import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.bittercat.plerocottage.content.menus.MenuContent;
import net.bittercat.plerocottage.menus.FurnitureMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class FurnitureBlockEntity extends RandomizableContainerBlockEntity
    implements MenuProvider, WorldlyContainer {
    private NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);
    private final LazyOptional<? extends IItemHandler>[] handlers =
        SidedInvWrapper.create(this, Direction.values());
    private int viewers;

    protected FurnitureBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, items);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, items);
        }
    }

    public static final class Cabinet extends FurnitureBlockEntity {
        public Cabinet(BlockPos pos, BlockState state) {
            super(BlockEntityContent.CABINET.get(), pos, state);
        }

        @Override
        protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
            return new FurnitureMenu(MenuContent.CABINET.get(), id, inventory, this, false);
        }
    }

    public static final class Drawer extends FurnitureBlockEntity {
        public Drawer(BlockPos pos, BlockState state) {
            super(BlockEntityContent.DRAWER.get(), pos, state);
        }

        @Override
        protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
            return new FurnitureMenu(MenuContent.DRAWER.get(), id, inventory, this, true);
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide) {
            level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return IntStream.range(0, getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return true;
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("block.plerocottage." +
            net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()).getPath());
    }

    @Override
    public Component getDisplayName() {
        return getDefaultName();
    }

    public void openFor(Player player) {
        if (!level.isClientSide && viewers++ == 0) {
            setOpen(true);
        }
        super.startOpen(player);
    }

    public void closeFor(Player player) {
        super.stopOpen(player);
        if (!level.isClientSide && viewers > 0 && --viewers == 0) {
            setOpen(false);
        }
    }

    private void setOpen(boolean open) {
        BlockState state = getBlockState();
        if (state.hasProperty(FurnitureBlock.OPEN) && state.getValue(FurnitureBlock.OPEN) != open) {
            level.setBlock(worldPosition, state.setValue(FurnitureBlock.OPEN, open), 3);
            level.playSound(null, worldPosition, open ? openSound() : closeSound(),
                SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private SoundEvent openSound() {
        String path = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()).getPath();
        if (path.startsWith("cherry_") || path.startsWith("cherries_tree_")) {
            return SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN;
        }
        if (path.startsWith("bamboo_")) {
            return SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN;
        }
        if (path.startsWith("crimson_") || path.startsWith("warped_")) {
            return SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN;
        }
        return SoundEvents.WOODEN_TRAPDOOR_OPEN;
    }

    private SoundEvent closeSound() {
        String path = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock()).getPath();
        if (path.startsWith("cherry_") || path.startsWith("cherries_tree_")) {
            return SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE;
        }
        if (path.startsWith("bamboo_")) {
            return SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE;
        }
        if (path.startsWith("crimson_") || path.startsWith("warped_")) {
            return SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE;
        }
        return SoundEvents.WOODEN_TRAPDOOR_CLOSE;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public net.minecraft.nbt.CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (!isRemoved() && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            return handlers[facing.ordinal()].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }
}
