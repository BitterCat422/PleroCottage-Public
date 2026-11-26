package net.bittercat.plerocottage.machine.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.menus.IceCreamMachineMenu;
import net.bittercat.plerocottage.machine.recipes.IceCreamRecipe;
import net.bittercat.plerocottage.init.BlockEntityRegistry;
import net.bittercat.plerocottage.init.RecipeRegistry;
import net.bittercat.plerocottage.init.SoundRegistry;
import net.bittercat.plerocottage.machine.util.MachineUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IceCreamMachineBlockEntity extends BaseMachineBlockEntity {
    public static final int ITEMS_SIZE = 5;
    public static final int FLUID_TANK_SLOT = 0;
    public static final int CONTAINER_SLOT = 1;
    public static final int LEFT_INPUT_SLOT = 2;
    public static final int RIGHT_INPUT_SLOT = 3;
    public static final int OUTPUT_SLOT = 4;
    public static final int[] INPUT_SLOTS = {CONTAINER_SLOT, LEFT_INPUT_SLOT, RIGHT_INPUT_SLOT};
    public static final int[] MATERIAL_SLOTS = {LEFT_INPUT_SLOT, RIGHT_INPUT_SLOT};
    public final RecipeManager.CachedCheck<IceCreamMachineBlockEntity, IceCreamRecipe> recipeCheck;
    public final FluidTank fluidTank = new FluidTank(3000, PleroCottageCompat::isIceCreamFluid) {
        @Override
        protected void onContentsChanged() {
            IceCreamMachineBlockEntity.this.setChanged();
        }
    };
    public final NonNullList<ItemStack> items = NonNullList.withSize(ITEMS_SIZE, ItemStack.EMPTY);
    public LazyOptional<IFluidHandler> fluidHandler = LazyOptional.empty();

    public IceCreamMachineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.ICE_CREAM_MACHINE.get(), pPos, pBlockState);
        this.recipeCheck = RecipeManager.createCheck(RecipeRegistry.ICE_CREAM_RECIPE_TYPE.get());
    }

    public void craftItem() {
        Optional<IceCreamRecipe> recipe = this.getRecipe();
        recipe.ifPresent(iceCreamRecipe -> {
            this.fluidTank.drain(iceCreamRecipe.fluidStack.getAmount(), IFluidHandler.FluidAction.EXECUTE);
            List<ItemStack> craftRemaining = new ArrayList<>();
            for (int i = 0; i < (iceCreamRecipe.isTowFlavor ? 3 : 2); i++) {
                ItemStack input = this.getItem(INPUT_SLOTS[i]);
                if (input.isEmpty()) continue;
                ItemStack remaining = MachineUtil.getCraftRemaining(input, 1);
                if (PleroCottageCompat.isDamageableMaterial(input)) {
                    PleroCottageCompat.damageItem(input, this.level);
                    if (!input.isEmpty()) {
                        remaining = ItemStack.EMPTY;
                    }
                } else {
                    input.shrink(1);
                }
                if (input.isEmpty() && !remaining.isEmpty()) {
                    this.setItem(INPUT_SLOTS[i], remaining);
                } else if (!remaining.isEmpty()) {
                    craftRemaining.add(remaining);
                }
            }
            this.setItem(OUTPUT_SLOT, iceCreamRecipe.assemble(this, this.level.registryAccess()));
            this.insertCraftRemaining(craftRemaining);
            this.popCraftRemaining(craftRemaining);
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
            this.level.playSound(null, this.getBlockPos(), SoundRegistry.ICE_CREAM_MACHINE_DONE.get(), SoundSource.BLOCKS);
        });
    }

    public void insertCraftRemaining(List<ItemStack> craftRemaining) {
        MachineUtil.insertCraftRemaining(this, INPUT_SLOTS, craftRemaining);
    }

    public void popCraftRemaining(List<ItemStack> craftRemaining) {
        MachineUtil.popCraftRemaining(this.level, this.getBlockPos(), craftRemaining);
    }

    public Optional<IceCreamRecipe> getRecipe() {
        return this.recipeCheck.getRecipeFor(this, this.level);
    }

    public List<IceCreamRecipe> getAllRecipe() {
        return this.level.getRecipeManager().getAllRecipesFor(RecipeRegistry.ICE_CREAM_RECIPE_TYPE.get());
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public IItemHandler getItemHandler() {
        return new InvWrapper(this);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new IceCreamMachineMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return this.fluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void loadAndRevive() {
        super.loadAndRevive();
        this.fluidHandler = LazyOptional.of(() -> this.fluidTank);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.fluidHandler.invalidate();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.fluidTank.readFromNBT(pTag.getCompound("fluidTank"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("fluidTank", this.fluidTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        super.setItem(pSlot, pStack);
        MachineUtil.handlerFluidSlot(FLUID_TANK_SLOT, this, this.fluidTank, this.level, this.getBlockPos());
    }
}
