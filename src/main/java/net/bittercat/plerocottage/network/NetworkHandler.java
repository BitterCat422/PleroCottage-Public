package net.bittercat.plerocottage.network;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.effects.MobEffectContent;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;
import java.util.Optional;

public final class NetworkHandler {
    private static final String PROTOCOL_VERSION = "4";
    private static final String GEM_EXCAVATION_PARTICLE_TICK = "PleroCottageGemExcavationParticleTick";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(PleroCottage.MODID, "main"),
        () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int nextId;

    static {
        CHANNEL.registerMessage(nextId++, TomatoKnowledgeMessage.class,
            TomatoKnowledgeMessage::encode, TomatoKnowledgeMessage::decode,
            TomatoKnowledgeMessage::handle);
        CHANNEL.registerMessage(nextId++, GemExcavationParticlesMessage.class,
            GemExcavationParticlesMessage::encode, GemExcavationParticlesMessage::decode,
            GemExcavationParticlesMessage::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(nextId++, ServerConfigSyncPacket.class,
            ServerConfigSyncPacket::encode, ServerConfigSyncPacket::decode,
            ServerConfigSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    private NetworkHandler() {
    }

    public static void init() {
    }

    public static void syncTomatoKnowledge(ServerPlayer player, boolean value) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
            new TomatoKnowledgeMessage(value));
    }

    public record GemExcavationParticlesMessage() {
        private static void encode(GemExcavationParticlesMessage message, FriendlyByteBuf buffer) {
        }

        private static GemExcavationParticlesMessage decode(FriendlyByteBuf buffer) {
            return new GemExcavationParticlesMessage();
        }

        private static void handle(GemExcavationParticlesMessage message,
                                   Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player == null || !player.hasEffect(MobEffectContent.GEM_EXCAVATION.get())) {
                    return;
                }
                var data = player.getPersistentData();
                int elapsed = player.tickCount - data.getInt(GEM_EXCAVATION_PARTICLE_TICK);
                // 为网络时序留出余量，同时限制客户端重复请求。
                if (data.contains(GEM_EXCAVATION_PARTICLE_TICK) && elapsed >= 0 && elapsed < 80) {
                    return;
                }
                data.putInt(GEM_EXCAVATION_PARTICLE_TICK, player.tickCount);
                var level = player.serverLevel();
                for (int i = 0; i < 16; i++) {
                    double x = player.getRandomX(1.0D);
                    double y = player.getRandomY();
                    double z = player.getRandomZ(1.0D);
                    for (ServerPlayer viewer : level.players()) {
                        if (viewer.distanceToSqr(player) <= 32.0D * 32.0D) {
                            level.sendParticles(viewer, DustParticleOptions.REDSTONE, false,
                                x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }

    public record TomatoKnowledgeMessage(boolean hasEatenTomato) {
        private static void encode(TomatoKnowledgeMessage message, FriendlyByteBuf buffer) {
            buffer.writeBoolean(message.hasEatenTomato);
        }

        private static TomatoKnowledgeMessage decode(FriendlyByteBuf buffer) {
            return new TomatoKnowledgeMessage(buffer.readBoolean());
        }

        private static void handle(TomatoKnowledgeMessage message,
                                   Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (context.getDirection().getReceptionSide().isClient()) {
                    DistExecutor.unsafeRunWhenOn(net.minecraftforge.api.distmarker.Dist.CLIENT,
                        () -> () -> TomatoKnowledgeClientHandler.handle(message.hasEatenTomato));
                }
            });
            context.setPacketHandled(true);
        }
    }
}
