package net.bittercat.plerocottage.init;

import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.Blocks;
import net.bittercat.plerocottage.wood.WoodSet;
import net.bittercat.plerocottage.content.wood.WoodSetContent;
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

    /** 可燃性与原版木材一致：原木和木头 5/5，木板、楼梯、台阶、栅栏、栅栏门 5/20；门、活板门、按钮等原版也不可燃。 */
    @SubscribeEvent
    public static void registerFlammables(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FireBlock fire = (FireBlock) Blocks.FIRE;
            for (WoodSet wood : WoodSetContent.all()) {
                fire.setFlammable(wood.log().get(), 5, 5);
                fire.setFlammable(wood.strippedLog().get(), 5, 5);
                if (wood.wood() != null) {
                    fire.setFlammable(wood.wood().get(), 5, 5);
                    fire.setFlammable(wood.strippedWood().get(), 5, 5);
                }
                fire.setFlammable(wood.planks().get(), 5, 20);
                fire.setFlammable(wood.stairs().get(), 5, 20);
                fire.setFlammable(wood.slab().get(), 5, 20);
                fire.setFlammable(wood.fence().get(), 5, 20);
                fire.setFlammable(wood.fenceGate().get(), 5, 20);
            }
        });
    }
}

