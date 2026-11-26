package net.bittercat.plerocottage.machine.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.bittercat.plerocottage.machine.blockentities.IceCreamMachineBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.init.MenuRegistry;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.recipes.IceCreamRecipe;

public class IceCreamMachineMenu extends RecipeBookMenu<IceCreamMachineBlockEntity> {
    public final FluidTank fluidTank;
    public final IceCreamMachineBlockEntity blockEntity;
    public final Inventory playerInventory;
    public final int playerInventoryYOffset;

    public IceCreamMachineMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory, (IceCreamMachineBlockEntity) pPlayerInventory.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public IceCreamMachineMenu(int pContainerId, Inventory pPlayerInventory, IceCreamMachineBlockEntity blockEntity) {
        super(MenuRegistry.ICE_CREAM_MACHINE.get(), pContainerId);
        this.fluidTank = blockEntity.fluidTank;
        this.blockEntity = blockEntity;
        this.playerInventory = pPlayerInventory;
        this.playerInventoryYOffset = 42;
        this.initMachineInventorySlot();
        this.initPlayerInventorySlot();
    }

    public void initMachineInventorySlot() {
        this.addSlot(new Slot(this.blockEntity, IceCreamMachineBlockEntity.LEFT_INPUT_SLOT, 105, 117));
        this.addSlot(new Slot(this.blockEntity, IceCreamMachineBlockEntity.RIGHT_INPUT_SLOT, 144, 117));
        this.addSlot(new Slot(this.blockEntity, IceCreamMachineBlockEntity.CONTAINER_SLOT, 38, 119) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return super.mayPlace(pStack)
                        && (PleroCottageCompat.isIceCreamCone(pStack)
                        || SlotInputLimitManager.ICE_CREAM_MACHINE_INPUT_LIMIT.stream()
                        .anyMatch(ingredient -> ingredient.test(pStack)));
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.blockEntity, IceCreamMachineBlockEntity.OUTPUT_SLOT, 38, 86) {
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
        this.addSlot(new Slot(this.blockEntity, IceCreamMachineBlockEntity.FLUID_TANK_SLOT, 146, 43) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return (super.mayPlace(pStack) && FluidUtil.getFluidHandler(pStack).isPresent())
                        || PleroCottageCompat.BUCKET_FLUID_MAP.containsKey(pStack.getItem());
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
        for (int slot : IceCreamMachineBlockEntity.MATERIAL_SLOTS) {
            MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(slot));
        }
        MachineRecipeBookUtil.accountStack(helper, this.blockEntity.getItem(IceCreamMachineBlockEntity.CONTAINER_SLOT));
    }

    @Override
    public void clearCraftingContent() {
        for (int slot : IceCreamMachineBlockEntity.MATERIAL_SLOTS) {
            this.blockEntity.setItem(slot, ItemStack.EMPTY);
        }
        this.blockEntity.setItem(IceCreamMachineBlockEntity.CONTAINER_SLOT, ItemStack.EMPTY);
    }

    @Override
    public boolean recipeMatches(Recipe<? super IceCreamMachineBlockEntity> recipe) {
        return recipe instanceof IceCreamRecipe iceCreamRecipe
                && iceCreamRecipe.matches(this.blockEntity, this.blockEntity.getLevel());
    }

    @Override
    public int getResultSlotIndex() {
        return 3;
    }

    @Override
    public int getGridWidth() {
        return 3;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return PleroCottageMachine.RECIPE_TYPE_ICE_CREAM_MACHINE;
    }

    @Override
    public boolean shouldMoveToInventory(int slot) {
        return slot < getSize() && slot != getResultSlotIndex();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handlePlacement(boolean useMaxItems, Recipe<?> recipe, ServerPlayer player) {
        if (!(recipe instanceof IceCreamRecipe)
                || !this.blockEntity.getItem(IceCreamMachineBlockEntity.OUTPUT_SLOT).isEmpty()) {
            return;
        }
        new ServerPlaceRecipe<IceCreamMachineBlockEntity>(this) {
            @Override
            protected int getStackSize(boolean maxItems, int available, boolean matches) {
                return 1;
            }
        }.recipeClicked(player, (Recipe<IceCreamMachineBlockEntity>) recipe, false);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem() || !slot.mayPickup(player)) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < IceCreamMachineBlockEntity.ITEMS_SIZE) {
            if (!this.moveItemStackTo(stack, IceCreamMachineBlockEntity.ITEMS_SIZE, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            boolean fluidContainer = FluidUtil.getFluidHandler(stack).isPresent()
                    || PleroCottageCompat.BUCKET_FLUID_MAP.containsKey(stack.getItem());
            boolean recipeContainer = this.slots.get(2).mayPlace(stack);
            int start = fluidContainer ? 4 : recipeContainer ? 2 : 0;
            int end = fluidContainer ? 5 : recipeContainer ? 3 : 2;
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
