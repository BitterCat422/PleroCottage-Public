package net.bittercat.plerocottage.config;

import java.util.List;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

public final class SyncedConfig {
    public record Snapshot(double rareVariantChance, double leavesCanFruitChance,
                           double leavesBlossomChance, double leavesFruitChance,
                           double leavesDropChance, double leavesFlowerDecayChance,
                           double blackDirtGrowTruffleChance, boolean enableMediterraneanPlain,
                           List<String> mentholAwakeningBlacklist, List<String> mentholAwakeningWhitelist,
                           List<String> gemExcavationBlacklist, List<String> gemExcavationWhitelist,
                           List<String> incubatorHatchingMappings) {
        public Snapshot {
            mentholAwakeningBlacklist = List.copyOf(mentholAwakeningBlacklist);
            mentholAwakeningWhitelist = List.copyOf(mentholAwakeningWhitelist);
            gemExcavationBlacklist = List.copyOf(gemExcavationBlacklist);
            gemExcavationWhitelist = List.copyOf(gemExcavationWhitelist);
            incubatorHatchingMappings = List.copyOf(incubatorHatchingMappings);
        }
    }

    private static volatile Snapshot snapshot;

    private SyncedConfig() {
    }

    public static Snapshot localSnapshot() {
        return new Snapshot(Config.RARE_VARIANT_CHANCE.get(), Config.LEAVES_CAN_FRUIT_CHANCE.get(),
            Config.LEAVES_BLOSSOM_CHANCE.get(), Config.LEAVES_FRUIT_CHANCE.get(),
            Config.LEAVES_DROP_CHANCE.get(), Config.LEAVES_FLOWER_DECAY_CHANCE.get(),
            Config.BLACK_DIRT_GROW_TRUFFLE_CHANCE.get(), Config.ENABLE_MEDITERRANEAN_PLAIN.get(),
            List.copyOf(Config.MENTHOL_AWAKENING_BLACKLIST.get()),
            List.copyOf(Config.MENTHOL_AWAKENING_WHITELIST.get()),
            List.copyOf(Config.GEM_EXCAVATION_BLACKLIST.get()),
            List.copyOf(Config.GEM_EXCAVATION_WHITELIST.get()),
            List.copyOf(Config.INCUBATOR_HATCHING_MAPPINGS.get()));
    }

    public static Snapshot clientSnapshot() {
        // 单人游戏两端共享静态字段，逻辑服务端必须始终读取本地配置。
        return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER ? null : snapshot;
    }

    public static void update(Snapshot value) {
        snapshot = value;
    }

    public static void clear() {
        snapshot = null;
    }
}
