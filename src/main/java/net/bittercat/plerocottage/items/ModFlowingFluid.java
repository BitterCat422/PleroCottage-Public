package net.bittercat.plerocottage.items;

import net.bittercat.plerocottage.content.fluids.FluidContent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;

public abstract class ModFlowingFluid extends ForgeFlowingFluid {
    protected ModFlowingFluid(String name, boolean flowing) {
        super(new Properties(() -> FluidContent.type(name).get(),
            () -> FluidContent.source(name).get(), () -> FluidContent.flowing(name).get())
            .explosionResistance(100F).tickRate(name.contains("ice_cream") ? 30 : 10)
            .levelDecreasePerBlock(2).slopeFindDistance(1)
            .bucket(() -> FluidContent.bucket(name).get())
            .block(() -> (LiquidBlock) FluidContent.block(name).get()));
    }

    public static final class Source extends ModFlowingFluid {
        public Source(String name) { super(name, false); }
        @Override public int getAmount(FluidState state) { return 8; }
        @Override public boolean isSource(FluidState state) { return true; }
    }

    public static final class Flowing extends ModFlowingFluid {
        public Flowing(String name) { super(name, true); }
        @Override protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
        @Override public int getAmount(FluidState state) { return state.getValue(LEVEL); }
        @Override public boolean isSource(FluidState state) { return false; }
    }
}
