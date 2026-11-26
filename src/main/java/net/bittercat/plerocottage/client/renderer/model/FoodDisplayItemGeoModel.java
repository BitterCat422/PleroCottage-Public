package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.content.food.DisplayModelResources;
import net.bittercat.plerocottage.items.GeckoDisplayFoodItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public final class FoodDisplayItemGeoModel extends GeoModel<GeckoDisplayFoodItem> {
    private final DisplayModelResources resources;

    public FoodDisplayItemGeoModel(DisplayModelResources resources) {
        this.resources = resources;
    }

    @Override
    public ResourceLocation getModelResource(GeckoDisplayFoodItem animatable) {
        return resources.model();
    }

    @Override
    public ResourceLocation getTextureResource(GeckoDisplayFoodItem animatable) {
        return resources.texture();
    }

    @Override
    public ResourceLocation getAnimationResource(GeckoDisplayFoodItem animatable) {
        return resources.animation();
    }
}
