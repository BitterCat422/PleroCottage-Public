package net.bittercat.plerocottage.items;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public final class AnimalKnifeItem extends SwordItem {
    private final Item repairMaterial;
    private final int burnTime;

    public AnimalKnifeItem(Tier tier, int attackDamage, float attackSpeed, Properties properties,
                           Item repairMaterial, int burnTime) {
        super(tier, attackDamage, attackSpeed, properties);
        this.repairMaterial = repairMaterial;
        this.burnTime = burnTime;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        return repair.is(repairMaterial) || super.isValidRepairItem(stack, repair);
    }

    @Override
    public int getBurnTime(ItemStack stack, RecipeType<?> recipeType) {
        return burnTime;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tips.plerocottage.animal_knife_damage"));
    }
}
