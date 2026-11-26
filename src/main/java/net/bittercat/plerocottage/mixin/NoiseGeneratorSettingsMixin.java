package net.bittercat.plerocottage.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Holder;
import net.bittercat.plerocottage.worldgen.ModBiomes;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;

@Mixin(NoiseGeneratorSettings.class)
public abstract class NoiseGeneratorSettingsMixin implements ModBiomes.SurfaceRuleTarget {

    @Unique
    private Holder<DimensionType> pleroCottage$dimensionType;

    @Inject(method = "surfaceRule", at = @At("RETURN"), cancellable = true)
    private void pleroCottage$surfaceRule(CallbackInfoReturnable<SurfaceRules.RuleSource> cir) {
        if (pleroCottage$dimensionType != null
            && pleroCottage$dimensionType.is(BuiltinDimensionTypes.OVERWORLD)) {
            cir.setReturnValue(ModBiomes.withBiomeSurfaceRules(cir.getReturnValue()));
        }
    }

    @Override
    public void pleroCottage$setDimensionType(Holder<DimensionType> dimensionType) {
        pleroCottage$dimensionType = dimensionType;
    }
}
