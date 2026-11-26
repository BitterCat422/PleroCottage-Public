package net.bittercat.plerocottage.machine.client.events;

import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CookingPotRecipeCompat;
import net.bittercat.plerocottage.machine.compat.farmersdelight.CuttingBoardRecipeCompat;
import net.bittercat.plerocottage.machine.compat.plerocottage.SlotInputLimitManager;
import net.bittercat.plerocottage.machine.config.MachineSyncedConfig;
import net.bittercat.plerocottage.machine.compat.jei.PleroCottageMachineJeiPlugin;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

@Mod.EventBusSubscriber(modid = PleroCottageMachine.MOD_ID, value = Dist.CLIENT)
public final class MachineClientConfigSyncEvents {
    private static RecipeManager recipes;

    private MachineClientConfigSyncEvents() {
    }

    public static void apply(MachineSyncedConfig.Snapshot snapshot) {
        if (MachineSyncedConfig.clientSnapshot() != null) {
            return;
        }
        MachineSyncedConfig.update(snapshot);
        if (recipes != null) {
            rebuild();
            SlotInputLimitManager.refreshCompatLimits(recipes);
        }
    }

    public static boolean isReady() {
        return recipes != null && MachineSyncedConfig.clientSnapshot() != null;
    }

    public static void recipesUpdated(RecipeManager manager) {
        recipes = manager;
        rebuild();
    }

    private static void rebuild() {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER || !isReady()) {
            return;
        }
        CuttingBoardRecipeCompat.init(recipes);
        CookingPotRecipeCompat.init(recipes);
        if (ModList.get().isLoaded("jei")) {
            PleroCottageMachineJeiPlugin.completeInitialConfigSync();
        }
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        recipes = null;
        MachineSyncedConfig.clear();
        CuttingBoardRecipeCompat.clearClient();
        CookingPotRecipeCompat.clearClient();
        SlotInputLimitManager.clearClientCompatLimits();
    }
}
