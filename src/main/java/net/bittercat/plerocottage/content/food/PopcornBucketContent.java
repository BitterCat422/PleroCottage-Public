package net.bittercat.plerocottage.content.food;

import java.util.List;

import net.bittercat.plerocottage.items.PopcornBucketItem;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import static net.bittercat.plerocottage.init.CreativeTabRegistry.Tab.PROCESSED;

public final class PopcornBucketContent {

    public static final RegistryObject<Item> POPCORN_BUCKET =
        ItemRegistry.register("popcorn_bucket",
            () -> new PopcornBucketItem(FoodDefinition.get("popcorn_bucket")));
    public static final RegistryObject<Item> SALTED_POPCORN_BUCKET =
        ItemRegistry.register("salted_popcorn_bucket",
            () -> new PopcornBucketItem(FoodDefinition.get("salted_popcorn_bucket")));

    private PopcornBucketContent() {
    }

    public static void bootstrap() {
        
    }
}
