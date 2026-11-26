package net.bittercat.plerocottage.machine.network.server.packet;

import net.bittercat.plerocottage.machine.items.ShakerItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShakerSelectPacket {
    private final int containerId;
    private final int slotIndex;
    private final int selectedSlot;

    public ShakerSelectPacket(int containerId, int slotIndex, int selectedSlot) {
        this.containerId = containerId;
        this.slotIndex = slotIndex;
        this.selectedSlot = selectedSlot;
    }

    public static ShakerSelectPacket decode(FriendlyByteBuf buffer) {
        return new ShakerSelectPacket(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(containerId);
        buffer.writeVarInt(slotIndex);
        buffer.writeVarInt(selectedSlot);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        ServerPlayer player = context.getSender();
        if (player != null) apply(player);
        context.setPacketHandled(true);
    }

    private void apply(ServerPlayer player) {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu.containerId != containerId || slotIndex < 0 || slotIndex >= menu.slots.size()) return;
        Slot slot = menu.getSlot(slotIndex);
        if (!slot.allowModification(player) || !(slot.getItem().getItem() instanceof ShakerItem)) return;
        ItemStack shaker = slot.getItem();
        ItemStackHandler contents = ShakerItem.contents(shaker);
        if (!ShakerItem.selectExtractionSlot(shaker, contents, selectedSlot)) return;
        slot.setChanged();
        menu.broadcastChanges();
    }
}
