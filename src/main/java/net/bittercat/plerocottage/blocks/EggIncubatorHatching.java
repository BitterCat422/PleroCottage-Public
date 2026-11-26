package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.blockentities.EggIncubatorBlockEntity;
import net.bittercat.plerocottage.config.Config;
import net.bittercat.plerocottage.content.entities.PoultryContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;

final class EggIncubatorHatching {
    private static final double[][][] POSITIONS = {
        {{0.5, 0.5}},
        {{0.375, 0.375}, {0.6686, 0.625}},
        {{0.498, 0.3125}, {0.6875, 0.5829}, {0.375, 0.6445}}
    };
    private EggIncubatorHatching() {}

    static void tick(ServerLevel level, BlockPos pos, BlockState state) {
        if (!(level.getBlockEntity(pos) instanceof EggIncubatorBlockEntity incubator)
            || incubator.getItem(0).isEmpty()) return;
        int hatch = state.getValue(EggIncubatorBlock.HATCH);
        if (hatch < 2) {
            level.setBlock(pos, state.setValue(EggIncubatorBlock.HATCH, hatch + 1), 3);
            level.playSound(null, pos.above(), SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 1, 1);
            return;
        }
        int count = 0;
        for (int i = 0; i < 3; i++) if (!incubator.getItem(i).isEmpty()) count++;
        int rendered = 0;
        for (int i = 0; i < 3; i++) {
            ItemStack egg = incubator.getItem(i);
            if (!egg.isEmpty()) {
                hatch(level, pos, POSITIONS[count - 1][rendered][0],
                    POSITIONS[count - 1][rendered][1], egg);
                rendered++;
            }
        }
        for (int i = 0; i < 3; i++) incubator.removeItem(i, 1);
        level.setBlock(pos, state.setValue(EggIncubatorBlock.HATCH, 0), 3);
        level.playSound(null, pos.above(), SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 1, 1);
        incubator.setChanged();
    }

    private static void hatch(ServerLevel level, BlockPos pos, double x, double z, ItemStack stack) {
        Item egg = stack.getItem();
        EntityType<?> type = getHatchingMappings().get(egg);
        if (type == null) return;
        int age = type == EntityType.SNIFFER ? -54000 : -24000;
        Entity entity = type.create(level, null, null,
            BlockPos.containing(pos.getX() + x, pos.getY() + 1.05, pos.getZ() + z),
            MobSpawnType.MOB_SUMMONED, false, false);
        if (entity != null) {
            entity.setYRot(level.getRandom().nextFloat() * 360F);
            if (entity instanceof AgeableMob ageable) ageable.setAge(age);
            level.addFreshEntity(entity);
        }
        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + x, pos.getY() + 1.05,
            pos.getZ() + z, 5, 0.1, -0.1, 0.1, 0.05);
    }

    static boolean hasHatchingMapping(Item egg) {
        return getHatchingMappings().containsKey(egg);
    }

    private static Map<Item, EntityType<?>> getHatchingMappings() {
        Map<Item, EntityType<?>> mappings = new LinkedHashMap<>();
        for (String mapping : Config.incubatorHatchingMappings()) {
            String[] ids = mapping.split("=", 2);
            if (ids.length != 2) continue;
            ResourceLocation itemId = ResourceLocation.tryParse(ids[0].trim());
            ResourceLocation entityId = ResourceLocation.tryParse(ids[1].trim());
            if (itemId == null || entityId == null) continue;
            if (!ForgeRegistries.ITEMS.containsKey(itemId)
                || !ForgeRegistries.ENTITY_TYPES.containsKey(entityId)) continue;
            Item mappedItem = ForgeRegistries.ITEMS.getValue(itemId);
            EntityType<?> mappedEntity = ForgeRegistries.ENTITY_TYPES.getValue(entityId);
            if (mappedItem != null && mappedItem != Items.AIR && mappedEntity != null) {
                mappings.putIfAbsent(mappedItem, mappedEntity);
            }
        }
        return mappings;
    }
}
