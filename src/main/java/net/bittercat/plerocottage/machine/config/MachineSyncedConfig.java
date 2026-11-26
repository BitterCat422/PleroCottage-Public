package net.bittercat.plerocottage.machine.config;

import net.minecraftforge.fml.util.thread.SidedThreadGroups;

public final class MachineSyncedConfig {
    public record Snapshot(boolean cuttingBoardCompat, boolean cookingPotCompat) {
    }

    private static volatile Snapshot snapshot;

    private MachineSyncedConfig() {
    }

    public static Snapshot clientSnapshot() {
        // 单人游戏两端共享静态字段，服务端不能读取客户端快照。
        return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER ? null : snapshot;
    }

    public static void update(Snapshot value) {
        snapshot = value;
    }

    public static void clear() {
        snapshot = null;
    }
}
