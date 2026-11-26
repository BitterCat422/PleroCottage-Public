package net.bittercat.plerocottage.content.wood;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.bittercat.plerocottage.wood.WoodSet;
import net.bittercat.plerocottage.wood.WoodSetSpec;
import net.bittercat.plerocottage.wood.WoodSets;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class WoodSetContent {

    private static final List<WoodSet> ALL = new ArrayList<>();
    private static final Map<String, WoodSet> BY_NAME = new LinkedHashMap<>();
    private static boolean bootstrapped;

    private WoodSetContent() {
    }

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;

        for (WoodType woodType : WoodTypeContent.all()) {
            create(woodType);
        }
    }

    private static void create(WoodType woodType) {
        WoodSetSpec spec = woodType == WoodTypeContent.BANANA_TREE
            ? WoodSetSpec.bamboo(woodType) : WoodSetSpec.standard(woodType);
        WoodSet set = WoodSets.create(BlockRegistry.BLOCKS, ItemRegistry.ITEMS, spec);
        ALL.add(set);
        BY_NAME.put(set.name(), set);
    }

    public static List<WoodSet> all() {
        return List.copyOf(ALL);
    }

    public static Optional<WoodSet> byName(String name) {
        return Optional.ofNullable(BY_NAME.get(name));
    }
}
