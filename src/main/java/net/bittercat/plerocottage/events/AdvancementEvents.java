package net.bittercat.plerocottage.events;

import java.util.List;

import net.bittercat.plerocottage.content.effects.MobEffectContent;
import net.bittercat.plerocottage.content.plants.DoubleCrops;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "plerocottage")
public final class AdvancementEvents {
    private static final ResourceLocation LYCOPERSICON =
        new ResourceLocation("plerocottage", "lycopersicon");
    private static final ResourceLocation AN_INSANE_COCKTAIL =
        new ResourceLocation("plerocottage", "an_insane_cocktail");
    private static final List<RegistryObject<MobEffect>> REQUIRED_FRUIT_EFFECTS = List.of(
        MobEffectContent.ROSA_HEDGE,
        MobEffectContent.RUTIN_LIMONENE,
        MobEffectContent.BURSTING_BERRY,
        MobEffectContent.CHERRY_BLOSSOMS_WEEPING,
        MobEffectContent.MOMENTARY_METEOR,
        MobEffectContent.SUMMER_HEATWAVE,
        MobEffectContent.ORIGINAL_EVOLUTION,
        MobEffectContent.MELON_GRAVITY,
        MobEffectContent.SEA_TOUCH,
        MobEffectContent.HACKED_THORNS,
        MobEffectContent.KIWING_WHEREABOUTS,
        MobEffectContent.LURKING_DANGER,
        MobEffectContent.GEM_EXCAVATION,
        MobEffectContent.POISON_HEAL_MEDICINE,
        MobEffectContent.HAPPY_AURA,
        MobEffectContent.DARK_WRATH,
        MobEffectContent.TRANSMUTATION_ELIXIR
    );

    private AdvancementEvents() {
    }

    public static void clearFruitEffects(LivingEntity entity) {
        REQUIRED_FRUIT_EFFECTS.stream()
            .map(RegistryObject::get)
            .forEach(entity::removeEffect);
    }

    @SubscribeEvent
    public static void onItemUseFinished(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer player
            && (event.getItem().is(DoubleCrops.TOMATO.get())
                || event.getItem().is(net.bittercat.plerocottage.content.food.FoodContent.item("tomato_slice").get()))) {
            award(player, LYCOPERSICON);
        }
    }

    @SubscribeEvent
    public static void onMobEffectAdded(MobEffectEvent.Added event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        boolean hasAllEffects = REQUIRED_FRUIT_EFFECTS.stream()
            .map(RegistryObject::get)
            .allMatch(player::hasEffect);
        if (hasAllEffects) {
            award(player, AN_INSANE_COCKTAIL);
        }
    }

    private static void award(ServerPlayer player, ResourceLocation id) {
        Advancement advancement = player.server.getAdvancements().getAdvancement(id);
        if (advancement == null) {
            return;
        }

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (!progress.isDone()) {
            progress.getRemainingCriteria()
                .forEach(criteria -> player.getAdvancements().award(advancement, criteria));
        }
    }
}
