package net.bittercat.plerocottage.machine.recipes;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface HasFluidRecipe {
    FluidStack getFluidStack();

    default boolean isFluidStackMatch(FluidStack fluidStack) {
        return fluidStack.containsFluid(this.getFluidStack());
    }

    default boolean isFluidStackMatch(FluidTank fluidTank) {
        return this.isFluidStackMatch(fluidTank.getFluid());
    }
}
