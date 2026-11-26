package net.bittercat.plerocottage.content.food;

import net.bittercat.plerocottage.init.ItemRegistry;
import net.bittercat.plerocottage.items.DigestiveTabletsItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class DigestiveTabletsContent {
    public static final RegistryObject<Item> DIGESTIVE_TABLETS =
        ItemRegistry.register("digestive_tablets", DigestiveTabletsItem::new);

    private DigestiveTabletsContent() {
    }

    public static void bootstrap() {
    }
}
