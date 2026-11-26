package net.bittercat.plerocottage.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.bittercat.plerocottage.client.ClientConfigSyncEvents;
import net.bittercat.plerocottage.config.SyncedConfig;
import net.bittercat.plerocottage.config.ConfigValidation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public record ServerConfigSyncPacket(SyncedConfig.Snapshot snapshot) {
    public static void encode(ServerConfigSyncPacket packet, FriendlyByteBuf buffer) {
        var value = packet.snapshot();
        buffer.writeDouble(value.rareVariantChance());
        buffer.writeDouble(value.leavesCanFruitChance());
        buffer.writeDouble(value.leavesBlossomChance());
        buffer.writeDouble(value.leavesFruitChance());
        buffer.writeDouble(value.leavesDropChance());
        buffer.writeDouble(value.leavesFlowerDecayChance());
        buffer.writeDouble(value.blackDirtGrowTruffleChance());
        buffer.writeBoolean(value.enableMediterraneanPlain());
        writeList(buffer, value.mentholAwakeningBlacklist());
        writeList(buffer, value.mentholAwakeningWhitelist());
        writeList(buffer, value.gemExcavationBlacklist());
        writeList(buffer, value.gemExcavationWhitelist());
        writeList(buffer, value.incubatorHatchingMappings());
    }

    public static ServerConfigSyncPacket decode(FriendlyByteBuf buffer) {
        return new ServerConfigSyncPacket(new SyncedConfig.Snapshot(
            buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
            buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean(),
            readList(buffer), readList(buffer), readList(buffer), readList(buffer), readList(buffer)));
    }

    public static void handle(ServerConfigSyncPacket packet, Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
            () -> () -> ClientConfigSyncEvents.apply(packet.snapshot())));
        context.setPacketHandled(true);
    }

    private static void writeList(FriendlyByteBuf buffer, List<String> values) {
        if (!ConfigValidation.fitsNetworkLimits(values)) {
            throw new IllegalArgumentException("Configuration list exceeds network limits");
        }
        buffer.writeVarInt(values.size());
        for (String value : values) {
            buffer.writeUtf(value, ConfigValidation.MAX_STRING_LENGTH);
        }
    }

    private static List<String> readList(FriendlyByteBuf buffer) {
        int start = buffer.readerIndex();
        int size = buffer.readVarInt();
        if (size < 0 || size > ConfigValidation.MAX_LIST_ENTRIES) {
            throw new IllegalArgumentException("Invalid configuration list size: " + size);
        }
        List<String> values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            values.add(buffer.readUtf(ConfigValidation.MAX_STRING_LENGTH));
            if (buffer.readerIndex() - start > ConfigValidation.MAX_LIST_BYTES) {
                throw new IllegalArgumentException("Configuration list exceeds encoded size limit");
            }
        }
        return values;
    }
}
