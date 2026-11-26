package net.bittercat.plerocottage.items;

import net.bittercat.plerocottage.events.AlfalfaFeeding;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AlfalfaItem extends Item {

    public AlfalfaItem() {
        super(new Item.Properties());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean hurt = super.hurtEnemy(stack, target, attacker);
        AlfalfaFeeding.feed(target, attacker);
        return hurt;
    }
}
