package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.content.building.ColoredDecorationContent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ModRegistries {
    private ModRegistries() {
    }

    public static void register(IEventBus modEventBus) {
        SoundRegistry.SOUND_EVENTS.register(modEventBus);
        ParticleTypeRegistry.PARTICLE_TYPES.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);
        EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        FeatureRegistry.FEATURES.register(modEventBus);
        TreeDecoratorRegistry.TREE_DECORATOR_TYPES.register(modEventBus);
        CreativeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        MenuRegistry.MENU_TYPES.register(modEventBus);
        PotionEffectRegistry.MOB_EFFECTS.register(modEventBus);
        FluidRegistry.FLUIDS.register(modEventBus);
        FluidRegistry.FLUID_TYPES.register(modEventBus);
        RecipeRegistry.RECIPE_TYPES.register(modEventBus);
        RecipeRegistry.RECIPE_SERIALIZERS.register(modEventBus);
        modEventBus.addListener(ColoredDecorationContent::registerCauldronInteractions);
    }
}
