package net.bittercat.plerocottage.events;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.entities.PoultryEntity;
import net.bittercat.plerocottage.content.entities.EntityContent;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
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

    @SubscribeEvent
    public static void spawnPlacements(SpawnPlacementRegisterEvent event) {
        // 火鸡在草甸生成（forge/biome_modifier/turkey_biome_modifier.json），规则同原版家畜：草地上、够亮
        event.register(EntityContent.TURKEY.get(), SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules,
            SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
