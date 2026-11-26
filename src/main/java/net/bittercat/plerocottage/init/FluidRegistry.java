package net.bittercat.plerocottage.init;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class FluidRegistry {

    public static final DeferredRegister<Fluid> FLUIDS =
        DeferredRegister.create(ForgeRegistries.FLUIDS, net.bittercat.plerocottage.PleroCottage.MODID);

    public static final DeferredRegister<FluidType> FLUID_TYPES =
        DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, net.bittercat.plerocottage.PleroCottage.MODID);
    private FluidRegistry() {
    }

}
