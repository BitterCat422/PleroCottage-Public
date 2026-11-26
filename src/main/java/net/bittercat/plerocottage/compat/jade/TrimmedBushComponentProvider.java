package net.bittercat.plerocottage.compat.jade;

import net.bittercat.plerocottage.blocks.BerryBushBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum TrimmedBushComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = new ResourceLocation("plerocottage", "trimmed_bush");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        BlockState state = accessor.getBlockState();
        if (!(state.getBlock() instanceof BerryBushBlock bush)) {
            return;
        }
        int age = state.getValue(BerryBushBlock.AGE);
        int maxAge = bush.settings().maxAge();

        if (age < maxAge) {
            tooltip.add(Component.translatable("tooltip.jade.crop_growth", age * 100 / maxAge + "%"));
        } else {
            tooltip.add(Component.translatable("tooltip.jade.crop_growth",
                Component.translatable("tooltip.jade.crop_mature").withStyle(ChatFormatting.GREEN)));
        }

        if (state.getValue(BerryBushBlock.TRIMMED)) {
            tooltip.add(Component.translatable("tips.plerocottage.trimmed"));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
