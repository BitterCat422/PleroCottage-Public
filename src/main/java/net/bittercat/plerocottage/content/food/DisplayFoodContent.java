package net.bittercat.plerocottage.content.food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import net.bittercat.plerocottage.blocks.StaticFoodDisplayBlock;
import net.bittercat.plerocottage.blocks.FoodCollisionShapes;
import net.bittercat.plerocottage.blocks.FoodShapeBlock;
import net.bittercat.plerocottage.blocks.FoodModelProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.bittercat.plerocottage.items.DisplayFoodItem;
import net.bittercat.plerocottage.items.GeckoDisplayFoodItem;
import net.bittercat.plerocottage.content.plants.Coconuts;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.content.blocks.MarbleContent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.registries.RegistryObject;

public final class DisplayFoodContent {
    public enum ItemModelType {
        DEFAULT,
        JAVA_3D,
        GECKO_CUP,
        GECKO_BOWL
    }

    private static final List<RegistryObject<Block>> DISPLAY_BLOCKS = new ArrayList<>();
    private static final List<DisplayLoot> DISPLAY_LOOT = new ArrayList<>();
    private static final List<DisplayModel> DISPLAY_MODELS = new ArrayList<>();
    private static final Map<String, ItemModelType> SPECIAL_ITEM_MODELS = new HashMap<>();

    public record DisplayLoot(RegistryObject<Block> block, RegistryObject<Item> item) {}
    public record DisplayModel(RegistryObject<Block> block, String modelName, boolean geo,
                               boolean rotatable, String transparencyType) {}

    private DisplayFoodContent() {}

