package net.bittercat.plerocottage.machine.blockentities;

import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.bittercat.plerocottage.machine.blocks.StockPotBlock;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CookingPotRecipeCompat;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.menus.StockPotMenu;
import net.bittercat.plerocottage.machine.recipes.StockPotRecipe;
import net.bittercat.plerocottage.init.BlockEntityRegistry;
import net.bittercat.plerocottage.init.ParticleTypeRegistry;
import net.bittercat.plerocottage.init.RecipeRegistry;
import net.bittercat.plerocottage.init.SoundRegistry;
import net.bittercat.plerocottage.machine.util.MachineUtil;
import net.bittercat.plerocottage.machine.util.SoundUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockPotBlockEntity extends BaseMachineBlockEntity {
    public static final int ITEMS_SIZE = 14;
    public static final int CONTAINER = 0;
    public static final int MAIN_TOP_LEFT = 1;
    public static final int MAIN_TOP_CENTER_LEFT = 2;
    public static final int MAIN_TOP_CENTER_RIGHT = 3;
    public static final int MAIN_TOP_RIGHT = 4;
    public static final int MAIN_BOTTOM_LEFT = 5;
    public static final int MAIN_BOTTOM_CENTER_LEFT = 6;
    public static final int MAIN_BOTTOM_CENTER_RIGHT = 7;
    public static final int MAIN_BOTTOM_RIGHT = 8;
    public static final int SECONDARY_TOP_LEFT = 9;
    public static final int SECONDARY_TOP_RIGHT = 10;
    public static final int SECONDARY_BOTTOM_LEFT = 11;
    public static final int SECONDARY_BOTTOM_RIGHT = 12;
    public static final int RESULT = 13;
    public static final int[] MAIN_INPUT_SLOTS = {MAIN_TOP_LEFT, MAIN_TOP_CENTER_LEFT, MAIN_TOP_CENTER_RIGHT, MAIN_TOP_RIGHT, MAIN_BOTTOM_LEFT, MAIN_BOTTOM_CENTER_LEFT, MAIN_BOTTOM_CENTER_RIGHT, MAIN_BOTTOM_RIGHT};
    public static final int[] SECONDARY_INPUT_SLOTS = {SECONDARY_TOP_LEFT, SECONDARY_TOP_RIGHT, SECONDARY_BOTTOM_LEFT, SECONDARY_BOTTOM_RIGHT};
    public static final int[] INPUT_SLOTS = {CONTAINER, MAIN_TOP_LEFT, MAIN_TOP_CENTER_LEFT, MAIN_TOP_CENTER_RIGHT, MAIN_TOP_RIGHT, MAIN_BOTTOM_LEFT, MAIN_BOTTOM_CENTER_LEFT, MAIN_BOTTOM_CENTER_RIGHT, MAIN_BOTTOM_RIGHT, SECONDARY_TOP_LEFT, SECONDARY_TOP_RIGHT, SECONDARY_BOTTOM_LEFT, SECONDARY_BOTTOM_RIGHT};
    public final RecipeManager.CachedCheck<RecipeWrapper, StockPotRecipe> recipeCheck;
    public final NonNullList<ItemStack> items = NonNullList.withSize(ITEMS_SIZE, ItemStack.EMPTY);
    public boolean isRunning = false;
    public final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level pLevel, BlockPos pPos, BlockState pState) {
            StockPotBlockEntity.this.playStockPotSound();
            StockPotBlockEntity.this.setOpen(true);
        }

        @Override
        protected void onClose(Level pLevel, BlockPos pPos, BlockState pState) {
            StockPotBlockEntity.this.playStockPotSound();
            StockPotBlockEntity.this.setOpen(false);
        }

        @Override
        protected void openerCountChanged(Level pLevel, BlockPos pPos, BlockState pState, int pCount, int pOpenCount) {
        }

        @Override
        protected boolean isOwnContainer(Player pPlayer) {
            if (pPlayer.containerMenu instanceof StockPotMenu menu) {
                return menu.blockEntity == StockPotBlockEntity.this;
            }
            return false;
        }
    };
    public int cookingTime = 0;
    public int maxCookingTime = 0;
    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> StockPotBlockEntity.this.cookingTime;
                case 1 -> StockPotBlockEntity.this.maxCookingTime;
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> StockPotBlockEntity.this.setCookingTime(pValue);
                case 1 -> StockPotBlockEntity.this.setMaxCookingTime(pValue);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };
    public boolean isPlaySound = false;
    public int outputMultiple = 0;

    public StockPotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.STOCK_POT.get(), pPos, pBlockState);
        this.recipeCheck = RecipeManager.createCheck(RecipeRegistry.STOCK_POT_RECIPE_TYPE.get());
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, StockPotBlockEntity pBlockEntity) {
        if (pBlockEntity.isRunning) {
            if (pBlockEntity.isOnStockPotHeatBlock()) {
                RandomSource random = pLevel.random;
                if (random.nextFloat() < 0.11F) {
                    for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                        pLevel.addAlwaysVisibleParticle(ParticleTypeRegistry.COSY_STEAM.get(), true, (double) pPos.getX() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), (double) pPos.getY() + random.nextDouble() + random.nextDouble(), (double) pPos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
                    }
                }
            }
            if (!pBlockEntity.isPlaySound
                    || (pBlockEntity.getLevel().getGameTime() % 20 == 0 && !SoundUtil.isLoopActive(pBlockEntity))) {
                pBlockEntity.isPlaySound = true;
                SoundUtil.playStockPotSoundInstance(pBlockEntity);
            }
        }
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, StockPotBlockEntity pBlockEntity) {
        // 和原版箱子一样每 5 刻核对一次打开界面的玩家，不必每刻都查周围的实体
        if (pBlockEntity.getLevel().getGameTime() % 5 == 0) {
            pBlockEntity.recheckOpen();
        }
        if (pBlockEntity.isRunning) {
            if (pBlockEntity.isOnStockPotHeatBlock()) {
                pBlockEntity.cookingTime++;
            }
            if (pBlockEntity.cookingTime >= pBlockEntity.maxCookingTime) {
                pBlockEntity.craftItem();
            }
            pBlockEntity.setChanged();
        }
    }

    public void startRunning() {
        if (this.isRunning) return;
        if (!this.getItem(RESULT).isEmpty()) return;
        if (!PleroCottageCompat.isStockPotHeatBlock(this.level.getBlockState(this.getBlockPos().below()))) return;
        Optional<StockPotRecipe> recipe = this.getRecipe();
        if (recipe.isEmpty()) return;
        this.isRunning = true;
        this.setCookingTime(0);
        this.setMaxCookingTime(recipe.get().maxCookingTime);
        this.setOpen(false);
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    public void stopRunning() {
        this.isRunning = false;
        this.setCookingTime(0);
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    public void craftItem() {
        this.getRecipe().ifPresent(recipe -> {
            ItemStack output = recipe.assemble(this, this.level.registryAccess());
            List<ItemStack> craftRemaining = new ArrayList<>();
            for (int slot : INPUT_SLOTS) {
                ItemStack input = this.getItem(slot);
                if (input.isEmpty()) continue;
                ItemStack remaining = MachineUtil.getCraftRemaining(input, this.outputMultiple);
                if (PleroCottageCompat.isDamageableMaterial(input)) {
                    PleroCottageCompat.damageItem(this.outputMultiple, input, this.level);
                    if (!input.isEmpty()) {
                        remaining = ItemStack.EMPTY;
                    }
                } else {
                    input.shrink(this.outputMultiple);
                }
                if (input.isEmpty() && !remaining.isEmpty()) {
                    this.setItem(slot, remaining);
                } else if (!remaining.isEmpty()) {
                    craftRemaining.add(remaining);
                }
            }
            ItemStack outputRemaining = this.getItem(RESULT).copy();
            this.setItem(RESULT, output);
            this.insertCraftRemaining(craftRemaining);
            if (!outputRemaining.isEmpty()) {
                craftRemaining.add(outputRemaining);
            }
            this.popCraftRemaining(craftRemaining);
            this.playStockPotSound();
        });
        this.stopRunning();
    }

    public void insertCraftRemaining(List<ItemStack> craftRemaining) {
        MachineUtil.insertCraftRemaining(this, INPUT_SLOTS, craftRemaining);
    }

    public void popCraftRemaining(List<ItemStack> craftRemaining) {
        MachineUtil.popCraftRemaining(this.level, this.getBlockPos(), craftRemaining);
    }

    public boolean isOnStockPotHeatBlock() {
        return PleroCottageCompat.isStockPotHeatBlock(this.level.getBlockState(this.getBlockPos().below()));
    }

    public void setCookingTime(int value) {
        this.cookingTime = value;
        this.setChanged();
    }

    public void setMaxCookingTime(int value) {
        this.maxCookingTime = value;
        this.setChanged();
    }

    public Optional<StockPotRecipe> getRecipe() {
        ItemStackHandler inventory = new ItemStackHandler(ITEMS_SIZE);
        for (int slot = 0; slot < ITEMS_SIZE; slot++) {
            inventory.setStackInSlot(slot, this.getItem(slot));
        }
        Optional<StockPotRecipe> recipe = this.recipeCheck.getRecipeFor(new RecipeWrapper(inventory), this.level);
        for (StockPotRecipe stockPotRecipe : CookingPotRecipeCompat.recipes()) {
            if (stockPotRecipe.matches(this, this.level)) {
                return Optional.of(stockPotRecipe);
            }
        }
        return recipe;
    }

    public List<StockPotRecipe> getAllRecipe() {
        return Stream.concat(
                this.level.getRecipeManager().getAllRecipesFor(RecipeRegistry.STOCK_POT_RECIPE_TYPE.get()).stream(),
                CookingPotRecipeCompat.recipes().stream()
        ).collect(Collectors.toList());
    }

    public NonNullList<ItemStack> getMainInput() {
        NonNullList<ItemStack> input = NonNullList.withSize(MAIN_INPUT_SLOTS.length, ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            input.set(i, this.getItem(MAIN_INPUT_SLOTS[i]));
        }
        return input;
    }

    public NonNullList<ItemStack> getSecondaryInput() {
        NonNullList<ItemStack> input = NonNullList.withSize(SECONDARY_INPUT_SLOTS.length, ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            input.set(i, this.getItem(SECONDARY_INPUT_SLOTS[i]));
        }
        return input;
    }

    public void playStockPotSound() {
        this.level.playSound(null, this.getBlockPos(), SoundRegistry.STOCK_POT_COVER_MOVING.get(), SoundSource.BLOCKS);
    }

    public void setOpen(boolean open) {
        this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(StockPotBlock.OPEN, !this.isRunning && open));
    }

    @Override
    public void startOpen(Player pPlayer) {
        super.startOpen(pPlayer);
        if (!this.remove && !pPlayer.isSpectator()) {
            this.openersCounter.incrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player pPlayer) {
        super.stopOpen(pPlayer);
        if (!this.remove && !pPlayer.isSpectator()) {
            this.openersCounter.decrementOpeners(pPlayer, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected boolean canAutomationInsert(int slot, ItemStack stack) {
        if (this.isRunning) {
            return false;
        }
        if (slot == CONTAINER) {
            return accepts(SlotInputLimitManager.stockPotInputLimit(), stack);
        }
        return slot >= MAIN_TOP_LEFT && slot <= SECONDARY_BOTTOM_RIGHT;
    }

    @Override
    protected int automationSlotLimit(int slot) {
        return slot == RESULT ? super.automationSlotLimit(slot) : 8;
    }

    @Override
    protected boolean isOutputSlot(int slot) {
        return slot == RESULT;
    }

    @Override
    public IItemHandler getItemHandler() {
        return new InvWrapper(this);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new StockPotMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.isRunning = pTag.getBoolean("isRunning");
        this.setCookingTime(pTag.getInt("cookingTime"));
        this.setMaxCookingTime(pTag.getInt("maxCookingTime"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("isRunning", this.isRunning);
        pTag.putInt("cookingTime", this.cookingTime);
        pTag.putInt("maxCookingTime", this.maxCookingTime);
    }
}
