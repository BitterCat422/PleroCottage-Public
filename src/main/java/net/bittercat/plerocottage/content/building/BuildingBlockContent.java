package net.bittercat.plerocottage.content.building;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.BUILDING;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.bittercat.plerocottage.blocks.ColoredCarpetBlock;
import net.bittercat.plerocottage.blocks.ColoredConcreteBlock;
import net.bittercat.plerocottage.blocks.ColoredConcretePowderBlock;
import net.bittercat.plerocottage.blocks.ColoredGlassBlock;
import net.bittercat.plerocottage.blocks.ColoredGlassPaneBlock;
import net.bittercat.plerocottage.blocks.ColoredGlazedTerracottaBlock;
import net.bittercat.plerocottage.blocks.ColoredTerracottaBlock;
import net.bittercat.plerocottage.blocks.ColoredWoolBlock;
import net.bittercat.plerocottage.blocks.WallpaperBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public final class BuildingBlockContent {
    private static final List<String> COLORS = List.of(
        "white", "light_gray", "gray", "black", "brown", "red", "orange", "yellow",
        "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink",
        "snow", "lavender", "turquoise");
    private static final List<String> DYED_COLORS = List.of("snow", "lavender", "turquoise");
    private static final Map<String, Map<String, RegistryObject<Block>>> BLOCKS = new LinkedHashMap<>();
    private static boolean bootstrapped;

    private BuildingBlockContent() {
    }

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        registerDyeBlocks();
        registerWallpaperBlocks();
    }

    public static RegistryObject<Block> block(String id) {
        return BLOCKS.values().stream().map(map -> map.get(id)).filter(java.util.Objects::nonNull)
            .findFirst().orElseThrow(() -> new IllegalArgumentException("未知建筑方块：" + id));
    }

    public static Map<String, Map<String, RegistryObject<Block>>> all() {
        return Map.copyOf(BLOCKS);
    }

    private static void registerDyeBlocks() {
        for (String color : DYED_COLORS) {
            Map<String, RegistryObject<Block>> entries = new LinkedHashMap<>();
            registerDye(entries, color, "terracotta", ColoredTerracottaBlock::new);
            registerDye(entries, color, "glazed_terracotta", ColoredGlazedTerracottaBlock::new);
            RegistryObject<Block> concrete = registerDye(entries, color, "concrete", ColoredConcreteBlock::new);
            registerDye(entries, color, "concrete_powder", () -> new ColoredConcretePowderBlock(concrete.get()));
            registerDye(entries, color, "glass", () -> new ColoredGlassBlock(mapColor(color)));
            registerDye(entries, color, color.equals("turquoise") ? "glasspane" : "glass_pane",
                () -> new ColoredGlassPaneBlock(mapColor(color)));
            registerDye(entries, color, "wool", ColoredWoolBlock::new);
            registerDye(entries, color, "carpet", ColoredCarpetBlock::new);
            BLOCKS.put(color, entries);
        }
    }

    private static void registerWallpaperBlocks() {
        for (String color : COLORS) {
            Map<String, RegistryObject<Block>> entries = BLOCKS.computeIfAbsent(color,
                ignored -> new LinkedHashMap<>());
            registerDye(entries, color, "wallpaper_block", WallpaperBlock::new);
            registerDye(entries, color, "panelled_wallpaper_block", WallpaperBlock::new);
        }
        Map<String, RegistryObject<Block>> base = BLOCKS.computeIfAbsent("base",
            ignored -> new LinkedHashMap<>());
        registerDye(base, "", "wallpaper_block", WallpaperBlock::new);
        registerDye(base, "", "panelled_wallpaper_block", WallpaperBlock::new);
    }

    private static RegistryObject<Block> registerDye(Map<String, RegistryObject<Block>> entries, String color,
                                                      String suffix, java.util.function.Supplier<Block> factory) {
        String id = color.isEmpty() ? suffix : color + "_" + suffix;
        RegistryObject<Block> block = BlockRegistry.register(id, factory);
        ItemRegistry.registerBlockItem(id, block);
        entries.put(id, block);
        
        return block;
    }

    private static MapColor mapColor(String color) {
        return switch (color) {
            case "lavender" -> MapColor.COLOR_PURPLE;
            default -> MapColor.DIAMOND;
        };
    }
}
