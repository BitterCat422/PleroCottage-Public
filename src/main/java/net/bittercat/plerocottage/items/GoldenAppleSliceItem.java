package net.bittercat.plerocottage.items;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class GoldenAppleSliceItem extends ConsumableItem {

    private final boolean enchanted;

    public GoldenAppleSliceItem(Properties properties, boolean enchanted) {
        super(properties);
        this.enchanted = enchanted;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return enchanted || super.isFoil(stack);
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if (enchanted) {
            consumer.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 800, 3, false, true));
            consumer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 140, 1, false, true));
            consumer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2_000, 0, false, true));
            consumer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2_000, 0, false, true));
        } else {
            consumer.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 800, 0, false, true));
            consumer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 1, false, true));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        ConsumableFoodSupport.appendEffectTooltip(tooltip,
            new MobEffectInstance(MobEffects.ABSORPTION, 800, enchanted ? 3 : 0));
        ConsumableFoodSupport.appendEffectTooltip(tooltip,
            new MobEffectInstance(MobEffects.REGENERATION, enchanted ? 140 : 40, 1));
        if (enchanted) {
            ConsumableFoodSupport.appendEffectTooltip(tooltip,
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2_000, 0));
            ConsumableFoodSupport.appendEffectTooltip(tooltip,
                new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2_000, 0));
        }
    }
}
