package net.bittercat.plerocottage.items;

import java.util.function.Supplier;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.bittercat.plerocottage.events.AdvancementEvents;

public class DisplayFoodItem extends ItemNameBlockItem {

    private final int useDuration;
    private final UseAnim useAnim;
    private final Supplier<? extends Item> container;

    public DisplayFoodItem(Block block, Properties properties, int useDuration,
                           UseAnim useAnim, Supplier<? extends Item> container) {
        super(block, properties);
        this.useDuration = useDuration;
        this.useAnim = useAnim;
        this.container = container;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useDuration;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return useAnim;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return container != null;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return container == null ? ItemStack.EMPTY : new ItemStack(container.get());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return super.useOn(context);
        }
        return InteractionResult.PASS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        ItemStack result = super.finishUsingItem(stack, level, consumer);
        if (!level.isClientSide() && blockPath().equals("lemonade")) {
            AdvancementEvents.clearFruitEffects(consumer);
        }
        if (container != null && result.isEmpty()) {
            return new ItemStack(container.get());
        }
        if (container != null && consumer instanceof Player player && !player.getAbilities().instabuild) {
            ItemStack returnedContainer = new ItemStack(container.get());
            if (!player.getInventory().add(returnedContainer)) {
                player.drop(returnedContainer, false);
            }
        }
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, java.util.List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        ConsumableFoodSupport.appendFoodEffects(stack, tooltip);
        if (blockPath().equals("lemonade")) {
            tooltip.add(Component.translatable("tips.plerocottage.lemonade_tips"));
        }
    }

    private String blockPath() {
        return ForgeRegistries.BLOCKS.getKey(getBlock()).getPath();
    }

}
