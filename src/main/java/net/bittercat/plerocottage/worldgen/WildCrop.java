package net.bittercat.plerocottage.worldgen;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;

public record WildCrop(String name, Supplier<? extends Block> block, int patchTries, int xzSpread, int ySpread,
                       @Nullable WildGround ground, @Nullable IntProvider age, int count, int rarity,
                       Heightmap.Types heightmap) {

    public static WildCrop crop(String name, Supplier<? extends Block> block, IntProvider age, WildGround ground,
                                int patchTries, int xzSpread, int ySpread, int rarity, Heightmap.Types heightmap) {
        return new WildCrop(name, block, patchTries, xzSpread, ySpread, ground, age, 0, rarity, heightmap);
    }

    public static WildCrop plant(String name, Supplier<? extends Block> block, int patchTries, int count,
                                 int rarity, Heightmap.Types heightmap) {
        return new WildCrop(name, block, patchTries, 0, 0, null, null, count, rarity, heightmap);
    }

    public static WildCrop plant(String name, Supplier<? extends Block> block, int patchTries, int xzSpread,
                                 int ySpread, int count, int rarity, Heightmap.Types heightmap) {
        return new WildCrop(name, block, patchTries, xzSpread, ySpread, null, null, count, rarity, heightmap);
    }

    public static WildCrop fruit(String name, Supplier<? extends Block> block, WildGround ground,
                                 int patchTries, int xzSpread, int ySpread, int rarity,
                                 Heightmap.Types heightmap) {
        return new WildCrop(name, block, patchTries, xzSpread, ySpread, ground, null, 0, rarity, heightmap);
    }
}
