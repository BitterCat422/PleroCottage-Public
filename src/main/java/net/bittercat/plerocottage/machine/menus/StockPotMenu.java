package net.bittercat.plerocottage.machine.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.StockPotBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CookingPotRecipeCompat;
import net.bittercat.plerocottage.machine.compat.farmersdelight.FarmersDelightCompat;
import net.bittercat.plerocottage.machine.client.recipebook.StockPotRecipeBookCategories;
import net.bittercat.plerocottage.init.MenuRegistry;
import net.bittercat.plerocottage.machine.recipes.StockPotRecipe;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import net.minecraft.client.RecipeBookCategories;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static net.bittercat.plerocottage.machine.blockentities.StockPotBlockEntity.*;

public class StockPotMenu extends RecipeBookMenu<RecipeWrapper> {
    public final Inventory playerInventory;
    public int playerInventoryYOffset;
    public StockPotBlockEntity blockEntity;
    public ContainerData data;

    public StockPotMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory, (StockPotBlockEntity) pPlayerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(21));
    }

    public StockPotMenu(int pContainerId, Inventory pPlayerInventory, StockPotBlockEntity blockEntity, ContainerData data) {
        super(MenuRegistry.STOCK_POT.get(), pContainerId);
        this.playerInventory = pPlayerInventory;
        this.blockEntity = blockEntity;
        this.data = data;
        this.playerInventoryYOffset = 47;
        this.initMachineInventorySlot();
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
        for (int[] slot : new int[][]{
                {MAIN_TOP_LEFT, 50, 91},
                {MAIN_TOP_CENTER_LEFT, 70, 91},
                {MAIN_TOP_CENTER_RIGHT, 90, 91},
                {MAIN_TOP_RIGHT, 110, 91},
                {MAIN_BOTTOM_LEFT, 50, 111},
                {MAIN_BOTTOM_CENTER_LEFT, 70, 111},
                {MAIN_BOTTOM_CENTER_RIGHT, 90, 111},
                {MAIN_BOTTOM_RIGHT, 110, 111},
                {SECONDARY_TOP_LEFT, 100, 8},
                {SECONDARY_TOP_RIGHT, 120, 8},
                {SECONDARY_BOTTOM_LEFT, 100, 28},
                {SECONDARY_BOTTOM_RIGHT, 120, 28}
        }) {
            this.addInputSlot(slot[0], slot[1], slot[2]);
        }
        this.addSlot(new Slot(this.blockEntity, CONTAINER, 80, 69) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return super.mayPlace(pStack)
                        && SlotInputLimitManager.stockPotInputLimit().stream().anyMatch(ingredient -> ingredient.test(pStack))
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

            @Override
            public boolean isActive() {
                return super.isActive() && isIdle();
            }
        });
        this.addSlot(new Slot(this.blockEntity, RESULT, 80, 45) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }

            @Override
            public int getMaxStackSize() {
                return 64;
            }

            @Override
            public boolean isActive() {
                return super.isActive() && this.hasItem();
            }
        });
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                this.addSlot(new Slot(this.playerInventory, column + row * 9 + 9, 8 + column * 18, 103 + row * 18 + this.playerInventoryYOffset));
            }
        }
        for (int column = 0; column < 9; column++) {
            this.addSlot(new Slot(this.playerInventory, column, 8 + column * 18, 161 + this.playerInventoryYOffset));
        }
    }

    private void addInputSlot(int slotIndex, int x, int y) {
        this.addSlot(new Slot(this.blockEntity, slotIndex, x, y) {
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
                return 8;
            }

            @Override
            public boolean isActive() {
                return super.isActive() && isIdle();
            }
        });
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents pHelper) {
        MachineRecipeBookUtil.accountInventoryDamageable(pHelper, this.playerInventory);
        for (int slot : MAIN_INPUT_SLOTS) {
            MachineRecipeBookUtil.accountStack(pHelper, this.blockEntity.getItem(slot));
        }
        for (int slot : SECONDARY_INPUT_SLOTS) {
            MachineRecipeBookUtil.accountStack(pHelper, this.blockEntity.getItem(slot));
        }
        MachineRecipeBookUtil.accountStack(pHelper, this.blockEntity.getItem(CONTAINER));
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
    public void handlePlacement(boolean pUseMaxItems, Recipe<?> pRecipe, ServerPlayer pPlayer) {
        if (this.blockEntity.isRunning || !pPlayer.getRecipeBook().contains(pRecipe)) {
            return;
        }
        Recipe<?> placementRecipe;
        if (pRecipe instanceof StockPotRecipe) {
            placementRecipe = pRecipe;
        } else if (FarmersDelightCompat.isLoad() && pRecipe instanceof CookingPotRecipe cookingRecipe) {
            if (!FarmersDelightCompat.canCookingPotCompat()) {
                return;
            }
            placementRecipe = CookingPotRecipeCompat.toStockPotRecipe(cookingRecipe);
        } else {
            return;
        }
        BoundedRecipeBookPlacement.place(this, pPlayer, placementRecipe, pUseMaxItems, 12, 1);
        this.broadcastChanges();
    }

    private boolean matchesPlacementRecipe(Recipe<?> recipe) {
        if (recipe instanceof StockPotRecipe stockPotRecipe) {
            return stockPotRecipe.matches(this.blockEntity, this.blockEntity.getLevel());
        }
        if (FarmersDelightCompat.isLoad() && recipe instanceof CookingPotRecipe cookingPotRecipe) {
            return FarmersDelightCompat.canCookingPotCompat()
                    && CookingPotRecipeCompat.toStockPotRecipe(cookingPotRecipe)
                    .matches(this.blockEntity, this.blockEntity.getLevel());
        }
        return false;
    }

    @Override
    public boolean recipeMatches(Recipe<? super RecipeWrapper> pRecipe) {
        return matchesPlacementRecipe(pRecipe);
    }

    @Override
    public int getResultSlotIndex() {
        return 13;
    }

    @Override
    public int getGridWidth() {
        return 13;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 14;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return PleroCottageMachine.RECIPE_TYPE_STOCK_POT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<RecipeBookCategories> getRecipeBookCategories() {
        return StockPotRecipeBookCategories.categories();
    }

    @Override
    public boolean shouldMoveToInventory(int pSlot) {
        return pSlot >= 0 && pSlot < 13;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack moved = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            moved = stack.copy();
            if (pIndex < ITEMS_SIZE) {
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
        }
        return moved;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.blockEntity.stillValid(pPlayer);
    }

    /** 启动的那一刻进度还是 0，所以要连同运行标记一起判断。 */
    private boolean isIdle() {
        return !this.blockEntity.isRunning && this.getCookingTime() == 0;
    }
}
