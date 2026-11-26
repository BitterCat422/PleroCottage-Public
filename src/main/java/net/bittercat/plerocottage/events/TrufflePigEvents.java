package net.bittercat.plerocottage.events;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.entities.ai.TruffleDigGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID)
public final class TrufflePigEvents {

    private TrufflePigEvents() {
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Pig pig) || event.getLevel().isClientSide()) {
            return;
        }
        boolean alreadyAdded = pig.goalSelector.getAvailableGoals().stream()
            .anyMatch(goal -> goal.getGoal() instanceof TruffleDigGoal);
        if (!alreadyAdded) {
            pig.goalSelector.addGoal(4, new TruffleDigGoal(pig));
        }
    }
}
