package net.bittercat.plerocottage.items;

import java.util.Map;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

import net.minecraft.world.item.enchantment.EnchantmentHelper;

public final class PopcornBucketItem extends ConsumableItem {

    public PopcornBucketItem(FoodDefinition definition) {
        super(definition.itemProperties().durability(18),
            definition.useDuration(), definition.useAnim(), null, false, false);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        if (consumer instanceof Player player) {
            player.getFoodData().eat(1, 0.6F);
        }

        if (stack.getDamageValue() == 5 || stack.getDamageValue() == 11 || stack.getDamageValue() == 17) {
            if (level.isClientSide()) {
                level.playLocalSound(consumer.getX(), consumer.getY(), consumer.getZ(),
                    SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, 1.0F, false);
            } else {
                level.playSound(null, BlockPos.containing(consumer.getX(), consumer.getY(), consumer.getZ()),
                    SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }

        removeRepairEnchantments(stack);
        if (!(consumer instanceof Player player && player.getAbilities().instabuild)
            && stack.hurt(1, RandomSource.create(), null)) {
            stack.shrink(1);
            stack.setDamageValue(0);
        }
        return stack;
    }

    private static void removeRepairEnchantments(ItemStack stack) {
        if (!stack.isEnchanted()) {
            return;
        }
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        enchantments.remove(Enchantments.UNBREAKING);
        enchantments.remove(Enchantments.MENDING);
        EnchantmentHelper.setEnchantments(enchantments, stack);
    }
}
