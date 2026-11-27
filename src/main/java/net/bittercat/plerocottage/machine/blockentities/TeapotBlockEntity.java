package net.bittercat.plerocottage.machine.blockentities;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
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
import net.minecraftforge.items.wrapper.InvWrapper;
import net.bittercat.plerocottage.machine.blocks.TeapotBlock;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.menus.TeapotMenu;
import net.bittercat.plerocottage.machine.recipes.TeapotRecipe;
import net.bittercat.plerocottage.init.BlockEntityRegistry;
import net.bittercat.plerocottage.init.ParticleTypeRegistry;
import net.bittercat.plerocottage.init.RecipeRegistry;
import net.bittercat.plerocottage.init.SoundRegistry;
import net.bittercat.plerocottage.machine.util.MachineUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeapotBlockEntity extends BaseMachineBlockEntity {
    public static final int ITEMS_SIZE = 8;
    public static final int CONTAINER = 0;
    public static final int DRINK = 1;
    public static final int MATERIAL = 2;
    public static final int TOP_LEFT = 3;
    public static final int TOP_RIGHT = 4;
    public static final int BOTTOM_LEFT = 5;
    public static final int BOTTOM_RIGHT = 6;
    public static final int RESULT = 7;
    public static final int[] INPUT_SLOTS = {DRINK, MATERIAL, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};
    public static final int SOUND_TIME = 60;
    public final RecipeManager.CachedCheck<TeapotBlockEntity, TeapotRecipe> recipeCheck;
    public final NonNullList<ItemStack> items = NonNullList.withSize(ITEMS_SIZE, ItemStack.EMPTY);
    public boolean isRunning = false;
    public final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level pLevel, BlockPos pPos, BlockState pState) {
            TeapotBlockEntity.this.playTeapotOpenSound();
            TeapotBlockEntity.this.setOpen(true);
        }

        @Override
        protected void onClose(Level pLevel, BlockPos pPos, BlockState pState) {
            TeapotBlockEntity.this.playTeapotCloseSound();
            TeapotBlockEntity.this.setOpen(false);
        }

        @Override
        protected void openerCountChanged(Level pLevel, BlockPos pPos, BlockState pState, int pCount, int pOpenCount) {
        }

        @Override
        protected boolean isOwnContainer(Player pPlayer) {
            if (pPlayer.containerMenu instanceof TeapotMenu menu) {
                return menu.blockEntity == TeapotBlockEntity.this;
            }
            return false;
        }
    };
    public int cookingTime = 0;
    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> TeapotBlockEntity.this.cookingTime;
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> TeapotBlockEntity.this.setCookingTime(pValue);
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    public ItemStack output = ItemStack.EMPTY;
    /** 本次冲泡开始时消耗掉的原料：冲泡中途方块被破坏时原样返还。 */
    public final List<ItemStack> consumedInputs = new ArrayList<>();
    public int maxCookingTime = SOUND_TIME;
    public int playSoundCooldown = SOUND_TIME;
    public int outputMultiple = 0;

    public TeapotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.TEAPOT.get(), pPos, pBlockState);
        this.recipeCheck = RecipeManager.createCheck(RecipeRegistry.TEAPOT_RECIPE_TYPE.get());
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, TeapotBlockEntity pBlockEntity) {
        RandomSource random = pLevel.random;
        if (pBlockEntity.isRunning && random.nextFloat() < 0.11F) {
            for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                pLevel.addAlwaysVisibleParticle(ParticleTypeRegistry.COSY_STEAM.get(), true, (double) pPos.getX() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), (double) pPos.getY() + random.nextDouble() + random.nextDouble(), (double) pPos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
            }
        }
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, TeapotBlockEntity pBlockEntity) {
        // 和原版箱子一样每 5 刻核对一次打开界面的玩家，不必每刻都查周围的实体
        if (pBlockEntity.getLevel().getGameTime() % 5 == 0) {
            pBlockEntity.recheckOpen();
        }
        if (pBlockEntity.isRunning) {
            pBlockEntity.cookingTime++;
            if (pBlockEntity.cookingTime >= pBlockEntity.maxCookingTime) {
                pBlockEntity.craftItem();
                pBlockEntity.level.playSound(null, pBlockEntity.getBlockPos(), SoundRegistry.TEAPOT_DONE.get(), SoundSource.BLOCKS);
            }
            if (pBlockEntity.playSoundCooldown <= 0) {
                pBlockEntity.level.playSound(null, pBlockEntity.getBlockPos(), SoundRegistry.TEAPOT_RUNNING.get(), SoundSource.BLOCKS);
                pBlockEntity.playSoundCooldown += SOUND_TIME;
            }
            pBlockEntity.setChanged();
        }
    }

    public void startRunning() {
        if (this.isRunning) return;
        if (!this.getItem(RESULT).isEmpty()) return;
        if (this.getItem(CONTAINER).isEmpty() || this.getItem(DRINK).isEmpty() || this.getItem(MATERIAL).isEmpty()) return;
        this.getRecipe().ifPresent(teapotRecipe -> {
            this.isRunning = true;
            this.playSoundCooldown = 0;
            this.setCookingTime(0);
            this.handlerRecipe(teapotRecipe);
            this.setOpen(false);
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
        });
    }

    public void stopRunning() {
        this.isRunning = false;
        this.setCookingTime(0);
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    public void handlerRecipe(TeapotRecipe recipe) {
        this.output = recipe.assemble(this, this.level.registryAccess());
        this.consumedInputs.clear();
        List<ItemStack> craftRemaining = new ArrayList<>();
        for (int i = 0; i < RESULT; i++) {
            ItemStack input = this.getItem(i);
            ItemStack remaining = MachineUtil.getCraftRemaining(input, this.outputMultiple);
            if (PleroCottageCompat.isDamageableMaterial(input)) {
                PleroCottageCompat.damageItem(this.outputMultiple, input, this.level);
                if (!input.isEmpty()) {
                    remaining = ItemStack.EMPTY;
                }
            } else {
                int used = Math.min(input.getCount(), i == CONTAINER ? output.getCount() : this.outputMultiple);
                // 带返还物的原料（如桶）已经把返还物给出去了，再记下来会多出一份
                if (used > 0 && remaining.isEmpty()) {
                    this.consumedInputs.add(input.copyWithCount(used));
                }
                input.shrink(used);
            }
            if (input.isEmpty() && !remaining.isEmpty()) {
                this.setItem(i, remaining);
            } else if (!remaining.isEmpty()) {
                craftRemaining.add(remaining);
            }
        }
        this.insertCraftRemaining(craftRemaining);
        this.popCraftRemaining(craftRemaining);
        this.setChanged();
    }

    public void craftItem() {
        this.setItem(RESULT, this.output);
        this.setOutput(ItemStack.EMPTY);
        this.consumedInputs.clear();
        this.stopRunning();
    }

    public void insertCraftRemaining(List<ItemStack> craftRemaining) {
        MachineUtil.insertCraftRemaining(this, INPUT_SLOTS, craftRemaining);
    }

    public void popCraftRemaining(List<ItemStack> craftRemaining) {
        MachineUtil.popCraftRemaining(this.level, this.getBlockPos(), craftRemaining);
    }

    public NonNullList<ItemStack> getInput() {
        NonNullList<ItemStack> input = NonNullList.withSize(INPUT_SLOTS.length, ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            input.set(i, this.getItem(INPUT_SLOTS[i]));
        }
        return input;
    }

    public void setCookingTime(int value) {
        this.cookingTime = value;
        this.setChanged();
    }

    public void setOutput(ItemStack output) {
        this.output = output;
        this.setChanged();
    }

    public Optional<TeapotRecipe> getRecipe() {
        return this.recipeCheck.getRecipeFor(this, this.level);
    }

    public List<TeapotRecipe> getAllRecipe() {
        return this.level.getRecipeManager().getAllRecipesFor(RecipeRegistry.TEAPOT_RECIPE_TYPE.get());
    }

    public void playTeapotOpenSound() {
        this.level.playSound(null, this.getBlockPos(), SoundRegistry.TEAPOT_OPEN.get(), SoundSource.BLOCKS);
    }

    public void playTeapotCloseSound() {
        this.level.playSound(null, this.getBlockPos(), SoundRegistry.TEAPOT_CLOSE.get(), SoundSource.BLOCKS);
    }

    public void setOpen(boolean open) {
        this.level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(TeapotBlock.OPEN, !this.isRunning && open));
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
        return switch (slot) {
            case CONTAINER -> accepts(SlotInputLimitManager.TEAPOT_CUP_LIMIT, stack);
            case DRINK -> accepts(SlotInputLimitManager.TEAPOT_DRINK_LIMIT, stack);
            case MATERIAL -> accepts(SlotInputLimitManager.TEAPOT_MATERIAL_LIMIT, stack);
            default -> slot >= TOP_LEFT && slot <= BOTTOM_RIGHT;
        };
    }

    @Override
    protected int automationSlotLimit(int slot) {
        return slot == RESULT ? super.automationSlotLimit(slot) : 4;
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
        return new TeapotMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.isRunning = pTag.getBoolean("isRunning");
        this.setCookingTime(pTag.getInt("cookingTime"));
        this.setOutput(ItemStack.of(pTag.getCompound("output")));
        this.consumedInputs.clear();
        ListTag consumed = pTag.getList("consumedInputs", Tag.TAG_COMPOUND);
        for (int i = 0; i < consumed.size(); i++) {
            this.consumedInputs.add(ItemStack.of(consumed.getCompound(i)));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("isRunning", this.isRunning);
        pTag.putInt("cookingTime", this.cookingTime);
        pTag.put("output", this.output.save(new CompoundTag()));
        ListTag consumed = new ListTag();
        for (ItemStack stack : this.consumedInputs) {
            consumed.add(stack.save(new CompoundTag()));
        }
        pTag.put("consumedInputs", consumed);
    }
}
