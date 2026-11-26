package net.bittercat.plerocottage.items;

import java.util.List;

import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public final class DigestiveTabletsItem extends ConsumableItem {
    public static final int MAX_USES = 6;

    public DigestiveTabletsItem() {
        super(new Item.Properties()
            .food(FoodDefinition.basicProperties(0, 0.0F, true))
            .durability(MAX_USES)
            .setNoRepair(),
            16);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        if (!level.isClientSide && consumer instanceof Player player) {
            FoodData foodData = player.getFoodData();
            foodData.setFoodLevel(Math.max(0, foodData.getFoodLevel() - 6));
            foodData.setSaturation(0.0F);
            if (!player.getAbilities().instabuild) {
                int damage = stack.getDamageValue() + 1;
                if (damage >= stack.getMaxDamage()) {
                    return ItemStack.EMPTY;
                }
                stack.setDamageValue(damage);
            }
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tips.plerocottage.digestive_tablets"));
    }
}
