package net.bittercat.plerocottage.mixin;

import net.bittercat.plerocottage.content.plants.Bushes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {

    @Shadow public int[] costs;
    @Shadow public int[] enchantClue;
    @Shadow public int[] levelClue;
    @Shadow private ContainerLevelAccess access;
    @Inject(method = "slotsChanged", at = @At("HEAD"), cancellable = true)
    private void pleroCottage$specialSlotsChanged(net.minecraft.world.Container container, CallbackInfo ci) {
        EnchantmentMenu menu = (EnchantmentMenu) (Object) this;
        ItemStack input = menu.getSlot(0).getItem();
        if (!isRaspberry(input)) {
            return;
        }

        costs[0] = 1;
        costs[1] = 0;
        costs[2] = 0;
        enchantClue[0] = 0;
        enchantClue[1] = -1;
        enchantClue[2] = -1;
        levelClue[0] = 0;
        levelClue[1] = 0;
        levelClue[2] = 0;
        ci.cancel();
    }

    @Inject(method = "clickMenuButton", at = @At("HEAD"), cancellable = true)
    private void pleroCottage$convertRaspberry(Player player, int button, CallbackInfoReturnable<Boolean> cir) {
        EnchantmentMenu menu = (EnchantmentMenu) (Object) this;
        if (button != 0 || !isRaspberry(menu.getSlot(0).getItem())) {
            return;
        }

        ItemStack lapis = menu.getSlot(1).getItem();
        if (!player.getAbilities().instabuild && !lapis.is(Items.LAPIS_LAZULI)) {
            cir.setReturnValue(false);
            return;
        }

        menu.getSlot(0).set(new ItemStack(Bushes.BLUE_RASPBERRY_BUSH.fruitItem()));
        if (!player.getAbilities().instabuild) {
            lapis.shrink(1);
        }
        access.execute((level, pos) -> level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE,
            SoundSource.BLOCKS, 1.0F, 1.0F));
        menu.broadcastChanges();
        cir.setReturnValue(true);
    }

    private static boolean isRaspberry(ItemStack stack) {
        return stack.is(Bushes.RASPBERRY_BUSH.fruitItem());
    }
}
