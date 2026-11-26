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
import net.bittercat.plerocottage.machine.blockentities.TeapotBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.init.MenuRegistry;
import net.bittercat.plerocottage.machine.recipes.TeapotRecipe;

import static net.bittercat.plerocottage.machine.blockentities.TeapotBlockEntity.*;

public class TeapotMenu extends RecipeBookMenu<TeapotBlockEntity> {
    public final Inventory playerInventory;
    public final TeapotBlockEntity blockEntity;
    public ContainerData data;
    public int playerInventoryYOffset;

    public TeapotMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory,
                (TeapotBlockEntity) pPlayerInventory.player.level().getBlockEntity(extraData.readBlockPos()),
                new SimpleContainerData(1));
    }

    public TeapotMenu(int pContainerId, Inventory pPlayerInventory, TeapotBlockEntity blockEntity, ContainerData data) {
        super(MenuRegistry.TEAPOT.get(), pContainerId);
        this.playerInventory = pPlayerInventory;
        this.blockEntity = blockEntity;
        this.data = data;
        this.playerInventoryYOffset = 42;
        this.initMachineInventorySlot();
        this.initPlayerInventorySlot();
        this.addDataSlots(data);
    }

    public int getCookingTime() {
        return this.data.get(0);
    }

    private void initMachineInventorySlot() {
        this.addSlot(new Slot(this.blockEntity, CONTAINER, 33, 90) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return super.mayPlace(stack)
                        && SlotInputLimitManager.TEAPOT_CUP_LIMIT.stream().anyMatch(ingredient -> ingredient.test(stack))
                        && getCookingTime() == 0;
            }

            @Override
            public boolean mayPickup(Player player) {
                return super.mayPickup(player) && getCookingTime() == 0;
            }

            @Override
            public int getMaxStackSize() {
                return 4;
            }
        });
        this.addSlot(new Slot(this.blockEntity, DRINK, 121, 41) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return super.mayPlace(stack)
                        && SlotInputLimitManager.TEAPOT_DRINK_LIMIT.stream().anyMatch(ingredient -> ingredient.test(stack));
            }

            @Override
            public int getMaxStackSize() {
                return 4;
            }
        });
        this.addSlot(new Slot(this.blockEntity, MATERIAL, 96, 118) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return super.mayPlace(stack)
                        && SlotInputLimitManager.TEAPOT_MATERIAL_LIMIT.stream().anyMatch(ingredient -> ingredient.test(stack));
            }

            @Override
            public int getMaxStackSize() {
                return 4;
            }
        });
        int[][] slots = {
                {TOP_LEFT, 30, 9}, {TOP_RIGHT, 54, 9},
                {BOTTOM_LEFT, 30, 32}, {BOTTOM_RIGHT, 54, 32}
        };
        for (int[] slot : slots) {
            this.addSlot(new Slot(this.blockEntity, slot[0], slot[1], slot[2]) {
                @Override
                public int getMaxStackSize() {
                    return 4;
                }
            });
        }
        this.addSlot(new Slot(this.blockEntity, RESULT, 33, 71) {
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

    @Override
    public void fillCraftSlotsStackedContents(StackedContents helper) {
        MachineRecipeBookUtil.accountInventoryDamageable(helper, this.playerInventory);
        MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(CONTAINER));
        for (int slot : INPUT_SLOTS) {
            MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(slot));
        }
    }

    @Override
    public void clearCraftingContent() {
        this.blockEntity.setItem(CONTAINER, ItemStack.EMPTY);
        for (int slot : INPUT_SLOTS) {
            this.blockEntity.setItem(slot, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean recipeMatches(Recipe<? super TeapotBlockEntity> recipe) {
        return recipe instanceof TeapotRecipe teapotRecipe
                && teapotRecipe.matches(this.blockEntity, this.blockEntity.getLevel());
    }

    @Override
    public int getResultSlotIndex() {
        return RESULT;
    }

    @Override
    public int getGridWidth() {
        return INPUT_SLOTS.length + 1;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return ITEMS_SIZE - 1;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return PleroCottageMachine.RECIPE_TYPE_TEAPOT;
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot >= 0 && slot < this.getSize();
    }

    @Override
    public void handlePlacement(boolean useMaxItems, Recipe<?> recipe, ServerPlayer player) {
        if (this.blockEntity.isRunning || this.getCookingTime() != 0
                || !(recipe instanceof TeapotRecipe)
                || !player.getRecipeBook().contains(recipe)
                || !this.blockEntity.getItem(RESULT).isEmpty()) {
            return;
        }
        TeapotRecipe teapotRecipe = (TeapotRecipe) recipe;
        BoundedRecipeBookPlacement.place(this, player, recipe, useMaxItems, 0,
                teapotRecipe.output.getCount());
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
    public boolean stillValid(Player player) {
        return this.blockEntity.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.blockEntity.stopOpen(player);
    }
}
