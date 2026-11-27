package net.bittercat.plerocottage.events;

import net.minecraft.tags.DamageTypeTags;
import net.bittercat.plerocottage.config.Config;
import net.bittercat.plerocottage.content.effects.MobEffectContent;
import net.bittercat.plerocottage.content.audio.SoundContent;
import net.bittercat.plerocottage.content.particles.ParticleContent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.bittercat.plerocottage.items.AnimalKnifeItem;

import java.util.UUID;
import java.util.List;

@Mod.EventBusSubscriber(modid = "plerocottage")
public final class MobEffectEvents {

    private static final String TRANSMUTATION_REENTRY_TAG = "PleroCottageTransmutationReentry";
    private static final String TRANSMUTATION_TARGET_TAG = "PleroCottageTransmutationTarget";
    private static final String TRANSMUTATION_COUNT_TAG = "PleroCottageTransmutationCount";
    private static final String MOMENTARY_METEOR_PENDING_TARGET_TAG = "PleroCottageMomentaryMeteorPendingTarget";
    private static final String MENTHOL_AWAKENING_REENTRY_TAG = "PleroCottageMentholAwakeningReentry";
    private static final String MENTHOL_AWAKENING_PENDING_UPGRADES_TAG = "PleroCottageMentholAwakeningPendingUpgrades";
    private static final String MENTHOL_AWAKENING_PENDING_DOWNGRADE_TAG = "PleroCottageMentholAwakeningPendingDowngrade";
    private static final String MENTHOL_AWAKENING_ACTIVE_LAST_TICK_TAG = "PleroCottageMentholAwakeningActiveLastTick";
    private static final int MAX_EFFECT_DURATION = 72_000;
    private static final String BERRY_ARMOR_TICKS = "PleroCottageBerryArmorTicks";
    private static final UUID BERRY_ARMOR_MODIFIER_ID =
        UUID.fromString("b5d18f64-410c-4d25-9975-1ec82c8e7f48");

    private MobEffectEvents() {
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity target = event.getEntity();

        if (target.hasEffect(MobEffectContent.ROSA_HEDGE.get())) {
            int amplifier = target.getEffect(MobEffectContent.ROSA_HEDGE.get()).getAmplifier();
            if (event.getAmount() <= 2.0F + 0.25F * (amplifier + 1)
                && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                event.setCanceled(true);
            }
        }

        if (target.hasEffect(MobEffectContent.HACKED_THORNS.get()) && event.getSource().getDirectEntity() instanceof Projectile) {
            event.setCanceled(true);
        }
        if (event.isCanceled()) {
            return;
        }

        if (event.getSource().getEntity() instanceof Player attacker
            && attacker.hasEffect(MobEffectContent.BURSTING_BERRY.get())
            && isCritical(event.getSource(), attacker)) {
            applyTemporaryArmorBreak(target);
        }

        if (event.getSource().getEntity() instanceof Player attacker
            && attacker.hasEffect(MobEffectContent.LURKING_DANGER.get())) {
            attacker.getPersistentData().putInt("PleroCottageLurkingTicks", -10);
        }

    }

    private static boolean isCritical(net.minecraft.world.damagesource.DamageSource source, Player player) {
        if (source.getDirectEntity() instanceof AbstractArrow arrow) {
            return arrow.isCritArrow();
        }
        return source.getDirectEntity() == player
            && player.fallDistance > 0 && !player.onGround() && !player.isInWater()
            && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger() && !player.isSprinting()
            && AttackCharge.of(player) >= 0.9F;
    }

