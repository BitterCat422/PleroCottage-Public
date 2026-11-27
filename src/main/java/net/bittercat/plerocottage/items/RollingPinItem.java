package net.bittercat.plerocottage.items;

import net.bittercat.plerocottage.events.AttackCharge;
import java.util.List;

import net.bittercat.plerocottage.content.effects.MobEffectContent;
import net.bittercat.plerocottage.content.audio.SoundContent;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public final class RollingPinItem extends TieredToolItem {
    public RollingPinItem(Tier tier, Item repairMaterial, int burnTime) {
        super(tier, 1, -2.8F, new Properties().durability(tier.getUses()), 0, burnTime, repairMaterial);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return toolAction != ToolActions.SWORD_SWEEP && super.canPerformAction(stack, toolAction);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return !isForbiddenEnchantment(enchantment) && super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return EnchantmentHelper.getEnchantments(book).keySet().stream()
            .noneMatch(RollingPinItem::isForbiddenEnchantment) && super.isBookEnchantable(stack, book);
    }

    private static boolean isForbiddenEnchantment(Enchantment enchantment) {
        return enchantment == Enchantments.SHARPNESS
            || enchantment == Enchantments.SWEEPING_EDGE
            || enchantment == Enchantments.SMITE
            || enchantment == Enchantments.BANE_OF_ARTHROPODS
            || enchantment == Enchantments.FIRE_ASPECT;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (result && !attacker.level().isClientSide() && attacker instanceof Player player
            && AttackCharge.of(player) > 0.9F) {
            MobEffectInstance current = target.getEffect(MobEffectContent.FLATTEN.get());
            int amplifier = current == null ? 0 : current.getAmplifier();
            MobEffectInstance flatten = new MobEffectInstance(MobEffectContent.FLATTEN.get(), 100, amplifier);
            
            target.forceAddEffect(flatten, attacker);
            if (current == null && target.hasEffect(MobEffectContent.FLATTEN.get())) {
                target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundContent.ROLLING_PIN_FLATTEN.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tips.plerocottage.rolling_pin_flatten"));
    }
}
