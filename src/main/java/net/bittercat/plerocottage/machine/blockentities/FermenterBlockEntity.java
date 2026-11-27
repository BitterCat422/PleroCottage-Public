package net.bittercat.plerocottage.machine.blockentities;

import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.bittercat.plerocottage.machine.blocks.FermenterBlock;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.menus.FermenterMenu;
import net.bittercat.plerocottage.machine.recipes.BaseFermentationRecipe;
import net.bittercat.plerocottage.machine.recipes.BrightFermentationRecipe;
import net.bittercat.plerocottage.machine.recipes.DimFermentationRecipe;
import net.bittercat.plerocottage.machine.recipes.NormalFermentationRecipe;
import net.bittercat.plerocottage.init.BlockEntityRegistry;
import net.bittercat.plerocottage.init.RecipeRegistry;
import net.bittercat.plerocottage.machine.util.MachineUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FermenterBlockEntity extends BaseMachineBlockEntity {
    public static final int ITEMS_SIZE = 8;
    public static final int CATALYSTS_TOP = 0;
    public static final int CATALYSTS_BOTTOM = 1;
    public static final int CONTAINER = 2;
    public static final int TOP_LEFT = 3;
    public static final int TOP_RIGHT = 4;
    public static final int BOTTOM_LEFT = 5;
    public static final int BOTTOM_RIGHT = 6;
    public static final int OUTPUT = 7;
    public static final int[] INPUT_SLOTS = {CONTAINER, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};
    public static final int[] MATERIAL_SLOTS = {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};
    public static final double CATALYST_A_EFFECT = 0.85;
    public static final double CATALYST_B_EFFECT = 0.7;
    public static final double CATALYST_C_EFFECT = 0.5;
    public static final double CATALYST_X_EFFECT = 0;
    public final RecipeManager.CachedCheck<FermenterBlockEntity, DimFermentationRecipe> dimRecipeCheck;
    public final RecipeManager.CachedCheck<FermenterBlockEntity, NormalFermentationRecipe> normalRecipeCheck;
    public final RecipeManager.CachedCheck<FermenterBlockEntity, BrightFermentationRecipe> brightRecipeCheck;
    public final NonNullList<ItemStack> items = NonNullList.withSize(ITEMS_SIZE, ItemStack.EMPTY);
    public boolean isRunning = false;
    public final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level pLevel, BlockPos pPos, BlockState pState) {
            FermenterBlockEntity.this.playBarrelOpenSound();
            FermenterBlockEntity.this.setOpen(true);
        }

        @Override
        protected void onClose(Level pLevel, BlockPos pPos, BlockState pState) {
            FermenterBlockEntity.this.playBarrelCloseSound();
            FermenterBlockEntity.this.setOpen(false);
        }

        @Override
        protected void openerCountChanged(Level pLevel, BlockPos pPos, BlockState pState, int pCount, int pOpenCount) {
        }

        @Override
        protected boolean isOwnContainer(Player pPlayer) {
            if (pPlayer.containerMenu instanceof FermenterMenu menu) {
                return menu.blockEntity == FermenterBlockEntity.this;
            }
            return false;
        }
    };
    public int tickCount = 0;
    public int cookingTime = 0;
    public int maxCookingTime = 0;
    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> FermenterBlockEntity.this.cookingTime;
                case 1 -> FermenterBlockEntity.this.maxCookingTime;
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> FermenterBlockEntity.this.setCookingTime(pValue);
                case 1 -> FermenterBlockEntity.this.setMaxCookingTime(pValue);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };
    public LightState recipeLightState = LightState.DIM;
    public int outputMultiple = 0;

    public FermenterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.FERMENTER.get(), pPos, pBlockState);
        this.dimRecipeCheck = RecipeManager.createCheck(RecipeRegistry.DIM_FERMENTATION_RECIPE_TYPE.get());
        this.normalRecipeCheck = RecipeManager.createCheck(RecipeRegistry.NORMAL_FERMENTATION_RECIPE_TYPE.get());
        this.brightRecipeCheck = RecipeManager.createCheck(RecipeRegistry.BRIGHT_FERMENTATION_RECIPE_TYPE.get());
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, FermenterBlockEntity pBlockEntity) {
        // 和原版箱子一样每 5 刻核对一次打开界面的玩家，不必每刻都查周围的实体
        if (pBlockEntity.getLevel().getGameTime() % 5 == 0) {
            pBlockEntity.recheckOpen();
        }
        if (pBlockEntity.isRunning) {
            pBlockEntity.tickCount++;
            // ContainerData 的窗口同步值按 short 编码。
            if (pBlockEntity.tickCount >= 20) {
                pBlockEntity.cookingTime++;
                pBlockEntity.tickCount = 0;
            }
            if (pBlockEntity.cookingTime - 1 >= pBlockEntity.maxCookingTime) {
                pBlockEntity.craftItem();
            }
            pBlockEntity.setChanged();
        }
    }

    public void startRunning() {
        if (this.isRunning) return;
        if (!this.getItem(OUTPUT).isEmpty()) return;
        BaseFermentationRecipe recipe = this.getPresentRecipe();
        if (recipe == null) return;
        this.recipeLightState = recipe.lightState;
        this.playBarrelCloseSound();
        this.setOpen(false);
        this.isRunning = true;
        this.setCookingTime(1);
        this.setMaxCookingTime(this.handleMaxCookingTime(recipe.maxCookingTime));
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    public void stopRunning() {
        this.playBarrelOpenSound();
        this.isRunning = false;
        this.setCookingTime(0);
        this.setMaxCookingTime(0);
    }

    public void craftItem() {
        BaseFermentationRecipe recipe = this.getPresentRecipe();
        ItemStack output = ItemStack.EMPTY;
        if (recipe == null) {
            this.outputMultiple = 1;
        } else {
            output = recipe.assemble(this, this.level.registryAccess());
        }
        List<ItemStack> craftRemaining = new ArrayList<>();
        boolean hasInput = false;
        for (int slot : INPUT_SLOTS) {
            ItemStack input = this.getItem(slot);
            if (input.isEmpty()) continue;
            hasInput = true;
            if (slot == CONTAINER && (recipe == null || !recipe.hasContainer())) continue;
            ItemStack remaining = MachineUtil.getCraftRemaining(input, this.outputMultiple);
            if (PleroCottageCompat.isDamageableMaterial(input)) {
                PleroCottageCompat.damageItem(this.outputMultiple, input, this.level);
                if (!input.isEmpty()) {
                    remaining = ItemStack.EMPTY;
                }
            } else {
                if (slot == CONTAINER) {
                    input.shrink(output.getCount());
                } else {
                    input.shrink(this.outputMultiple);
                }
            }
            if (input.isEmpty() && !remaining.isEmpty()) {
                this.setItem(slot, remaining);
            } else if (!remaining.isEmpty()) {
                craftRemaining.add(remaining);
            }
        }
        if (!hasInput) {
            this.setItem(OUTPUT, ItemStack.EMPTY);
        } else if (this.recipeLightState != this.getLightState() || recipe == null) {
            int count = 1;
            int minutes = (recipe == null ? this.maxCookingTime : recipe.maxCookingTime) / 60;
            if (minutes >= 40) {
                count = 8;
            } else if (minutes >= 20) {
                count = 4;
            } else if (minutes >= 10) {
                count = 2;
            }
            ItemStack failItem = PleroCottageCompat.getPleroCottageItem("suspicious_mold").getDefaultInstance();
            this.setItem(OUTPUT, failItem.copyWithCount(Math.min(count * this.outputMultiple, failItem.getMaxStackSize())));
        } else {
            this.setItem(OUTPUT, output);
        }
        this.insertCraftRemaining(craftRemaining);
        this.popCraftRemaining(craftRemaining);
        this.stopRunning();
    }

    public BaseFermentationRecipe getPresentRecipe() {
        Optional<DimFermentationRecipe> dimRecipe = this.getDimRecipe();
        if (dimRecipe.isPresent()) {
            return dimRecipe.get();
        }
        Optional<NormalFermentationRecipe> normalRecipe = this.getNormalRecipe();
        if (normalRecipe.isPresent()) {
            return normalRecipe.get();
        }
        return this.getBrightRecipe().orElse(null);
    }

    public void insertCraftRemaining(List<ItemStack> craftRemaining) {
        MachineUtil.insertCraftRemaining(this, MATERIAL_SLOTS, craftRemaining);
    }

    public void popCraftRemaining(List<ItemStack> craftRemaining) {
        MachineUtil.popCraftRemaining(this.level, this.getBlockPos(), craftRemaining);
    }

    public NonNullList<ItemStack> getInput() {
        NonNullList<ItemStack> input = NonNullList.withSize(MATERIAL_SLOTS.length, ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            input.set(i, this.getItem(MATERIAL_SLOTS[i]));
        }
        return input;
    }

    public void setCookingTime(int value) {
        this.cookingTime = value;
        this.setChanged();
    }

    public void setMaxCookingTime(int value) {
        this.maxCookingTime = value;
        this.setChanged();
    }

    public int handleMaxCookingTime(int value) {
        ItemStack catalystsTop = this.getItem(CATALYSTS_TOP);
        ItemStack catalystsBottom = this.getItem(CATALYSTS_BOTTOM);
        value = applyCatalystEffect(value, catalystsTop);
        value = applyCatalystEffect(value, catalystsBottom);
        return Math.max(value, 1);
    }

    private int applyCatalystEffect(int value, ItemStack catalyst) {
        if (catalyst.isEmpty()) return value;
        if (PleroCottageCompat.isCatalystsA(catalyst)) {
            catalyst.shrink(1);
            return (int) (value * CATALYST_A_EFFECT);
        } else if (PleroCottageCompat.isCatalystsB(catalyst)) {
            catalyst.shrink(1);
            return (int) (value * CATALYST_B_EFFECT);
        } else if (PleroCottageCompat.isCatalystsC(catalyst)) {
            catalyst.shrink(1);
            return (int) (value * CATALYST_C_EFFECT);
        } else if (PleroCottageCompat.isCatalystsX(catalyst)) {
            catalyst.shrink(1);
            return (int) (value * CATALYST_X_EFFECT);
        }
        return value;
    }

    public int getLightLevel() {
        return this.level.getRawBrightness(this.getBlockPos(), 0);
    }

    public LightState getLightState() {
        int lightLevel = this.getLightLevel();
        if (lightLevel <= 5) return LightState.DIM;
        if (lightLevel <= 10) return LightState.NORMAL;
        return LightState.BRIGHT;
    }

    public Optional<DimFermentationRecipe> getDimRecipe() {
        return this.dimRecipeCheck.getRecipeFor(this, this.level);
    }

    public Optional<NormalFermentationRecipe> getNormalRecipe() {
        return this.normalRecipeCheck.getRecipeFor(this, this.level);
    }

    public Optional<BrightFermentationRecipe> getBrightRecipe() {
        return this.brightRecipeCheck.getRecipeFor(this, this.level);
    }

    public List<DimFermentationRecipe> getAllDimRecipe() {
        return this.level.getRecipeManager().getAllRecipesFor(RecipeRegistry.DIM_FERMENTATION_RECIPE_TYPE.get());
    }

    public List<NormalFermentationRecipe> getAllNormalRecipe() {
        return this.level.getRecipeManager().getAllRecipesFor(RecipeRegistry.NORMAL_FERMENTATION_RECIPE_TYPE.get());
    }

    public List<BrightFermentationRecipe> getAllBrightRecipe() {
        return this.level.getRecipeManager().getAllRecipesFor(RecipeRegistry.BRIGHT_FERMENTATION_RECIPE_TYPE.get());
    }

    public void playBarrelOpenSound() {
        this.level.playSound(null, this.getBlockPos(), SoundEvents.BARREL_OPEN, SoundSource.BLOCKS);
    }

    public void playBarrelCloseSound() {
        this.level.playSound(null, this.getBlockPos(), SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS);
    }

    public void setOpen(boolean open) {
        this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(FermenterBlock.OPEN, !this.isRunning && open));
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
        if (slot == CATALYSTS_TOP || slot == CATALYSTS_BOTTOM) {
            return PleroCottageCompat.isCatalysts(stack);
        }
        if (slot == CONTAINER) {
            return accepts(SlotInputLimitManager.FERMENTER_INPUT_LIMIT, stack);
        }
        return slot >= TOP_LEFT && slot <= BOTTOM_RIGHT;
    }

    @Override
    protected int automationSlotLimit(int slot) {
        return slot >= TOP_LEFT && slot <= BOTTOM_RIGHT ? 4 : super.automationSlotLimit(slot);
    }

    @Override
    protected boolean isOutputSlot(int slot) {
        return slot == OUTPUT;
    }

    @Override
    public IItemHandler getItemHandler() {
        return new InvWrapper(this);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FermenterMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.isRunning = pTag.getBoolean("isRunning");
        this.cookingTime = pTag.getInt("cookingTime");
        this.maxCookingTime = pTag.getInt("maxCookingTime");
        this.recipeLightState = switch (pTag.getString("recipeLightState")) {
            case "DIM" -> LightState.DIM;
            case "NORMAL" -> LightState.NORMAL;
            default -> LightState.BRIGHT;
        };
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("isRunning", this.isRunning);
        pTag.putInt("cookingTime", this.cookingTime);
        pTag.putInt("maxCookingTime", this.maxCookingTime);
        pTag.putString("recipeLightState", this.recipeLightState.toString());
    }

    public enum LightState {
        DIM(0, 5),
        NORMAL(6, 10),
        BRIGHT(11, 15);
        public final int minLight;
        public final int maxLight;

        LightState(int minLight, int maxLight) {
            this.minLight = minLight;
            this.maxLight = maxLight;
        }
    }
}
