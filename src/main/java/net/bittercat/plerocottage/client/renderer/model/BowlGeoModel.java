package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.blockentities.BowlBlockEntity;
import net.bittercat.plerocottage.content.food.DisplayModelResources;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public final class BowlGeoModel extends GeoModel<BowlBlockEntity> {
    @Override
    public ResourceLocation getModelResource(BowlBlockEntity animatable) {
        return DisplayModelResources.BOWL.model();
    }

    @Override
    public ResourceLocation getTextureResource(BowlBlockEntity animatable) {
        return DisplayModelResources.BOWL.texture();
    }

    @Override
    public ResourceLocation getAnimationResource(BowlBlockEntity animatable) {
        return DisplayModelResources.BOWL.animation();
    }
}
