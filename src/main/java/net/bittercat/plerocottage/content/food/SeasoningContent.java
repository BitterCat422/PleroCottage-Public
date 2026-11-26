package net.bittercat.plerocottage.content.food;

import java.util.List;

import net.bittercat.plerocottage.items.SeasoningItem;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.PROCESSED;

public final class SeasoningContent {

    public static final RegistryObject<Item> CAVIAR = register("caviar");
    public static final RegistryObject<Item> RICE_VINEGAR = register("rice_vinegar");
    public static final RegistryObject<Item> GANACHE = register("ganache");
    public static final RegistryObject<Item> ORANGE_BLOSSOM_WATER = register("orange_blossom_water");
    public static final RegistryObject<Item> ROSA_WATER = register("rosa_water");
    public static final RegistryObject<Item> CHAMOY = register("chamoy");
    public static final RegistryObject<Item> LAVENDER_ESSENTIAL_OIL = register("lavender_essential_oil");
    public static final RegistryObject<Item> JALAPENO_POWDER = register("jalapeno_powder");
    public static final RegistryObject<Item> MIXED_SPICE = register("mixed_spice");
    public static final RegistryObject<Item> CARAMEL_SYRUP = register("caramel_syrup");
    public static final RegistryObject<Item> CUSTARD = register("custard");
    public static final RegistryObject<Item> CONDENSED_MILK = register("condensed_milk");
    public static final RegistryObject<Item> BOTTLED_OLIVE_OIL = register("bottled_olive_oil");
    public static final RegistryObject<Item> GRENADINE = register("grenadine");
    public static final RegistryObject<Item> SALT_SHAKER = register("salt_shaker");

    private SeasoningContent() {
    }

    public static void bootstrap() {
        
    }

    private static RegistryObject<Item> register(String name) {
        return ItemRegistry.register(name, SeasoningItem::new);
    }
}
