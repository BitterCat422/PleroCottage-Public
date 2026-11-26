package net.bittercat.plerocottage.content.items;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.BUILDING;
import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.MAIN;

import java.util.Map;

import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class DyeContent {
    private static final Map<String, RegistryObject<Item>> DYES = Map.of(
        "snow", item("snow_dye"),
        "lavender", item("lavender_dye"),
        "turquoise", item("turquoise_dye"));

    private DyeContent() {
    }

    private static RegistryObject<Item> item(String id) {
        return ItemRegistry.register(id, () -> new Item(new Item.Properties()));
    }

    public static void bootstrap() {
        for (RegistryObject<Item> dye : DYES.values()) {

        }
    }

    public static RegistryObject<Item> dye(String color) {
        RegistryObject<Item> dye = DYES.get(color);
        if (dye == null) {
            throw new IllegalArgumentException("未知染料颜色：" + color);
        }
        return dye;
    }

    public static Map<String, RegistryObject<Item>> all() {
        return DYES;
    }
}
