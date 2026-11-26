package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;

public class CheeseWheelBlock extends PortionFoodBlock {
    public CheeseWheelBlock(Supplier<? extends Item> wedgeItem) {
        super(wedgeItem);
    }

    @Override
    protected int portionHeight() {
        return 6;
    }
}
