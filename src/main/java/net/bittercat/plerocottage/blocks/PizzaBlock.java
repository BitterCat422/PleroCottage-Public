package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;

public class PizzaBlock extends PortionFoodBlock {
    public PizzaBlock(Supplier<? extends Item> sliceItem) {
        super(sliceItem);
    }

    @Override
    protected int portionHeight() {
        return 2;
    }
}
