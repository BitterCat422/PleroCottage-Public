package net.bittercat.plerocottage.content.entities;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blocks.PoultryEggBlock;
import net.bittercat.plerocottage.init.BlockRegistry;
import net.bittercat.plerocottage.init.ItemRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.bittercat.plerocottage.entities.PoultryEggProjectile;
import net.bittercat.plerocottage.items.PoultryEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

public final class PoultryContent {
    public static final RegistryObject<Item> MALLARD_DUCK_SPAWN_EGG =
        spawnEgg("mallard_duck_spawn_egg", EntityContent.MALLARD_DUCK, 0x51714F, 0xD6A22B);
    public static final RegistryObject<Item> TURKEY_SPAWN_EGG =
        spawnEgg("turkey_spawn_egg", EntityContent.TURKEY, 0x907353, 0xA64B42);

    public static final RegistryObject<Block> MALLARD_EGG_BLOCK = eggBlock("mallard_duck_egg_block", 0x72915F, false);
    public static final RegistryObject<Block> TURKEY_EGG_BLOCK = eggBlock("turkey_egg_block", 0xA17B54, true);
    public static final RegistryObject<Block> EGG_DISPLAY_BLOCK = BlockRegistry.register("egg_display",
        () -> new PoultryEggBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN)
            .strength(0.5F).sound(SoundType.METAL).noOcclusion(), false));

    public static final RegistryObject<Item> MALLARD_EGG = projectileEgg("mallard_duck_egg", "mallard",
        EntityContent.MALLARD_EGG_PROJECTILE, MALLARD_EGG_BLOCK);
    public static final RegistryObject<Item> TURKEY_EGG = projectileEgg("turkey_egg", "turkey",
        EntityContent.TURKEY_EGG_PROJECTILE, TURKEY_EGG_BLOCK);

    private static boolean bootstrapped;

    private PoultryContent() {
    }

    public static void bootstrap() {
        if (bootstrapped) return;
        bootstrapped = true;

    }

    private static RegistryObject<Item> spawnEgg(String id, RegistryObject<? extends EntityType<? extends Mob>> type,
                                                   int background, int spots) {
        return ItemRegistry.register(id,
            () -> new ForgeSpawnEggItem(type, background, spots, new Item.Properties()));
    }

    private static RegistryObject<Item> projectileEgg(String id, String variant,
                                                      RegistryObject<net.minecraft.world.entity.EntityType<PoultryEggProjectile>> type,
                                                      RegistryObject<Block> block) {
        return ItemRegistry.register(id, () -> new PoultryEggItem(variant, type, block));
    }

    private static RegistryObject<Block> eggBlock(String id, int color, boolean turkeyShape) {
        RegistryObject<Block> block = BlockRegistry.register(id, () -> new PoultryEggBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.5F)
                .sound(SoundType.METAL).noOcclusion(), turkeyShape));
        return block;
    }
}
