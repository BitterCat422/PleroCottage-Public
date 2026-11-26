package net.bittercat.plerocottage.content.food;

import java.util.Set;
import java.util.Map;
import java.util.List;

import net.bittercat.plerocottage.content.effects.MobEffectContent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.bittercat.plerocottage.items.ConsumableItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.registries.RegistryObject;

public record FoodDefinition(String name, int nutrition, float saturation, boolean meat, boolean alwaysEat,
                             int stackSize, Rarity rarity, int useDuration, UseAnim useAnim) {

    private static final java.util.Map<String, FoodDefinition> DEFINITIONS = createDefinitions();

    private static java.util.Map<String, FoodDefinition> createDefinitions() {
        java.util.Map<String, FoodDefinition> definitions = new java.util.LinkedHashMap<>();
        register(definitions, "ice_cubes", 0, 0.0F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "abalone", 1, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fugu_slice", 1, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "mentaiko", 1, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "raw_cod_slice", 1, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "raw_prawn", 1, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "raw_salmon_slice", 1, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "shirako", 1, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "tarako", 1, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "tropical_fish_slice", 1, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pumpkin_slice", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "durian_flesh", 2, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "coffee_beans", 2, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "dried_jalapeno", 2, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "half_of_avocado", 3, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "half_of_kiwifruit", 3, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "half_of_gold_kiwifruit", 3, 0.3F, false, false, 64, Rarity.RARE, 32, UseAnim.EAT);
        register(definitions, "cooked_durian_flesh", 4, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "dried_hawthorns", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cooked_prawn", 4, 0.6F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cooked_abalone", 4, 0.8F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "sunny_side_up_egg", 5, 0.8F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cooked_banana", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "prune", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cooked_sunflower_seeds", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "suspicious_mold", 0, 0.0F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "rainbow_sprinkles", 1, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "meringue", 1, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "nori", 1, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "scoop_of_strawberry_ice_cream", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "scoop_of_chocolate_ice_cream", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "scoop_of_ice_cream", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "scoop_of_tiger_tail_ice_cream", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "scoop_of_moon_mist_ice_cream", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "scoop_of_mint_chocolate_ice_cream", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "chocolate_chips", 2, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "dark_chocolate_chips", 2, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "white_chocolate_chips", 2, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "surimi_crab_stick", 2, 0.3F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "buntan_ames", 3, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "mochi", 3, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pickle_slices", 3, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "raw_beef_patty", 3, 0.3F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "swiss_roll_slice", 3, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "waffle", 3, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "corn_chips", 3, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cheddar_cheese_wedge", 3, 0.8F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "emmental_cheese_wedge", 3, 0.8F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "mozzarella_cheese_wedge", 3, 0.8F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "sausage", 3, 0.8F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "blueberry_cookie", 4, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cherries_cookie", 4, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cranberry_cookie", 4, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "lemon_cookie", 4, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "matcha_cookie", 4, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "dried_apricots", 4, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "jalebi", 4, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pistachio_macaron", 4, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "raspberry_bubble_gum", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "blue_raspberry_bubble_gum", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "blackberry_bubble_gum", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "rainbow_cookie", 4, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "snowflake_hawthorn_balls", 4, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "spam", 4, 0.3F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "blackcurrant_macaron", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "chocolate_macaron", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cream_puff", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "gulab_jamuns", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "kuri_manju", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "lavender_macaron", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "lemon_macaron", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pastel_de_nata", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "sachima", 6, 0.8F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "raspberry_macaron", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "loaf_slice", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "stuffed_olives", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "kappa_maki", 4, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "tekka_maki", 4, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cream_cheese", 4, 0.8F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "licorice_candy", 5, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "bun", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cooked_mochi", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cornbread", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "eclair", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "naan", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "nama_chocolate", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pretzel", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "savoiardi", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "soul_cake", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "umeboshis", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cupcake_qaq", 5, 0.6F, false, false, 64, Rarity.EPIC, 32, UseAnim.EAT);
        register(definitions, "cod_nigiri", 5, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fugu_nigiri", 5, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "mentaiko_nigiri", 5, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "salmon_nigiri", 5, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "tropical_fish_nigiri", 5, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "marshmallows", 6, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "butter_cookie", 6, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "chocolate", 6, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "dark_chocolate", 6, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "guilinggao", 6, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "hawthorn_flakes", 6, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pickle", 6, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "white_chocolate", 6, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "blueberry_cherries_ice_cream", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "blueberry_ice_cream", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cherries_ice_cream", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "chocolate_ice_cream", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "starfruit_ice_cream", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "starfruit_jalapeno_ice_cream", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "vanilla_chocolate_ice_cream", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "vanilla_ice_cream", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "jalapeno_ice_cream", 6, 0.3F, false, true, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "vanilla_gelato", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "strawberry_gelato", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "chocolate_gelato", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "tiger_tail_gelato", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "moon_mist_gelato", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "mint_chocolate_gelato", 6, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "durian_crisp", 6, 0.35F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cheesy_mushroom_bacon_pizza_slice", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cinnamon_roll", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "durian_pizza_slice", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "graham_cacker", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "hawaiian_pizza_slice", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "kadayif", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "kaju_katlis", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "lamingtons", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "madeleine", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "mont_blanc", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "nata_de_coco", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "nougat", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "elderberry_gummy_bear", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "konpeito", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pepperoni_pizza_slice", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "sakura_mochi", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "seafood_pizza_slice", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "supreme_pizza_slice", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "vanilla_pizza_slice", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "california_roll", 6, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "lavender_pon_de_ring", 6, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "matcha_pon_de_ring", 6, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "mince_pie", 6, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pineberry_pon_de_ring", 6, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pon_de_ring", 6, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "shirako_gunkan", 6, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fried_tempura", 6, 0.65F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fried_abalone", 6, 0.85F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "tanghulu", 7, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pineapple_pies", 7, 0.35F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "checkerboard_cookie", 7, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "latiya", 7, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "melon_pan", 7, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "onigiri", 7, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "scone", 7, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "avocado_nigiri", 7, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "donut", 7, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fries", 7, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "elderberry_flowers_fritters", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "ikura_gunkan", 7, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "surimi_crab_stick_nigiri", 7, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "you_tiao", 7, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fried_fish", 7, 0.65F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "avocado_egg_bake", 8, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "bagel", 8, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "baked_apple", 8, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "boloyau", 8, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "caneles", 8, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "choco_cornet", 8, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cream_bun", 8, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "croissant", 8, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "battenberg_cake", 6, 0.8F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "brownie", 6, 0.8F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fruit_sandwich", 8, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "peanut_butter_cup", 8, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "hot_dog", 8, 0.6F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "abalone_nigiri", 8, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fried_banana", 8, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "shrimp_nigiri", 8, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "wagyu_with_caviar_nigiri", 8, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fried_chicken", 8, 0.65F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cooked_onigiri", 8, 0.7F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "senbei", 8, 0.75F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cooked_beef_patty", 8, 0.8F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "hawthorn_rolls", 9, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "swiss_roll", 9, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "chocolate_donut", 9, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "lemon_donut", 9, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "snow_donut", 9, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "strawberry_donut", 9, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "tostada_shell", 9, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "white_chocolate_donut", 9, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "dubai_chocolate", 10, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "focaccia", 10, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "napoleon_pastry", 10, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "sausage_bun", 10, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "taco", 10, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "spam_musubi", 10, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fried_porkchop", 10, 0.85F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fried_steak", 10, 0.85F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "tempura_nigiri", 11, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "baklava", 12, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "s_mores", 24, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "shawarma", 28, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "ice_cream_cone", 2, 0.6F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raisins", 2, 0.6F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "fortune_cookie", 2, 0.6F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "wafer", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "fried_drumstick", 2, 0.65F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "popcorn_chicken", 2, 0.65F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "pineapple_slices", 3, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "pineapple_pastries", 5, 0.6F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "muffin", 5, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "blueberry_muffin", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cranberry_muffin", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "chocolate_muffin", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "orange_muffin", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "elderberry_muffin", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "durian_mile_crepe_cake_slice", 2, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cucumber_slices", 2, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "rainbow_chocolate_candies", 2, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "caramel_chocolate_cake_slice", 2, 0.1F, false, false, 64, Rarity.COMMON, 16, UseAnim.EAT);
        register(definitions, "sweet_berry_cake_slice", 2, 0.1F, false, false, 64, Rarity.COMMON, 16, UseAnim.EAT);
        register(definitions, "chorus_flower_jelly_cake_slice", 2, 0.1F, false, false, 64, Rarity.COMMON, 16, UseAnim.EAT);
        register(definitions, "nether_wart_soul_cake_slice", 2, 0.1F, false, false, 64, Rarity.COMMON, 16, UseAnim.EAT);
        register(definitions, "vanilla_cake_slice", 2, 0.1F, false, false, 64, Rarity.COMMON, 16, UseAnim.EAT);
        register(definitions, "pavlova_cake_slice", 2, 0.1F, false, false, 64, Rarity.COMMON, 16, UseAnim.EAT);
        register(definitions, "strawberry_shortcake_slice", 2, 0.1F, false, false, 64, Rarity.COMMON, 16, UseAnim.EAT);
        register(definitions, "cooked_pineapple_slices", 5, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "emmental_cheese_slice", 1, 0.8F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cheddar_cheese_slice", 1, 0.8F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "mozzarella_cheese_slice", 1, 0.8F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "popcorn", 2, 0.3F, false, false, 64, Rarity.COMMON, 16, UseAnim.EAT);
        register(definitions, "banana_chips", 3, 0.6F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "pedas", 3, 0.6F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "apricot_kernel", 4, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "green_bell_pepper_slices", 4, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "baguette", 12, 0.6F, false, false, 64, Rarity.COMMON, 64, UseAnim.EAT);
        register(definitions, "loaf", 16, 0.6F, false, false, 64, Rarity.COMMON, 64, UseAnim.EAT);
        register(definitions, "raw_chicken_drumstick", 1, 0.1F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raw_chicken_breast", 1, 0.1F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raw_chicken_wing", 1, 0.1F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raw_duck_drumstick", 1, 0.1F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raw_duck_wing", 1, 0.1F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raw_duck_breast", 1, 0.1F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cooked_chicken_drumstick", 2, 0.6F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cooked_chicken_breast", 2, 0.6F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cooked_chicken_wing", 2, 0.6F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cooked_duck_drumstick", 2, 0.6F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cooked_duck_wing", 2, 0.6F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cooked_duck_breast", 2, 0.6F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "sumeshi", 4, 0.6F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raw_beef_cubes", 1, 0.3F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raw_mutton_ribs", 1, 0.3F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raw_pork_bellies", 1, 0.3F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "spam_slice", 1, 0.3F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cooked_beef_cubes", 3, 0.8F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cooked_mutton_ribs", 3, 0.8F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "bacon", 3, 0.8F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cooked_spam_slice", 3, 0.8F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "carrot_slices", 1, 0.6F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "potato_slices", 1, 0.1F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "beetroot_slice", 4, 0.2F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "jalapeno_slices", 1, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "wild_jalapeno_slices", 1, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "pimento_slices", 1, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "end_pepper_slices", 1, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "candy_corn", 3, 0.1F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "gummy_worm", 4, 0.1F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "eyeball_jelly", 2, 0.1F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raw_turkey_drumstick", 2, 0.1F, true, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "raw_mallard_duck", 6, 0.1F, true, false, 64, Rarity.COMMON, 48, UseAnim.EAT);
        register(definitions, "cooked_mallard_duck", 10, 0.6F, true, false, 64, Rarity.COMMON, 48, UseAnim.EAT);
        register(definitions, "raw_turkey", 8, 0.1F, true, false, 64, Rarity.COMMON, 64, UseAnim.EAT);
        register(definitions, "cooked_turkey", 14, 0.6F, true, false, 64, Rarity.COMMON, 64, UseAnim.EAT);
        register(definitions, "raw_turkey_breast", 2, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "raw_turkey_wing", 2, 0.1F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cooked_turkey_breast", 4, 0.6F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cooked_turkey_drumstick", 4, 0.6F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cooked_turkey_wing", 4, 0.6F, true, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "boxed_chocolate_milk", 3, 0.1F, false, true, 64, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "boxed_lemon_tea", 3, 0.1F, false, true, 64, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "cough_syrup", 2, 0.1F, false, true, 64, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "boxed_whipping_cream", 3, 0.1F, false, true, 64, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "bomb_paleta", 8, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "luau_paleta", 12, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "watermelon_paleta", 8, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pink_snowman_paleta", 8, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "heart_crush_paleta", 10, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pineapple_paleta", 10, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "muskmelon_paleta", 8, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "hanami_dango", 4, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "kiwifruit_paleta", 6, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pom_bloom_paleta", 6, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "hawpsicle_paleta", 8, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "dragonbloom_paleta", 8, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "chocolate_banana_pop", 9, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "grape_paleta", 4, 0.2F, false, false, 16, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "muscat_grape_paleta", 4, 0.2F, false, false, 16, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "inkoco_paleta", 4, 0.2F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "ringo_ame", 7, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "marshmallow_skewer", 2, 0.1F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "campfire_marshmallow", 3, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "taffy_apple", 7, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cherries_pie_slice", 8, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "truffle_pie_slice", 6, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "chorus_fruit_juice", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "socata", 2, 0.1F, false, true, 64, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "coca_cola", 1, 0.1F, false, true, 1, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "boxed_milk", 1, 0.1F, false, true, 64, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "yogurt", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "bottled_water", 0, 0.0F, false, true, 64, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "magic_rock_candy", 8, 1.2F, false, true, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "golden_apple_slice", 2, 1.2F, false, true, 64, Rarity.UNCOMMON, 32, UseAnim.EAT);
        register(definitions, "enchanted_golden_apple_slice", 2, 1.2F, false, true, 64, Rarity.RARE, 32, UseAnim.EAT);
        register(definitions, "tomato_slice", 1, 0.3F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "cooked_rice", 6, 0.8F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "wrapped_candy", 1, 0.1F, false, false, 64, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "chewing_gum", 1, 0.1F, false, true, 64, Rarity.COMMON, 16, UseAnim.EAT);
        register(definitions, "borscht", 20, 0.8F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "fliederbeersuppe", 12, 0.8F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "mushroom_potage", 16, 0.8F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "miso_soup", 16, 0.8F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "pancake", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "durian_mile_crepe_cake", 8, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "prawn_and_corn_potage", 16, 0.8F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "coffee", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "melon_soda", 4, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "bubble_tea", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "coconut_milk", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "grimace_shake", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "orange_juice", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "lemonade", 5, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "apple_juice", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "pear_with_rock_sugar", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "bellini_base", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "strawberry_shake", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "blueberry_yogurt", 2, 0.1F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "mango_juice", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "pineapple_juice", 4, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "watermelon_juice", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "cosmic_dragon_salad", 9, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "rosa_salad", 7, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "mango_pudding", 3, 0.1F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "martini", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "risalamande", 2, 0.1F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "eton_mess", 3, 0.1F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "marmalade", 12, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "sea_pearl_salad", 8, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "melon_salad", 8, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "jewellery_box_salad", 8, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "primary_colors_salad", 8, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pomegranate_juice", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "kiwifruit_juice", 4, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "avocado_shake", 4, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "banana_shake", 4, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "banana_split", 16, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cranberry_jelly", 5, 0.1F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pudding", 4, 0.1F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pudding_a_la_mode", 14, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "figgy_pudding", 10, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "colorful_fruit_parfait", 16, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "canton_bloom_soup", 16, 0.8F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "grape_juice", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "mediterranean_sundae", 14, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "grapefruit_juice", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "yeung_zhi_gum_lo", 4, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "citrus_bavarois", 12, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "plum_juice", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "ota_ika", 4, 0.3F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "frutas_con_chamoy", 12, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "mangonada", 12, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "red_wine", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "white_wine", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "champagne", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "gin", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "rum", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "umeshu", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "beer", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "absinthe", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "sake", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "brandy", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "whiskey", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "hot_cocoa", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "green_tea", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "black_tea", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "mate", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "apricot_kernel_drink", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "yuenyeung", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "po_cha", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "lavender_latte", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "sweet_hot_cocoa", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "espresso", 1, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "americano", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "latte", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "glass_of_red_wine", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "glass_of_white_wine", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "glass_of_champagne", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "glass_of_gin", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "glass_of_rum", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "glass_of_umeshu", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "glass_of_beer", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "glass_of_absinthe", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "glass_of_sake", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "glass_of_brandy", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "glass_of_whiskey", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "ochazuke", 13, 0.6F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "suanmeitang", 14, 0.6F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "bulletproof_coffee", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "cafe_au_lait", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "kombucha", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "fruit_flavored_kombucha", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "floral_kombucha", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "espresso_con_panna", 1, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "mocha_coffee", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "matcha", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "suutei_tsai", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "caramel_macchiato", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "egg_coffee", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "eggnog", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "cappuccino", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "einspanner", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "irish_coffee", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "hk_milk_tea", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "nata_de_coco_milk_tea", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "thai_tea", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "apricot_kernel_tofu", 12, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "pina_colada", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "hurricane_cocktail", 3, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "tiramisu", 16, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "opera_cake", 10, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "cornflakes", 10, 0.65F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "starfruit_wine", 6, 0.1F, false, true, 16, Rarity.EPIC, 32, UseAnim.DRINK);
        register(definitions, "glass_of_starfruit_wine", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "kvass", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "glass_of_kvass", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "coffee_jelly", 5, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "flat_white", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "affogato", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "goofy_goober_sundae", 16, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "earl_grey_tea", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "jasmine_tea", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "chamomile_tea", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "mint_tea", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "castella", 6, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "gooseberry_fool", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "russian_tea", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "saffron_tea", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "bandung", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "pumpkin_spice_latte", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "vodka", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "glass_of_vodka", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "creme_de_cassis", 6, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "glass_of_creme_de_cassis", 2, 0.1F, false, true, 16, Rarity.COMMON, 24, UseAnim.DRINK);
        register(definitions, "hakuto_tea", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "red_velvet_hot_cocoa", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "matcha_latte", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "clover_club", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "kir_royal", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "halloween_parfait", 22, 0.3F, false, false, 16, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "punch_bowl", 5, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "watergate_salad", 16, 0.6F, false, false, 64, Rarity.COMMON, 32, UseAnim.EAT);
        register(definitions, "masala_chai", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        register(definitions, "daiquiri", 2, 0.1F, false, true, 16, Rarity.COMMON, 32, UseAnim.DRINK);
        registerPlantFoods(definitions);
        return java.util.Map.copyOf(definitions);
    }

    private static void registerPlantFoods(java.util.Map<String, FoodDefinition> definitions) {
        register(definitions, "banana", 4, 0.3F, 32);
        register(definitions, "banana_slices", 2, 0.3F, 24);
        register(definitions, "half_of_coconut", 4, 0.3F, 32);
        register(definitions, "coconut_meat", 2, 0.3F, 24);
        register(definitions, "grated_coconut", 2, 0.35F, 16);
        register(definitions, "coconut_bun", 7, 0.6F, 32);
        register(definitions, "dragon_fruit", 5, 0.3F, 32);
        register(definitions, "dragon_fruit_slices", 2, 0.3F, 24);
        register(definitions, "muskmelon_slice", 2, 0.3F, 24);
        register(definitions, "cooked_peanuts", 2, 0.6F, 24);
        register(definitions, "coffee_berries", 2, 0.3F, 32);
        register(definitions, "jalapeno", 2, 0.3F, 32);
        register(definitions, "tomato", 3, 0.3F, 32);
        register(definitions, "cucumber", 4, 0.3F, 32);
        register(definitions, "grapes", 5, 0.3F, 32);
        register(definitions, "passion_fruit", 4, 0.3F, 32);
        register(definitions, "half_of_passion_fruit", 2, 0.3F, 24);
        register(definitions, "muscat_grapes", 5, 0.3F, false, false, 64, Rarity.EPIC, 32, UseAnim.EAT);
        register(definitions, "gooseberries", 2, 0.1F, false, false, 64, Rarity.EPIC, 32, UseAnim.EAT);
        register(definitions, "blackberry", 2, 0.1F, false, false, 64, Rarity.EPIC, 32, UseAnim.EAT);
        register(definitions, "pineberry", 2, 0.2F, false, false, 64, Rarity.EPIC, 32, UseAnim.EAT);
        register(definitions, "strawberry", 2, 0.1F, 32);
        register(definitions, "green_bell_pepper", 3, 0.3F, 32);
        register(definitions, "pimento", 3, 0.3F, 32);
        register(definitions, "end_pepper", 3, 0.3F, 32);
        register(definitions, "blueberries", 2, 0.1F, 32);
        register(definitions, "blackcurrants", 2, 0.1F, 32);
        register(definitions, "raspberry", 2, 0.1F, 32);
        register(definitions, "blue_raspberry", 2, 0.1F, 32);
        register(definitions, "cranberries", 2, 0.1F, 32);
        register(definitions, "cherry_tomatoes", 2, 0.3F, 32);
        register(definitions, "wild_jalapeno", 2, 0.3F, 32);
        register(definitions, "baby_corn", 2, 0.3F, 32);
        register(definitions, "apple_slice", 2, 0.2F, 32);
        register(definitions, "green_apple", 5, 0.3F, 32);
        register(definitions, "green_apple_slice", 2, 0.3F, 32);
        register(definitions, "apricots", 4, 0.3F, 32);
        register(definitions, "umes", 4, 0.3F, false, false, 64, Rarity.EPIC, 32, UseAnim.EAT);
        register(definitions, "avocado_slice", 2, 0.3F, 32);
        register(definitions, "cherries", 2, 0.1F, 32);
        register(definitions, "fig", 5, 0.3F, 32);
        register(definitions, "fig_slice", 3, 0.3F, 32);
        register(definitions, "hawthorns", 3, 0.3F, 32);
        register(definitions, "kiwifruit_slice", 2, 0.3F, 32);
        register(definitions, "gold_kiwifruit_slice", 2, 0.3F, 32);
        register(definitions, "lemon", 5, 0.3F, 32);
        register(definitions, "lemon_slice", 2, 0.3F, 32);
        register(definitions, "lime", 5, 0.3F, false, false, 64, Rarity.EPIC, 32, UseAnim.EAT);
        register(definitions, "lime_slice", 2, 0.3F, 32);
        register(definitions, "mango", 5, 0.3F, 32);
        register(definitions, "mango_slice", 3, 0.3F, 32);
        register(definitions, "olive_fruits", 2, 0.6F, 32);
        register(definitions, "black_olive_fruits", 2, 0.6F, false, false, 64, Rarity.EPIC, 32, UseAnim.EAT);
        register(definitions, "orange", 5, 0.3F, 32);
        register(definitions, "orange_segment", 2, 0.3F, 32);
        register(definitions, "grapefruit", 5, 0.3F, false, false, 64, Rarity.EPIC, 32, UseAnim.EAT);
        register(definitions, "grapefruit_slice", 2, 0.3F, 32);
        register(definitions, "peach", 5, 0.3F, 32);
        register(definitions, "peach_slice", 2, 0.3F, 32);
        register(definitions, "plum", 5, 0.3F, false, false, 64, Rarity.EPIC, 32, UseAnim.EAT);
        register(definitions, "plum_slice", 2, 0.3F, 32);
        register(definitions, "pear", 4, 0.3F, 32);
        register(definitions, "pear_slice", 2, 0.3F, 32);
        register(definitions, "pomegranate_wedges", 3, 0.3F, 32);
        register(definitions, "starfruit", 6, 0.3F, 32);
        register(definitions, "starfruit_slice", 2, 0.3F, 32);
        register(definitions, "pomelo_wedge", 3, 0.3F, 32);
        register(definitions, "half_of_durian", 6, 0.3F, 32);
        register(definitions, "cashew", 5, 0.6F, 32);
        register(definitions, "juniper_berries", 2, 0.1F, 32);
        register(definitions, "holly_berries", 2, 0.1F, 24);
        register(definitions, "nutmeg", 2, 0.3F, 24);
        register(definitions, "elderberries", 2, 0.3F, 32);
        for (String name : new String[] {"cooked_almonds", "cooked_pistachios", "cooked_walpecans",
            "cooked_hazelnuts", "cooked_chestnuts", "cooked_cashew", "pine_nuts", "dried_nutmeg", "dried_star_anise"}) {
            register(definitions, name, 2, 0.8F, 24);
        }
        register(definitions, "purple_sugar_canes", 1, 0.1F, false, true, 64, Rarity.COMMON, 48, UseAnim.EAT);
        register(definitions, "popcorn_bucket", 1, 0.6F, false, false, 1, Rarity.COMMON, 24, UseAnim.EAT);
        register(definitions, "salted_popcorn_bucket", 1, 0.6F, false, false, 1, Rarity.COMMON, 24, UseAnim.EAT);
    }

    private static void register(java.util.Map<String, FoodDefinition> definitions, String name,
                                 int nutrition, float saturation, int useDuration) {
        register(definitions, name, nutrition, saturation, false, false, 64, Rarity.COMMON,
            useDuration, UseAnim.EAT);
    }

    public Item item() {
        return new Item(itemProperties()) {
            @Override
            public int getUseDuration(ItemStack stack) {
                return useDuration;
            }

            @Override
            public UseAnim getUseAnimation(ItemStack stack) {
                return useAnim;
            }
        };
    }

    public ConsumableItem consumable() {
        return new ConsumableItem(itemProperties(), useDuration, useAnim, null, false, false);
    }

    public BlockItem blockItem(Block block, boolean itemName) {
        if (itemName) {
            return new ItemNameBlockItem(block, itemProperties()) {
                @Override
                public int getUseDuration(ItemStack stack) {
                    return useDuration;
                }

                @Override
                public UseAnim getUseAnimation(ItemStack stack) {
                    return useAnim;
                }
            };
        }
        return new BlockItem(block, itemProperties()) {
            @Override
            public int getUseDuration(ItemStack stack) {
                return useDuration;
            }

            @Override
            public UseAnim getUseAnimation(ItemStack stack) {
                return useAnim;
            }
        };
    }

    private static void register(java.util.Map<String, FoodDefinition> definitions, String name, int nutrition,
                                 float saturation, boolean meat, boolean alwaysEat, int stackSize,
                                 Rarity rarity, int useDuration, UseAnim useAnim) {
        FoodDefinition definition = of(name, nutrition, saturation, meat, alwaysEat, stackSize,
            rarity, useDuration, useAnim);
        if (definitions.putIfAbsent(name, definition) != null) {
            throw new IllegalStateException("重复食物定义：" + name);
        }
    }

    public static FoodDefinition get(String name) {
        FoodDefinition definition = DEFINITIONS.get(name);
        if (definition == null) throw new IllegalArgumentException("缺少食物定义：" + name);
        return definition;
    }

    public static FoodDefinition of(String name, int nutrition, float saturation, boolean meat,
                                    boolean alwaysEat, int stackSize, Rarity rarity,
                                    int useDuration, UseAnim useAnim) {
        return new FoodDefinition(name, nutrition, saturation, meat, alwaysEat,
            stackSize, rarity, useDuration, useAnim);
    }

    public FoodProperties properties() {
        return properties(name, nutrition, saturation, meat, alwaysEat, useAnim);
    }

    public Item.Properties itemProperties() {
        return new Item.Properties()
            .food(properties())
            .stacksTo(stackSize)
            .rarity(rarity);
    }

    public static FoodProperties basicProperties(int nutrition, float saturation, boolean alwaysEat) {
        return properties("", nutrition, saturation, false, alwaysEat);
    }

    public static FoodProperties properties(String name, int nutrition, float saturation,
                                            boolean meat, boolean alwaysEat) {
        FoodDefinition definition = DEFINITIONS.get(name);
        return properties(name, nutrition, saturation, meat, alwaysEat,
            definition == null ? UseAnim.EAT : definition.useAnim());
    }

    private static FoodProperties properties(String name, int nutrition, float saturation,
                                              boolean meat, boolean alwaysEat, UseAnim useAnim) {
        FoodProperties.Builder builder = new FoodProperties.Builder()
            .nutrition(nutrition)
            .saturationMod(saturation);
        if (meat) {
            builder.meat();
        }
        if (alwaysEat) {
            builder.alwaysEat();
        }
        addEffects(builder, name);
        addFormerFruitEffects(builder, name, useAnim);
        return builder.build();
    }

    private static void addFormerFruitEffects(FoodProperties.Builder builder, String name, UseAnim useAnim) {
        List<RegistryObject<MobEffect>> effects = FRUIT_EFFECTS.get(name);
        if (effects == null) {
            return;
        }
        boolean rare = RARE_EFFECT_FOODS.contains(name);
        boolean drink = useAnim == UseAnim.DRINK;
        int amplifier = drink ? (rare ? 5 : 3) : (rare ? 9 : 7);
        int duration = drink ? (rare ? 21_600 : 14_400) : (rare ? 10_800 : 7_200);
        for (RegistryObject<MobEffect> effect : effects) {
            builder.effect(() -> new MobEffectInstance(effect.get(), duration, amplifier, false, true), 1.0F);
        }
    }

    private static final Map<String, List<RegistryObject<MobEffect>>> FRUIT_EFFECTS = fruitEffects();
    private static final Set<String> RARE_EFFECT_FOODS = Set.of(
        "glass_of_white_wine", "brandy", "mince_pie", "glass_of_starfruit_wine", "earl_grey_tea",
        "jewellery_box_salad", "prune", "frutas_con_chamoy", "starfruit_wine",
        "pineberry_pon_de_ring", "glass_of_brandy", "daiquiri", "plum_juice", "champagne",
        "grapefruit_juice", "bellini_base", "umeboshis", "gummy_worm", "umeshu", "suanmeitang",
        "ochazuke", "glass_of_umeshu", "fruit_flavored_kombucha",
        "pink_snowman_paleta", "supreme_pizza_slice", "ota_ika", "white_wine",
        "hurricane_cocktail", "taffy_apple", "focaccia", "kir_royal", "yeung_zhi_gum_lo",
        "glass_of_champagne", "muscat_grape_paleta", "citrus_bavarois", "gooseberry_fool",
        "blackberry_bubble_gum", "bomb_paleta");

    private static Map<String, List<RegistryObject<MobEffect>>> fruitEffects() {
        Map<String, List<RegistryObject<MobEffect>>> effects = new java.util.HashMap<>();
        addFruitEffect(effects, MobEffectContent.BURSTING_BERRY,
            "figgy_pudding", "strawberry_shortcake", "colorful_fruit_parfait", "pink_snowman_paleta",
            "pavlova_cake_slice", "strawberry_donut", "pudding_a_la_mode", "pineberry_pon_de_ring",
            "eton_mess", "grimace_shake", "napoleon_pastry", "strawberry_shortcake_slice",
            "strawberry_shake", "bomb_paleta", "fruit_sandwich", "primary_colors_salad",
            "gummy_worm", "kir_royal", "pavlova_cake", "clover_club", "raspberry_macaron",
            "sea_pearl_salad", "blueberry_cherries_ice_cream", "blueberry_yogurt", "blueberry_muffin",
            "blueberry_ice_cream", "blueberry_cookie", "cranberry_muffin", "jewellery_box_salad",
            "punch_bowl", "cranberry_jelly", "cranberry_cookie", "mont_blanc",
            "glass_of_creme_de_cassis", "creme_de_cassis", "blackcurrant_macaron", "gooseberry_fool",
            "sweet_berry_cake_slice", "sweet_berry_cake", "scoop_of_strawberry_ice_cream", "banana_split", "nougat", "scoop_of_moon_mist_ice_cream", "moon_mist_gelato", "strawberry_gelato",
            "raspberry_bubble_gum", "blue_raspberry_bubble_gum", "blackberry_bubble_gum",
            "cough_syrup", "elderberry_gummy_bear", "elderberry_muffin", "fliederbeersuppe", "mate", "gin", "glass_of_gin", "martini", "earl_grey_tea");
        addFruitEffect(effects, MobEffectContent.CHERRY_BLOSSOMS_WEEPING,
            "cherries_cookie", "brandy", "colorful_fruit_parfait", "goofy_goober_sundae", "risalamande",
            "apricot_kernel_tofu", "cherries_pie", "pudding_a_la_mode", "glass_of_brandy",
            "heart_crush_paleta", "pina_colada", "watergate_salad", "blueberry_cherries_ice_cream",
            "mediterranean_sundae", "cherries_ice_cream", "melon_soda", "hurricane_cocktail", "grimace_shake", "banana_split",
            "sweet_berry_cake", "citrus_bavarois", "cherries_pie_slice", "vanilla_gelato", "bomb_paleta",
            "figgy_pudding", "mince_pie");
        addFruitEffect(effects, MobEffectContent.DARK_WRATH,
            "chorus_flower_jelly_cake", "chorus_fruit_juice", "chorus_flower_jelly_cake_slice",
            "mont_blanc", "kir_royal", "glass_of_creme_de_cassis", "creme_de_cassis",
            "blackcurrant_macaron", "gooseberry_fool", "cough_syrup", "elderberry_gummy_bear",
            "elderberry_muffin", "fliederbeersuppe");
        addFruitEffect(effects, MobEffectContent.GEM_EXCAVATION,
            "goofy_goober_sundae", "jewellery_box_salad", "halloween_parfait", "clover_club",
            "punch_bowl", "hurricane_cocktail", "eyeball_jelly", "shawarma", "pomegranate_juice",
            "pom_bloom_paleta");
        addFruitEffect(effects, MobEffectContent.HACKED_THORNS,
            "dragonbloom_paleta", "cosmic_dragon_salad", "durian_pizza_slice", "cooked_durian_flesh",
            "durian_mile_crepe_cake_slice", "durian_mile_crepe_cake", "durian_crisp");
        addFruitEffect(effects, MobEffectContent.HAPPY_AURA,
            "colorful_fruit_parfait", "goofy_goober_sundae", "banana_shake", "cooked_banana",
            "banana_chips", "banana_split", "chocolate_banana_pop", "fried_banana", "scoop_of_moon_mist_ice_cream", "moon_mist_gelato");
        addFruitEffect(effects, MobEffectContent.KIWING_WHEREABOUTS,
            "kiwifruit_paleta", "jewellery_box_salad", "pavlova_cake_slice", "pudding_a_la_mode",
            "kiwifruit_juice", "pavlova_cake", "fruit_sandwich", "primary_colors_salad");
        addFruitEffect(effects, MobEffectContent.LURKING_DANGER,
            "avocado_nigiri", "avocado_egg_bake", "avocado_shake", "taco");
        addFruitEffect(effects, MobEffectContent.MELON_GRAVITY,
            "melon_soda", "pudding_a_la_mode", "muskmelon_paleta", "melon_salad", "watermelon_juice",
            "watermelon_paleta");
        addFruitEffect(effects, MobEffectContent.MOMENTARY_METEOR,
            "starfruit_ice_cream", "starfruit_jalapeno_ice_cream", "glass_of_starfruit_wine",
            "cosmic_dragon_salad", "halloween_parfait", "starfruit_wine");
        addFruitEffect(effects, MobEffectContent.ORIGINAL_EVOLUTION,
            "mango_juice", "luau_paleta", "colorful_fruit_parfait", "frutas_con_chamoy",
            "yeung_zhi_gum_lo", "mango_pudding", "mangonada", "primary_colors_salad");
        addFruitEffect(effects, MobEffectContent.POISON_HEAL_MEDICINE,
            "dried_hawthorns", "suanmeitang", "hawthorn_flakes", "tanghulu", "hawpsicle_paleta",
            "snowflake_hawthorn_balls", "guilinggao", "hawthorn_rolls");
        addFruitEffect(effects, MobEffectContent.ROSA_HEDGE,
            "rosa_salad", "canton_bloom_soup", "pear_with_rock_sugar", "mince_pie", "baked_apple",
            "frutas_con_chamoy", "ringo_ame", "starfruit_wine", "pudding_a_la_mode", "taffy_apple",
            "apple_juice", "gummy_worm", "suanmeitang", "ochazuke", "glass_of_umeshu",
            "apricot_kernel_tofu", "dried_apricots", "umeboshis", "apricot_kernel", "umeshu",
            "plum_juice", "heart_crush_paleta", "prune", "fruit_flavored_kombucha",
            "bellini_base", "hakuto_tea", "fliederbeersuppe", "battenberg_cake");
        addFruitEffect(effects, MobEffectContent.RUTIN_LEMONENE,
            "buntan_ames", "lemon_cookie", "earl_grey_tea", "frutas_con_chamoy",
            "fruit_flavored_kombucha", "punch_bowl", "ota_ika", "hurricane_cocktail",
            "boxed_lemon_tea", "daiquiri", "russian_tea", "lemon_macaron", "madeleine",
            "lemon_donut", "citrus_bavarois", "colorful_fruit_parfait", "halloween_parfait",
            "apricot_kernel_tofu", "scoop_of_tiger_tail_ice_cream", "orange_muffin",
            "grapefruit_juice", "yeung_zhi_gum_lo", "marmalade", "blackcurrant_macaron",
            "orange_juice", "fruit_sandwich", "tiger_tail_gelato", "socata", "fliederbeersuppe", "elderberry_gummy_bear", "bomb_paleta", "clover_club", "mince_pie");
        addFruitEffect(effects, MobEffectContent.TRANSMUTATION_ELIXIR,
            "cough_syrup", "elderberry_gummy_bear", "elderberry_muffin", "fliederbeersuppe");
        addFruitEffect(effects, MobEffectContent.SEA_TOUCH,
            "mince_pie", "glass_of_white_wine", "brandy", "glass_of_starfruit_wine", "halloween_parfait",
            "figgy_pudding", "red_wine", "starfruit_wine", "white_wine", "glass_of_brandy", "kir_royal",
            "mediterranean_sundae", "champagne", "glass_of_champagne", "bellini_base", "raisins",
            "grape_paleta", "soul_cake", "grape_juice", "glass_of_red_wine",
            "muscat_grape_paleta", "canton_bloom_soup", "martini", "focaccia", "stuffed_olives", "scoop_of_moon_mist_ice_cream", "moon_mist_gelato");
        addFruitEffect(effects, MobEffectContent.SUMMER_HEATWAVE,
            "pavlova_cake", "fruit_flavored_kombucha", "pavlova_cake_slice", "hurricane_cocktail",
            "luau_paleta", "cooked_pineapple_slices", "apricot_kernel_tofu", "punch_bowl",
            "pineapple_pastries", "pineapple_paleta", "pina_colada", "watergate_salad",
            "hawaiian_pizza_slice", "pineapple_juice", "pineapple_pies", "colorful_fruit_parfait",
            "goofy_goober_sundae", "banana_shake", "cooked_banana", "banana_chips", "banana_split",
            "chocolate_banana_pop", "fried_banana", "mango_juice", "frutas_con_chamoy",
            "yeung_zhi_gum_lo", "mango_pudding", "mangonada", "primary_colors_salad", "lamingtons",
            "lavender_pon_de_ring", "coconut_milk", "ota_ika", "coconut_bun", "inkoco_paleta",
            "thai_tea", "cream_bun", "starfruit_ice_cream", "starfruit_jalapeno_ice_cream",
            "glass_of_starfruit_wine", "cosmic_dragon_salad", "halloween_parfait", "starfruit_wine",
            "dragonbloom_paleta", "durian_pizza_slice", "cooked_durian_flesh",
            "durian_mile_crepe_cake_slice", "durian_mile_crepe_cake", "durian_crisp", "scoop_of_moon_mist_ice_cream", "moon_mist_gelato");
        effects.replaceAll((name, values) -> List.copyOf(values));
        return Map.copyOf(effects);
    }

    private static void addFruitEffect(Map<String, List<RegistryObject<MobEffect>>> effects,
                                       RegistryObject<MobEffect> effect,
                                       String... names) {
        for (String name : names) {
            effects.computeIfAbsent(name, ignored -> new java.util.ArrayList<>()).add(effect);
        }
    }

    private static void addEffects(FoodProperties.Builder builder, String name) {
        if ("magic_rock_candy".equals(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 10_080, 1, false, true), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.LUCK, 10_080, 4, false, true), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10_080, 0, false, true), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10_080, 1, false, true), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10_080, 1, false, true), 1.0F);
        }
        if ("cough_syrup".equals(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 9_600, 0, false, true), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 9_600, 0, false, true), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 1_800, 0, false, true), 1.0F);
        }
        if (MENTHOL_AWAKENING_FOODS.contains(name)) {
            builder.effect(() -> new MobEffectInstance(
                MobEffectContent.MENTHOL_AWAKENING.get(), 1800, 0, false, true), 1.0F);
        }
        if (WINE_GLASSES.contains(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffectContent.TIPSY.get(), 1800, 1, false, true), 1.0F);
        } else if (WINE.contains(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffectContent.TIPSY.get(), 1800, 3, false, true), 1.0F);
        }
        if (STRONG_COFFEE.contains(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffectContent.INNER_FOCUS.get(), 9600, 5, false, true), 1.0F);
        } else if (WEAK_COFFEE.contains(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffectContent.INNER_FOCUS.get(), 4800, 1, false, true), 1.0F);
        } else if (COFFEE.contains(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffectContent.INNER_FOCUS.get(), 7200, 3, false, true), 1.0F);
        }
        if (PALETERIA.contains(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffectContent.ICE_BARRIER.get(), 7200, 3, false, true), 1.0F);
        }
        if (ICE_CREAM.contains(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffectContent.SNOW_VEIL.get(), 7200, 3, false, true), 1.0F);
        }
        if (TEA.contains(name)) {
            builder.effect(() -> new MobEffectInstance(
                MILK_TEA.contains(name) ? MobEffectContent.PURITY.get() : MobEffectContent.FRAGRANT.get(),
                1, 1, false, true), 1.0F);
        }
        if (NUT_LEVEL_TWO.contains(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffectContent.WALLNUT_FIRST_AID.get(), 7200, 1, false, true), 1.0F);
        } else if (NUT_LEVEL_ONE.contains(name)) {
            builder.effect(() -> new MobEffectInstance(MobEffectContent.WALLNUT_FIRST_AID.get(), 7200, 0, false, true), 1.0F);
        }
    }

    private static final Set<String> MENTHOL_AWAKENING_FOODS = Set.of(
        "scoop_of_mint_chocolate_ice_cream", "mint_chocolate_gelato", "tiramisu",
        "cough_syrup", "citrus_bavarois", "gooseberry_fool", "fliederbeersuppe", "chewing_gum",
        "mint_tea");
    private static final Set<String> WINE_GLASSES = Set.of(
        "glass_of_white_wine", "glass_of_starfruit_wine", "glass_of_vodka", "glass_of_sake",
        "glass_of_whiskey", "glass_of_umeshu", "glass_of_absinthe", "glass_of_brandy",
        "glass_of_beer", "glass_of_creme_de_cassis", "glass_of_champagne",
        "glass_of_red_wine", "glass_of_rum", "glass_of_gin", "daiquiri", "earl_grey_tea",
        "martini", "kir_royal", "eggnog", "clover_club", "bellini_base", "irish_coffee", "tiramisu", "russian_tea", "hurricane_cocktail", "pina_colada");
    private static final Set<String> WINE = Set.of(
        "brandy", "rum", "gin", "red_wine", "starfruit_wine", "absinthe", "white_wine", "beer",
        "sake", "creme_de_cassis", "vodka", "champagne", "whiskey", "umeshu");
    private static final Set<String> STRONG_COFFEE = Set.of(
        "espresso_con_panna", "espresso", "affogato", "pumpkin_spice_latte", "tiramisu", "opera_cake");
    private static final Set<String> WEAK_COFFEE = Set.of(
        "yuenyeung", "coffee_jelly", "latte", "cappuccino", "cafe_au_lait", "mocha_coffee",
        "americano", "lavender_latte", "irish_coffee");
    private static final Set<String> COFFEE = Set.of(
        "espresso_con_panna", "latte", "americano", "affogato", "lavender_latte", "caramel_macchiato",
        "flat_white", "yuenyeung", "egg_coffee", "cappuccino", "cafe_au_lait", "mocha_coffee",
        "espresso", "bulletproof_coffee", "coffee", "einspanner", "pumpkin_spice_latte",
        "coffee_jelly", "irish_coffee");
    private static final Set<String> PALETERIA = Set.of(
        "luau_paleta", "heart_crush_paleta", "inkoco_paleta", "pink_snowman_paleta",
        "pineapple_paleta", "grape_paleta", "bomb_paleta", "watermelon_paleta",
        "muskmelon_paleta", "muscat_grape_paleta", "sweet_berry_cake_slice", "goofy_goober_sundae",
        "halloween_parfait", "banana_shake", "pudding_a_la_mode", "grimace_shake",
        "mediterranean_sundae", "avocado_shake", "banana_split", "melon_soda", "sweet_berry_cake",
        "strawberry_shake", "scoop_of_strawberry_ice_cream", "scoop_of_chocolate_ice_cream",
        "scoop_of_tiger_tail_ice_cream", "scoop_of_ice_cream", "coffee_jelly", "affogato", "hawpsicle_paleta", "pom_bloom_paleta", "kiwifruit_paleta", "dragonbloom_paleta", "scoop_of_moon_mist_ice_cream", "scoop_of_mint_chocolate_ice_cream", "vanilla_gelato", "strawberry_gelato", "chocolate_gelato", "tiger_tail_gelato", "moon_mist_gelato", "mint_chocolate_gelato");
    private static final Set<String> ICE_CREAM = Set.of(
        "starfruit_ice_cream", "starfruit_jalapeno_ice_cream", "vanilla_chocolate_ice_cream",
        "jalapeno_ice_cream", "blueberry_cherries_ice_cream", "vanilla_ice_cream",
        "cherries_ice_cream", "blueberry_ice_cream", "chocolate_ice_cream", "colorful_fruit_parfait");
    private static final Set<String> TEA = Set.of(
        "kombucha", "green_tea", "black_tea", "earl_grey_tea", "bubble_tea", "suutei_tsai",
        "fruit_flavored_kombucha", "yuenyeung", "boxed_lemon_tea", "saffron_tea",
        "nata_de_coco_milk_tea", "thai_tea", "russian_tea", "po_cha", "jasmine_tea",
        "floral_kombucha", "hk_milk_tea", "matcha", "matcha_pon_de_ring", "matcha_cookie", "matcha_latte", "ochazuke", "masala_chai");
    private static final Set<String> MILK_TEA = Set.of(
        "yuenyeung", "bubble_tea", "suutei_tsai", "nata_de_coco_milk_tea", "thai_tea",
        "masala_chai", "hk_milk_tea");
    private static final Set<String> NUT_LEVEL_TWO = Set.of("watergate_salad", "baklava", "pistachio_macaron");
    private static final Set<String> NUT_LEVEL_ONE = Set.of(
        "opera_cake", "mont_blanc", "pedas", "risalamande", "kadayif", "kaju_katlis", "dubai_chocolate",
        "apricot_kernel_drink", "apricot_kernel_tofu", "raspberry_macaron", "chocolate_macaron",
        "gulab_jamuns", "taffy_apple", "lavender_macaron", "lemon_macaron", "madeleine", "wafer",
        "peanut_butter_cup", "kuri_manju", "blackcurrant_macaron", "canton_bloom_soup", "nougat", "battenberg_cake", "brownie");
}
