package net.bittercat.plerocottage.mixin;

import net.bittercat.plerocottage.content.plants.Bushes;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin {

    // require = 0：别的模组也重定向了同一处调用时，这里放弃注入而不是让游戏启动崩溃
    @Redirect(require = 0, method = "renderBg", at = @At(value = "FIELD",
        target = "Lnet/minecraft/client/player/LocalPlayer;experienceLevel:I", opcode = 180))
    private int pleroCottage$ignoreExperienceForConversion(LocalPlayer player) {
        AbstractContainerScreenAccessor screen = (AbstractContainerScreenAccessor) (Object) this;
        EnchantmentMenu menu = (EnchantmentMenu) screen.pleroCottage$getMenu();
        return menu.getSlot(0).getItem().is(Bushes.RASPBERRY_BUSH.fruitItem())
            ? Math.max(player.experienceLevel, menu.costs[0])
            : player.experienceLevel;
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void pleroCottage$clickConversion(double mouseX, double mouseY, int button,
                                               CallbackInfoReturnable<Boolean> cir) {
        AbstractContainerScreenAccessor screen = (AbstractContainerScreenAccessor) (Object) this;
        if (button == 0
            && screen.pleroCottage$getMenu().getSlot(0).getItem().is(Bushes.RASPBERRY_BUSH.fruitItem())
            && mouseX >= screen.pleroCottage$getLeftPos() + 60
            && mouseX < screen.pleroCottage$getLeftPos() + 80
            && mouseY >= screen.pleroCottage$getTopPos() + 14
            && mouseY < screen.pleroCottage$getTopPos() + 34) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.gameMode != null) {
                minecraft.gameMode.handleInventoryButtonClick(screen.pleroCottage$getMenu().containerId, 0);
                cir.setReturnValue(true);
            }
        }
    }

    // require = 0：别的模组也重定向了同一处调用时，这里放弃注入而不是让游戏启动崩溃
    @Redirect(require = 0, method = "render", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/gui/GuiGraphics;renderComponentTooltip("
            + "Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V"))
    private void pleroCottage$renderConversionTooltip(GuiGraphics graphics, Font font,
                                                       List<Component> lines, int x, int y) {
        AbstractContainerScreenAccessor screen = (AbstractContainerScreenAccessor) (Object) this;
        if (screen.pleroCottage$getMenu().getSlot(0).getItem().is(Bushes.RASPBERRY_BUSH.fruitItem())) {
            List<Component> conversion = new ArrayList<>();
            conversion.add(Component.translatable("gui.plerocottage.raspberry_conversion"));
            conversion.add(Component.translatable("gui.plerocottage.raspberry_conversion_cost")
                .withStyle(ChatFormatting.GRAY));
            graphics.renderComponentTooltip(font, conversion, x, y);
        } else {
            graphics.renderComponentTooltip(font, lines, x, y);
        }
    }
}
