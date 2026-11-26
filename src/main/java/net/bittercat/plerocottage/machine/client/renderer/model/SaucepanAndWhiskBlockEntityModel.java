package net.bittercat.plerocottage.machine.client.renderer.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blockentities.SaucepanAndWhiskBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class SaucepanAndWhiskBlockEntityModel extends GeoModel<SaucepanAndWhiskBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SaucepanAndWhiskBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "geo/saucepan_and_whisk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SaucepanAndWhiskBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/block/saucepan_and_whisk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SaucepanAndWhiskBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "animations/saucepan_and_whisk.animation.json");
    }

    @Override
    public RenderType getRenderType(SaucepanAndWhiskBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}
