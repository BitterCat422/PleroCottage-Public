package net.bittercat.plerocottage.events;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.entities.PoultryEntity;
import net.bittercat.plerocottage.content.entities.EntityContent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PoultryEvents {
    private PoultryEvents() {
    }

    @SubscribeEvent
    public static void attributes(EntityAttributeCreationEvent event) {
        event.put(EntityContent.MALLARD_DUCK.get(), PoultryEntity.createAttributes().build());
        event.put(EntityContent.TURKEY.get(), PoultryEntity.createAttributes().build());
    }
}
