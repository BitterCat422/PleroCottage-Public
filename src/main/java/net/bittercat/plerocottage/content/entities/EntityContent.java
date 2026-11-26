package net.bittercat.plerocottage.content.entities;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.entities.ModBoat;
import net.bittercat.plerocottage.entities.ModChestBoat;
import net.bittercat.plerocottage.entities.PoultryEntity;
import net.bittercat.plerocottage.entities.PoultryEggProjectile;
import net.bittercat.plerocottage.init.EntityTypeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class EntityContent {

    public static final RegistryObject<EntityType<ModBoat>> BOAT =
        EntityTypeRegistry.ENTITY_TYPES.register("boat",
        () -> EntityType.Builder.<ModBoat>of(ModBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
            .build(PleroCottage.MODID + ":boat"));

    public static final RegistryObject<EntityType<ModChestBoat>> CHEST_BOAT =
        EntityTypeRegistry.ENTITY_TYPES.register("chest_boat",
        () -> EntityType.Builder.<ModChestBoat>of(ModChestBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
            .build(PleroCottage.MODID + ":chest_boat"));

    public static final RegistryObject<EntityType<PoultryEntity>> MALLARD_DUCK = poultry("mallard_duck", 0.5F, 0.938F);
    public static final RegistryObject<EntityType<PoultryEntity>> TURKEY = poultry("turkey", 0.75F, 1.1F);
    public static final RegistryObject<EntityType<PoultryEggProjectile>> MALLARD_EGG_PROJECTILE = eggProjectile("thrown_mallard_duck_egg", "mallard");
    public static final RegistryObject<EntityType<PoultryEggProjectile>> TURKEY_EGG_PROJECTILE = eggProjectile("thrown_turkey_egg", "turkey");

    private static RegistryObject<EntityType<PoultryEntity>> poultry(String id, float width, float height) {
        return EntityTypeRegistry.ENTITY_TYPES.register(id, () -> EntityType.Builder.<PoultryEntity>of(
            (type, level) -> new PoultryEntity(type, level, id), MobCategory.CREATURE)
            .sized(width, height).clientTrackingRange(8).build(PleroCottage.MODID + ":" + id));
    }

    private static RegistryObject<EntityType<PoultryEggProjectile>> eggProjectile(String id, String variant) {
        return EntityTypeRegistry.ENTITY_TYPES.register(id, () -> EntityType.Builder.<PoultryEggProjectile>of(
            (type, level) -> new PoultryEggProjectile(type, level, variant), MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
            .build(PleroCottage.MODID + ":" + id));
    }

    private EntityContent() {
    }

    public static void bootstrap() {
    }
}
