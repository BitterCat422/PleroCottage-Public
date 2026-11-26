package net.bittercat.plerocottage.machine.config;

import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.PleroCottageMachineConfig;
import net.bittercat.plerocottage.machine.network.PleroCottageMachinePacket;
import net.bittercat.plerocottage.machine.network.client.packet.ServerConfigSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottageMachine.MOD_ID)
public final class MachineConfigSyncManager {
    private MachineConfigSyncManager() {
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PleroCottageMachinePacket.sendToClient(player,
                new ServerConfigSyncPacket(PleroCottageMachineConfig.localSnapshot()));
        }
    }
}
