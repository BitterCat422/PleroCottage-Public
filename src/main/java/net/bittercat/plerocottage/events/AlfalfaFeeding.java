package net.bittercat.plerocottage.events;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.content.plants.Crops;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID)
public final class AlfalfaFeeding {

    private static final int MAX_FEEDINGS = 3;

    private static final String FED_TAG = "alfalfaUsed";

    private AlfalfaFeeding() {
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != event.getEntity().getUsedItemHand()) {
            return;
        }
        feed(event.getTarget(), event.getEntity());
    }

    public static void feed(Entity target, Entity source) {
        if (!(target instanceof Animal animal) || !(source instanceof Player player)) {
            return;
        }
        ItemStack held = player.getMainHandItem();
        if (held.getItem() != Crops.ALFALFA.get()) {
            return;
        }
        if (animal.isBaby()) {
            int fed = animal.getPersistentData().getInt(FED_TAG);
            if (fed >= MAX_FEEDINGS) {
                return;
            }
            player.swing(InteractionHand.MAIN_HAND, true);
            animal.setAge(animal.getAge() - animal.getAge() / 2);
            spawnHappyParticles(animal);
            animal.getPersistentData().putInt(FED_TAG, fed + 1);
            consume(player, held);
        } else if (animal.getLoveCause() == null && !animal.isInLove() && animal.getAge() == 0) {
            player.swing(InteractionHand.MAIN_HAND, true);
            animal.setInLove(player);
            consume(player, held);
        }
    }

    private static void spawnHappyParticles(Animal animal) {
        if (!(animal.level() instanceof ServerLevel level)) {
            return;
        }
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, animal.getX(), animal.getY() + animal.getBbHeight(),
            animal.getZ(), 20, 0.25, 0.25, 0.25, 0.5);
    }

    private static void consume(Player player, ItemStack held) {
        if (!player.getAbilities().instabuild) {
            held.shrink(1);
        }
    }
}
