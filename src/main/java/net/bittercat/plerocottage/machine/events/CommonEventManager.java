package net.bittercat.plerocottage.machine.events;

import net.minecraftforge.eventbus.api.IEventBus;
import net.bittercat.plerocottage.machine.PleroCottageMachineConfig;

public class CommonEventManager {
    public static void init(IEventBus modEventBus, IEventBus forgeEventBus) {
        CommonEventManager.modBusListener(modEventBus);
        CommonEventManager.forgeBusListener(forgeEventBus);
    }

    public static void modBusListener(IEventBus modEventBus) {
        modEventBus.addListener(PleroCottageMachineConfig::handlerModConfigEvent);
    }

    public static void forgeBusListener(IEventBus forgeEventBus) {
        forgeEventBus.addListener(CommonEventHandler::handlerServerStartedEvent);
        forgeEventBus.addListener(CommonEventHandler::handlerTagsUpdatedEvent);
        forgeEventBus.addListener(CommonEventHandler::handlerVillagerTradesEvent);
        forgeEventBus.addListener(CommonEventHandler::handlerMissingMappingsEvent);
    }
}
