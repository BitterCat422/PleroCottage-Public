package net.bittercat.plerocottage.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public final class Config {

    public static final ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.DoubleValue RARE_VARIANT_CHANCE;
    public static final ForgeConfigSpec.DoubleValue LEAVES_CAN_FRUIT_CHANCE;
    public static final ForgeConfigSpec.DoubleValue LEAVES_BLOSSOM_CHANCE;
    public static final ForgeConfigSpec.DoubleValue LEAVES_FRUIT_CHANCE;
    public static final ForgeConfigSpec.DoubleValue LEAVES_DROP_CHANCE;
    public static final ForgeConfigSpec.DoubleValue LEAVES_FLOWER_DECAY_CHANCE;

    public static final ForgeConfigSpec.DoubleValue BLACK_DIRT_GROW_TRUFFLE_CHANCE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_MEDITERRANEAN_PLAIN;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> INCUBATOR_HATCHING_MAPPINGS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MENTHOL_AWAKENING_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MENTHOL_AWAKENING_WHITELIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> GEM_EXCAVATION_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> GEM_EXCAVATION_WHITELIST;
    public static final ForgeConfigSpec.BooleanValue FARMERS_DELIGHT_CUTTING_BOARD_COMPAT;
    public static final ForgeConfigSpec.BooleanValue FARMERS_DELIGHT_COOKING_POT_COMPAT;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("模组中所有树木共用的结果行为",
            "Fruiting behaviour shared by every tree in the mod.")
            .push("fruiting");

        RARE_VARIANT_CHANCE = builder
            .comment("支持稀有变种的植物产出稀有果实的概率",
                "Chance for plants that support rare variants to produce rare fruit.")
            .defineInRange("rare_variant_chance", 0.005D, 0.0D, 1.0D);

        LEAVES_CAN_FRUIT_CHANCE = builder
            .comment("新加载的结果树叶能够结果的概率",
                "每片树叶仅在首次随机刻时判定一次",
                "Chance that a newly loaded fruit leaf is allowed to bear fruit at all.",
                "It is only rolled once per leaf, on its first random tick.")
            .defineInRange("leaves_can_fruit_chance", 0.4D, 0.0D, 1.0D);

        LEAVES_BLOSSOM_CHANCE = builder
            .comment("未结果树叶每次随机刻开花的概率",
                "Chance per random tick for fruitless leaves to blossom.")
            .defineInRange("leaves_blossom_chance", 0.3D, 0.0D, 1.0D);

        LEAVES_FRUIT_CHANCE = builder
            .comment("已开花树叶每次随机刻长出果实的概率",
                "Chance per random tick for blossomed leaves to grow fruit.")
            .defineInRange("leaves_fruit_chance", 0.025D, 0.0D, 1.0D);

        LEAVES_DROP_CHANCE = builder
            .comment("成熟树叶每次随机刻掉落果实的概率",
                "设为 0 时，成熟果实会留在树上直到被玩家采摘",
                "设为负数时，成熟果实会在接到随机刻时被清除而不掉落",
                "Chance per random tick for ripe leaves to drop their fruit.",
                "0 keeps the ripe fruit on the tree until a player picks it.",
                "A negative value clears the ripe fruit without dropping anything.")
            .defineInRange("leaves_drop_chance", 0.025D, -1.0D, 1.0D);

        LEAVES_FLOWER_DECAY_CHANCE = builder
            .comment("结果树叶不将花朵传递给相邻树叶的概率",
                "数值越高，果实越不容易扩散到整片树冠",
                "Chance that a ripening leaf does not pass its blossom on to a neighbouring leaf.",
                "Higher values keep the fruit from spreading through the whole canopy.")
            .defineInRange("leaves_flower_decay_chance", 0.35D, 0.0D, 1.0D);

        builder.pop();

        builder.comment("北欧赤松树下方的松露生长",
            "Truffle growing below Scots pines.").push("truffle");

        BLACK_DIRT_GROW_TRUFFLE_CHANCE = builder
            .comment("北欧赤松原木下的黑土每次随机刻转化为松露土的概率",
                "八格内每只猪使概率提高四分之一，最多计算八只猪",
                "Chance per random tick that black dirt below a Scots pine log grows into truffle dirt.",
                "Each pig within eight blocks raises it by a quarter, up to eight pigs.")
            .defineInRange("black_dirt_grow_truffle_chance", 0.035D, 0.0D, 1.0D);

        builder.pop();

        builder.comment("世界生成",
            "World generation.").push("biomes");

        ENABLE_MEDITERRANEAN_PLAIN = builder
            .comment("地中海平原是否可以在主世界生成",
                "修改后需重新进入世界或重启服务端生效",
                "Whether Mediterranean Plain can generate in the Overworld.",
                "Reopen the world or restart the server for changes to take effect.")
            .define("enable_mediterranean_plain", true);

        builder.pop();

        builder.comment("效果过滤设置",
            "列表必须使用完整 ID；最多 4096 项、每项 8192 字符、编码后每列表最多 192 KiB",
            "Effect filtering settings.",
            "Lists require full IDs; at most 4096 entries, 8192 characters per entry, and 192 KiB encoded per list.")
            .push("effects");

        MENTHOL_AWAKENING_BLACKLIST = builder
            .comment("不受清新薄荷等级变化影响的效果 ID",
                "白名单非空时忽略此黑名单",
                "Effect IDs excluded from Menthol Awakening level changes.",
                "Ignored when the whitelist is not empty.")
            .define("menthol_awakening_blacklist", List.of("minecraft:luck"),
                value -> ConfigValidation.idList(value, "effects.menthol_awakening_blacklist"));

        MENTHOL_AWAKENING_WHITELIST = builder
            .comment("非空时，仅这些效果 ID 会受到清新薄荷等级变化影响",
                "When non-empty, only these effect IDs receive Menthol Awakening level changes.")
            .define("menthol_awakening_whitelist", List.of(),
                value -> ConfigValidation.idList(value, "effects.menthol_awakening_whitelist"));

        GEM_EXCAVATION_BLACKLIST = builder
            .comment("宝石猎手不扫描高亮的方块 ID",
                "白名单非空时忽略此列表；白名单为空时扫描 plerocottage:render_ores 并排除这些方块",
                "Block IDs excluded from Gem Excavation scanning and highlighting.",
                "Ignored when the whitelist is non-empty; otherwise scans plerocottage:render_ores and excludes these blocks.")
            .define("gem_excavation_blacklist", List.of("minecraft:ancient_debris"),
                value -> ConfigValidation.idList(value, "effects.gem_excavation_blacklist"));

        GEM_EXCAVATION_WHITELIST = builder
            .comment("非空时，宝石猎手仅扫描高亮这些方块 ID",
                "此时忽略黑名单和 plerocottage:render_ores 标签；请使用完整方块 ID，例如 minecraft:diamond_ore",
                "When non-empty, Gem Excavation scans and highlights only these block IDs.",
                "The blacklist and plerocottage:render_ores tag are ignored; use full block IDs such as minecraft:diamond_ore.")
            .define("gem_excavation_whitelist", List.of(),
                value -> ConfigValidation.idList(value, "effects.gem_excavation_whitelist"));

        builder.pop();

        builder.comment("孵蛋器使用的蛋物品与实体类型",
            "Egg items and entity types used by the egg incubator.")
            .push("incubator");

        INCUBATOR_HATCHING_MAPPINGS = builder
            .comment("蛋物品 ID = 实体类型 ID，例如 minecraft:egg=minecraft:chicken",
                "两侧必须使用完整 ID；最多 4096 项、每项 8192 字符、编码后最多 192 KiB",
                "Egg item ID = entity type ID, for example minecraft:egg=minecraft:chicken.",
                "Both IDs must be fully qualified; at most 4096 entries, 8192 characters per entry, and 192 KiB encoded.")
            .define("hatching_mappings", List.of(
                "minecraft:egg=minecraft:chicken",
                "minecraft:turtle_egg=minecraft:turtle",
                "minecraft:sniffer_egg=minecraft:sniffer",
                "plerocottage:mallard_duck_egg=plerocottage:mallard_duck",
                "plerocottage:turkey_egg=plerocottage:turkey"
            ), value -> ConfigValidation.hatchingList(value, "incubator.hatching_mappings"));

        builder.pop();

        FARMERS_DELIGHT_CUTTING_BOARD_COMPAT = builder
            .comment("是否启用农夫乐事砧板配方兼容",
                "修改后需重启游戏或服务端生效",
                "Enable Farmer's Delight cutting board recipe compatibility.",
                "Changes take effect after restarting the game or server.")
            .define("cutting_board_compat", true);

        FARMERS_DELIGHT_COOKING_POT_COMPAT = builder
            .comment("是否启用农夫乐事厨锅配方兼容",
                "修改后需重启游戏或服务端生效",
                "Enable Farmer's Delight cooking pot recipe compatibility.",
                "Changes take effect after restarting the game or server.")
            .define("cooking_pot_compat", true);

        COMMON_CONFIG = builder.build();
    }

    public static double rareVariantChance() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.rareVariantChance() : RARE_VARIANT_CHANCE.get();
    }

    public static double leavesCanFruitChance() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.leavesCanFruitChance() : LEAVES_CAN_FRUIT_CHANCE.get();
    }

    public static double leavesBlossomChance() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.leavesBlossomChance() : LEAVES_BLOSSOM_CHANCE.get();
    }

    public static double leavesFruitChance() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.leavesFruitChance() : LEAVES_FRUIT_CHANCE.get();
    }

    public static double leavesDropChance() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.leavesDropChance() : LEAVES_DROP_CHANCE.get();
    }

    public static double leavesFlowerDecayChance() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.leavesFlowerDecayChance() : LEAVES_FLOWER_DECAY_CHANCE.get();
    }

    public static double blackDirtGrowTruffleChance() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.blackDirtGrowTruffleChance() : BLACK_DIRT_GROW_TRUFFLE_CHANCE.get();
    }

    public static boolean enableMediterraneanPlain() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.enableMediterraneanPlain() : ENABLE_MEDITERRANEAN_PLAIN.get();
    }

    public static List<? extends String> mentholAwakeningBlacklist() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.mentholAwakeningBlacklist() : MENTHOL_AWAKENING_BLACKLIST.get();
    }

    public static List<? extends String> mentholAwakeningWhitelist() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.mentholAwakeningWhitelist() : MENTHOL_AWAKENING_WHITELIST.get();
    }

    public static List<? extends String> gemExcavationBlacklist() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.gemExcavationBlacklist() : GEM_EXCAVATION_BLACKLIST.get();
    }

    public static List<? extends String> gemExcavationWhitelist() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.gemExcavationWhitelist() : GEM_EXCAVATION_WHITELIST.get();
    }

    public static List<? extends String> incubatorHatchingMappings() {
        var synced = SyncedConfig.clientSnapshot();
        return synced != null ? synced.incubatorHatchingMappings() : INCUBATOR_HATCHING_MAPPINGS.get();
    }

    private Config() {
    }
}
