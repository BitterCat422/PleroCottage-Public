package net.bittercat.plerocottage.content.food;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.bittercat.plerocottage.items.BlockFoodItem;
import net.bittercat.plerocottage.items.ConsumableItem;
import net.bittercat.plerocottage.items.DrinkRemainderConsumableItem;
import net.bittercat.plerocottage.items.GoldenAppleSliceItem;
import net.bittercat.plerocottage.items.GrapeTwinPaleteriaItem;
import net.bittercat.plerocottage.content.plants.Coconuts;
import net.bittercat.plerocottage.content.plants.PlantCatalog;
import net.bittercat.plerocottage.content.blocks.MarbleContent;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.init.CreativeTabRegistry;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public final class FoodContent {

    private FoodContent() {
    }

    // 物品提示每帧都会按名字查，逐个比对全部注册项太慢；只缓存查到的结果
    private static final Map<String, RegistryObject<Item>> BY_NAME = new ConcurrentHashMap<>();

    public static RegistryObject<Item> item(String name) {
        RegistryObject<Item> cached = BY_NAME.get(name);
        if (cached != null) {
            return cached;
        }
        RegistryObject<Item> found = ItemRegistry.ITEMS.getEntries().stream()
            .filter(entry -> entry.getId().getPath().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("找不到食物物品：" + name));
        BY_NAME.put(name, found);
        return found;
    }

    public static RegistryObject<Item> registerPlantFood(String name,
                                                         Supplier<? extends Item> factory) {
        return ItemRegistry.register(name, factory);
    }

    public static void bootstrap() {
        registerPendingFoods();
        PlantCatalog.addCompostable(() -> item("pumpkin_slice").get(), 0.5F);

        registerFoods(CreativeTabRegistry.Tab.NATURAL, "ice_cubes");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "abalone", "fugu_slice", "mentaiko", "raw_cod_slice", "raw_prawn", "raw_salmon_slice", "shirako", "tarako", "tropical_fish_slice");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "pumpkin_slice");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "durian_flesh");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "coffee_beans", "dried_jalapeno");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "half_of_avocado", "half_of_kiwifruit");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "half_of_gold_kiwifruit");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "cooked_durian_flesh");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "dried_hawthorns");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "cooked_prawn");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "cooked_abalone");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "sunny_side_up_egg");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "cooked_banana", "prune");
        registerFoods(CreativeTabRegistry.Tab.NATURAL, "cooked_sunflower_seeds");

        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "suspicious_mold");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "rainbow_sprinkles");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "meringue");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "nori");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "scoop_of_strawberry_ice_cream", "scoop_of_chocolate_ice_cream", "scoop_of_ice_cream", "scoop_of_tiger_tail_ice_cream", "scoop_of_moon_mist_ice_cream", "scoop_of_mint_chocolate_ice_cream");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "chocolate_chips", "dark_chocolate_chips", "white_chocolate_chips");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "surimi_crab_stick");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "bontan_ame", "mochi", "pickle_slices");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "raw_beef_patty");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "swiss_roll_slice", "waffle");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "corn_chips");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cheddar_cheese_wedge", "emmental_cheese_wedge", "mozzarella_cheese_wedge");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "sausage");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "blueberry_cookie", "cherries_cookie", "cranberry_cookie", "lemon_cookie", "matcha_cookie");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "dried_apricots", "jalebi", "pistachio_macaron", "rainbow_cookie", "snowflake_hawthorn_balls");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "raspberry_bubble_gum", "blue_raspberry_bubble_gum", "blackberry_bubble_gum");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "chewing_gum");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "spam");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "blackcurrant_macaron", "chocolate_macaron", "cream_puff", "gulab_jamun", "kuri_manju", "lavender_macaron", "lemon_macaron", "pastel_de_nata", "sachima", "raspberry_macaron", "loaf_slice", "stuffed_olives");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "kappa_maki", "tekka_maki");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cream_cheese");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "licorice_candy");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "bun", "cooked_mochi", "cornbread", "eclair", "naan", "nama_chocolate", "pretzel", "savoiardi", "soul_cake", "umeboshi");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cupcake_qaq");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cod_nigiri", "fugu_nigiri", "mentaiko_nigiri", "salmon_nigiri", "tropical_fish_nigiri");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "marshmallows");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "butter_cookie", "chocolate", "dark_chocolate", "guilinggao", "hawthorn_flakes", "pickle", "white_chocolate");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "blueberry_cherries_ice_cream", "blueberry_ice_cream", "cherries_ice_cream", "chocolate_ice_cream", "starfruit_ice_cream", "starfruit_jalapeno_ice_cream", "vanilla_chocolate_ice_cream", "vanilla_ice_cream");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "jalapeno_ice_cream");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "vanilla_gelato", "strawberry_gelato", "chocolate_gelato", "tiger_tail_gelato", "moon_mist_gelato", "mint_chocolate_gelato");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "durian_crisp");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cheesy_mushroom_bacon_pizza_slice", "cinnamon_roll", "durian_pizza_slice", "graham_cracker", "hawaiian_pizza_slice", "kadayif", "kaju_katli", "lamingtons", "madeleine", "mont_blanc", "nata_de_coco", "nougat", "pepperoni_pizza_slice", "sakura_mochi", "seafood_pizza_slice", "supreme_pizza_slice", "vanilla_pizza_slice");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "california_roll", "lavender_pon_de_ring", "matcha_pon_de_ring", "mince_pie", "pineberry_pon_de_ring", "pon_de_ring", "shirako_gunkan");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "fried_tempura");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "fried_abalone");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "tanghulu");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "pineapple_pies");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "checkerboard_cookie", "latiya", "melon_pan", "onigiri", "scone");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "avocado_nigiri", "donut", "fries", "ikura_gunkan", "surimi_crab_stick_nigiri", "you_tiao");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "elderberry_flowers_fritters");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "fried_fish");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "avocado_egg_bake", "bagel", "baked_apple", "bo_lo_yau", "caneles", "choco_cornet", "cream_bun", "croissant", "battenberg_cake", "brownie", "fruit_sandwich", "peanut_butter_cup");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "hot_dog");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "abalone_nigiri", "fried_banana", "shrimp_nigiri", "wagyu_with_caviar_nigiri");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "fried_chicken");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cooked_onigiri");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "senbei");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cooked_beef_patty");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "hawthorn_rolls");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "swiss_roll");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "chocolate_donut", "lemon_donut", "snow_donut", "strawberry_donut", "tostada_shell", "white_chocolate_donut");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "dubai_chocolate", "focaccia", "napoleon_pastry", "sausage_bun", "taco");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "spam_musubi");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "fried_porkchop", "fried_steak");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "tempura_nigiri");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "baklava");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "s_mores");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "shawarma");

        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "ice_cream_cone", "raisins", "fortune_cookie");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "wafer");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "fried_drumstick", "popcorn_chicken");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "pineapple_slices");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "pineapple_pastries");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "durian_mile_crepe_cake_slice", "cucumber_slices", "rainbow_chocolate_candies");
        registerPieSlice("cherries_pie_slice", "cherries_pie");
        registerPieSlice("truffle_pie_slice", "truffle_pie");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "caramel_chocolate_cake_slice", "sweet_berry_cake_slice", "chorus_flower_jelly_cake_slice", "nether_wart_soul_cake_slice", "vanilla_cake_slice", "pavlova_cake_slice", "strawberry_shortcake_slice");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cooked_pineapple_slices");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "emmental_cheese_slice", "cheddar_cheese_slice", "mozzarella_cheese_slice");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "popcorn");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "banana_chips", "peda");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "apricot_kernel", "green_bell_pepper_slices");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "loaf");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "raw_chicken_drumstick", "raw_chicken_breast", "raw_chicken_wing", "raw_duck_drumstick", "raw_duck_wing", "raw_duck_breast");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cooked_chicken_drumstick", "cooked_chicken_breast", "cooked_chicken_wing", "cooked_duck_drumstick", "cooked_duck_wing", "cooked_duck_breast");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "sumeshi");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "raw_beef_cubes", "raw_mutton_ribs", "raw_pork_bellies", "spam_slice");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cooked_beef_cubes", "cooked_mutton_ribs", "bacon", "cooked_spam_slice");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "carrot_slices");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "potato_slices");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "beetroot_slice");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "jalapeno_slices", "wild_jalapeno_slices", "pimento_slices", "end_pepper_slices");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "candy_corn");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "gummy_worm");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "eyeball_jelly");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "raw_turkey_drumstick");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "raw_mallard_duck");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cooked_mallard_duck");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "raw_turkey");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "cooked_turkey");
        registerFoods(CreativeTabRegistry.Tab.PROCESSED, "elderberry_gummy_bear", "konpeito");
        registerRemainderFoods("bomb_paleta");
        registerRemainderFoods("luau_paleta");
        registerRemainderFoods("watermelon_paleta");
        registerRemainderFoods("pink_snowman_paleta");
        registerRemainderFoods("heart_crush_paleta");
        registerRemainderFoods("pineapple_paleta");
        registerRemainderFoods("muskmelon_paleta");
        registerRemainderFoods("hanami_dango");
        registerRemainderFoods("kiwifruit_paleta");
        registerRemainderFoods("pom_bloom_paleta");
        registerRemainderFoods("hawpsicle_paleta");
        registerRemainderFoods("dragonbloom_paleta");
        registerRemainderFoods("chocolate_banana_pop");
        registerRemainderFoods("grape_paleta");
        registerRemainderFoods("muscat_grape_paleta");
        RegistryObject<Item> grapeTwinPaleteria = ItemRegistry.register("grape_twin_paleta",
            () -> new GrapeTwinPaleteriaItem(
                () -> item("grape_paleta").get(),
                () -> item("muscat_grape_paleta").get()));
        
        registerRemainderFoods("inkoco_paleta");
        registerRemainderFoods("ringo_ame");
        registerRemainderFoods("marshmallow_skewer");
        registerRemainderFoods("campfire_marshmallow");
        registerRemainderFoods("taffy_apple");
        registerFoods(null, "raw_turkey_breast", "raw_turkey_wing");
        registerFoods(null, "cooked_turkey_breast", "cooked_turkey_drumstick", "cooked_turkey_wing");
        registerFoods(null, "boxed_chocolate_milk", "boxed_lemon_tea");
        registerFoods(null, "boxed_whipping_cream");
    }

    private static void registerPendingFoods() {
        FoodDefinition cocaCola = FoodDefinition.get("coca_cola");
        registerFood("coca_cola", () -> new ConsumableItem(cocaCola.itemProperties(),
            cocaCola.useDuration(), cocaCola.useAnim(), null, false, true));
        registerDrinkRemainder(FoodDefinition.get("boxed_milk"),
            () -> Items.GLASS_BOTTLE, true);
        registerDrinkRemainder(FoodDefinition.get("yogurt"),
            () -> Items.GLASS_BOTTLE, false);
        registerDrinkRemainder(FoodDefinition.get("bottled_water"),
            () -> Items.GLASS_BOTTLE, false);
        registerDrinkRemainder(FoodDefinition.get("cough_syrup"),
            () -> Items.GLASS_BOTTLE, false);
        FoodDefinition magicRockCandy = FoodDefinition.get("magic_rock_candy");
        registerFood("magic_rock_candy", () -> new ConsumableItem(magicRockCandy.itemProperties(),
            magicRockCandy.useDuration(), magicRockCandy.useAnim(),
            () -> Items.STICK, false, false));
        FoodDefinition goldenAppleSlice = FoodDefinition.get("golden_apple_slice");
        registerFood(CreativeTabRegistry.Tab.NATURAL, goldenAppleSlice.name(),
            () -> new GoldenAppleSliceItem(goldenAppleSlice.itemProperties(), false));
        FoodDefinition enchantedGoldenAppleSlice = FoodDefinition.get("enchanted_golden_apple_slice");
        registerFood(CreativeTabRegistry.Tab.NATURAL, enchantedGoldenAppleSlice.name(),
            () -> new GoldenAppleSliceItem(enchantedGoldenAppleSlice.itemProperties(), true));
        FoodDefinition tomatoSlice = FoodDefinition.get("tomato_slice");
        registerFood(CreativeTabRegistry.Tab.NATURAL, tomatoSlice.name(),
            () -> new ConsumableItem(tomatoSlice.itemProperties(), tomatoSlice.useDuration(),
                tomatoSlice.useAnim(), null, false, false));
        FoodDefinition cookedRice = FoodDefinition.get("cooked_rice");
        registerFood(cookedRice.name(), () -> new ConsumableItem(cookedRice.itemProperties(),
            cookedRice.useDuration(), cookedRice.useAnim(), () -> MarbleContent.BOWL.get().asItem(), true, false));
        FoodDefinition wrappedCandy = FoodDefinition.get("wrapped_candy");
        registerFood(wrappedCandy.name(), () -> new ConsumableItem(wrappedCandy.itemProperties(),
            wrappedCandy.useDuration(), wrappedCandy.useAnim(), () -> Items.PAPER, false, false));
    }

    private static void registerDrinkRemainder(FoodDefinition definition,
                                              Supplier<? extends Item> remainder, boolean clearsEffects) {
        registerFood(definition.name(), () -> new DrinkRemainderConsumableItem(
            definition.itemProperties(), definition.useDuration(), remainder, clearsEffects));
    }

    private static void registerFood(String name, java.util.function.Supplier<? extends Item> factory) {
        registerFood(CreativeTabRegistry.Tab.PROCESSED, name, factory);
    }

    private static void registerFood(CreativeTabRegistry.Tab tab, String name,
                                     java.util.function.Supplier<? extends Item> factory) {
        RegistryObject<Item> item = ItemRegistry.register(name, factory);
        
    }

    private static void registerPieSlice(String name, String pieName) {
        registerFood(CreativeTabRegistry.Tab.PROCESSED, name, () -> {
            Block pie = BlockRegistry.BLOCKS.getEntries().stream()
                .filter(entry -> entry.getId().getPath().equals(pieName))
                .findFirst().orElseThrow().get();
            FoodDefinition definition = FoodDefinition.get(name);
            return new BlockFoodItem(pie, definition.itemProperties(), definition.useDuration(),
                true, definition.useAnim(), null, false, true);
        });
    }

    public static FoodProperties foodProperties(String name, int nutrition, float saturation,
                                                 boolean meat, boolean alwaysEat) {
        return FoodDefinition.properties(name, nutrition, saturation, meat, alwaysEat);
    }

    private static void registerFoods(CreativeTabRegistry.Tab tab, String... names) {
        for (String name : names) {
            FoodDefinition definition = FoodDefinition.get(name);
            registerFood(name, () -> new ConsumableItem(
                definition.itemProperties(), definition.useDuration(), definition.useAnim(),
                null, false, false));
        }
    }

    private static void registerRemainderFoods(String... names) {
        for (String name : names) {
            FoodDefinition definition = FoodDefinition.get(name);
            registerFood(name, () -> new ConsumableItem(definition.itemProperties(),
                definition.useDuration(), definition.useAnim(), () -> Items.STICK, true, false));
        }
    }

}

