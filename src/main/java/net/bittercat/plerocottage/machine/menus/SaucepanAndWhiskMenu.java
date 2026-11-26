package net.bittercat.plerocottage.machine.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.recipes.SaucepanAndWhiskRecipe;
import net.bittercat.plerocottage.machine.blockentities.SaucepanAndWhiskBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.init.MenuRegistry;

import static net.bittercat.plerocottage.machine.blockentities.SaucepanAndWhiskBlockEntity.*;

public class SaucepanAndWhiskMenu extends RecipeBookMenu<SaucepanAndWhiskBlockEntity> {
    public SaucepanAndWhiskBlockEntity blockEntity;
    public final Inventory playerInventory;
    public ContainerData data;
    public int playerInventoryYOffset;

    public SaucepanAndWhiskMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory, (SaucepanAndWhiskBlockEntity) pPlayerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public SaucepanAndWhiskMenu(int pContainerId, Inventory pPlayerInventory, SaucepanAndWhiskBlockEntity blockEntity, ContainerData data) {
        super(MenuRegistry.SAUCEPAN_AND_WHISK.get(), pContainerId);
        this.playerInventory = pPlayerInventory;
        this.blockEntity = blockEntity;
        this.data = data;
        this.playerInventoryYOffset = 51;
        this.initMachineInventorySlot();
        this.addDataSlots(data);
    }

    public int getStirsCount() {
        return this.data.get(0);
    }

    @Override
    public void removed(net.minecraft.world.entity.player.Player player) {
        super.removed(player);
        this.blockEntity.stopOpen(player);
    }

    public void initMachineInventorySlot() {
        int[][] slots = {
                {MAIN_TOP_LEFT, 61, 67},
                {MAIN_TOP_CENTER, 80, 67},
                {MAIN_TOP_RIGHT, 99, 67},
                {MAIN_BOTTOM_LEFT, 61, 87},
                {MAIN_BOTTOM_CENTER, 80, 87},
                {MAIN_BOTTOM_RIGHT, 99, 87},
                {SECONDARY_TOP_LEFT, 8, 27},
                {SECONDARY_TOP_RIGHT, 26, 27},
                {SECONDARY_BOTTOM_LEFT, 8, 45},
                {SECONDARY_BOTTOM_RIGHT, 26, 45},
        };
        for (int[] slot : slots) {
            this.addSlot(new Slot(this.blockEntity, slot[0], slot[1], slot[2]) {
                @Override
                public int getMaxStackSize() {
                    return 4;
                }
            });
        }
        this.addSlot(new Slot(this.blockEntity, CONTAINER, 80, 47) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return super.mayPlace(pStack) && SlotInputLimitManager.SAUCEPAN_AND_WHISK_INPUT_LIMIT.stream().anyMatch(ingredient -> ingredient.test(pStack));
            }

            @Override
            public int getMaxStackSize() {
                return 4;
            }
        });
        this.addSlot(new Slot(this.blockEntity, RESULT, 80, 24) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }

            @Override
            public boolean isActive() {
                return super.isActive() && this.hasItem();
            }

            @Override
            public int getMaxStackSize() {
                return 64;
            }
        });
        this.initPlayerInventorySlot();
    }

    private void initPlayerInventorySlot() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(this.playerInventory, column + row * 9 + 9,
                        6 + column * 18, 103 + row * 18 + this.playerInventoryYOffset));
            }
        }
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(this.playerInventory, column,
                    6 + column * 18, 161 + this.playerInventoryYOffset));
        }
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents helper) {
        MachineRecipeBookUtil.accountInventoryDamageable(helper, this.playerInventory);
        for (int slot : MAIN_INPUT_SLOTS) {
            MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(slot));
        }
        for (int slot : SECONDARY_INPUT_SLOTS) {
            MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(slot));
        }
        MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(CONTAINER));
    }

    @Override
    public void clearCraftingContent() {
        for (int slot : MAIN_INPUT_SLOTS) {
            this.blockEntity.setItem(slot, ItemStack.EMPTY);
        }
        for (int slot : SECONDARY_INPUT_SLOTS) {
            this.blockEntity.setItem(slot, ItemStack.EMPTY);
        }
        this.blockEntity.setItem(CONTAINER, ItemStack.EMPTY);
    }

    @Override
    public boolean recipeMatches(Recipe<? super SaucepanAndWhiskBlockEntity> recipe) {
        return recipe instanceof SaucepanAndWhiskRecipe saucepanRecipe
                && saucepanRecipe.matches(this.blockEntity, this.blockEntity.getLevel());
    }

    @Override
    public int getResultSlotIndex() {
        return 11;
    }

    @Override
    public int getGridWidth() {
        return 11;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 12;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return PleroCottageMachine.RECIPE_TYPE_SAUCEPAN_AND_WHISK;
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot >= 0 && slot < 11;
    }

    @Override
    public void handlePlacement(boolean useMaxItems, Recipe<?> recipe, ServerPlayer player) {
        if (!(recipe instanceof SaucepanAndWhiskRecipe)
                || !player.getRecipeBook().contains(recipe)
                || !this.blockEntity.getItem(RESULT).isEmpty()) {
            return;
        }
        BoundedRecipeBookPlacement.place(this, player, recipe, useMaxItems, 10, 1);
        this.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(net.minecraft.world.entity.player.Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem() || !slot.mayPickup(player)) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < ITEMS_SIZE) {
            if (!this.moveItemStackTo(stack, ITEMS_SIZE, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(stack, 0, ITEMS_SIZE, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        slot.onTake(player, stack);
        return original;
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return this.blockEntity.stillValid(player);
    }
}
