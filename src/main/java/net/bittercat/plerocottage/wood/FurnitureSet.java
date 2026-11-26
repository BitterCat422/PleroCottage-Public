package net.bittercat.plerocottage.wood;

import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record FurnitureSet(
    String name,
    RegistryObject<Block> cabinet,
    RegistryObject<Item> cabinetItem,
    RegistryObject<Block> drawer,
    RegistryObject<Item> drawerItem
) {
}
