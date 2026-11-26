package net.bittercat.plerocottage.machine.client.gui;

import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.items.ShakerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public final class ShakerTooltip {
    private static final int COLUMNS = 4;
    private static final int SLOT_SIZE = 18;
    private static final int INSERTION_ICON_SIZE = 8;
    private static final ResourceLocation ALLOW = icon("storage_bag_allow");
    private static final ResourceLocation NOT_ALLOW = icon("storage_bag_notallow");
    private static final ResourceLocation STACK = icon("storage_bag_stack");

    private ShakerTooltip() {
    }

    public static void render(GuiGraphics graphics, int mouseX, int mouseY, ItemStack shaker, ItemStack carried,
                              ItemStackHandler contents, boolean modifiable) {
        Minecraft minecraft = Minecraft.getInstance();
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 400.0F);
        if (!carried.isEmpty()) {
            int target = modifiable ? ShakerItem.nextInsertionSlot(contents, carried) : -1;
            ResourceLocation state = target < 0 ? NOT_ALLOW : contents.getStackInSlot(target).isEmpty() ? ALLOW : STACK;
            graphics.blit(state, mouseX - 4, Math.max(0, mouseY - 17), 0, 0,
                    INSERTION_ICON_SIZE, INSERTION_ICON_SIZE, INSERTION_ICON_SIZE, INSERTION_ICON_SIZE);
        }
        int selected = ShakerItem.nextExtractionSlot(shaker, contents);
        if (selected >= 0) {
            int occupied = 0;
            for (int slot = 0; slot < contents.getSlots(); slot++) {
                if (!contents.getStackInSlot(slot).isEmpty()) occupied++;
            }
            int rows = (occupied + COLUMNS - 1) / COLUMNS;
            int width = COLUMNS * SLOT_SIZE + 8;
            int height = rows * SLOT_SIZE + 20;
            int maxLeft = Math.max(4, minecraft.getWindow().getGuiScaledWidth() - width - 4);
            int maxTop = Math.max(4, minecraft.getWindow().getGuiScaledHeight() - height - 4);
            int left = mouseX - width - 12;
            int top = mouseY - 12;
            if (left < 4) {
                left = mouseX + 12;
                top = mouseY + 18 <= maxTop ? mouseY + 18 : mouseY - height - 20;
            }
            left = Mth.clamp(left, 4, maxLeft);
            top = Mth.clamp(top, 4, maxTop);
            graphics.fill(left, top, left + width, top + height, 0xE0101010);
            graphics.fill(left + 1, top + 1, left + width - 1, top + height - 1, 0xE0303A4A);

            int index = 0;
            for (int slot = 0; slot < contents.getSlots(); slot++) {
                ItemStack item = contents.getStackInSlot(slot);
                if (item.isEmpty()) continue;
                int x = left + 4 + (index % COLUMNS) * SLOT_SIZE;
                int y = top + 4 + (index / COLUMNS) * SLOT_SIZE;
                if (slot == selected) graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF60B6FF);
                graphics.renderItem(item, x, y);
                graphics.renderItemDecorations(minecraft.font, item, x, y);
                index++;
            }
            String name = contents.getStackInSlot(selected).getHoverName().getString();
            graphics.drawString(minecraft.font, minecraft.font.plainSubstrByWidth(name, width - 8),
                    left + 4, top + rows * SLOT_SIZE + 7, 0xFFFFFF, false);
        }
        graphics.pose().popPose();
    }

    private static ResourceLocation icon(String name) {
        return new ResourceLocation(PleroCottageMachine.MOD_ID, "textures/gui/" + name + ".png");
    }
}