    private static void applyTemporaryArmorBreak(LivingEntity target) {
        if (target.level().isClientSide()) {
            return;
        }
        var armor = target.getAttribute(Attributes.ARMOR);
        if (armor == null) {
            return;
        }
        AttributeModifier modifier = new AttributeModifier(BERRY_ARMOR_MODIFIER_ID,
            "Plero Cottage Berry Armor Break", -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        if (armor.getModifier(BERRY_ARMOR_MODIFIER_ID) != null) {
            armor.removeModifier(BERRY_ARMOR_MODIFIER_ID);
        }
        armor.addTransientModifier(modifier);
        target.getPersistentData().putInt(BERRY_ARMOR_TICKS, 60);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return;
        }
        processMentholAwakeningChanges(entity);
        var data = entity.getPersistentData();
        int ticks = data.getInt(BERRY_ARMOR_TICKS);
        if (ticks <= 0) {
            return;
        }
        var armor = entity.getAttribute(Attributes.ARMOR);
        if (armor != null && armor.getModifier(BERRY_ARMOR_MODIFIER_ID) == null) {
            armor.addTransientModifier(new AttributeModifier(BERRY_ARMOR_MODIFIER_ID,
                "Plero Cottage Berry Armor Break", -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        if (ticks == 1) {
            if (armor != null) {
                armor.removeModifier(BERRY_ARMOR_MODIFIER_ID);
            }
            data.remove(BERRY_ARMOR_TICKS);
        } else {
            data.putInt(BERRY_ARMOR_TICKS, ticks - 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onTransmutationAttack(LivingAttackEvent event) {
        LivingEntity target = event.getEntity();
        LivingEntity attacker = event.getSource().getEntity() instanceof LivingEntity living ? living : null;
        if (target.level().isClientSide() || event.isCanceled()
            || target.getPersistentData().getBoolean(TRANSMUTATION_REENTRY_TAG)
            || !(attacker instanceof Player player) || event.getSource().getDirectEntity() != attacker
            || player.getPersistentData().getBoolean("PleroCottageHappySweep")
            || AttackCharge.of(player) < 0.9F
            || !attacker.hasEffect(MobEffectContent.TRANSMUTATION_ELIXIR.get())) {
            return;
        }
        var data = attacker.getPersistentData();
        String targetId = target.getStringUUID();
        if (!data.getString(TRANSMUTATION_TARGET_TAG).equals(targetId)) {
            data.putString(TRANSMUTATION_TARGET_TAG, targetId);
            data.putInt(TRANSMUTATION_COUNT_TAG, 0);
        }
        int count = data.getInt(TRANSMUTATION_COUNT_TAG) + 1;
        data.putInt(TRANSMUTATION_COUNT_TAG, count % 3);
        if (count % 3 == 0) {
            int amplifier = attacker.getEffect(MobEffectContent.TRANSMUTATION_ELIXIR.get()).getAmplifier();
            float baseRatio = 0.45F + 0.025F * (amplifier + 1);
            float randomMultiplier = 0.15F + target.getRandom().nextFloat() * 1.10F;
            float ratio = Math.min(0.85F, Math.round(baseRatio * randomMultiplier * 100.0F) / 100.0F);
            float extraDamage = target.getHealth() * ratio;
            target.getPersistentData().putBoolean(TRANSMUTATION_REENTRY_TAG, true);
            boolean damaged;
            try {
                damaged = target.hurt(target.damageSources().magic(), extraDamage);
            } finally {
                target.getPersistentData().remove(TRANSMUTATION_REENTRY_TAG);
            }
            if (damaged) {
                target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundContent.TRANSMUTATION_DAZZLING_GLEAM.get(), SoundSource.PLAYERS, 1.25F, 1.0F);
                if (player.level() instanceof ServerLevel level) {
                    var targetBox = target.getBoundingBox();
                    double collisionScale = Math.max(
                        targetBox.getXsize() / 0.6D,
                        targetBox.getYsize() / 1.8D);
                    double scale = 1.0D + (collisionScale - 1.0D) * 0.25D;
                    
                    level.sendParticles(ParticleContent.TRANSMUTATION_MAGIC_CIRCLE.get(),
                        targetBox.getCenter().x, targetBox.getCenter().y, targetBox.getCenter().z,
                        0, scale, 0.0D, 0.0D, 1.0D);
                }
                if (player.hasEffect(MobEffectContent.HAPPY_AURA.get())) {
                    applyHappyAuraSweep(player, target, extraDamage);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        var damageSource = event.getSource();
        var attacker = damageSource.getEntity();

        if (target instanceof Animal animal && !animal.isBaby()
            && damageSource.getDirectEntity() == attacker
            && attacker instanceof LivingEntity livingAttacker
            && livingAttacker.getMainHandItem().getItem() instanceof AnimalKnifeItem) {
            event.setAmount(event.getAmount() * 2.0F);
        }

        if (target.hasEffect(MobEffectContent.DARK_WRATH.get()) && attacker instanceof LivingEntity) {
            int amplifier = target.getEffect(MobEffectContent.DARK_WRATH.get()).getAmplifier();
            event.setAmount(event.getAmount() * (1.0F - Math.min(1.0F, 0.50F + 0.05F * (amplifier + 1))));
        }

        if (attacker instanceof Player livingAttacker && damageSource.getDirectEntity() == livingAttacker
            && !livingAttacker.getPersistentData().getBoolean("PleroCottageHappySweep")
            && livingAttacker.hasEffect(MobEffectContent.DARK_WRATH.get())) {
            int amplifier = livingAttacker.getEffect(MobEffectContent.DARK_WRATH.get()).getAmplifier();
            event.setAmount(event.getAmount() * (1.5F + 0.05F * (amplifier + 1)));
        }

        if (attacker instanceof Player livingAttacker
            && !livingAttacker.getPersistentData().getBoolean("PleroCottageHappySweep")
            && isCritical(event)
            && livingAttacker.hasEffect(MobEffectContent.BURSTING_BERRY.get())) {
            int amplifier = livingAttacker.getEffect(MobEffectContent.BURSTING_BERRY.get()).getAmplifier();
            event.setAmount(event.getAmount() * (1.0F + 0.50F + 0.05F * (amplifier + 1)));
        }

        if (attacker instanceof Player livingAttacker && damageSource.getDirectEntity() == livingAttacker
            && !livingAttacker.getPersistentData().getBoolean("PleroCottageHappySweep")
            && livingAttacker.hasEffect(MobEffectContent.MOMENTARY_METEOR.get())
            && AttackCharge.of(livingAttacker) >= 0.9F) {
            int amplifier = livingAttacker.getEffect(MobEffectContent.MOMENTARY_METEOR.get()).getAmplifier();
            event.setAmount(event.getAmount() * (5.0F + 0.15F * (amplifier + 1)));
            livingAttacker.getPersistentData().putString(
                MOMENTARY_METEOR_PENDING_TARGET_TAG, target.getStringUUID());
        }

        if (attacker instanceof Player player && damageSource.getDirectEntity() == player
            && !player.getPersistentData().getBoolean("PleroCottageHappySweep")
            && player.hasEffect(MobEffectContent.HAPPY_AURA.get())) {
            applyHappyAuraSweep(player, target, event.getAmount());
        }

        if (target.hasEffect(MobEffectContent.KIWING_WHEREABOUTS.get())) {
            int amplifier = target.getEffect(MobEffectContent.KIWING_WHEREABOUTS.get()).getAmplifier();
            if (attacker instanceof LivingEntity && !target.level().isClientSide()) {
                int duration = 10 + (amplifier + 1) * 2;
                target.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, duration, 0, false, false));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, 2, false, false));
                target.invulnerableTime = Math.max(target.invulnerableTime, duration + 10);
            }
            if (damageSource.is(net.minecraft.tags.DamageTypeTags.IS_FALL)) {
                event.setAmount(event.getAmount() * (1.0F - (0.4F + (amplifier + 1) * 0.04F)));
            }
        }

        if (target.hasEffect(MobEffectContent.ICE_BARRIER.get())) {
            reduceWithIceBarrier(event);
        }

        if (target.hasEffect(MobEffectContent.SNOW_VEIL.get())) {
            reduceWithSnowVeil(event);
        }

        if (target.hasEffect(MobEffectContent.AURORA_VEIL.get())) {
            reduceWithAuroraVeil(event);
        }

        if (target.hasEffect(MobEffectContent.HACKED_THORNS.get()) && attacker instanceof LivingEntity
            && damageSource.getDirectEntity() == attacker && !damageSource.is(DamageTypes.THORNS)) {
            int amplifier = target.getEffect(MobEffectContent.HACKED_THORNS.get()).getAmplifier();
            Vec3 direction = attacker.position().subtract(target.position()).normalize()
                .scale(0.35 * (amplifier + 1));
            attacker.setDeltaMovement(direction);
            float reflected = event.getAmount() * target.getArmorValue()
                * (0.05F + 0.005F * (amplifier + 1));
            if (reflected > 0) {
                attacker.hurt(target.damageSources().thorns(target), reflected);
            }
            target.level().playSound(null, target.blockPosition(),
                net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_KNOCKBACK,
                net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 1.5F);
        }

        if (target.hasEffect(MobEffectContent.POISON_HEAL_MEDICINE.get()) && damageSource.is(DamageTypes.MAGIC)) {
            int amplifier = target.getEffect(MobEffectContent.POISON_HEAL_MEDICINE.get()).getAmplifier();
            float heal = event.getAmount() * (0.70F + 0.05F * (amplifier + 1));
            event.setAmount(0.0F);
            target.heal(heal);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!(event.getSource().getDirectEntity() instanceof Player player)) {
            return;
        }
        var data = player.getPersistentData();
        if (!data.getString(MOMENTARY_METEOR_PENDING_TARGET_TAG).equals(event.getEntity().getStringUUID())) {
            return;
        }
        data.remove(MOMENTARY_METEOR_PENDING_TARGET_TAG);
        if (event.getAmount() > 0.0F && player.level() instanceof ServerLevel level) {
            level.playSound(null, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                SoundContent.MOMENTARY_METEOR_STARDUST_SHATTER.get(), SoundSource.PLAYERS, 1.15F, 1.0F);
            spawnMomentaryMeteorStars(level, event.getEntity());
        }
        if (event.getAmount() < event.getEntity().getHealth()) {
            player.removeEffect(MobEffectContent.MOMENTARY_METEOR.get());
        }
    }

    private static void spawnMomentaryMeteorStars(ServerLevel level, LivingEntity target) {
        Vec3 center = target.getBoundingBox().getCenter();
        level.sendParticles(ParticleContent.MOMENTARY_METEOR_STARS.get(),
            center.x, center.y, center.z, 5,
            0.15D, 0.15D, 0.15D, 0.30D);
    }

    private static boolean isCritical(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof AbstractArrow arrow) {
            return arrow.isCritArrow();
        }
        return event.getSource().getEntity() instanceof Player player
            && event.getSource().getDirectEntity() == player
            && player.fallDistance > 0 && !player.onGround() && !player.isInWater()
            && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger() && !player.isSprinting()
            && AttackCharge.of(player) >= 0.9F;
    }

    private static void applyHappyAuraSweep(Player player, LivingEntity target, float damage) {
        int amplifier = player.getEffect(MobEffectContent.HAPPY_AURA.get()).getAmplifier();
        float sweepDamage = damage * (0.70F + 0.05F * (amplifier + 1));
        for (LivingEntity nearby : player.level().getEntitiesOfClass(LivingEntity.class,
            target.getBoundingBox().inflate(1.0D, 0.25D, 1.0D),
            other -> other != target && other != player
                && !other.hasEffect(MobEffectContent.HAPPY_AURA.get())
                && !(other instanceof TamableAnimal tamable && tamable.isTame()))) {
            player.getPersistentData().putBoolean("PleroCottageHappySweep", true);
            try {
                nearby.hurt(player.damageSources().playerAttack(player), sweepDamage);
            } finally {
                player.getPersistentData().remove("PleroCottageHappySweep");
            }
        }
    }

    private static void reduceWithIceBarrier(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        var source = event.getSource();
        int amplifier = entity.getEffect(MobEffectContent.ICE_BARRIER.get()).getAmplifier();
        float temperature = entity.level().getBiome(entity.blockPosition()).value().getBaseTemperature() * 100.0F;
        float threshold = (float) Math.floor((amplifier + 1) * 0.5F)
            + (temperature < 15.0F ? 14.0F : temperature > 150.0F ? 2.0F : 6.0F);
        boolean meleeAttack = source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)
            || source.is(DamageTypes.PLAYER_ATTACK);
        boolean fireDamage = source.is(net.minecraft.tags.DamageTypeTags.IS_FIRE);
        boolean depleted = event.getAmount() >= threshold || meleeAttack || fireDamage;
        event.setAmount(event.getAmount() * 0.55F);
        entity.setTicksFrozen(Math.max(entity.getTicksFrozen(), 40));
        if (depleted) {
            entity.level().playSound(null, entity.blockPosition(),
                net.minecraft.sounds.SoundEvents.GLASS_BREAK,
                net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 1.2F);
            entity.level().levelEvent(2001, entity.blockPosition().above(),
                net.minecraft.world.level.block.Block.getId(
                    net.minecraft.world.level.block.Blocks.ICE.defaultBlockState()));
            entity.removeEffect(MobEffectContent.ICE_BARRIER.get());
        } else {
            entity.level().playSound(null, entity.blockPosition(),
                net.minecraft.sounds.SoundEvents.PLAYER_HURT_FREEZE,
                net.minecraft.sounds.SoundSource.PLAYERS, 1.25F, 1.0F);
        }
    }

    private static void reduceWithSnowVeil(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        int amplifier = entity.getEffect(MobEffectContent.SNOW_VEIL.get()).getAmplifier();
        if (event.getSource().is(net.minecraft.tags.DamageTypeTags.IS_FIRE)) {
            entity.level().playSound(null, entity.blockPosition(),
                net.minecraft.sounds.SoundEvents.POWDER_SNOW_BREAK,
                net.minecraft.sounds.SoundSource.PLAYERS, 2.0F, 1.0F);
            entity.removeEffect(MobEffectContent.SNOW_VEIL.get());
            return;
        }
        if (!(event.getSource().getEntity() instanceof LivingEntity)) {
            return;
        }

        float temperature = entity.level().getBiome(entity.blockPosition()).value().getBaseTemperature() * 100.0F;
        float reduction = temperature > 150.0F
            ? (amplifier + 1) * 0.05F + 0.05F
            : (amplifier + 1) * 0.05F + 0.25F;
        event.setAmount(event.getAmount() * (1.0F - reduction));
        entity.setTicksFrozen(Math.max(entity.getTicksFrozen(), 80));
    }

    private static void reduceWithAuroraVeil(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        int amplifier = entity.getEffect(MobEffectContent.AURORA_VEIL.get()).getAmplifier();
        entity.setTicksFrozen(Math.max(entity.getTicksFrozen(), 40));

        boolean livingAttacker = event.getSource().getEntity() instanceof LivingEntity;
        float reduction = livingAttacker
            ? Math.min(1.0F, 0.35F + 0.05F * (amplifier + 1))
            : 0.75F;
        event.setAmount(event.getAmount() * (1.0F - reduction));

        if (event.getSource().is(net.minecraft.tags.DamageTypeTags.IS_FIRE)) {
            entity.level().playSound(null, entity.blockPosition(),
                net.minecraft.sounds.SoundEvents.POWDER_SNOW_BREAK,
                net.minecraft.sounds.SoundSource.PLAYERS, 2.0F, 1.0F);
            entity.removeEffect(MobEffectContent.AURORA_VEIL.get());
        }
    }

    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (player == null || !player.hasEffect(MobEffectContent.RUTIN_LIMONENE.get())
            || !(event.getEntity().level() instanceof ServerLevel level)) {
            return;
        }
        int amplifier = player.getEffect(MobEffectContent.RUTIN_LIMONENE.get()).getAmplifier();
        int extraExperience = (int) Math.floor(event.getDroppedExperience()
            * (0.8 + 0.05 * (amplifier + 1)));
        if (extraExperience > 0) {
            level.addFreshEntity(new ExperienceOrb(level, event.getEntity().getX(), event.getEntity().getY(),
                event.getEntity().getZ(), extraExperience));
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(net.minecraftforge.event.entity.living.LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) {
            return;
        }
        if (attacker.hasEffect(MobEffectContent.DARK_WRATH.get())) {
            attacker.removeEffect(MobEffectContent.DARK_WRATH.get());
        }
        if (!attacker.hasEffect(MobEffectContent.ORIGINAL_EVOLUTION.get())) {
            return;
        }
        int amplifier = attacker.getEffect(MobEffectContent.ORIGINAL_EVOLUTION.get()).getAmplifier();
        int addedDuration = (int) ((15.0D + 0.2D * (amplifier + 1)) * 20.0D);
        for (MobEffectInstance instance : attacker.getActiveEffects()) {
            if (instance.getEffect().getCategory() != net.minecraft.world.effect.MobEffectCategory.BENEFICIAL
                || isFruitEffect(instance)) {
                continue;
            }
            int newDuration = Math.min(MAX_EFFECT_DURATION, instance.getDuration() + addedDuration);
            attacker.addEffect(new MobEffectInstance(instance.getEffect(), newDuration,
                instance.getAmplifier(), instance.isAmbient(), instance.isVisible(), instance.showIcon()));
        }
    }

    private static boolean isFruitEffect(MobEffectInstance instance) {
        return instance.getEffect() == MobEffectContent.ROSA_HEDGE.get()
            || instance.getEffect() == MobEffectContent.RUTIN_LIMONENE.get()
            || instance.getEffect() == MobEffectContent.BURSTING_BERRY.get()
            || instance.getEffect() == MobEffectContent.CHERRY_BLOSSOMS_WEEPING.get()
            || instance.getEffect() == MobEffectContent.MOMENTARY_METEOR.get()
            || instance.getEffect() == MobEffectContent.SUMMER_HEATWAVE.get()
            || instance.getEffect() == MobEffectContent.ORIGINAL_EVOLUTION.get()
            || instance.getEffect() == MobEffectContent.MELON_GRAVITY.get()
            || instance.getEffect() == MobEffectContent.SEA_TOUCH.get()
            || instance.getEffect() == MobEffectContent.HACKED_THORNS.get()
            || instance.getEffect() == MobEffectContent.KIWING_WHEREABOUTS.get()
            || instance.getEffect() == MobEffectContent.LURKING_DANGER.get()
            || instance.getEffect() == MobEffectContent.GEM_EXCAVATION.get()
            || instance.getEffect() == MobEffectContent.POISON_HEAL_MEDICINE.get()
            || instance.getEffect() == MobEffectContent.HAPPY_AURA.get()
            || instance.getEffect() == MobEffectContent.DARK_WRATH.get()
            || instance.getEffect() == MobEffectContent.TRANSMUTATION_ELIXIR.get();
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        if (event.getEffect() == MobEffectContent.MENTHOL_AWAKENING.get()
            && !event.getEntity().level().isClientSide()) {
            event.getEntity().getPersistentData().putBoolean(MENTHOL_AWAKENING_PENDING_DOWNGRADE_TAG, true);
        }
        if (event.getEffect() == MobEffectContent.TRANSMUTATION_ELIXIR.get()) {
            event.getEntity().getPersistentData().remove(TRANSMUTATION_TARGET_TAG);
            event.getEntity().getPersistentData().remove(TRANSMUTATION_COUNT_TAG);
        }
        if (event.getEffect() == MobEffectContent.MOMENTARY_METEOR.get()) {
            event.getEntity().getPersistentData().remove(MOMENTARY_METEOR_PENDING_TARGET_TAG);
        }
        if (event.getEffect() != MobEffectContent.LURKING_DANGER.get()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        var movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed != null && movementSpeed.getModifier(
            java.util.UUID.fromString("50e887ba-84fb-438f-a3fc-fc27ed3a7607")) != null) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 4, false, true));
            movementSpeed.removeModifier(java.util.UUID.fromString("50e887ba-84fb-438f-a3fc-fc27ed3a7607"));
        }
        entity.getPersistentData().putInt("PleroCottageLurkingTicks", 0);
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        MobEffectInstance instance = event.getEffectInstance();
        LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide()
            && !entity.getPersistentData().getBoolean(MENTHOL_AWAKENING_REENTRY_TAG)) {
            if (instance.getEffect() == MobEffectContent.MENTHOL_AWAKENING.get()) {
                entity.getPersistentData().putBoolean(MENTHOL_AWAKENING_REENTRY_TAG, true);
                try {
                    // 只在首次获得时升级：效果还在时续上不再加级，结束时只会降一级
                    if (event.getOldEffectInstance() == null) {
                        for (MobEffectInstance activeEffect : entity.getActiveEffects().stream().toList()) {
                            if (activeEffect.getEffect() != MobEffectContent.MENTHOL_AWAKENING.get()
                                && activeEffect.getEffect().getCategory() == net.minecraft.world.effect.MobEffectCategory.BENEFICIAL
                                && isMentholAffectedEffect(activeEffect.getEffect())) {
                                upgradeBeneficialEffect(entity, activeEffect);
                            }
                        }
                    }
                } finally {
                    entity.getPersistentData().remove(MENTHOL_AWAKENING_REENTRY_TAG);
                }
            } else if (instance.getEffect().getCategory() == net.minecraft.world.effect.MobEffectCategory.BENEFICIAL
                && shouldUpgradeNewBeneficialEffect(event)
                && isMentholAffectedEffect(instance.getEffect())) {
                String effectId = ForgeRegistries.MOB_EFFECTS.getKey(instance.getEffect()).toString();
                ListTag pending = entity.getPersistentData().getList(MENTHOL_AWAKENING_PENDING_UPGRADES_TAG, 8);
                if (!pending.contains(StringTag.valueOf(effectId))) {
                    pending.add(StringTag.valueOf(effectId));
                    entity.getPersistentData().put(MENTHOL_AWAKENING_PENDING_UPGRADES_TAG, pending);
                }
            }
        }
        if (!entity.level().isClientSide()
            && (instance.getEffect() == MobEffectContent.ICE_BARRIER.get()
                || instance.getEffect() == MobEffectContent.SNOW_VEIL.get())) {
            MobEffect pairedEffect = instance.getEffect() == MobEffectContent.ICE_BARRIER.get()
                ? MobEffectContent.SNOW_VEIL.get()
                : MobEffectContent.ICE_BARRIER.get();
            MobEffectInstance pairedInstance = entity.getEffect(pairedEffect);
            if (pairedInstance != null) {
                MobEffectInstance selected = selectAuroraVeilSource(instance, pairedInstance);
                entity.removeEffect(MobEffectContent.ICE_BARRIER.get());
                entity.removeEffect(MobEffectContent.SNOW_VEIL.get());
                entity.addEffect(new MobEffectInstance(MobEffectContent.AURORA_VEIL.get(),
                    selected.getDuration(), selected.getAmplifier()));
            }
        }
        if (instance.getEffect() != MobEffectContent.WALLNUT_FIRST_AID.get()
            || event.getOldEffectInstance() == null) {
            return;
        }
        MobEffectInstance old = event.getOldEffectInstance();
        int amplifier = Math.max(old.getAmplifier(), instance.getAmplifier());
        float newGranted = (float) Math.floor((0.60F + (amplifier + 1) * 0.15F) * entity.getMaxHealth());
        float oldGranted = entity.getPersistentData().getFloat("PleroCottageWallnutAbsorption");
        float otherAbsorption = Math.max(0,
            entity.getAbsorptionAmount() - Math.min(oldGranted, entity.getAbsorptionAmount()));
        entity.setAbsorptionAmount(otherAbsorption + newGranted);
        entity.getPersistentData().putFloat("PleroCottageWallnutAbsorption", newGranted);
    }

    private static void upgradeBeneficialEffect(LivingEntity entity, MobEffectInstance instance) {
        if (instance.getAmplifier() >= 255) {
            return;
        }
        entity.getPersistentData().putBoolean(MENTHOL_AWAKENING_REENTRY_TAG, true);
        try {
            entity.addEffect(new MobEffectInstance(instance.getEffect(), instance.getDuration(),
                instance.getAmplifier() + 1, instance.isAmbient(), instance.isVisible(), instance.showIcon()));
        } finally {
            entity.getPersistentData().remove(MENTHOL_AWAKENING_REENTRY_TAG);
        }
    }

    private static boolean shouldUpgradeNewBeneficialEffect(MobEffectEvent.Added event) {
        MobEffectInstance old = event.getOldEffectInstance();
        return old == null || event.getEffectInstance().getAmplifier() > old.getAmplifier();
    }

    private static void downgradeBeneficialEffect(LivingEntity entity, MobEffectInstance instance) {
        entity.getPersistentData().putBoolean(MENTHOL_AWAKENING_REENTRY_TAG, true);
        try {
            entity.removeEffect(instance.getEffect());
            entity.addEffect(new MobEffectInstance(instance.getEffect(), instance.getDuration(),
                Math.max(0, instance.getAmplifier() - 1), instance.isAmbient(), instance.isVisible(), instance.showIcon()));
        } finally {
            entity.getPersistentData().remove(MENTHOL_AWAKENING_REENTRY_TAG);
        }
    }

    private static void processMentholAwakeningChanges(LivingEntity entity) {
        var data = entity.getPersistentData();
        boolean active = entity.hasEffect(MobEffectContent.MENTHOL_AWAKENING.get());
        if (data.getBoolean(MENTHOL_AWAKENING_ACTIVE_LAST_TICK_TAG) && !active) {
            data.putBoolean(MENTHOL_AWAKENING_PENDING_DOWNGRADE_TAG, true);
        }
        // 每个生物每刻都会走到这里，只在状态变化时才写数据
        if (data.getBoolean(MENTHOL_AWAKENING_ACTIVE_LAST_TICK_TAG) != active) {
            if (active) {
                data.putBoolean(MENTHOL_AWAKENING_ACTIVE_LAST_TICK_TAG, true);
            } else {
                data.remove(MENTHOL_AWAKENING_ACTIVE_LAST_TICK_TAG);
            }
        }
        if (data.getBoolean(MENTHOL_AWAKENING_PENDING_DOWNGRADE_TAG)) {
            data.remove(MENTHOL_AWAKENING_PENDING_DOWNGRADE_TAG);
            if (!entity.hasEffect(MobEffectContent.MENTHOL_AWAKENING.get())) {
                for (MobEffectInstance instance : entity.getActiveEffects().stream().toList()) {
                    if (instance.getEffect().getCategory() == net.minecraft.world.effect.MobEffectCategory.BENEFICIAL
                        && instance.getAmplifier() > 0
                        && isMentholAffectedEffect(instance.getEffect())) {
                        downgradeBeneficialEffect(entity, instance);
                    }
                }
            }
        }
        if (!active) {
            data.remove(MENTHOL_AWAKENING_PENDING_UPGRADES_TAG);
            return;
        }
        ListTag pending = data.getList(MENTHOL_AWAKENING_PENDING_UPGRADES_TAG, 8);
        data.remove(MENTHOL_AWAKENING_PENDING_UPGRADES_TAG);
        for (int i = 0; i < pending.size(); i++) {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new net.minecraft.resources.ResourceLocation(pending.getString(i)));
            MobEffectInstance instance = effect == null ? null : entity.getEffect(effect);
            if (instance != null && effect.getCategory() == net.minecraft.world.effect.MobEffectCategory.BENEFICIAL
                && effect != MobEffectContent.MENTHOL_AWAKENING.get() && isMentholAffectedEffect(effect)) {
                upgradeBeneficialEffect(entity, instance);
            }
        }
    }

    private static boolean isMentholAffectedEffect(MobEffect effect) {
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);
        if (id == null) {
            return false;
        }
        List<? extends String> whitelist = Config.mentholAwakeningWhitelist();
        String idString = id.toString();
        if (!whitelist.isEmpty()) {
            return whitelist.contains(idString);
        }
        return !Config.mentholAwakeningBlacklist().contains(idString);
    }

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEntity().hasEffect(MobEffectContent.AURORA_VEIL.get())
            && (event.getEffectInstance().getEffect() == MobEffectContent.ICE_BARRIER.get()
                || event.getEffectInstance().getEffect() == MobEffectContent.SNOW_VEIL.get())) {
            event.setResult(net.minecraftforge.eventbus.api.Event.Result.DENY);
        }
    }

    private static MobEffectInstance selectAuroraVeilSource(
        MobEffectInstance first,
        MobEffectInstance second
    ) {
        if (first.getAmplifier() != second.getAmplifier()) {
            return first.getAmplifier() > second.getAmplifier() ? first : second;
        }
        return first.getDuration() > second.getDuration() ? first : second;
    }

}
