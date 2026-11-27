package net.bittercat.plerocottage.mixin;

import net.bittercat.plerocottage.items.TieredSwordItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerAttackMixin {
    // require = 0：别的模组也重定向了同一处调用时，这里放弃注入而不是让游戏启动崩溃
    @Redirect(require = 0, method = "attack", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean pleroCottage$restoreUtensilFood(Entity target, DamageSource source, float damage) {
        Player player = (Player) (Object) this;
        if (player.level().isClientSide() || !(target instanceof LivingEntity living)
            || !(player.getMainHandItem().getItem() instanceof TieredSwordItem utensil)) {
            return target.hurt(source, damage);
        }

        float health = living.getHealth();
        float absorption = living.getAbsorptionAmount();
        boolean hit = target.hurt(source, damage);
        if (hit && (living.getHealth() < health || living.getAbsorptionAmount() < absorption)) {
            utensil.restoreFoodOnHit(player);
        }
        return hit;
    }
}
