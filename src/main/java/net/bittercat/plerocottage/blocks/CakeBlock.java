package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;

public class CakeBlock extends PortionFoodBlock {
    public CakeBlock(Supplier<? extends Item> sliceItem) {
        super(sliceItem);
    }
}
