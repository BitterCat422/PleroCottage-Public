package net.bittercat.plerocottage.config;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.network.NetworkHandler;
import net.bittercat.plerocottage.network.ServerConfigSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

public final class ConfigSyncManager {
    private ConfigSyncManager() {
    }

    public static void send(ServerPlayer player) {
        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
            new ServerConfigSyncPacket(SyncedConfig.localSnapshot()));
    }

    @Mod.EventBusSubscriber(modid = PleroCottage.MODID)
    public static final class ForgeEvents {
        @SubscribeEvent
        public static void onServerStart(ServerAboutToStartEvent event) {
            ConfigValidation.checkHatchingRegistries();
        }

        @SubscribeEvent
        public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                send(player);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class ModEvents {
        @SubscribeEvent
        public static void onReload(ModConfigEvent.Reloading event) {
            if (event.getConfig().getSpec() != Config.COMMON_CONFIG) {
                return;
            }
            var server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                server.execute(() -> {
                    if (ServerLifecycleHooks.getCurrentServer() == server) {
                        ConfigValidation.checkHatchingRegistries();
                        NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(),
                            new ServerConfigSyncPacket(SyncedConfig.localSnapshot()));
                    }
                });
            }
        }
    }
}
