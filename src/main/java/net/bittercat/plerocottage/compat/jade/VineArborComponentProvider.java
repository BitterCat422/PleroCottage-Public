package net.bittercat.plerocottage.compat.jade;

import net.bittercat.plerocottage.blocks.VineArborBlock;
import net.bittercat.plerocottage.blocks.VineSpecies;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum VineArborComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = new ResourceLocation("plerocottage", "vine_arbor");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        BlockState state = accessor.getBlockState();
        if (!(state.getBlock() instanceof VineArborBlock)) {
            return;
        }
        VineSpecies species = state.getValue(VineArborBlock.SPECIES);
        tooltip.add(Component.translatable("tips.plerocottage.vine_species",
            Component.translatable(species.translationKey())));
        if (species != VineSpecies.EMPTY) {
            tooltip.add(Component.translatable("tips.plerocottage.vine_arbor_spread",
                state.getValue(VineArborBlock.SPREAD)));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
