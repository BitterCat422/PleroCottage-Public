package net.bittercat.plerocottage.client;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.config.SyncedConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, value = Dist.CLIENT)
public final class ClientConfigSyncEvents {
    private ClientConfigSyncEvents() {
    }

    public static void apply(SyncedConfig.Snapshot snapshot) {
        var previous = SyncedConfig.clientSnapshot();
        SyncedConfig.update(snapshot);
        if (previous == null
            || !previous.gemExcavationBlacklist().equals(snapshot.gemExcavationBlacklist())
            || !previous.gemExcavationWhitelist().equals(snapshot.gemExcavationWhitelist())) {
            GemExcavationRenderEvents.refreshScan();
        }
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        SyncedConfig.clear();
        GemExcavationRenderEvents.reset();
    }
}