    public static void bootstrap() {
        register("borscht", "borscht", false, true, false, true, ItemModelType.GECKO_BOWL);
        register("fliederbeersuppe", "fliederbeersuppe", false, true, false, true, ItemModelType.GECKO_BOWL);
        register("mushroom_potage", "mushroom_potage", false, true, false, true, ItemModelType.GECKO_BOWL);
        register("miso_soup", "miso_soup", false, false, false, true, ItemModelType.GECKO_BOWL);
        register("pancake", "pancake", false, false, false, true);
        register("durian_mile_crepe_cake", "durian_mile_crepe_cake", false, false, false, true);
        register("prawn_and_corn_potage", "prawn_and_corn_potage", false, true, false, true, ItemModelType.GECKO_BOWL);
        register("coffee", "coffee", true, true, false, false, ItemModelType.JAVA_3D);
        register("chorus_fruit_juice", "chorus_fruit_juice", true, true, true, false);
        register("socata", "socata", true, true, true, false);
        register("melon_soda", "melon_soda", true, true, true, false);
        register("bubble_tea", "bubble_tea", true, true, true, false);
        register("coconut_milk", "coconut_milk", true, true, true, false);
        register("grimace_shake", "grimace_shake", true, true, true, false);
        register("orange_juice", "orange_juice", true, true, true, false);
        register("lemonade", "lemonade", true, true, true, false);
        register("apple_juice", "apple_juice", true, true, true, false);
        register("pear_with_rock_sugar", "pear_with_rock_sugar", true, true, true, false);
        register("bellini_base", "bellini_base", true, true, true, false);
        register("strawberry_shake", "strawberry_shake", true, true, true, false);
        register("blueberry_yogurt", "blueberry_yogurt", true, true, true, false);
        register("mango_juice", "mango_juice", true, true, true, false);
        register("pineapple_juice", "pineapple_juice", true, true, true, false);
        register("watermelon_juice", "watermelon_juice", true, true, true, false);
        register("cosmic_dragon_salad", "cosmic_dragon_salad", false, false, false, true, ItemModelType.GECKO_BOWL);
        register("rosa_salad", "rosa_salad", false, false, false, true, ItemModelType.GECKO_BOWL);
        register("mango_pudding", "mango_pudding", true, true, true, false);
        register("martini", "martini", true, true, true, false);
        register("risalamande", "risalamande", true, true, true, false);
        register("eton_mess", "eton_mess", true, true, true, false);
        register("marmalade", "marmalade", true, true, true, false);
        register("sea_pearl_salad", "sea_pearl_salad", false, false, false, true, ItemModelType.GECKO_BOWL);
        register("melon_salad", "melon_salad", false, false, false, true, ItemModelType.GECKO_BOWL);
        register("jewellery_box_salad", "jewellery_box_salad", false, false, false, true, ItemModelType.GECKO_BOWL);
        register("primary_colors_salad", "primary_colors_salad", false, false, false, true, ItemModelType.GECKO_BOWL);
        register("pomegranate_juice", "pomegranate_juice", true, true, true, false);
        register("kiwifruit_juice", "kiwifruit_juice", true, true, true, false);
        register("avocado_shake", "avocado_shake", true, true, true, false);
        register("banana_shake", "banana_shake", true, true, true, false);
        register("banana_split", "banana_split", true, true, true, false);
        register("cranberry_jelly", "cranberry_jelly", true, true, true, false);
        register("pudding", "pudding", true, true, true, false);
        register("pudding_a_la_mode", "pudding_a_la_mode", true, true, true, true);
        register("figgy_pudding", "figgy_pudding", false, false, false, true);
        register("colorful_fruit_parfait", "colorful_fruit_parfait", true, true, true, false);
        register("canton_bloom_soup", "canton_bloom_soup", false, true, false, true, ItemModelType.GECKO_BOWL);
        register("grape_juice", "grape_juice", true, true, true, false);
        register("mediterranean_sundae", "mediterranean_sundae", true, true, true, false);
        register("grapefruit_juice", "grapefruit_juice", true, true, true, false);
        register("yeung_ji_gam_lo", "yeung_ji_gam_lo", true, true, true, false);
        register("citrus_bavarois", "citrus_bavarois", true, true, true, true);
        register("plum_juice", "plum_juice", true, true, true, false);
        register("ota_ika", "ota_ika", false, false, false, true);
        register("frutas_con_chamoy", "frutas_con_chamoy", true, true, true, false);
        register("mangonada", "mangonada", true, true, true, false);
        register("red_wine", "red_wine", true, true, false, false, ItemModelType.JAVA_3D);
        register("white_wine", "white_wine", true, true, false, false, ItemModelType.JAVA_3D);
        register("champagne", "champagne", true, true, false, false, ItemModelType.JAVA_3D);
        register("gin", "gin", true, true, false, false, ItemModelType.JAVA_3D);
        register("rum", "rum", true, true, false, false, ItemModelType.JAVA_3D);
        register("umeshu", "umeshu", true, true, false, false, ItemModelType.JAVA_3D);
        register("beer", "beer", true, true, false, false, ItemModelType.JAVA_3D);
        register("absinthe", "absinthe", true, true, false, false, ItemModelType.JAVA_3D);
        register("sake", "sake", true, true, false, false, ItemModelType.JAVA_3D);
        register("brandy", "brandy", true, true, false, false, ItemModelType.JAVA_3D);
        register("whiskey", "whiskey", true, true, false, false, ItemModelType.JAVA_3D);
        register("hot_cocoa", "hot_cocoa", true, true, false, false, ItemModelType.JAVA_3D);
        register("green_tea", "green_tea", true, true, false, true, ItemModelType.GECKO_CUP);
        register("black_tea", "black_tea", true, true, false, true, ItemModelType.GECKO_CUP);
        register("mate", "mate", true, true, false, false, ItemModelType.JAVA_3D);
        register("apricot_kernel_drink", "apricot_kernel_drink", true, true, false, false, ItemModelType.JAVA_3D);
        register("yuenyeung", "yuenyeung", true, true, false, false, ItemModelType.JAVA_3D);
        register("po_cha", "po_cha", true, true, false, false, ItemModelType.JAVA_3D);
        register("lavender_latte", "lavender_latte", true, true, false, false, ItemModelType.JAVA_3D);
        register("sweet_hot_cocoa", "sweet_hot_cocoa", true, true, false, false, ItemModelType.JAVA_3D);
        register("espresso", "espresso", true, true, false, false, ItemModelType.JAVA_3D);
        register("americano", "americano", true, true, false, false, ItemModelType.JAVA_3D);
        register("latte", "latte", true, true, false, false, ItemModelType.JAVA_3D);
        register("glass_of_red_wine", "glass_of_red_wine", true, true, true, false);
        register("glass_of_white_wine", "glass_of_white_wine", true, true, true, false);
        register("glass_of_champagne", "glass_of_champagne", true, true, true, false);
        register("glass_of_gin", "glass_of_gin", true, true, true, false);
        register("glass_of_rum", "glass_of_rum", true, true, true, false);
        register("cup_of_umeshu", "cup_of_umeshu", true, true, true, false);
        register("glass_of_beer", "glass_of_beer", true, true, true, false);
        register("glass_of_absinthe", "glass_of_absinthe", true, true, true, false);
        register("cup_of_sake", "cup_of_sake", true, true, true, false);
        register("glass_of_brandy", "glass_of_brandy", true, true, true, false);
        register("glass_of_whiskey", "glass_of_whiskey", true, true, true, false);
        register("ochazuke", "ochazuke", false, false, false, true, ItemModelType.GECKO_BOWL);
        register("suanmeitang", "suanmeitang", false, false, false, true, ItemModelType.GECKO_BOWL);
        register("bulletproof_coffee", "bulletproof_coffee", true, true, false, false, ItemModelType.JAVA_3D);
        register("cafe_au_lait", "cafe_au_lait", true, true, false, false, ItemModelType.JAVA_3D);
        register("kombucha", "kombucha", true, true, false, true, ItemModelType.GECKO_CUP);
        register("fruit_flavored_kombucha", "fruit_flavored_kombucha", true, true, false, true, ItemModelType.GECKO_CUP);
        register("floral_kombucha", "floral_kombucha", true, true, false, true, ItemModelType.GECKO_CUP);
        register("espresso_con_panna", "espresso_con_panna", true, true, false, true, ItemModelType.GECKO_CUP);
        register("mocha_coffee", "mocha_coffee", true, true, false, false, ItemModelType.JAVA_3D);
        register("matcha", "matcha", true, true, false, false, ItemModelType.JAVA_3D);
        register("suutei_tsai", "suutei_tsai", true, true, false, false, ItemModelType.JAVA_3D);
        register("caramel_macchiato", "caramel_macchiato", true, true, false, false, ItemModelType.JAVA_3D);
        register("egg_coffee", "egg_coffee", true, true, false, false, ItemModelType.JAVA_3D);
        register("eggnog", "eggnog", true, true, false, false, ItemModelType.JAVA_3D);
        register("cappuccino", "cappuccino", true, true, false, false, ItemModelType.JAVA_3D);
        register("einspanner", "einspanner", true, true, false, true, ItemModelType.GECKO_CUP);
        register("irish_coffee", "irish_coffee", true, true, true, false);
        register("hk_milk_tea", "hk_milk_tea", true, true, true, false);
        register("nata_de_coco_milk_tea", "nata_de_coco_milk_tea", true, true, true, false);
        register("thai_tea", "thai_tea", true, true, true, false);
        register("apricot_kernel_tofu", "apricot_kernel_tofu", true, true, true, false);
        register("pina_colada", "pina_colada", true, true, true, false);
        register("hurricane_cocktail", "hurricane_cocktail", true, true, true, false);
        register("tiramisu", "tiramisu", false, true, false, false);
        register("opera_cake", "opera_cake", false, true, false, false);
        register("cornflakes", "cornflakes", true, false, false, true, ItemModelType.GECKO_BOWL);
        register("starfruit_wine", "starfruit_wine", true, true, false, false, ItemModelType.JAVA_3D);
        register("glass_of_starfruit_wine", "glass_of_starfruit_wine", true, true, true, false);
        register("kvass", "kvass", true, true, false, false, ItemModelType.JAVA_3D);
        register("glass_of_kvass", "glass_of_kvass", true, true, true, false);
        register("coffee_jelly", "coffee_jelly", true, true, true, false);
        register("flat_white", "flat_white", true, true, false, false, ItemModelType.JAVA_3D);
        register("affogato", "affogato", true, true, false, false, ItemModelType.JAVA_3D);
        register("goofy_goober_sundae", "goofy_goober_sundae", true, true, true, true);
        register("earl_grey_tea", "earl_grey_tea", true, true, false, true, ItemModelType.GECKO_CUP);
        register("jasmine_tea", "jasmine_tea", true, true, false, true, ItemModelType.GECKO_CUP);
        register("chamomile_tea", "chamomile_tea", true, true, false, false, ItemModelType.JAVA_3D);
        register("mint_tea", "mint_tea", true, true, false, false, ItemModelType.JAVA_3D);
        register("castella", "castella", false, true, false, false);
        register("gooseberry_fool", "gooseberry_fool", true, true, false, true, ItemModelType.GECKO_CUP);
        register("russian_tea", "russian_tea", true, true, false, true, ItemModelType.GECKO_CUP);
        register("saffron_tea", "saffron_tea", true, true, false, true, ItemModelType.GECKO_CUP);
        register("bandung", "bandung", true, true, false, false, ItemModelType.JAVA_3D);
        register("pumpkin_spice_latte", "pumpkin_spice_latte", true, true, false, true, ItemModelType.GECKO_CUP);
        register("vodka", "vodka", true, true, false, false, ItemModelType.JAVA_3D);
        register("glass_of_vodka", "glass_of_vodka", true, true, true, false);
        register("creme_de_cassis", "creme_de_cassis", true, true, false, false, ItemModelType.JAVA_3D);
        register("glass_of_creme_de_cassis", "glass_of_creme_de_cassis", true, true, true, false);
        register("hakuto_tea", "hakuto_tea", true, true, false, true, ItemModelType.GECKO_CUP);
        register("red_velvet_hot_cocoa", "red_velvet_hot_cocoa", true, true, false, true, ItemModelType.GECKO_CUP);
        register("matcha_latte", "matcha_latte", true, true, false, false, ItemModelType.JAVA_3D);
        register("clover_club", "clover_club", true, true, true, false);
        register("kir_royal", "kir_royal", true, true, true, false);
        register("halloween_parfait", "halloween_parfait", true, true, false, false);
        register("punch_bowl", "punch_bowl", true, true, false, false);
        register("watergate_salad", "watergate_salad", true, true, false, false);
        register("masala_chai", "masala_chai", true, true, false, false);
        register("daiquiri", "daiquiri", true, true, true, false);
        for (String name : GlassFoodModels.all()) {
            if (DISPLAY_MODELS.stream().noneMatch(display -> display.modelName().equals(name))) {
                throw new IllegalStateException("Layered glass model has no registered display food: " + name);
            }
        }
    }

