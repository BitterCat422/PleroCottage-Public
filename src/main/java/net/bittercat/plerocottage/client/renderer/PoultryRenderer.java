package net.bittercat.plerocottage.client.renderer;

import net.bittercat.plerocottage.entities.PoultryEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public final class PoultryRenderer extends GeoEntityRenderer<PoultryEntity> {
    public PoultryRenderer(EntityRendererProvider.Context context) {
        super(context, new GeoModel<>() {
            @Override
            public ResourceLocation getModelResource(PoultryEntity entity) {
                return new ResourceLocation("plerocottage", "geo/"
                    + (entity.isBaby() ? "baby_" : "") + entity.species() + ".geo.json");
            }

            @Override
            public ResourceLocation getTextureResource(PoultryEntity entity) {
                return new ResourceLocation("plerocottage", "textures/entities/" + entity.textureName() + ".png");
            }

            @Override
            public ResourceLocation getAnimationResource(PoultryEntity entity) {
                return new ResourceLocation("plerocottage", "animations/"
                    + (entity.isBaby() ? "baby_" : "") + entity.species() + ".animation.json");
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(PoultryEntity entity) {
        return new ResourceLocation("plerocottage", "textures/entities/" + entity.textureName() + ".png");
    }
}
