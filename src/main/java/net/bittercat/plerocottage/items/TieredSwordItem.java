package net.bittercat.plerocottage.items;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public final class TieredSwordItem extends SwordItem {
    private final Item repairMaterial;
    private final int burnTime;

    public TieredSwordItem(Tier tier, int attackDamage, Item repairMaterial, int burnTime) {
        
        super(tier, attackDamage - 1 - (int) tier.getAttackDamageBonus(), -2.0F,
            new Item.Properties().durability(tier.getUses()));
        this.repairMaterial = repairMaterial;
        this.burnTime = burnTime;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return repairMaterial != null ? repair.is(repairMaterial) : super.isValidRepairItem(stack, repair);
    }

    @Override
    public int getBurnTime(ItemStack stack, RecipeType<?> recipeType) {
        return burnTime;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return UtensilFoodInteraction.eat(context);
    }

    public void restoreFoodOnHit(Player player) {
        FoodData food = player.getFoodData();
        food.eat(1, player.getRandom().nextInt(100) < 67 ? 1.0F : 0.0F);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return toolAction != ToolActions.SWORD_SWEEP && super.canPerformAction(stack, toolAction);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment != Enchantments.SWEEPING_EDGE && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return EnchantmentHelper.getEnchantments(book).keySet().stream()
            .noneMatch(enchantment -> enchantment == Enchantments.SWEEPING_EDGE)
            && super.isBookEnchantable(stack, book);
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tips.plerocottage.utensil_eating"));
    }
}