    private static void register(String foodName, String blockName, boolean returnsBottle, boolean facing,
                                 boolean waterlogged, boolean geo) {
        register(foodName, blockName, returnsBottle, facing, waterlogged, geo, ItemModelType.DEFAULT);
    }

    private static void register(String foodName, String blockName, boolean returnsBottle, boolean facing,
                                 boolean waterlogged, boolean geo, ItemModelType itemModelType) {
        String id = foodName;
        if (geo && GlassFoodModels.contains(foodName)) {
            throw new IllegalStateException("Layered glass requires a Java display block: " + foodName);
        }
        if (itemModelType != ItemModelType.DEFAULT) {
            boolean geckoItem = itemModelType == ItemModelType.GECKO_CUP
                || itemModelType == ItemModelType.GECKO_BOWL;
            if (geckoItem != geo || GlassFoodModels.contains(foodName)
                || SPECIAL_ITEM_MODELS.putIfAbsent(foodName, itemModelType) != null) {
                throw new IllegalStateException("Invalid special item model registration: " + foodName);
            }
        }
        FoodModelProperties modelProperties = FoodModelProperties.get(id);
        boolean canRotate = modelProperties.rotationMode() != 0;
        boolean canWaterlog = modelProperties.waterloggable();
        RegistryObject<Block> block = BlockRegistry.register(id,
            () -> geo ? new StaticFoodDisplayBlock(canRotate, canWaterlog, FoodCollisionShapes.get(id),
                    false, id, () -> FoodContent.item(foodName).get())
                : new FoodShapeBlock(BlockBehaviour.Properties.of().strength(0.2F).noOcclusion()
                    .isRedstoneConductor((state, level, pos) -> false),
                    FoodCollisionShapes.get(id), canRotate, canWaterlog, id,
                    () -> FoodContent.item(foodName).get()));
        DISPLAY_BLOCKS.add(block);
        DISPLAY_MODELS.add(new DisplayModel(block, foodName, geo, canRotate, modelProperties.transparencyType()));
        RegistryObject<Item> food = ItemRegistry.register(foodName, () -> {
            FoodDefinition definition = FoodDefinition.get(foodName);
            if (itemModelType == ItemModelType.GECKO_CUP || itemModelType == ItemModelType.GECKO_BOWL) {
                FoodDisplayTransform transforms = itemModelType == ItemModelType.GECKO_BOWL
                    ? FoodDisplayTransform.BOWL : FoodDisplayTransform.CUP;
                return new GeckoDisplayFoodItem(block.get(), definition.itemProperties(),
                    definition.useDuration(), definition.useAnim(), containerFor(foodName, returnsBottle),
                    DisplayModelResources.forBlock(id), transforms);
            }
            return new DisplayFoodItem(block.get(), definition.itemProperties(), definition.useDuration(),
                definition.useAnim(), containerFor(foodName, returnsBottle));
        });
        DISPLAY_LOOT.add(new DisplayLoot(block, food));
        
    }

