package net.bittercat.plerocottage.content.plants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;

public final class PlantCatalog {

    private static final List<PlantEntry> ALL = new ArrayList<>();
    private static final Map<Supplier<? extends Item>, Float> EXTRA_COMPOSTABLES = new LinkedHashMap<>();
    private static final Set<Supplier<? extends Item>> FLAT_ICONS = new HashSet<>();

    private PlantCatalog() {
    }

    public static void add(PlantEntry entry) {
        ALL.add(entry);
    }

    public static List<PlantEntry> all() {
        return List.copyOf(ALL);
    }

    public static void addCompostable(Supplier<? extends Item> item, float chance) {
        EXTRA_COMPOSTABLES.put(item, chance);
    }

    public static Map<Supplier<? extends Item>, Float> extraCompostables() {
        return Map.copyOf(EXTRA_COMPOSTABLES);
    }

    public static void markFlatIcon(Supplier<? extends Item> item) {
        FLAT_ICONS.add(item);
    }

    public static boolean usesFlatIcon(Supplier<? extends Item> item) {
        return FLAT_ICONS.contains(item);
    }
}
