package net.bittercat.plerocottage.items;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mojang.datafixers.util.Pair;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.food.FoodProperties;

public final class ConsumableFoodSupport {

    private ConsumableFoodSupport() {
    }

    public static void appendFoodEffects(ItemStack stack, List<Component> tooltip) {
        Map<MobEffect, MobEffectInstance> effects = new LinkedHashMap<>();
        FoodProperties food = stack.getItem().getFoodProperties(stack, null);
        if (food != null) {
            for (Pair<MobEffectInstance, Float> pair : food.getEffects()) {
                effects.put(pair.getFirst().getEffect(), pair.getFirst());
            }
        }

        for (MobEffectInstance effect : effects.values()) {
            appendEffectTooltip(tooltip, effect);
        }
    }

    public static void appendEffectTooltip(List<Component> tooltip, MobEffectInstance effect) {
        tooltip.add(formatEffect(effect));
    }

    private static Component formatEffect(MobEffectInstance instance) {
        MutableComponent text = Component.translatable(instance.getDescriptionId());
        if (instance.getAmplifier() > 0) {
            text = Component.translatable("potion.withAmplifier", text,
                Component.translatable("potion.potency." + instance.getAmplifier()));
        }
        if (instance.getDuration() > 20) {
            text = Component.translatable("potion.withDuration", text,
                MobEffectUtil.formatDuration(instance, 1.0F));
        }
        return text.withStyle(instance.getEffect().getCategory().getTooltipFormatting());
    }

}
