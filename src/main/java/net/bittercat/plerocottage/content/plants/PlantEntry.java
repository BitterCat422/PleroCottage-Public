package net.bittercat.plerocottage.content.plants;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item;

public interface PlantEntry {

    String name();

    Item fruitItem();

    default boolean isFruit() {
        return true;
    }

    @Nullable
    default String fruitTagName() {
        return isFruit() ? name() : null;
    }

    @Nullable
    default Item saplingItem() {
        return null;
    }

    default float compostChance() {
        return 0.3F;
    }

    default Map<Item, Float> compostables() {
        return Map.of(fruitItem(), compostChance());
    }
}
