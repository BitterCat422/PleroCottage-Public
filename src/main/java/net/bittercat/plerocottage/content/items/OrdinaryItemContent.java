package net.bittercat.plerocottage.content.items;

import java.util.List;

import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.NATURAL;
import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.MAIN;
import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.PROCESSED;

public final class OrdinaryItemContent {

    public static final RegistryObject<Item> BRILLIANT_DIAMOND = register("brilliant_diamond");
    public static final RegistryObject<Item> GARNET = register("garnet");
    public static final RegistryObject<Item> PEARL_ROCK = register("pearl_rock");
    public static final RegistryObject<Item> PINK_CRYSTAL = register("pink_crystal");
    public static final RegistryObject<Item> TOPAZ = register("topaz");
    public static final RegistryObject<Item> DUCK_DOWN = register("duck_down");
    public static final RegistryObject<Item> TURKEY_FEATHER = register("turkey_feather");
    public static final RegistryObject<Item> GREEN_TEA_LEAVES = register("green_tea_leaves");
    public static final RegistryObject<Item> BLACK_TEA_LEAVES = register("black_tea_leaves");
    public static final RegistryObject<Item> IKURA = register("ikura");
    public static final RegistryObject<Item> RAW_RICE = register("raw_rice");
    public static final RegistryObject<Item> SUPER_ANCIENT_MUSHROOM = register("super_ancient_mushroom");
    public static final RegistryObject<Item> TROPICAL_FISH_ROE = register("tropical_fish_roe");
    public static final RegistryObject<Item> CINNAMON = register("cinnamon");
    public static final RegistryObject<Item> CLOVE = register("cloves");
    public static final RegistryObject<Item> BAY_LEAVES = register("bay_leaves");
    public static final RegistryObject<Item> CREATIVITY_MECHANISM = register("creativity_mechanism");
    public static final RegistryObject<Item> BROWN_SUGAR = register("brown_sugar");
    public static final RegistryObject<Item> BUTTER = register("butter");
    public static final RegistryObject<Item> COCOA_POWDER = register("cocoa_powder");
    public static final RegistryObject<Item> COCOA_BUTTER = register("cocoa_butter");
    public static final RegistryObject<Item> APRICOT_KERNEL_POWDER = register("apricot_kernel_powder");
    public static final RegistryObject<Item> ALMOND_POWDER = register("almond_powder");
    public static final RegistryObject<Item> COFFEE_POWDER = register("coffee_powder");
    public static final RegistryObject<Item> CINNAMON_POWDER = register("cinnamon_powder");
    public static final RegistryObject<Item> SAGO = register("sago");
    public static final RegistryObject<Item> BOBA = register("boba");
    public static final RegistryObject<Item> FLOUR = register("flour");
    public static final RegistryObject<Item> CORN_FLOUR = register("corn_flour");
    public static final RegistryObject<Item> DOUGH = register("dough");
    public static final RegistryObject<Item> CORN_DOUGH = register("corn_dough");
    public static final RegistryObject<Item> SWEET_DOUGH = register("sweet_dough");
    public static final RegistryObject<Item> MATCHA_POWDER = register("matcha_powder");

    private OrdinaryItemContent() {
    }

    public static void bootstrap() {

    }

    private static RegistryObject<Item> register(String name) {
        return ItemRegistry.register(name);
    }
}
