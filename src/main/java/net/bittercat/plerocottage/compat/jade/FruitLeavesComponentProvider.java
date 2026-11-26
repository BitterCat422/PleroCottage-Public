package net.bittercat.plerocottage.compat.jade;

import net.bittercat.plerocottage.blocks.AbstractFruitLeavesBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum FruitLeavesComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = new ResourceLocation("plerocottage", "fruit_leaves");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        BlockState state = accessor.getBlockState();
        if (!(state.getBlock() instanceof AbstractFruitLeavesBlock<?> leaves)) {
            return;
        }
        tooltip.add(Component.translatable("tips.plerocottage.leaves_stage",
            Component.translatable("tips.plerocottage.stage." + leaves.stageName(state))));
        if (leaves.isRareStage(state)) {
            tooltip.add(Component.translatable("tips.plerocottage.rare_variant")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
