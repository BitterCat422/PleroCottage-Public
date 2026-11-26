package net.bittercat.plerocottage.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class SeasoningItem extends Item {

    public SeasoningItem() {
        super(new Item.Properties().stacksTo(1).durability(8).setNoRepair());
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack remainder = new ItemStack(this);
        remainder.setDamageValue(stack.getDamageValue() + 1);
        return remainder.getDamageValue() >= remainder.getMaxDamage() ? ItemStack.EMPTY : remainder;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
