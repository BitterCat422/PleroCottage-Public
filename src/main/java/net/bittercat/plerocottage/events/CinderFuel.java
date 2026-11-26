package net.bittercat.plerocottage.events;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.core.CoreContent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID)
public final class CinderFuel {

    private CinderFuel() {
    }

    @SubscribeEvent
    public static void onFuel(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().is(CoreContent.CINDER.get())) {
            event.setBurnTime(30);
        }
    }
}
