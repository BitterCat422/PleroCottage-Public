package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.content.food.DisplayModelResources;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public final class ConfiguredGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
    private final DisplayModelResources resources;

    public ConfiguredGeoModel(DisplayModelResources resources) {
        this.resources = resources;
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return resources.model();
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return resources.texture();
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return resources.animation();
    }
}
