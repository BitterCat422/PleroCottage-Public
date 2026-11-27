package net.bittercat.plerocottage.machine.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.OvenBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.machine.compat.plerocottage.SmokingRecipeManager;
import net.bittercat.plerocottage.init.MenuRegistry;
import net.bittercat.plerocottage.machine.recipes.OvenRecipe;

import static net.bittercat.plerocottage.machine.blockentities.OvenBlockEntity.*;

public class OvenMenu extends RecipeBookMenu<OvenBlockEntity> {
    public static final int CONTAINER_MENU_SLOT = INPUT_SLOTS.length;
    public static final int OUTPUT_MENU_SLOT = CONTAINER_MENU_SLOT + 1;
    public final Inventory playerInventory;
    public OvenBlockEntity blockEntity;
    public ContainerData data;
    public int playerInventoryYOffset;

    public OvenMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory, (OvenBlockEntity) pPlayerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public OvenMenu(int pContainerId, Inventory pPlayerInventory, OvenBlockEntity blockEntity, ContainerData data) {
        super(MenuRegistry.OVEN.get(), pContainerId);
        this.playerInventory = pPlayerInventory;
        this.blockEntity = blockEntity;
        this.data = data;
        this.playerInventoryYOffset = 42;
        this.initMachineInventorySlot();
        this.addDataSlots(data);
    }

    public Temperature getTemperature() {
        return switch (this.data.get(0)) {
            case 1, 150 -> Temperature.ONE_HUNDRED_FIFTY;
            case 2, 200 -> Temperature.TWO_HUNDRED;
            case 3, 250 -> Temperature.TWO_HUNDRED_FIFTY;
            default -> Temperature.ZERO;
        };
    }

    public int getCookingTime() {
        return this.data.get(1);
    }

    public MaxCookingTime getMaxCookingTime() {
        return switch (this.data.get(2)) {
            case 1, 5, 100 -> MaxCookingTime.FIVE;
            case 2, 10, 200 -> MaxCookingTime.TEN;
            case 3, 15, 300 -> MaxCookingTime.FIFTEEN;
            default -> MaxCookingTime.ZERO;
        };
    }

    public void setMaxCookingTime(int value) {
        this.data.set(2, value);
    }

    public void initMachineInventorySlot() {
        int[][] slots = {
                {TOP_LEFT, 45, 58},
                {TOP_CENTER, 69, 58},
                {TOP_RIGHT, 93, 58},
                {TOP_FAR_RIGHT, 117, 58},
                {BOTTOM_LEFT, 45, 78},
                {BOTTOM_CENTER, 69, 78},
                {BOTTOM_RIGHT, 93, 78},
                {BOTTOM_FAR_RIGHT, 117, 78}
        };
        for (int[] slot : slots) {
            this.addSlot(new Slot(this.blockEntity, slot[0], slot[1], slot[2]) {
                @Override
                public boolean mayPlace(ItemStack pStack) {
                    return super.mayPlace(pStack)
                            && SlotInputLimitManager.OVEN_INPUT_LIMIT.stream().anyMatch(ingredient -> ingredient.test(pStack))
                            && isIdle();
                }

                @Override
                public boolean mayPickup(Player pPlayer) {
                    return super.mayPickup(pPlayer) && isIdle();
                }

                @Override
                public int getMaxStackSize() {
                    return 8;
                }
            });
        }
        this.addSlot(new Slot(this.blockEntity, CONTAINER, 81, 99) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return super.mayPlace(pStack)
                        && SlotInputLimitManager.OVEN_CONTAINER_LIMIT.stream()
                        .anyMatch(ingredient -> ingredient.test(pStack))
                        && isIdle();
            }

            @Override
            public boolean mayPickup(Player pPlayer) {
                return super.mayPickup(pPlayer) && isIdle();
            }

            @Override
            public int getMaxStackSize() {
                return 8;
            }
        });
        this.addSlot(new Slot(this.blockEntity, OUTPUT, 147, 100) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player pPlayer) {
                return super.mayPickup(pPlayer) && isIdle();
            }

            @Override
            public boolean isActive() {
                return super.isActive() && this.hasItem();
            }
        });
        this.initPlayerInventorySlot();
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
    public boolean recipeMatches(Recipe<? super OvenBlockEntity> recipe) {
        if (recipe instanceof OvenRecipe ovenRecipe) {
            return this.blockEntity.getItem(OUTPUT).isEmpty()
                    && (!ovenRecipe.hasContainer()
                    || (ovenRecipe.isContainerMatch(this.blockEntity.getItem(CONTAINER))
                    && this.blockEntity.getItem(CONTAINER).getCount() >= ovenRecipe.output.getCount()))
                    && ovenRecipe.isInputMatch(this.blockEntity.getInput());
        }
        if (recipe instanceof SmokingRecipe smokingRecipe) {
            return this.blockEntity.getItem(OUTPUT).isEmpty()
                    && SmokingRecipeManager.toOvenRecipe(smokingRecipe).isInputMatch(this.blockEntity.getInput());
        }
        return false;
    }

    @Override
    public int getResultSlotIndex() {
        return OUTPUT_MENU_SLOT;
    }

    @Override
    public int getGridWidth() {
        return 4;
    }

    @Override
    public int getGridHeight() {
        return 2;
    }

    @Override
    public int getSize() {
        return ITEMS_SIZE;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return PleroCottageMachine.RECIPE_TYPE_OVEN;
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot >= 0 && slot < OUTPUT_MENU_SLOT;
    }

    @Override
    public void handlePlacement(boolean useMaxItems, Recipe<?> recipe, ServerPlayer player) {
        if (this.blockEntity.isRunning || this.getCookingTime() != 0
                || !this.blockEntity.getItem(OUTPUT).isEmpty()
                || !player.getRecipeBook().contains(recipe)) {
            return;
        }
        if (recipe instanceof OvenRecipe ovenRecipe) {
            this.blockEntity.setTemperature(ovenRecipe.temperature);
            this.blockEntity.setMaxCookingTime(ovenRecipe.maxCookingTime);
        } else if (recipe instanceof SmokingRecipe) {
            this.blockEntity.setTemperature(200);
            this.blockEntity.setMaxCookingTime(5);
        } else {
            return;
        }
        int containerSlot = recipe instanceof OvenRecipe ovenRecipe && ovenRecipe.hasContainer()
                ? CONTAINER_MENU_SLOT
                : -1;
        int containersPerBatch = recipe instanceof OvenRecipe ovenRecipe
                ? ovenRecipe.output.getCount()
                : 1;
        BoundedRecipeBookPlacement.place(this, player, recipe, useMaxItems, containerSlot, containersPerBatch);
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
            if (!this.moveItemStackTo(stack, 0, OUTPUT_MENU_SLOT, false)) {
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

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.blockEntity.stopOpen(player);
    }

    /** 启动的那一刻进度还是 0，所以要连同运行标记一起判断。 */
    private boolean isIdle() {
        return !this.blockEntity.isRunning && this.getCookingTime() == 0;
    }
}
