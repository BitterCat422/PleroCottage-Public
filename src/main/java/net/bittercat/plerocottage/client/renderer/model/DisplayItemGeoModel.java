package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.items.DisplayBlockItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DisplayItemGeoModel extends GeoModel<DisplayBlockItem> {

    private final ResourceLocation model;
    private final ResourceLocation animation;
    private final ResourceLocation texture;

    public DisplayItemGeoModel(ResourceLocation model, ResourceLocation animation, ResourceLocation texture) {
        this.model = model;
        this.animation = animation;
        this.texture = texture;
    }

    @Override
    public ResourceLocation getModelResource(DisplayBlockItem animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(DisplayBlockItem animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(DisplayBlockItem animatable) {
        return animation;
    }
}
