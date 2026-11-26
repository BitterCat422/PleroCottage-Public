package net.bittercat.plerocottage.machine.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;
import net.bittercat.plerocottage.machine.recipes.ShakerRecipe;
import net.bittercat.plerocottage.init.RecipeRegistry;
import net.bittercat.plerocottage.machine.util.MachineUtil;
import net.bittercat.plerocottage.machine.util.SoundUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShakerItem extends Item {
    public static final int ITEMS_SIZE = 8;
    public static final int SLOT_LIMIT = 4;
    private static final String CONTENTS_TAG = "ShakerContents";
    private static final String SELECTED_SLOT_TAG = "ShakerSelectedSlot";
    private static final int SHAKE_DURATION = 40;

    public ShakerItem() {
        super(new Properties().stacksTo(1));
    }

    public static ItemStackHandler contents(ItemStack stack) {
        return new ShakerContents(stack);
    }

    public static int nextExtractionSlot(ItemStack shaker, ItemStackHandler contents) {
        CompoundTag tag = shaker.getTag();
        if (tag != null && tag.contains(SELECTED_SLOT_TAG)) {
            int selected = tag.getInt(SELECTED_SLOT_TAG);
            if (isOccupiedSlot(contents, selected)) return selected;
        }
        for (int slot = contents.getSlots() - 1; slot >= 0; slot--) {
            if (!contents.getStackInSlot(slot).isEmpty()) return slot;
        }
        return -1;
    }

    private static boolean isOccupiedSlot(ItemStackHandler contents, int slot) {
        return slot >= 0 && slot < contents.getSlots() && !contents.getStackInSlot(slot).isEmpty();
    }

    public static int cycleExtractionSlot(ItemStack shaker, ItemStackHandler contents, int direction) {
        int selected = nextExtractionSlot(shaker, contents);
        if (selected < 0) return -1;
        for (int step = 1; step < contents.getSlots(); step++) {
            int next = Math.floorMod(selected + direction * step, contents.getSlots());
            if (isOccupiedSlot(contents, next)) return next;
        }
        return selected;
    }

    public static boolean selectExtractionSlot(ItemStack shaker, ItemStackHandler contents, int slot) {
        if (!isOccupiedSlot(contents, slot) || nextExtractionSlot(shaker, contents) == slot) return false;
        shaker.getOrCreateTag().putInt(SELECTED_SLOT_TAG, slot);
        return true;
    }

    public static int nextInsertionSlot(ItemStackHandler contents, ItemStack incoming) {
        for (int slot = 0; slot < contents.getSlots(); slot++) {
            ItemStack existing = contents.getStackInSlot(slot);
            if (!existing.isEmpty() && contents.isItemValid(slot, incoming)
                    && ItemHandlerHelper.canItemStacksStack(existing, incoming)
                    && existing.getCount() < Math.min(contents.getSlotLimit(slot), existing.getMaxStackSize())) {
                return slot;
            }
        }
        for (int slot = 0; slot < contents.getSlots(); slot++) {
            if (contents.getStackInSlot(slot).isEmpty() && contents.isItemValid(slot, incoming)) return slot;
        }
        return -1;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack shaker, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player) || !slot.hasItem()) return false;
        ItemStackHandler contents = contents(shaker);
        int target = nextInsertionSlot(contents, slot.getItem());
        if (target < 0) return false;

        ItemStack existing = contents.getStackInSlot(target);
        int limit = existing.isEmpty()
                ? Math.min(contents.getSlotLimit(target), slot.getItem().getMaxStackSize())
                : Math.min(contents.getSlotLimit(target), existing.getMaxStackSize()) - existing.getCount();
        ItemStack extracted = slot.safeTake(limit, limit, player);
        if (extracted.isEmpty()) return false;
        contents.insertItem(target, extracted, false);
        syncClick(player, slot, SoundEvents.BUNDLE_INSERT);
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack shaker, ItemStack carried, Slot slot, ClickAction action,
                                             Player player, SlotAccess cursor) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;
        ItemStackHandler contents = contents(shaker);
        if (carried.isEmpty()) {
            int source = nextExtractionSlot(shaker, contents);
            if (source < 0 || !cursor.set(contents.getStackInSlot(source).copy())) return false;
            contents.setStackInSlot(source, ItemStack.EMPTY);
            shaker.removeTagKey(SELECTED_SLOT_TAG);
            syncClick(player, slot, SoundEvents.BUNDLE_REMOVE_ONE);
        } else {
            int target = nextInsertionSlot(contents, carried);
            if (target < 0) return false;
            ItemStack remainder = contents.insertItem(target, carried, true);
            int inserted = carried.getCount() - remainder.getCount();
            if (inserted <= 0 || !cursor.set(remainder)) return false;
            contents.insertItem(target, carried.copyWithCount(inserted), false);
            syncClick(player, slot, SoundEvents.BUNDLE_INSERT);
        }
        return true;
    }

    private static void syncClick(Player player, Slot slot, SoundEvent sound) {
        slot.setChanged();
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.containerMenu.broadcastChanges();
        }
        if (player.level().isClientSide()) player.playSound(sound, 0.8F, 1.0F);
    }

    private static void giveOrDrop(Player player, ItemStack stack) {
        if (stack.isEmpty()) return;
        ItemStack remainder = ItemHandlerHelper.insertItemStacked(new PlayerMainInvWrapper(player.getInventory()), stack, false);
        if (!remainder.isEmpty()) player.drop(remainder, false);
    }

    private static Optional<ShakerRecipe> findRecipe(Level level, ShakerRecipe.Input input) {
        return level.getRecipeManager().getRecipeFor(RecipeRegistry.SHAKER_RECIPE_TYPE.get(), input, level);
    }

    private static void craft(ServerPlayer player, Level level, ItemStack shaker, ItemStackHandler contents) {
        ItemStack offhand = player.getOffhandItem();
        ShakerRecipe.Input input = new ShakerRecipe.Input(contents, offhand);
        ShakerRecipe recipe = findRecipe(level, input).orElse(null);
        if (recipe == null) {
            syncFailedUse(player, shaker, "结束时无匹配配方");
            return;
        }
        ShakerRecipe.CraftPlan plan = recipe.plan(input);
        if (plan == null) {
            syncFailedUse(player, shaker, "结束时无法分配配料");
            return;
        }
        int batches = plan.batches();

        ItemStack output = recipe.assemble(input, level.registryAccess());
        output.setCount(output.getCount() * batches);
        List<ItemStack> toReturn = new ArrayList<>();
        for (int slot = 0; slot < input.getContainerSize(); slot++) {
            int used = plan.used()[slot];
            if (used == 0) continue;
            ItemStack material = input.getItem(slot).copy();
            ItemStack remainder = MachineUtil.getCraftRemaining(material, used);
            if (PleroCottageCompat.isDamageableMaterial(material)) {
                PleroCottageCompat.damageItem(used, material, level);
                if (!material.isEmpty()) remainder = ItemStack.EMPTY;
            } else {
                material.shrink(used);
            }
            contents.setStackInSlot(slot, material.isEmpty() ? ItemStack.EMPTY : material);
            if (material.isEmpty() && !remainder.isEmpty()) {
                remainder = contents.insertItem(slot, remainder, false);
            }
            if (!remainder.isEmpty()) toReturn.add(remainder);
        }
        if (recipe.hasContainer()) {
            ItemStack remainder = MachineUtil.getCraftRemaining(offhand, batches);
            ItemStack unused = offhand.copyWithCount(offhand.getCount() - batches);
            player.setItemInHand(InteractionHand.OFF_HAND, output);
            giveOrDrop(player, unused);
            giveOrDrop(player, remainder);
        } else {
            giveOrDrop(player, output);
        }
        toReturn.forEach(stack -> giveOrDrop(player, stack));
        level.playSound(null, player.blockPosition(), SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS);
    }

    private static void syncFailedUse(ServerPlayer player, ItemStack shaker, String reason) {
        PleroCottage.LOGGER.debug("雪克壶调制失败（{}），配料 NBT：{}，副手：{}",
                reason, shaker.getTagElement(CONTENTS_TAG), player.getOffhandItem());
        player.inventoryMenu.sendAllDataToRemote();
        if (player.containerMenu != player.inventoryMenu) player.containerMenu.sendAllDataToRemote();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return SHAKE_DURATION;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pUsedHand != InteractionHand.MAIN_HAND) return InteractionResultHolder.fail(itemStack);
        if (pLevel.isClientSide()) {
            
            return InteractionResultHolder.consume(itemStack);
        }
        ShakerRecipe.Input input = new ShakerRecipe.Input(contents(itemStack), pPlayer.getOffhandItem());
        if (findRecipe(pLevel, input).isEmpty()) {
            if (pPlayer instanceof ServerPlayer serverPlayer) {
                syncFailedUse(serverPlayer, itemStack, "起手时无匹配配方");
            }
            return InteractionResultHolder.fail(itemStack);
        }
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
        if (level.isClientSide() && entity instanceof Player player && remaining == SHAKE_DURATION) {
            SoundUtil.playShakerSoundInstance(player);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof ServerPlayer player
                && player.getUsedItemHand() == InteractionHand.MAIN_HAND && player.getMainHandItem() == pStack) {
            craft(player, pLevel, pStack, contents(pStack));
        }
        return pStack;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    private static final class ShakerContents extends ItemStackHandler {
        private final ItemStack owner;

        private ShakerContents(ItemStack owner) {
            super(ITEMS_SIZE);
            this.owner = owner;
            CompoundTag tag = owner.getTagElement(CONTENTS_TAG);
            if (tag != null) deserializeNBT(tag);
        }

        @Override
        public int getSlotLimit(int slot) {
            return SLOT_LIMIT;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem().canFitInsideContainerItems();
        }

        @Override
        public void setSize(int size) {
            super.setSize(ITEMS_SIZE);
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (isEmpty()) {
                owner.removeTagKey(CONTENTS_TAG);
            } else {
                owner.getOrCreateTag().put(CONTENTS_TAG, serializeNBT());
            }
        }

        private boolean isEmpty() {
            for (int slot = 0; slot < getSlots(); slot++) {
                if (!getStackInSlot(slot).isEmpty()) return false;
            }
            return true;
        }
    }
}
