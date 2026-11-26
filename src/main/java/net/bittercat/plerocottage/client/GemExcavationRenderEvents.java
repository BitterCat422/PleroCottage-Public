package net.bittercat.plerocottage.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.config.Config;
import net.bittercat.plerocottage.content.blocks.GemExcavationContent;
import net.bittercat.plerocottage.content.effects.MobEffectContent;
import net.bittercat.plerocottage.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, value = Dist.CLIENT)
public final class GemExcavationRenderEvents {
    private static final int SCAN_INTERVAL_TICKS = 100;
    private static final int BASE_SCAN_RADIUS = 7;
    private static final int MAX_SCAN_RADIUS = 10;
    private static final TagKey<Block> RENDER_ORES =
        BlockTags.create(new ResourceLocation(PleroCottage.MODID, "render_ores"));
    private static final Map<BlockPos, RevealedDisplay> DISPLAYS = new HashMap<>();
    private static ClientLevel trackedLevel;
    private static LocalPlayer trackedPlayer;
    private static int ticksUntilScan;
    private static int nextEntityId = Integer.MIN_VALUE;
    private static boolean wasEffectActive;
    private static boolean scanRefreshRequested;

    private GemExcavationRenderEvents() {
    }

    public static void refreshScan() {
        scanRefreshRequested = true;
    }

    public static void reset() {
        clearDisplays();
        trackedLevel = null;
        trackedPlayer = null;
        ticksUntilScan = 0;
        wasEffectActive = false;
        scanRefreshRequested = false;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (trackedLevel != minecraft.level || trackedPlayer != minecraft.player) {
            clearDisplays();
            trackedLevel = minecraft.level;
            trackedPlayer = minecraft.player;
            ticksUntilScan = 0;
            wasEffectActive = false;
        }
        if (trackedLevel == null || trackedPlayer == null) {
            return;
        }
        if (minecraft.isPaused()) {
            return;
        }
        var effect = trackedPlayer.getEffect(MobEffectContent.GEM_EXCAVATION.get());
        if (effect == null) {
            clearDisplays();
            ticksUntilScan = 0;
            wasEffectActive = false;
            return;
        }
        if (!wasEffectActive || --ticksUntilScan <= 0) {
            int radius = Math.min(MAX_SCAN_RADIUS, BASE_SCAN_RADIUS + (effect.getAmplifier() + 1) / 3);
            scan(trackedLevel, trackedPlayer.blockPosition(), radius);
            NetworkHandler.CHANNEL.sendToServer(new NetworkHandler.GemExcavationParticlesMessage());
            ticksUntilScan = SCAN_INTERVAL_TICKS;
        } else if (scanRefreshRequested) {
            int radius = Math.min(MAX_SCAN_RADIUS, BASE_SCAN_RADIUS + (effect.getAmplifier() + 1) / 3);
            scan(trackedLevel, trackedPlayer.blockPosition(), radius);
        } else {
            removeInvalidDisplays();
        }
        scanRefreshRequested = false;
        wasEffectActive = true;
    }

    private static void scan(ClientLevel level, BlockPos center, int radius) {
        Set<BlockPos> found = new HashSet<>();
        int minY = Math.max(level.getMinBuildHeight(), center.getY() - radius);
        int maxY = Math.min(level.getMaxBuildHeight() - 1, center.getY() + radius);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = center.getX() - radius; x <= center.getX() + radius; x++) {
            for (int z = center.getZ() - radius; z <= center.getZ() + radius; z++) {
                for (int y = minY; y <= maxY; y++) {
                    cursor.set(x, y, z);
                    if (level.hasChunkAt(cursor) && isScanTarget(level.getBlockState(cursor))) {
                        found.add(cursor.immutable());
                    }
                }
            }
        }
        // 保留相同坐标的实体，避免重建时渲染状态尚未就绪导致轮廓闪烁。
        for (BlockPos position : found) {
            RevealedDisplay existing = DISPLAYS.get(position);
            if (existing == null || existing.display.isRemoved()) {
                createDisplay(level, position);
            }
        }
        Iterator<Map.Entry<BlockPos, RevealedDisplay>> iterator = DISPLAYS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, RevealedDisplay> entry = iterator.next();
            if (!found.contains(entry.getKey())) {
                removeDisplay(entry.getValue().display);
                iterator.remove();
            }
        }
    }

    private static boolean isScanTarget(BlockState state) {
        ResourceLocation id = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        if (id == null) {
            return false;
        }
        String idString = id.toString();
        List<? extends String> whitelist = Config.gemExcavationWhitelist();
        if (!whitelist.isEmpty()) {
            return whitelist.contains(idString);
        }
        return state.is(RENDER_ORES) && !Config.gemExcavationBlacklist().contains(idString);
    }

    private static void createDisplay(ClientLevel level, BlockPos position) {
        Display.BlockDisplay display = new OreDisplay(level);
        CompoundTag data = new CompoundTag();
        data.put("block_state", NbtUtils.writeBlockState(
            GemExcavationContent.ORE_DISPLAY.get().defaultBlockState()));
        data.putInt(Display.TAG_GLOW_COLOR_OVERRIDE, 0xFF0000);
        display.load(data);
        display.moveTo(position.getX(), position.getY(), position.getZ(), 0.0F, 0.0F);
        
        while (level.getEntity(nextEntityId) != null) {
            nextEntityId++;
        }
        display.setId(nextEntityId++);
        level.putNonPlayerEntity(display.getId(), display);
        BlockPos immutablePosition = position.immutable();
        DISPLAYS.put(immutablePosition, new RevealedDisplay(display, immutablePosition));
    }

    private static void removeInvalidDisplays() {
        Iterator<RevealedDisplay> iterator = DISPLAYS.values().iterator();
        while (iterator.hasNext()) {
            RevealedDisplay revealed = iterator.next();
            if (revealed.display.isRemoved()
                || !trackedLevel.hasChunkAt(revealed.position)
                || !isScanTarget(trackedLevel.getBlockState(revealed.position))) {
                removeDisplay(revealed.display);
                iterator.remove();
            }
        }
    }

    private static void removeDisplay(Display.BlockDisplay display) {
        if (trackedLevel != null && trackedLevel.getEntity(display.getId()) == display) {
            trackedLevel.removeEntity(display.getId(), Entity.RemovalReason.DISCARDED);
        }
    }

    private static void clearDisplays() {
        for (RevealedDisplay revealed : DISPLAYS.values()) {
            removeDisplay(revealed.display);
        }
        DISPLAYS.clear();
    }

    private static final class RevealedDisplay {
        private final Display.BlockDisplay display;
        private final BlockPos position;

        private RevealedDisplay(Display.BlockDisplay display, BlockPos position) {
            this.display = display;
            this.position = position;
        }
    }

    private static final class OreDisplay extends Display.BlockDisplay {
        private OreDisplay(ClientLevel level) {
            super(EntityType.BLOCK_DISPLAY, level);
        }

        @Override
        public boolean isCurrentlyGlowing() {
            
            return true;
        }
    }
}
