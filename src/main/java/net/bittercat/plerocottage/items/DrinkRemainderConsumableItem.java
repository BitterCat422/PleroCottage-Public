package net.bittercat.plerocottage.items;

import java.util.function.Supplier;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class DrinkRemainderConsumableItem extends ConsumableItem {

    private final boolean clearsEffects;

    public DrinkRemainderConsumableItem(Properties properties, int useDuration, Supplier<? extends Item> remainder,
                                        boolean clearsEffects) {
        super(properties, useDuration, UseAnim.DRINK, remainder, true, false);
        this.clearsEffects = clearsEffects;
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if (clearsEffects) {
            consumer.removeAllEffects();
        }
    }
}
