package net.bittercat.plerocottage.machine.network.client.packet;

import java.util.function.Supplier;
import net.bittercat.plerocottage.machine.client.events.MachineClientConfigSyncEvents;
import net.bittercat.plerocottage.machine.config.MachineSyncedConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public record ServerConfigSyncPacket(MachineSyncedConfig.Snapshot snapshot) {
    public static void encode(ServerConfigSyncPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.snapshot().cuttingBoardCompat());
        buffer.writeBoolean(packet.snapshot().cookingPotCompat());
    }

    public static ServerConfigSyncPacket decode(FriendlyByteBuf buffer) {
        return new ServerConfigSyncPacket(new MachineSyncedConfig.Snapshot(
            buffer.readBoolean(), buffer.readBoolean()));
    }

    public static void handle(ServerConfigSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
            () -> () -> MachineClientConfigSyncEvents.apply(packet.snapshot())));
        context.setPacketHandled(true);
    }
}
