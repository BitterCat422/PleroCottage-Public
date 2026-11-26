package net.bittercat.plerocottage.content.food;

import java.util.Set;
import java.util.Map;

public final class GlassFoodModels {
    private static final Map<String, String> SHARED_GLASS_MODELS = Map.ofEntries(
        Map.entry("chorus_fruit_juice", "klein_bottle_5x6"),
        Map.entry("marmalade", "glass_jar_6x8"),
        Map.entry("socata", "glass_jar_6x8"),
        Map.entry("apple_juice", "cylindrical_glass_4x10"),
        Map.entry("orange_juice", "cylindrical_glass_4x10"),
        Map.entry("watermelon_juice", "cylindrical_glass_4x10"),
        Map.entry("pear_with_rock_sugar", "cylindrical_glass_4x9"),
        Map.entry("glass_of_rum", "cylindrical_glass_4x8"),
        Map.entry("frutas_con_chamoy", "cylindrical_glass_6x9"),
        Map.entry("mangonada", "cylindrical_glass_6x9"),
        Map.entry("glass_of_whiskey", "cylindrical_glass_6x7"),
        Map.entry("blueberry_yogurt", "cylindrical_glass_5x5"),
        Map.entry("glass_of_gin", "cylindrical_glass_5x5"),
        Map.entry("mango_pudding", "cylindrical_glass_5x5"),
        Map.entry("apricot_kernel_tofu", "goblet_5x5_2"),
        Map.entry("bellini_base", "goblet_5x5_2"),
        Map.entry("glass_of_creme_de_cassis", "goblet_5x5_2"),
        Map.entry("glass_of_red_wine", "goblet_5x5_2"),
        Map.entry("glass_of_starfruit_wine", "goblet_5x5_2"),
        Map.entry("hurricane_cocktail", "goblet_5x5_2"),
        Map.entry("plum_juice", "goblet_5x5_2"),
        Map.entry("pomegranate_juice", "goblet_5x5_2"),
        Map.entry("coconut_milk", "goblet_5x5_2"),
        Map.entry("coffee_jelly", "goblet_5x5_2"),
        Map.entry("glass_of_brandy", "goblet_5x5_3"),
        Map.entry("glass_of_white_wine", "goblet_5x6_2"),
        Map.entry("grapefruit_juice", "goblet_5x8_3"),
        Map.entry("yeung_zhi_gum_lo", "goblet_5x8_3"),
        Map.entry("clover_club", "goblet_6x3_3"),
        Map.entry("daiquiri", "goblet_6x3_3"),
        Map.entry("risalamande", "goblet_6x4_3"),
        Map.entry("glass_of_champagne", "goblet_4x8_2"),
        Map.entry("grape_juice", "goblet_4x8_2"),
        Map.entry("grimace_shake", "goblet_4x8_2"),
        Map.entry("kir_royal", "goblet_4x8_2"),
        Map.entry("melon_soda", "goblet_4x8_2"),
        Map.entry("avocado_shake", "double_glass_4x4_5x4"),
        Map.entry("banana_shake", "double_glass_4x4_5x4"),
        Map.entry("glass_of_kvass", "double_glass_4x4_5x4"),
        Map.entry("kiwifruit_juice", "double_glass_4x4_5x4"),
        Map.entry("strawberry_shake", "double_glass_4x4_5x4"),
        Map.entry("bubble_tea", "double_glass_4x4_5x5"),
        Map.entry("hk_milk_tea", "double_glass_4x4_5x5"),
        Map.entry("nata_de_coco_milk_tea", "double_glass_4x4_5x5"),
        Map.entry("thai_tea", "double_glass_4x4_5x5"),
        Map.entry("eton_mess", "double_goblet_5x5_4x4_2"),
        Map.entry("pina_colada", "double_goblet_5x4_4x4_2"),
        Map.entry("glass_of_absinthe", "double_goblet_4x3_5x4_2"),
        Map.entry("martini", "double_goblet_4x2_7x3_3"),
        Map.entry("glass_of_beer", "beer_mug_6x8"),
        Map.entry("pineapple_juice", "beer_mug_6x8"),
        Map.entry("masala_chai", "beer_mug_5x6"),
        Map.entry("irish_coffee", "goblet_beer_mug_5x7"),
        Map.entry("glass_of_vodka", "thick_bottomed_glass_4x3_4x6"),
        Map.entry("mango_juice", "thick_bottomed_double_glass_5x1_4x5_5x4"),
        Map.entry("lemonade", "glass_jug_7x6_5x3"),
        Map.entry("punch_bowl", "glass_bowl_5x1_12x5"));

    private static final Set<String> MODELS = SHARED_GLASS_MODELS.keySet();

    private GlassFoodModels() {}

    public static boolean contains(String name) {
        return MODELS.contains(name);
    }

    public static Set<String> all() {
        return MODELS;
    }

    public static String glassModel(String foodId) {
        return SHARED_GLASS_MODELS.getOrDefault(foodId, foodId);
    }
}
