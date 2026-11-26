package net.bittercat.plerocottage.machine.menus;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;

public final class MachineRecipeBookUtil {
    private MachineRecipeBookUtil() {
    }

    public static void accountStack(StackedContents contents, ItemStack stack) {
        if (PleroCottageCompat.isDamageableMaterial(stack)) {
            int remainingDurability = stack.getMaxDamage() - stack.getDamageValue();
            if (remainingDurability > 0) {
                ItemStack durabilityStack = stack.copyWithCount(remainingDurability);
                contents.accountStack(durabilityStack, remainingDurability);
            }
        } else {
            contents.accountSimpleStack(stack);
        }
    }

    public static void accountInventoryDamageable(StackedContents contents, Inventory inventory) {
        for (ItemStack stack : inventory.items) {
            if (!PleroCottageCompat.isDamageableMaterial(stack)) {
                continue;
            }
            int remainingDurability = stack.getMaxDamage() - stack.getDamageValue();
            boolean accountedByVanilla = !stack.isDamaged()
                    && !stack.isEnchanted()
                    && !stack.hasCustomHoverName();
            int additionalDurability = remainingDurability - (accountedByVanilla ? stack.getCount() : 0);
            if (additionalDurability > 0) {
                ItemStack durabilityStack = stack.copyWithCount(additionalDurability);
                contents.accountStack(durabilityStack, additionalDurability);
            }
        }
    }
}
