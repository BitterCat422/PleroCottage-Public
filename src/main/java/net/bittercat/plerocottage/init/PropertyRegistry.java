package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.plants.PlantEntry;
import net.bittercat.plerocottage.content.plants.PlantCatalog;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PropertyRegistry {

    private PropertyRegistry() {
    }

    @SubscribeEvent
    public static void registerCompostables(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (PlantEntry plant : PlantCatalog.all()) {
                plant.compostables().forEach((item, chance) -> ComposterBlock.COMPOSTABLES.put(item, chance));
            }
            for (var entry : PlantCatalog.extraCompostables().entrySet()) {
                ComposterBlock.COMPOSTABLES.put(entry.getKey().get(), entry.getValue());
            }
            PleroCottage.LOGGER.debug("Registered {} plants.", PlantCatalog.all().size());
        });
    }
}

