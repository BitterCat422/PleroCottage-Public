package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.worldgen.GroundPetalsDecorator;
import net.bittercat.plerocottage.worldgen.HangingWildVineDecorator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class TreeDecoratorRegistry {
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPES =
        DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, PleroCottage.MODID);

    public static final RegistryObject<TreeDecoratorType<GroundPetalsDecorator>> GROUND_PETALS =
        TREE_DECORATOR_TYPES.register("ground_petals", () -> new TreeDecoratorType<>(GroundPetalsDecorator.CODEC));

    public static final RegistryObject<TreeDecoratorType<HangingWildVineDecorator>> HANGING_WILD_VINE =
        TREE_DECORATOR_TYPES.register("hanging_wild_vine",
            () -> new TreeDecoratorType<>(HangingWildVineDecorator.CODEC));

    private TreeDecoratorRegistry() {
    }
}
