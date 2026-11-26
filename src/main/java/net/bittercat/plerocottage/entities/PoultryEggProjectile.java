package net.bittercat.plerocottage.entities;

import net.bittercat.plerocottage.content.entities.EntityContent;
import net.bittercat.plerocottage.content.entities.PoultryContent;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public final class PoultryEggProjectile extends ThrowableItemProjectile {
    private final String variant;

    public PoultryEggProjectile(EntityType<? extends PoultryEggProjectile> type, Level level, String variant) {
        super(type, level);
        this.variant = variant;
    }

    public PoultryEggProjectile(EntityType<? extends PoultryEggProjectile> type, LivingEntity owner,
                                Level level, String variant) {
        super(type, owner, level);
        this.variant = variant;
    }

    @Override
    protected Item getDefaultItem() {
        return switch (variant) {
            case "mallard" -> PoultryContent.MALLARD_EGG.get();
            case "turkey" -> PoultryContent.TURKEY_EGG.get();
            default -> PoultryContent.MALLARD_EGG.get();
        };
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        finishHit();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        finishHit();
    }

    private void finishHit() {
        if (!(level() instanceof ServerLevel server)) {
            discard();
            return;
        }
        server.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, getItem()), getX(), getY(), getZ(),
            5, 0.1D, -0.1D, 0.1D, 0.1D);
        if (server.random.nextFloat() < 0.0625F) {
            EntityType<PoultryEntity> type = switch (variant) {
                case "mallard" -> EntityContent.MALLARD_DUCK.get();
                case "turkey" -> EntityContent.TURKEY.get();
                default -> EntityContent.MALLARD_DUCK.get();
            };
            PoultryEntity entity = type.spawn(server, blockPosition(), MobSpawnType.MOB_SUMMONED);
            if (entity != null) {
                entity.setYRot(server.random.nextFloat() * 360.0F);
                entity.setAge(-24000);
            }
        }
        discard();
    }
}
