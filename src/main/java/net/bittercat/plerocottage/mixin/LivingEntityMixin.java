package net.bittercat.plerocottage.mixin;

import net.bittercat.plerocottage.content.effects.MobEffectContent;
import net.bittercat.plerocottage.effects.FlattenState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements FlattenState {
    @Unique
    private static final EntityDataAccessor<Boolean> pleroCottage$flattened =
        SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void pleroCottage$defineFlattenState(CallbackInfo ci) {
        ((LivingEntity) (Object) this).getEntityData().define(pleroCottage$flattened, false);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void pleroCottage$updateFlattenState(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.level().isClientSide()) {
            entity.getEntityData().set(pleroCottage$flattened, entity.hasEffect(MobEffectContent.FLATTEN.get()));
        }
    }

    @Inject(method = "onSyncedDataUpdated(Lnet/minecraft/network/syncher/EntityDataAccessor;)V",
        at = @At("TAIL"))
    private void pleroCottage$refreshFlattenDimensions(EntityDataAccessor<?> accessor, CallbackInfo ci) {
        if (pleroCottage$flattened.equals(accessor)) {
            ((LivingEntity) (Object) this).refreshDimensions();
        }
    }

    @Override
    public boolean pleroCottage$isFlattened() {
        return ((LivingEntity) (Object) this).getEntityData().get(pleroCottage$flattened);
    }
}
