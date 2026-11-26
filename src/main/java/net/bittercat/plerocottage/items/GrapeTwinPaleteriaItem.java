package net.bittercat.plerocottage.items;

import java.util.List;
import java.util.function.Supplier;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;

public final class GrapeTwinPaleteriaItem extends Item {

    private final Supplier<? extends Item> grape;
    private final Supplier<? extends Item> muscatGrape;

    public GrapeTwinPaleteriaItem(Supplier<? extends Item> grape,
                                  Supplier<? extends Item> muscatGrape) {
        super(new Item.Properties().stacksTo(16));
        this.grape = grape;
        this.muscatGrape = muscatGrape;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable(
            "tips." + PleroCottage.MODID + ".grape_twin_paleta_tips"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(grape.get()));
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(muscatGrape.get()));
            level.playSound(null, player.blockPosition(), SoundEvents.GLASS_PLACE,
                SoundSource.PLAYERS, 1.0F, 1.0F);
            player.swing(hand, true);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}

