package net.bittercat.plerocottage.content.plants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.bittercat.plerocottage.blocks.ItemPottedBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.content.food.DisplayFoodContent;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class PottedPlantContent {
    private static final List<Entry> ENTRIES = new ArrayList<>();

    private PottedPlantContent() {
    }

    public static void bootstrap() {
        register("potted_alfalfa", "alfalfa_plant", "alfalfa_crop",
            () -> Crops.ALFALFA_SEED.get(), () -> Crops.ALFALFA_SEED.get());
        register("potted_almond_tree_sapling", "almond_sapling", "almond_tree_sapling",
            item("almonds"), item("almonds"));
        register("potted_juniper_sapling", "juniper_sapling", "juniper_sapling",
            item("juniper_berries"), item("juniper_berries"));
        register("potted_apple_seed", "apple_tree_sapling", "apple_seeds");
        register("potted_apricot_seed", "apricot_tree_sapling", "apricot_seed");
        register("potted_avocado_seed", "avocado_tree_sapling", "avocado_seed");
        register("potted_banana_flower", "potted_banana_flower", "banana_tree_flower");
        register("potted_banana_frond_sapling", "potted_banana_frond_sapling", "banana_frond_sapling");
        register("potted_blue_tulip", "blue_tulip", "blue_tulip");
        register("potted_camelia", "camelia", "camelia");
        register("potted_cashew_tree_sapling", "cashew_sapling", "cashew_tree_sapling",
            item("cashew"), item("cashew"));
        register("potted_cherries_seed", "cherries_tree_sapling", "cherries_seeds");
        register("potted_chestnut_tree_sapling", "chestnut_sapling", "chestnut_tree_sapling",
            item("chestnut"), item("chestnut"));
        register("potted_cinnise_tree_sapling", "cinnise_tree_sapling", "cinnise_tree_sapling",
            item("star_anise"), item("star_anise"));
        register("potted_coconut_sprout", "coconut_sapling", "coconut_sprout");
        register("potted_coffea_arabica", "coffea_arabica", "coffea_arabica");
        register("potted_dragon_fruit_cactus", "potted_dragon_fruit_cactus", "dragon_fruit_cactus");
        register("potted_dragon_fruit_cactus_flower", "potted_dragon_fruit_cactus_flower",
            "dragon_fruit_cactus_flower");
        register("potted_durian_seed", "durian_tree_sapling", "durian_seed");
        register("potted_elderberry_flower", "elderberry_tree_flower", "elderberry_flowers");
        register("potted_elderberry_tree_sapling", "elderberry_tree_sapling", "elderberry_tree_sapling",
            item("elderberries"), item("elderberries"));
        register("potted_fig_seed", "fig_tree_sapling", "fig_seeds");
        register("potted_galanthus_nivalis", "galanthus_nivalis", "galanthus_nivalis");
        register("potted_hawthorn_seed", "hawthorn_sapling", "hawthorn_seeds");
        register("potted_hazelnut_tree_sapling", "hazelnut_sapling", "hazelnut_tree_sapling",
            item("hazelnuts"), item("hazelnuts"));
        register("potted_holly_tree_sapling", "holly_tree_sapling", "holly_tree_sapling",
            item("holly_berries"), item("holly_berries"));
        register("potted_hyacinth", "hyacinth", "hyacinth");
        register("potted_jasmine", "jasmine", "jasmine");
        register("potted_kiwifruit_seed", "kiwifruit_tree_sapling", "kiwifruit_seeds");
        register("potted_lavender", "lavender", "lavender");
        register("potted_lemon_seed", "lemon_tree_sapling", "lemon_seeds");
        register("potted_licorice", "licorice", "licorice");
        register("potted_lucky_clovers", "potted_lucky_clovers", "lucky_clovers");
        register("potted_chamomiles", "potted_chamomiles", "chamomiles");
        register("potted_wild_roses", "potted_wild_roses", "wild_roses");
        register("potted_dianthuses", "potted_dianthuses", "dianthuses");
        register("potted_unlucky_clovers", "potted_unlucky_clovers", "unlucky_clovers");
        register("potted_mango_seed", "mango_tree_sapling", "mango_seed");
        register("potted_myriclave_tree_sapling", "myriclave_tree_sapling", "myriclave_tree_sapling",
            item("nutmeg"), item("nutmeg"));
        register("potted_olive_fruit_seed", "olive_tree_sapling", "olive_fruit_seed");
        register("potted_orange_blossom", "orange_blossom", "orange_blossom");
        register("potted_orange_seed", "orange_tree_sapling", "orange_seeds");
        register("potted_peach_seed", "peach_tree_sapling", "peach_seed");
        register("potted_pear_seed", "pear_tree_sapling", "pear_seeds");
        register("potted_pineapple_crop", "pineapple_seedling", "pineapple_crop");
        register("potted_pistachio_tree_sapling", "pistachio_sapling", "pistachio_tree_sapling",
            item("pistachios"), item("pistachios"));
        register("potted_pomegranate_seed", "pomegranate_tree_sapling", "pomegranate_tree_sapling",
            item("pomegranate_seeds"), item("pomegranate_seeds"));
        register("potted_purple_sugar_cane", "potted_purple_sugar_cane", "purple_sugar_canes",
            () -> PurpleSugarCanes.ITEM.get(), () -> PurpleSugarCanes.ITEM.get());
        register("potted_radde", "radde", "radde");
        register("potted_rose", "rose", "rose");
        register("potted_saffron", "saffron", "saffron");
        register("potted_sakura", "sakura", "sakura");
        register("potted_scots_pine_sapling", "scots_pine_sapling", "scots_pine_sapling",
            item("scots_pine_cone"), item("scots_pine_cone"));
        register("potted_semper_augustus", "semper_augustus", "semper_augustus");
        register("potted_starfruit_seed", "starfruit_tree_sapling", "starfruit_seeds");
        register("potted_tea_tree", "potted_tea_tree", "tea_tree", item("tea_leaves"), item("tea_leaves"));
        register("potted_walpecan_tree_sapling", "walpecan_sapling", "walpecan_tree_sapling",
            item("walpecans"), item("walpecans"));
        register("potted_wild_peanut", "wild_peanut", "wild_peanut");
        register("potted_winter_rose", "winter_rose", "winter_rose");
        register("potted_pomelo_seed", "pomelo_tree_sapling", "pomelo_seeds");
    }

    public static List<Entry> entries() {
        return List.copyOf(ENTRIES);
    }

    private static void register(String name, String texture, String contentName) {
        register(name, texture, contentName, null);
    }

    private static void register(String name, String texture, String contentName, Supplier<Item> dropItem) {
        register(name, texture, contentName, null, dropItem);
    }

    private static void register(String name, String texture, String contentName, Supplier<Item> placementItem,
                                 Supplier<Item> dropItem) {
        Supplier<Block> content = content(contentName);
        RegistryObject<FlowerPotBlock> pot = BlockRegistry.register(name, () -> {
            Block plant = content.get();
            if (placementItem != null) {
                return new ItemPottedBlock(content, placementItem, potSupplier(name));
            }
            FlowerPotBlock registeredPot = new FlowerPotBlock(
                () -> (FlowerPotBlock) Blocks.FLOWER_POT,
                () -> plant,
                BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY));
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ForgeRegistries.BLOCKS.getKey(plant), () -> registeredPot);
            return registeredPot;
        });
        ENTRIES.add(new Entry(name, texture, content, pot,
            dropItem == null ? () -> content.get().asItem() : dropItem));
    }

    private static Supplier<Block> potSupplier(String name) {
        return () -> BlockRegistry.BLOCKS.getEntries().stream()
            .filter(block -> block.getId().getPath().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("缺少盆栽方块 " + name))
            .get();
    }

    private static Supplier<Block> content(String name) {
        if (name.equals("alfalfa_crop")) {
            return Crops.ALFALFA_CROP_BLOCK::get;
        }
        for (Bushes.Entry bush : Bushes.entries()) {
            if (bush.name().equals(name)) {
                return bush.block()::get;
            }
        }
        RegistryObject<Block> flower = Flowers.block(name);
        if (flower != null) {
            return flower::get;
        }
        if (name.equals("black_dirt")) {
            return BlackDirts.BLACK_DIRT::get;
        }
        if (name.equals("banana_tree_flower")) {
            return Bananas.FLOWER::get;
        }
        if (name.equals("banana_frond_sapling")) {
            return Bananas.SAPLING::get;
        }
        if (name.equals("dragon_fruit_cactus")) {
            return DragonFruits.CACTUS::get;
        }
        if (name.equals("dragon_fruit_cactus_flower")) {
            return DragonFruits.CACTUS_FLOWER::get;
        }
        if (name.equals("pineapple_crop")) {
            return Crops.PINEAPPLE_CROP_BLOCK::get;
        }
        if (name.equals("purple_sugar_canes")) {
            return PurpleSugarCanes.CROP::get;
        }
        if (name.equals("wild_peanut")) {
            return Crops.WILD_PEANUT::get;
        }
        if (name.equals("green_tea_block")) {
            return DisplayFoodContent.displayModels().stream()
                .filter(display -> display.block().getId().getPath().equals(name))
                .map(display -> (Supplier<Block>) display.block()::get)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("缺少盆栽植物方块 " + name));
        }
        if (name.equals("elderberry_flowers")) {
            return Trees.elderberryFlower()::get;
        }
        for (Trees.TreeEntry tree : Trees.allTrees()) {
            if (tree.sapling().getId().getPath().equals(name)) {
                return tree.sapling()::get;
            }
            if (tree instanceof Trees.Entry fruitTree) {
                if (fruitTree.leaves().getId().getPath().equals(name)) {
                    return fruitTree.leaves()::get;
                }
            }
            if (tree instanceof Trees.PlainEntry plainTree) {
                if (plainTree.leaves().getId().getPath().equals(name)) {
                    return plainTree.leaves()::get;
                }
            }
        }
        throw new IllegalStateException("盆栽引用了未声明植物方块 " + name);
    }

    private static Supplier<Item> item(String name) {
        return () -> ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(
            net.bittercat.plerocottage.PleroCottage.MODID, name));
    }

    public record Entry(String name, String texture, Supplier<Block> content, RegistryObject<FlowerPotBlock> block,
                        Supplier<Item> dropItem) {
    }
}

