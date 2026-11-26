package net.bittercat.plerocottage.machine;

import net.bittercat.plerocottage.config.Config;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.bittercat.plerocottage.machine.config.MachineSyncedConfig;

public class PleroCottageMachineConfig {
    private static volatile MachineSyncedConfig.Snapshot startupSnapshot;

    public static void handlerModConfigEvent(ModConfigEvent event) {
        if (event instanceof ModConfigEvent.Loading && event.getConfig().getSpec() == Config.COMMON_CONFIG) {
            
            startupSnapshot = new MachineSyncedConfig.Snapshot(Config.FARMERS_DELIGHT_CUTTING_BOARD_COMPAT.get(),
                Config.FARMERS_DELIGHT_COOKING_POT_COMPAT.get());
        }
    }

    public static MachineSyncedConfig.Snapshot localSnapshot() {
        return startupSnapshot;
    }

    public static boolean cuttingBoardCompat() {
        var synced = MachineSyncedConfig.clientSnapshot();
        return synced != null ? synced.cuttingBoardCompat() : startupSnapshot.cuttingBoardCompat();
    }

    public static boolean cookingPotCompat() {
        var synced = MachineSyncedConfig.clientSnapshot();
        return synced != null ? synced.cookingPotCompat() : startupSnapshot.cookingPotCompat();
    }
}
