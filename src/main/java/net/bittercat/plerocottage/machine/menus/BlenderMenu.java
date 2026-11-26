package net.bittercat.plerocottage.machine.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.BlenderBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.init.MenuRegistry;
import net.bittercat.plerocottage.machine.recipes.BlenderRecipe;

import static net.bittercat.plerocottage.machine.blockentities.BlenderBlockEntity.*;

public class BlenderMenu extends RecipeBookMenu<BlenderBlockEntity> {
    public final Inventory playerInventory;
    public final BlenderBlockEntity blockEntity;
    public final ContainerData data;
    public int playerInventoryYOffset;

    public BlenderMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory,
                (BlenderBlockEntity) playerInventory.player.level().getBlockEntity(extraData.readBlockPos()),
                new SimpleContainerData(1));
    }

    public BlenderMenu(int containerId, Inventory playerInventory, BlenderBlockEntity blockEntity, ContainerData data) {
        super(MenuRegistry.BLENDER.get(), containerId);
        this.playerInventory = playerInventory;
        this.blockEntity = blockEntity;
        this.data = data;
        this.playerInventoryYOffset = 47;
        this.initMachineInventorySlot();
        this.initPlayerInventorySlot();
        this.addDataSlots(data);
    }

    public int getCookingTime() {
        return this.data.get(0);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.blockEntity.stopOpen(player);
    }

    private void initPlayerInventorySlot() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(this.playerInventory, column + row * 9 + 9,
                        8 + column * 18, 103 + row * 18 + this.playerInventoryYOffset));
            }
        }
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(this.playerInventory, column,
                    8 + column * 18, 161 + this.playerInventoryYOffset));
        }
    }

    public void initMachineInventorySlot() {
        int[][] inputs = {
                {MAIN_TOP_LEFT, 103, 54}, {MAIN_TOP_RIGHT, 129, 54},
                {MAIN_CENTER_LEFT, 103, 74}, {MAIN_CENTER_RIGHT, 129, 74},
                {MAIN_BOTTOM_LEFT, 103, 94}, {MAIN_BOTTOM_RIGHT, 129, 94},
                {SECONDARY_TOP_LEFT, 18, 14}, {SECONDARY_TOP_RIGHT, 38, 14},
                {SECONDARY_BOTTOM_LEFT, 18, 34}, {SECONDARY_BOTTOM_RIGHT, 38, 34}
        };
        for (int[] input : inputs) {
            this.addSlot(new Slot(this.blockEntity, input[0], input[1], input[2]) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return super.mayPlace(stack) && getCookingTime() == 0;
                }

                @Override
                public boolean mayPickup(Player player) {
                    return super.mayPickup(player) && getCookingTime() == 0;
                }

                @Override
                public int getMaxStackSize() {
                    return 8;
                }
            });
        }
        this.addSlot(new Slot(this.blockEntity, CONTAINER, 28, 63) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return super.mayPlace(stack)
                        && SlotInputLimitManager.BLENDER_INPUT_LIMIT.stream().anyMatch(ingredient -> ingredient.test(stack))
                        && getCookingTime() == 0;
            }

            @Override
            public boolean mayPickup(Player player) {
                return super.mayPickup(player) && getCookingTime() == 0;
            }

            @Override
            public int getMaxStackSize() {
                return 8;
            }
        });
        this.addOutputSlot(SECONDARY_OUTPUT, 28, 90);
        this.addOutputSlot(MAIN_OUTPUT, 28, 115);
    }

    private void addOutputSlot(int slotIndex, int x, int y) {
        this.addSlot(new Slot(this.blockEntity, slotIndex, x, y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return super.mayPickup(player) && getCookingTime() == 0;
            }

            @Override
            public boolean isActive() {
                return super.isActive() && this.hasItem();
            }
        });
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents helper) {
        MachineRecipeBookUtil.accountInventoryDamageable(helper, this.playerInventory);
        for (int slot : MAIN_INPUT_SLOTS) MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(slot));
        for (int slot : SECONDARY_INPUT_SLOTS) MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(slot));
        MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(CONTAINER));
    }

    @Override
    public void clearCraftingContent() {
        for (int slot : MAIN_INPUT_SLOTS) this.blockEntity.setItem(slot, ItemStack.EMPTY);
        for (int slot : SECONDARY_INPUT_SLOTS) this.blockEntity.setItem(slot, ItemStack.EMPTY);
        this.blockEntity.setItem(CONTAINER, ItemStack.EMPTY);
    }

    @Override
    public boolean recipeMatches(Recipe<? super BlenderBlockEntity> recipe) {
        if (!(recipe instanceof BlenderRecipe blenderRecipe)
                || !blenderRecipe.isInputMatch(this.blockEntity.getMainInput(), this.blockEntity.getSecondaryInput())) {
            return false;
        }
        if (!blenderRecipe.hasContainer()) {
            return true;
        }
        ItemStack container = this.blockEntity.getItem(CONTAINER);
        return blenderRecipe.isContainerMatch(container)
                && container.getCount() >= blenderRecipe.output.getCount();
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
        return PleroCottageMachine.RECIPE_TYPE_BLENDER;
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot >= 0 && slot < 11;
    }

    @Override
    public void handlePlacement(boolean useMaxItems, Recipe<?> recipe, ServerPlayer player) {
        if (this.blockEntity.isRunning || !(recipe instanceof BlenderRecipe blenderRecipe)
                || !player.getRecipeBook().contains(recipe)) {
            return;
        }
        BoundedRecipeBookPlacement.place(
                this, player, recipe, useMaxItems, 10,
                Math.max(blenderRecipe.output.getCount(), blenderRecipe.outgrowth.getCount())
        );
        this.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
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
        } else {
            boolean isContainer = SlotInputLimitManager.BLENDER_INPUT_LIMIT.stream()
                    .anyMatch(ingredient -> ingredient.test(stack));
            int start = isContainer ? 10 : 0;
            int end = isContainer ? 11 : 10;
            if (!this.moveItemStackTo(stack, start, end, false)) {
                return ItemStack.EMPTY;
            }
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
    public boolean stillValid(Player player) {
        return this.blockEntity.stillValid(player);
    }
}
