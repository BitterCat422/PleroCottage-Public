package net.bittercat.plerocottage.machine;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.bittercat.plerocottage.machine.client.events.ClientEventManager;

@OnlyIn(Dist.CLIENT)
public class PleroCottageMachineClient {
    public static void init(IEventBus modEventBus, IEventBus forgeEventBus) {
        ClientEventManager.init(modEventBus, forgeEventBus);
    }
}
