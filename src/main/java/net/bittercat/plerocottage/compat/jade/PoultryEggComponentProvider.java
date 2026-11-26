package net.bittercat.plerocottage.compat.jade;

import net.bittercat.plerocottage.entities.PoultryEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum PoultryEggComponentProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = new ResourceLocation("plerocottage", "poultry_egg_timer");
    private static final String EGG_TIME = "eggTime";
    private static final String IS_BABY = "isBaby";
    private static final int MAX_EGG_LAY_TIME = 9000;
    private static final int SEARCH_START_THRESHOLD = (int) (MAX_EGG_LAY_TIME * 0.3F);

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        if (!data.contains(EGG_TIME) || data.getBoolean(IS_BABY)) {
            return;
        }

        int seconds = Math.max(0, data.getInt(EGG_TIME)) / 20;
        tooltip.add(Component.translatable("tips.plerocottage.egg_timer")
            .withStyle(ChatFormatting.GRAY)
            .append(Component.translatable("tips.plerocottage.second", seconds)
                .withStyle(ChatFormatting.WHITE)));
        if (data.getInt(EGG_TIME) <= SEARCH_START_THRESHOLD) {
            tooltip.add(Component.translatable("tips.plerocottage.egg_soon")
                .withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public void appendServerData(CompoundTag data, EntityAccessor accessor) {
        if (accessor.getEntity() instanceof PoultryEntity poultry) {
            data.putInt(EGG_TIME, poultry.eggTimer());
            data.putBoolean(IS_BABY, poultry.isBaby());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
