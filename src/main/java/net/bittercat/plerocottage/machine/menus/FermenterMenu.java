package net.bittercat.plerocottage.machine.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
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
import net.bittercat.plerocottage.machine.blockentities.FermenterBlockEntity;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.init.MenuRegistry;
import net.bittercat.plerocottage.machine.recipes.BaseFermentationRecipe;

import static net.bittercat.plerocottage.machine.blockentities.FermenterBlockEntity.*;

public class FermenterMenu extends RecipeBookMenu<FermenterBlockEntity> {
    public static final int[] MATERIAL_MENU_SLOTS = {0, 1, 2, 3};
    public static final int CONTAINER_MENU_SLOT = 4;
    public static final int OUTPUT_MENU_SLOT = 5;
    public final Inventory playerInventory;
    public int playerInventoryYOffset;
    public FermenterBlockEntity blockEntity;
    public ContainerData data;

    public FermenterMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory, (FermenterBlockEntity) pPlayerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public FermenterMenu(int pContainerId, Inventory pPlayerInventory, FermenterBlockEntity blockEntity, ContainerData data) {
        super(MenuRegistry.FERMENTER.get(), pContainerId);
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

    public int getMaxCookingTime() {
        return this.data.get(1);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.blockEntity.stopOpen(pPlayer);
    }

    public void initMachineInventorySlot() {
        int[][] slots = new int[][]{
                {TOP_LEFT, 40, 50},
                {TOP_RIGHT, 72, 50},
                {BOTTOM_LEFT, 40, 76},
                {BOTTOM_RIGHT, 72, 76},
        };
        for (int[] slot : slots) {
            this.addSlot(new Slot(this.blockEntity, slot[0], slot[1], slot[2]) {
                @Override
                public boolean mayPlace(ItemStack pStack) {
                    return super.mayPlace(pStack) && isIdle();
                }

                @Override
                public boolean mayPickup(Player pPlayer) {
                    return super.mayPickup(pPlayer) && isIdle();
                }

                @Override
                public int getMaxStackSize() {
                    return 4;
                }
            });
        }
        this.addSlot(new Slot(this.blockEntity, CONTAINER, 121, 116) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return super.mayPlace(pStack)
                        && SlotInputLimitManager.FERMENTER_INPUT_LIMIT.stream().anyMatch(ingredient -> ingredient.test(pStack))
                        && isIdle();
            }

            @Override
            public boolean mayPickup(Player pPlayer) {
                return super.mayPickup(pPlayer) && isIdle();
            }
        });
        this.addSlot(new Slot(this.blockEntity, OUTPUT, 57, 63) {
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
        int[][] catalystSlots = {
                {CATALYSTS_TOP, 144, 48},
                {CATALYSTS_BOTTOM, 144, 74}
        };
        for (int[] slot : catalystSlots) {
            this.addSlot(new Slot(this.blockEntity, slot[0], slot[1], slot[2]) {
                @Override
                public boolean mayPlace(ItemStack pStack) {
                    return super.mayPlace(pStack)
                            && PleroCottageCompat.isCatalysts(pStack)
                            && isIdle();
                }

                @Override
                public boolean mayPickup(Player pPlayer) {
                    return super.mayPickup(pPlayer) && isIdle();
                }

                @Override
                public boolean isActive() {
                    return super.isActive()
                            && isIdle()
                            && blockEntity.getItem(OUTPUT).isEmpty();
                }
            });
        }
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
        for (int slot : MATERIAL_SLOTS) {
            MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(slot));
        }
    }

    @Override
    public void clearCraftingContent() {
        this.blockEntity.setItem(CONTAINER, ItemStack.EMPTY);
        for (int slot : MATERIAL_SLOTS) {
            this.blockEntity.setItem(slot, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean recipeMatches(Recipe<? super FermenterBlockEntity> recipe) {
        return recipe instanceof BaseFermentationRecipe fermentationRecipe
                && this.isIdle()
                && fermentationRecipe.matches(this.blockEntity, this.blockEntity.getLevel());
    }

    @Override
    public int getResultSlotIndex() {
        return OUTPUT_MENU_SLOT;
    }

    @Override
    public int getGridWidth() {
        return FermenterBlockEntity.INPUT_SLOTS.length;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return FermenterBlockEntity.INPUT_SLOTS.length + 1;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return PleroCottageMachine.RECIPE_TYPE_FERMENTER;
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot >= 0 && slot < FermenterBlockEntity.INPUT_SLOTS.length;
    }

    @Override
    public void handlePlacement(boolean useMaxItems, Recipe<?> recipe, ServerPlayer player) {
        if (this.blockEntity.isRunning || this.getCookingTime() != 0
                || !this.blockEntity.getItem(OUTPUT).isEmpty()
                || !player.getRecipeBook().contains(recipe)) {
            return;
        }
        if (!(recipe instanceof BaseFermentationRecipe fermentationRecipe)) {
            return;
        }
        if (fermentationRecipe.lightState != this.blockEntity.getLightState()) {
            player.connection.send(new ClientboundPlaceGhostRecipePacket(this.containerId, recipe));
            return;
        }
        BoundedRecipeBookPlacement.place(this, player, recipe, useMaxItems, 4,
                fermentationRecipe.output.getCount());
        this.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= this.slots.size()) {
            return ItemStack.EMPTY;
        }
        Slot slot = this.slots.get(index);
        if (!slot.hasItem() || !slot.mayPickup(player)) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < FermenterBlockEntity.ITEMS_SIZE) {
            if (!this.moveItemStackTo(stack, FermenterBlockEntity.ITEMS_SIZE, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(stack, 0, FermenterBlockEntity.ITEMS_SIZE, false)) {
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

    /** 启动的那一刻进度还是 0，所以要连同运行标记一起判断。 */
    private boolean isIdle() {
        return !this.blockEntity.isRunning && this.getCookingTime() == 0;
    }
}
