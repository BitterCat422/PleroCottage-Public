package net.bittercat.plerocottage.content.furniture;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.BUILDING;

import java.util.ArrayList;
import java.util.List;

import net.bittercat.plerocottage.blocks.FurnitureBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.wood.FurnitureSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.registries.RegistryObject;

public final class FurnitureContent {
    private static final List<FurnitureSet> ALL = new ArrayList<>();
    private static boolean bootstrapped;

    private static final List<String> NAMES = List.of(
        "oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry",
        "bamboo", "crimson", "warped", "cherries_tree", "starfruit_tree", "olive_tree",
        "rosaceae_tree", "rutaceae_tree", "durian_tree", "mango_tree", "kiwifruit_tree",
        "avocado_tree", "pomegranate_tree", "hawthorn_tree", "fig_tree", "coconut_tree",
        "scots_pine", "juniper", "banana_tree", "holly_tree", "nut_tree", "myriclave_tree",
        "cinnise_tree", "elderberry_tree");

    private FurnitureContent() {
    }

    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        for (String name : NAMES) {
            RegistryObject<Block> cabinet = BlockRegistry.register(name + "_cabinet",
                () -> new FurnitureBlock(false, soundType(name)));
            RegistryObject<Block> drawer = BlockRegistry.register(name + "_drawer",
                () -> new FurnitureBlock(true, soundType(name)));
            RegistryObject<Item> cabinetItem = blockItem(name + "_cabinet", cabinet);
            RegistryObject<Item> drawerItem = blockItem(name + "_drawer", drawer);
            ALL.add(new FurnitureSet(name, cabinet, cabinetItem, drawer, drawerItem));

        }
    }

    private static RegistryObject<Item> blockItem(String name, RegistryObject<Block> block) {
        return ItemRegistry.registerBlockItem(name, block);
    }

    private static SoundType soundType(String name) {
        return "cherries_tree".equals(name) ? SoundType.CHERRY_WOOD : SoundType.WOOD;
    }

    public static List<FurnitureSet> all() {
        return List.copyOf(ALL);
    }

    public static Block[] cabinets() {
        return ALL.stream().map(set -> set.cabinet().get()).toArray(Block[]::new);
    }

    public static Block[] drawers() {
        return ALL.stream().map(set -> set.drawer().get()).toArray(Block[]::new);
    }
}