    public static Block[] allBlocks() {
        return DISPLAY_BLOCKS.stream().map(RegistryObject::get).toArray(Block[]::new);
    }

    public static List<DisplayLoot> displayLoot() {
        return List.copyOf(DISPLAY_LOOT);
    }

    public static List<DisplayModel> displayModels() {
        return List.copyOf(DISPLAY_MODELS);
    }

    public static boolean hasSpecialItemModel(String foodName) {
        return SPECIAL_ITEM_MODELS.containsKey(foodName) || GlassFoodModels.contains(foodName);
    }

    public static Set<String> geckoSpecialItemModelNames() {
        return SPECIAL_ITEM_MODELS.entrySet().stream()
            .filter(entry -> entry.getValue() == ItemModelType.GECKO_CUP
                || entry.getValue() == ItemModelType.GECKO_BOWL)
            .map(Map.Entry::getKey)
            .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    public static boolean usesJavaSpecialItemModel(String foodName) {
        return SPECIAL_ITEM_MODELS.get(foodName) == ItemModelType.JAVA_3D;
    }

    private static Supplier<? extends Item> containerFor(String foodName, boolean returnsBottle) {
        if (BOWL_FOODS.contains(foodName)) {
            return () -> MarbleContent.BOWL.get().asItem();
        }
        if (!returnsBottle) {
            return null;
        }
        if (WINE_FOODS.contains(foodName)) {
            return () -> Items.GLASS_BOTTLE;
        }
        if (CUP_FOODS.contains(foodName)) {
            return () -> Coconuts.CUP.get();
        }
        return () -> Items.GLASS_BOTTLE;
    }

    private static final Set<String> BOWL_FOODS = Set.of(
        "borscht", "fliederbeersuppe", "mushroom_potage", "prawn_and_corn_potage", "canton_bloom_soup",
        "cosmic_dragon_salad", "rosa_salad", "sea_pearl_salad", "melon_salad",
        "jewellery_box_salad", "primary_colors_salad", "miso_soup", "ochazuke", "suanmeitang",
        "cornflakes");
    private static final Set<String> WINE_FOODS = Set.of(
        "red_wine", "white_wine", "champagne", "gin", "rum", "umeshu", "beer", "absinthe",
        "sake", "brandy", "whiskey", "starfruit_wine", "kvass", "vodka", "creme_de_cassis");
    private static final Set<String> CUP_FOODS = Set.of(
        "coffee", "hot_cocoa", "green_tea", "black_tea", "mate", "apricot_kernel_drink", "yuenyeung",
        "po_cha", "lavender_latte", "sweet_hot_cocoa", "espresso", "americano", "latte",
        "bulletproof_coffee", "cafe_au_lait", "kombucha", "fruit_flavored_kombucha",
        "floral_kombucha", "espresso_con_panna", "mocha_coffee", "matcha", "suutei_tsai",
        "caramel_macchiato", "egg_coffee", "eggnog", "cappuccino", "einspanner",
        "cup_of_sake", "cup_of_umeshu", "flat_white", "affogato", "earl_grey_tea", "jasmine_tea", "chamomile_tea", "mint_tea",
        "gooseberry_fool",
        "russian_tea", "saffron_tea", "bandung", "pumpkin_spice_latte", "hakuto_tea",
        "red_velvet_hot_cocoa", "matcha_latte");
}
