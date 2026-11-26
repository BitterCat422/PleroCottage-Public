package net.bittercat.plerocottage.client;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.effects.FlattenState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.event.GeoRenderEvent;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, value = Dist.CLIENT)
public final class FlattenRenderEvents {
    private FlattenRenderEvents() {
    }

    @SubscribeEvent
    public static void onGeoEntityRender(GeoRenderEvent.Entity.Pre event) {
        if (event.getEntity() instanceof LivingEntity entity
            && ((FlattenState) entity).pleroCottage$isFlattened()) {
            event.getPoseStack().scale(1.3F, 0.5F, 1.3F);
        }
    }
}
