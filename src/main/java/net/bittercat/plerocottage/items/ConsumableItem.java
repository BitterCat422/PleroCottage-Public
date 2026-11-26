package net.bittercat.plerocottage.items;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ConsumableItem extends Item {

    private final int useDuration;
    private final UseAnim useAnimation;
    private final Supplier<? extends Item> remainder;
    private final boolean remainderForCrafting;
    private final boolean returnsSelf;

    public ConsumableItem(Properties properties) {
        this(properties, 32);
    }

    public ConsumableItem(Properties properties, int useDuration) {
        this(properties, useDuration, UseAnim.EAT, null, false, false);
    }

    public ConsumableItem(Properties properties, int useDuration, UseAnim useAnimation,
                          Supplier<? extends Item> remainder, boolean remainderForCrafting,
                          boolean returnsSelf) {
        super(properties);
        this.useDuration = useDuration;
        this.useAnimation = useAnimation;
        this.remainder = remainder;
        this.remainderForCrafting = remainderForCrafting;
        this.returnsSelf = returnsSelf;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useDuration;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return useAnimation;
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return remainder != null && remainderForCrafting;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return hasCraftingRemainingItem() ? new ItemStack(remainder.get()) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        if (!level.isClientSide) {
            affectConsumer(stack, level, consumer);
        }
        ItemStack result = super.finishUsingItem(stack, level, consumer);
        if (remainder == null && !returnsSelf) {
            return result;
        }

        ItemStack returnedStack = new ItemStack(returnsSelf ? this : remainder.get());
        if (stack.isEmpty()) {
            return returnedStack;
        }
        if (consumer instanceof Player player && !player.getAbilities().instabuild
            && !player.getInventory().add(returnedStack)) {
            player.drop(returnedStack, false);
        }
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        ConsumableFoodSupport.appendFoodEffects(stack, tooltip);
    }

    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
    }
}
