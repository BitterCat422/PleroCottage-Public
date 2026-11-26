package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.client.renderer.BananaBlockRenderer;
import net.bittercat.plerocottage.client.renderer.ModBoatRenderer;
import net.bittercat.plerocottage.client.renderer.HalfOfCoconutRenderer;
import net.bittercat.plerocottage.client.renderer.YoungCoconutsRenderer;
import net.bittercat.plerocottage.client.renderer.StaticFoodDisplayRenderer;
import net.bittercat.plerocottage.client.renderer.BowlRenderer;
import net.bittercat.plerocottage.client.renderer.PlateBlockEntityRenderer;
import net.bittercat.plerocottage.client.renderer.HayNestBlockEntityRenderer;
import net.bittercat.plerocottage.client.renderer.EggIncubatorBlockEntityRenderer;
import net.bittercat.plerocottage.client.renderer.ColoredBedRenderer;
import net.bittercat.plerocottage.client.renderer.ColoredShulkerBoxRenderer;
import net.bittercat.plerocottage.client.renderer.model.BananaBunchGeoModel;
import net.bittercat.plerocottage.client.renderer.model.BananaCrownGeoModel;
import net.bittercat.plerocottage.client.renderer.model.BananaFrondGeoModel;
import net.bittercat.plerocottage.client.renderer.model.BananaFrondSaplingGeoModel;
import net.bittercat.plerocottage.client.renderer.model.BananaTreeBunchGeoModel;
import net.bittercat.plerocottage.client.renderer.PoultryRenderer;
import net.bittercat.plerocottage.content.blockentities.BlockEntityContent;
import net.bittercat.plerocottage.content.entities.EntityContent;
import net.bittercat.plerocottage.entities.PoultryEggProjectile;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class RendererRegistry {

    private RendererRegistry() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityContent.BOAT.get(), context -> new ModBoatRenderer(context, false));
        event.registerEntityRenderer(EntityContent.CHEST_BOAT.get(), context -> new ModBoatRenderer(context, true));
        event.registerEntityRenderer(EntityContent.MALLARD_DUCK.get(), PoultryRenderer::new);
        event.registerEntityRenderer(EntityContent.TURKEY.get(), PoultryRenderer::new);
        event.registerEntityRenderer(EntityContent.MALLARD_EGG_PROJECTILE.get(),
            context -> new ThrownItemRenderer<PoultryEggProjectile>(context));
        event.registerEntityRenderer(EntityContent.TURKEY_EGG_PROJECTILE.get(),
            context -> new ThrownItemRenderer<PoultryEggProjectile>(context));

        event.registerBlockEntityRenderer(BlockEntityContent.SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityContent.HANGING_SIGN.get(), HangingSignRenderer::new);

        event.registerBlockEntityRenderer(BlockEntityContent.YOUNG_COCONUTS.get(),
            context -> new YoungCoconutsRenderer());
        event.registerBlockEntityRenderer(BlockEntityContent.HALF_OF_COCONUT_BLOCK.get(),
            context -> new HalfOfCoconutRenderer());
        event.registerBlockEntityRenderer(BlockEntityContent.STATIC_FOOD_DISPLAY.get(),
            context -> new StaticFoodDisplayRenderer());
        event.registerBlockEntityRenderer(BlockEntityContent.BOWL.get(),
            context -> new BowlRenderer()::renderWithItems);
        event.registerBlockEntityRenderer(BlockEntityContent.PLATE.get(), PlateBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityContent.HAY_NEST.get(), HayNestBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityContent.EGG_INCUBATOR.get(), EggIncubatorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityContent.COLORED_BED.get(), ColoredBedRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityContent.COLORED_SHULKER_BOX.get(),
            ColoredShulkerBoxRenderer::new);

        event.registerBlockEntityRenderer(BlockEntityContent.BANANA_FROND.get(),
            context -> new BananaBlockRenderer(new BananaFrondGeoModel()));
        event.registerBlockEntityRenderer(BlockEntityContent.BANANA_CROWN.get(),
            context -> new BananaBlockRenderer(new BananaCrownGeoModel()));
        event.registerBlockEntityRenderer(BlockEntityContent.BANANA_SAPLING.get(),
            context -> new BananaBlockRenderer(new BananaFrondSaplingGeoModel()));
        event.registerBlockEntityRenderer(BlockEntityContent.BANANA_TREE_BUNCH.get(),
            context -> new BananaBlockRenderer(new BananaTreeBunchGeoModel()));
        event.registerBlockEntityRenderer(BlockEntityContent.BANANA_BUNCH.get(),
            context -> new BananaBlockRenderer(new BananaBunchGeoModel()));
    }

}
