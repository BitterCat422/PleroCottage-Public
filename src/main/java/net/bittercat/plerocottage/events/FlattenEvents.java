package net.bittercat.plerocottage.events;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.effects.FlattenState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID)
public final class FlattenEvents {
    private FlattenEvents() {
    }

    @SubscribeEvent
    public static void onEntitySize(EntityEvent.Size event) {
        // 尺寸事件也在实体构造期间触发，此时同步数据尚未初始化。
        if (event.getEntity() instanceof LivingEntity entity && entity.isAddedToWorld()
            && ((FlattenState) entity).pleroCottage$isFlattened()) {
            EntityDimensions size = event.getNewSize();
            event.setNewSize(new EntityDimensions(size.width * 1.3F, size.height * 0.5F, size.fixed));
            event.setNewEyeHeight(event.getNewEyeHeight() * 0.5F);
        }
    }
}
