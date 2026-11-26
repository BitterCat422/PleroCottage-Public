package net.bittercat.plerocottage.events;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.client.particle.FallingLeafParticle;
import net.bittercat.plerocottage.client.particle.MagicCircleParticle;
import net.bittercat.plerocottage.client.particle.MomentaryMeteorStarParticle;
import net.bittercat.plerocottage.client.renderer.model.StageFiveHatModels;
import net.bittercat.plerocottage.content.particles.ParticleContent;
import net.bittercat.plerocottage.content.plants.Flowers;
import net.bittercat.plerocottage.content.wood.WoodTypeContent;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import net.bittercat.plerocottage.client.gui.FurnitureScreen;
import net.bittercat.plerocottage.content.menus.MenuContent;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents {

    private ClientEvents() {
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        var colors = event.getBlockColors();
        for (String name : Flowers.petalNames()) {
            if (Flowers.petalStemTexture(name).equals("flower_petals_stem")) {
                event.register((state, level, pos, tintIndex) ->
                    colors.getColor(Blocks.PINK_PETALS.defaultBlockState(), level, pos, tintIndex),
                    Flowers.block(name).get());
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleContent.TRANSMUTATION_MAGIC_CIRCLE.get(),
            MagicCircleParticle.Provider::new);
        event.registerSpriteSet(ParticleContent.MOMENTARY_METEOR_STARS.get(),
            MomentaryMeteorStarParticle.Provider::new);
        for (RegistryObject<SimpleParticleType> particle : ParticleContent.leafParticles()) {
            event.registerSpriteSet(particle.get(), FallingLeafParticle.Provider::new);
        }
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(StageFiveHatModels.CHEF_HAT, StageFiveHatModels::createChefHat);
        event.registerLayerDefinition(StageFiveHatModels.CHRISTMAS_HAT,
            StageFiveHatModels::createChristmasHat);
        event.registerLayerDefinition(StageFiveHatModels.MINER_HAT, StageFiveHatModels::createMinerHat);
        event.registerLayerDefinition(StageFiveHatModels.PASTRY_CHEF_HAT,
            StageFiveHatModels::createPastryChefHat);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.<net.bittercat.plerocottage.menus.FurnitureMenu, FurnitureScreen>register(
                MenuContent.CABINET.get(),
                (menu, inventory, title) -> new FurnitureScreen(menu, inventory, title, false));
            MenuScreens.<net.bittercat.plerocottage.menus.FurnitureMenu, FurnitureScreen>register(
                MenuContent.DRAWER.get(),
                (menu, inventory, title) -> new FurnitureScreen(menu, inventory, title, true));

            for (WoodType woodType : WoodTypeContent.all()) {
                Sheets.addWoodType(woodType);
            }
            PleroCottage.LOGGER.debug("Registered {} wood types with the sign sheet.",
                WoodTypeContent.all().size());
        });
    }
}
