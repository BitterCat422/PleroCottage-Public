package net.bittercat.plerocottage.menus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import net.bittercat.plerocottage.blockentities.FurnitureBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class FurnitureMenu extends AbstractContainerMenu {
    private final FurnitureBlockEntity furniture;
    private final ContainerLevelAccess access;
    private final boolean drawer;

    public FurnitureMenu(MenuType<?> type, int id, Inventory inventory, FriendlyByteBuf data, boolean drawer) {
        this(type, id, inventory,
            inventory.player.level().getBlockEntity(data.readBlockPos()) instanceof FurnitureBlockEntity entity
                ? entity : null,
            drawer);
    }

    public FurnitureMenu(MenuType<?> type, int id, Inventory inventory,
                         FurnitureBlockEntity furniture, boolean drawer) {
        super(type, id);
        this.drawer = drawer;
        this.furniture = furniture;
        this.access = furniture == null
            ? ContainerLevelAccess.NULL
            : ContainerLevelAccess.create(furniture.getLevel(), furniture.getBlockPos());
        if (furniture != null) {
            for (int slot = 0; slot < 36; slot++) {
                int row = slot / 9;
                int[] cabinetRows = {18, 36, 56, 74};
                int[] drawerRows = {18, 36, 66, 84};
                addSlot(new Slot(furniture, slot, 8 + slot % 9 * 18,
                    drawer ? drawerRows[row] : cabinetRows[row]));
            }
            furniture.openFor(inventory.player);
        }
        int inventoryY = drawer ? 115 : 105;
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18,
                    inventoryY + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, inventoryY + 58));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return furniture != null && AbstractContainerMenu.stillValid(access, player,
            furniture.getBlockState().getBlock());
    }

    @Override
    public boolean clickMenuButton(Player player, int button) {
        if (furniture == null || button < 0 || button > (drawer ? 1 : 0)) {
            return false;
        }
        if (player instanceof ServerPlayer) {
            int first = drawer ? button * 18 : 0;
            sortRange(first, first + (drawer ? 18 : 36));
        }
        return true;
    }

    private void sortRange(int start, int end) {
        List<ItemStack> stacks = new ArrayList<>();
        List<net.minecraft.world.item.Item> registryOrder = BuiltInRegistries.ITEM.stream().toList();
        Map<net.minecraft.world.item.Item, Integer> positions = new HashMap<>();
        for (int index = 0; index < registryOrder.size(); index++) {
            positions.put(registryOrder.get(index), index);
        }
        for (int slot = start; slot < end; slot++) {
            ItemStack stack = furniture.getItem(slot);
            if (!stack.isEmpty()) {
                stacks.add(stack.copy());
            }
        }
        stacks.sort(Comparator.comparingInt(stack ->
            positions.getOrDefault(stack.getItem(), Integer.MAX_VALUE)));
        List<ItemStack> merged = new ArrayList<>();
        for (ItemStack stack : stacks) {
            for (ItemStack existing : merged) {
                if (ItemStack.isSameItemSameTags(existing, stack) && existing.getCount() < existing.getMaxStackSize()) {
                    int moved = Math.min(stack.getCount(), existing.getMaxStackSize() - existing.getCount());
                    existing.grow(moved);
                    stack.shrink(moved);
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
            if (!stack.isEmpty()) {
                merged.add(stack);
            }
        }
        for (int slot = start; slot < end; slot++) {
            furniture.setItem(slot, ItemStack.EMPTY);
        }
        int slot = start;
        for (ItemStack stack : merged) {
            while (!stack.isEmpty() && slot < end) {
                ItemStack placed = stack.copy();
                placed.setCount(Math.min(stack.getCount(), stack.getMaxStackSize()));
                furniture.setItem(slot++, placed);
                stack.shrink(placed.getCount());
            }
        }
        furniture.setChanged();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (furniture != null) {
            furniture.closeFor(player);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return empty;
        }
        ItemStack source = slot.getItem();
        ItemStack original = source.copy();
        int containerSlots = 36;
        if (index < containerSlots) {
            if (!moveItemStackTo(source, containerSlots, slots.size(), true)) {
                return empty;
            }
        } else if (!moveItemStackTo(source, 0, containerSlots, false)) {
            return empty;
        }
        if (source.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return source.getCount() == original.getCount() ? empty : original;
    }
}
