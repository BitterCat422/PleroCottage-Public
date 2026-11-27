package net.bittercat.plerocottage.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public final class ModMobEffect extends MobEffect {

    private static final String WALLNUT_ABSORPTION_KEY = "PleroCottageWallnutAbsorption";
    private static final UUID LURKING_DANGER_SPEED_ID =
        UUID.fromString("50e887ba-84fb-438f-a3fc-fc27ed3a7607");
    private final String effectId;

    public ModMobEffect(String effectId, MobEffectCategory category, int color) {
        super(category, color);
        this.effectId = effectId;
        addModifiers();
    }

    private void addModifier(Attribute attribute, String id, double amount, AttributeModifier.Operation operation) {
        addAttributeModifier(attribute, UUID.nameUUIDFromBytes((effectId + ":" + id).getBytes()).toString(), amount, operation);
    }

    private void addModifiers() {
        switch (effectId) {
            case "flatten" -> {
                addModifier(Attributes.ATTACK_DAMAGE, "attack_damage", -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
                addModifier(Attributes.MOVEMENT_SPEED, "movement_speed", -0.3, AttributeModifier.Operation.MULTIPLY_TOTAL);
            }
            case "cherry_blossoms_weeping" -> addModifier(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 0.1, AttributeModifier.Operation.ADDITION);
            case "inner_focus" -> {
                addModifier(Attributes.MAX_HEALTH, "max_health", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
                addModifier(Attributes.ATTACK_DAMAGE, "attack_damage", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
                addModifier(Attributes.ATTACK_KNOCKBACK, "attack_knockback", 0.075, AttributeModifier.Operation.MULTIPLY_TOTAL);
                addModifier(Attributes.ATTACK_SPEED, "attack_speed", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
                addModifier(Attributes.MOVEMENT_SPEED, "movement_speed", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
                addModifier(ForgeMod.SWIM_SPEED.get(), "swim_speed", 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
            }
            case "melon_gravity" -> {
                addModifier(Attributes.KNOCKBACK_RESISTANCE, "knockback_resistance", 0.08, AttributeModifier.Operation.ADDITION);
                addModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), "step_height", 0.15, AttributeModifier.Operation.ADDITION);
            }
            case "sea_touch" -> {
                addModifier(ForgeMod.BLOCK_REACH.get(), "block_reach", 4.25, AttributeModifier.Operation.ADDITION);
                addModifier(ForgeMod.ENTITY_REACH.get(), "entity_reach", 4.25, AttributeModifier.Operation.ADDITION);
            }
            case "summer_heatwave" -> {
                addModifier(Attributes.MOVEMENT_SPEED, "movement_speed", 0.02, AttributeModifier.Operation.MULTIPLY_TOTAL);
                addModifier(ForgeMod.SWIM_SPEED.get(), "swim_speed", 0.04, AttributeModifier.Operation.MULTIPLY_TOTAL);
            }
            case "tipsy" -> {
                addModifier(Attributes.MOVEMENT_SPEED, "movement_speed", -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL);
                addModifier(Attributes.ATTACK_DAMAGE, "attack_damage", 0.025, AttributeModifier.Operation.MULTIPLY_TOTAL);
            }
            default -> {
            }
        }
    }

    @Override
    public boolean isInstantenous() {
        return effectId.equals("purity") || effectId.equals("fragrant");
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        if (effectId.equals("flatten")) {
            return modifier.getAmount();
        }
        if (effectId.equals("sea_touch")) {
            return 4.0D + 0.25D * (amplifier + 1);
        }
        if (effectId.equals("melon_gravity") || effectId.equals("summer_heatwave")) {
            return modifier.getAmount() * (amplifier + 1);
        }
        return super.getAttributeModifierValue(amplifier, modifier);
    }

    @Override
    public void applyInstantenousEffect(net.minecraft.world.entity.Entity source, net.minecraft.world.entity.Entity indirectSource,
                                        LivingEntity entity, int amplifier, double health) {
        applyInstantEffect(entity, amplifier);
    }

    private void applyInstantEffect(LivingEntity entity, int amplifier) {
        switch (effectId) {
            case "purity" -> {
                entity.getActiveEffects().stream()
                    .filter(instance -> instance.getEffect().getCategory() == MobEffectCategory.HARMFUL)
                    .map(instance -> instance.getEffect()).toList().forEach(entity::removeEffect);
                float missingHealth = entity.getMaxHealth() - entity.getHealth();
                entity.heal((float) Math.floor(missingHealth * ((amplifier + 1) * 0.025 + 0.45)));
            }
            case "fragrant" -> {
                float missingHealth = entity.getMaxHealth() - entity.getHealth();
                entity.heal((float) Math.floor(missingHealth * ((amplifier + 1) * 0.025 + 0.6)));
            }
            default -> {
            }
        }
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (effectId.equals("purity") || effectId.equals("fragrant")) {
            applyInstantEffect(entity, amplifier);
        }
        if (effectId.equals("cherry_blossoms_weeping") && entity.level() instanceof ServerLevel level
            && entity.getRandom().nextFloat() < 0.2F) {
            level.sendParticles(ParticleTypes.CHERRY_LEAVES, entity.getX(), entity.getY() + entity.getEyeHeight(),
                entity.getZ(), 1 + entity.getRandom().nextInt(3), 1, 1, 1, 0.05);
        }
        if (effectId.equals("lurking_danger") && entity instanceof Player) {
            tickLurkingDanger(entity, amplifier);
        }
        if (effectId.equals("wallnut_first_aid")
            && entity.getPersistentData().getFloat(WALLNUT_ABSORPTION_KEY) > 0
            && entity.getAbsorptionAmount() <= 0) {
            entity.getPersistentData().remove(WALLNUT_ABSORPTION_KEY);
            entity.removeEffect(this);
        }
    }

    private void tickLurkingDanger(LivingEntity entity, int amplifier) {
        var data = entity.getPersistentData();
        var speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (entity.isCrouching()) {
            int ticks = data.getInt("PleroCottageLurkingTicks") + 1;
            data.putInt("PleroCottageLurkingTicks", ticks);
            int threshold = 30 - amplifier * 2;
            if (ticks >= threshold) {
                entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 2, 0, false, false));
                if (speed != null && speed.getModifier(LURKING_DANGER_SPEED_ID) == null) {
                    speed.addTransientModifier(new AttributeModifier(LURKING_DANGER_SPEED_ID, "Lurking Danger",
                        0.8 + (amplifier + 1) * 0.06, AttributeModifier.Operation.MULTIPLY_TOTAL));
                }
            }
        } else {
            if (speed != null && speed.getModifier(LURKING_DANGER_SPEED_ID) != null) {
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 4, false, true));
                speed.removeModifier(LURKING_DANGER_SPEED_ID);
            }
            data.putInt("PleroCottageLurkingTicks", 0);
        }
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.addAttributeModifiers(entity, attributes, amplifier);
        if (effectId.equals("wallnut_first_aid")) {
            float amount = (float) Math.floor((0.60F + (amplifier + 1) * 0.15F) * entity.getMaxHealth());
            entity.setAbsorptionAmount(entity.getAbsorptionAmount() + amount);
            entity.getPersistentData().putFloat(WALLNUT_ABSORPTION_KEY, amount);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        if (effectId.equals("lurking_danger")) {
            // 效果自然到期不会触发 Remove 事件，移速修饰符要在这里一并清掉
            var speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speed != null) {
                speed.removeModifier(LURKING_DANGER_SPEED_ID);
            }
            entity.getPersistentData().putInt("PleroCottageLurkingTicks", 0);
        }
        if (effectId.equals("wallnut_first_aid")) {
            float granted = entity.getPersistentData().getFloat(WALLNUT_ABSORPTION_KEY);
            entity.setAbsorptionAmount(Math.max(0, entity.getAbsorptionAmount() - Math.min(granted, entity.getAbsorptionAmount())));
            entity.getPersistentData().remove(WALLNUT_ABSORPTION_KEY);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }
}
