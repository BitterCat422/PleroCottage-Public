package net.bittercat.plerocottage.machine.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.FryerBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.compat.plerocottage.SmokingRecipeManager;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.init.MenuRegistry;
import net.bittercat.plerocottage.machine.recipes.FastFryRecipe;
import net.bittercat.plerocottage.machine.recipes.SlowFryRecipe;

import static net.bittercat.plerocottage.machine.blockentities.FryerBlockEntity.INPUT_SLOTS;

public class FryerMenu extends RecipeBookMenu<FryerBlockEntity.FryerCraftContainer> {
    public final Inventory playerInventory;
    public final FluidTank fluidTank;
    public final FryerBlockEntity blockEntity;
    public final ContainerData data;
    public int playerInventoryYOffset;

    public FryerMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory, (FryerBlockEntity) pPlayerInventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public FryerMenu(int pContainerId, Inventory pPlayerInventory, FryerBlockEntity blockEntity, ContainerData data) {
        super(MenuRegistry.FRYER.get(), pContainerId);
        this.playerInventory = pPlayerInventory;
        this.fluidTank = blockEntity.fluidTank;
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

    public void initMachineInventorySlot() {
        this.addSlot(new Slot(this.blockEntity, FryerBlockEntity.FLUID_TANK_SLOT, 141, 35) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return super.mayPlace(pStack) && FluidUtil.getFluidHandler(pStack).isPresent() || PleroCottageCompat.BUCKET_FLUID_MAP.containsKey(pStack.getItem());
            }
        });
        int[][] slots = {
                {FryerBlockEntity.TOP_LEFT, 42, 22},
                {FryerBlockEntity.TOP_RIGHT, 76, 22},
                {FryerBlockEntity.BOTTOM_LEFT, 42, 56},
                {FryerBlockEntity.BOTTOM_RIGHT, 76, 56}
        };
        for (int[] slot : slots) {
            this.addSlot(new Slot(this.blockEntity, slot[0], slot[1], slot[2]) {
                @Override
                public boolean mayPlace(ItemStack pStack) {
                    return super.mayPlace(pStack)
                            && getCookingTime() == 0
                            && SlotInputLimitManager.FRYER_INPUT_LIMIT.stream().anyMatch(ingredient -> ingredient.test(pStack));
                }

                @Override
                public boolean mayPickup(Player pPlayer) {
                    return super.mayPickup(pPlayer) && getCookingTime() == 0;
                }

                @Override
                public int getMaxStackSize() {
                    return 4;
                }

                @Override
                public boolean isActive() {
                    return super.isActive() && getCookingTime() == 0;
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
        for (int slot : INPUT_SLOTS) {
            MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(slot));
        }
    }

    @Override
    public void clearCraftingContent() {
        for (int slot : INPUT_SLOTS) {
            this.blockEntity.setItem(slot, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean recipeMatches(Recipe<? super FryerBlockEntity.FryerCraftContainer> recipe) {
        Ingredient input = getRecipeInput(recipe);
        if (input.isEmpty() || this.getCookingTime() != 0) {
            return false;
        }
        for (int slot : INPUT_SLOTS) {
            if (input.test(this.blockEntity.getItem(slot))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getResultSlotIndex() {
        
        return 0;
    }

    @Override
    public int getGridWidth() {
        return 2;
    }

    @Override
    public int getGridHeight() {
        return 2;
    }

    @Override
    public int getSize() {
        return INPUT_SLOTS.length + 1;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return PleroCottageMachine.RECIPE_TYPE_FRYER;
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot > 0 && slot < this.getSize();
    }

    @Override
    public void handlePlacement(boolean useMaxItems, Recipe<?> recipe, ServerPlayer player) {
        if (this.blockEntity.isRunning || this.getCookingTime() != 0
                || !player.getRecipeBook().contains(recipe)) {
            return;
        }
        Ingredient input = getRecipeInput(recipe);
        if (input.isEmpty()) {
            return;
        }

        boolean placed = false;
        
        for (int pass = 0; pass < 2; pass++) {
            for (int targetSlot : INPUT_SLOTS) {
                Slot target = this.slots.get(targetSlot);
                ItemStack current = target.getItem();
                if ((pass == 0 && current.isEmpty()) || (pass == 1 && !current.isEmpty())) {
                    continue;
                }
                for (int inventorySlot = 0; inventorySlot < player.getInventory().items.size(); inventorySlot++) {
                    ItemStack available = player.getInventory().getItem(inventorySlot);
                    if (available.isEmpty() || !input.test(available) || !target.mayPlace(available)
                            || (!current.isEmpty() && !ItemStack.isSameItemSameTags(current, available))) {
                        continue;
                    }
                    int capacity = Math.min(target.getMaxStackSize(available), available.getMaxStackSize())
                            - current.getCount();
                    int count = Math.min(useMaxItems ? capacity : Math.min(1, capacity), available.getCount());
                    if (count <= 0) continue;
                    ItemStack result = available.copyWithCount(current.getCount() + count);
                    available.shrink(count);
                    target.set(result);
                    current = target.getItem();
                    placed = true;
                    if (!useMaxItems) {
                        player.getInventory().setChanged();
                        this.broadcastChanges();
                        return;
                    }
                }
            }
        }
        if (placed) {
            player.getInventory().setChanged();
            this.broadcastChanges();
        } else if (!this.recipeMatchesInput(input)) {
            player.connection.send(new ClientboundPlaceGhostRecipePacket(this.containerId, recipe));
        }
    }

    private boolean recipeMatchesInput(Ingredient input) {
        for (int slot : INPUT_SLOTS) {
            if (input.test(this.blockEntity.getItem(slot))) return true;
        }
        return false;
    }

    public static Ingredient getRecipeInput(Recipe<?> recipe) {
        if (recipe instanceof FastFryRecipe fastRecipe) {
            return fastRecipe.input;
        }
        if (recipe instanceof SlowFryRecipe slowRecipe) {
            return slowRecipe.input;
        }
        if (recipe instanceof SmokingRecipe smokingRecipe) {
            return SmokingRecipeManager.toFastFryRecipe(smokingRecipe).input;
        }
        return Ingredient.EMPTY;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem() || !slot.mayPickup(player)) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < this.blockEntity.getContainerSize()) {
            if (!this.moveItemStackTo(stack, this.blockEntity.getContainerSize(), this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(stack, 0, this.blockEntity.getContainerSize(), false)) {
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
