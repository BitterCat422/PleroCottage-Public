package net.bittercat.plerocottage.machine.client.events;

import net.bittercat.plerocottage.machine.client.gui.ShakerTooltip;
import net.bittercat.plerocottage.machine.items.ShakerItem;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.machine.network.server.packet.ShakerSelectPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.items.ItemStackHandler;

public final class ShakerClientEvents {
    private ShakerClientEvents() {
    }

    public static void onMouseScrolled(ScreenEvent.MouseScrolled.Pre event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) return;
        Slot slot = screen.getSlotUnderMouse();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || slot == null || !slot.allowModification(player) || event.getScrollDelta() == 0) return;
        ItemStack shaker = slot.getItem();
        if (!(shaker.getItem() instanceof ShakerItem)) return;
        AbstractContainerMenu menu = screen.getMenu();
        int slotIndex = menu.slots.indexOf(slot);
        if (screen instanceof CreativeModeInventoryScreen) {
            menu = player.inventoryMenu;
            slotIndex = -1;
            for (int i = 0; i < menu.slots.size(); i++) {
                Slot inventorySlot = menu.getSlot(i);
                if (inventorySlot.getItem() == shaker && inventorySlot.allowModification(player)) {
                    slotIndex = i;
                    break;
                }
            }
        }
        if (slotIndex < 0) return;
        ItemStackHandler contents = ShakerItem.contents(shaker);
        event.setCanceled(true);
        int direction = event.getScrollDelta() > 0 ? -1 : 1;
        int selected = ShakerItem.cycleExtractionSlot(shaker, contents, direction);
        if (selected < 0) return;
        if (ShakerItem.selectExtractionSlot(shaker, contents, selected)) {
            PleroCottageMachinePacket.sendToServer(new ShakerSelectPacket(menu.containerId, slotIndex, selected));
        }
    }

    public static void onRenderPost(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) return;
        Slot slot = screen.getSlotUnderMouse();
        if (slot == null || !(slot.getItem().getItem() instanceof ShakerItem)) return;
        ItemStack shaker = slot.getItem();
        ItemStackHandler contents = ShakerItem.contents(shaker);
        LocalPlayer player = Minecraft.getInstance().player;
        ShakerTooltip.render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), shaker,
                screen.getMenu().getCarried(), contents, player != null && slot.allowModification(player));
    }
}
