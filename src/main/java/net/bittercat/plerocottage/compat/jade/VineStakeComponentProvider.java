package net.bittercat.plerocottage.compat.jade;

import net.bittercat.plerocottage.blocks.VineSpecies;
import net.bittercat.plerocottage.blocks.VineStakeBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum VineStakeComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = new ResourceLocation("plerocottage", "vine_stake");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        BlockState state = accessor.getBlockState();
        if (!(state.getBlock() instanceof VineStakeBlock)) {
            return;
        }
        VineSpecies species = state.getValue(VineStakeBlock.SPECIES);
        tooltip.add(Component.translatable("tips.plerocottage.vine_species",
            Component.translatable(species.translationKey())));
        if (species == VineSpecies.EMPTY) {
            return;
        }
        if (state.getValue(VineStakeBlock.CLIMBED)) {
            tooltip.add(Component.translatable("tips.plerocottage.vine_stake_climbed"));
        } else {
            tooltip.add(Component.translatable("tips.plerocottage.vine_stake_sown",
                VineStakeBlock.MAX_HEIGHT - state.getValue(VineStakeBlock.HEIGHT)));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
