package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blockentities.BananaBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BananaBunchGeoModel extends GeoModel<BananaBlockEntity> {

    private static final String SHAPE = "banana_bunch";

    @Override
    public ResourceLocation getModelResource(BananaBlockEntity animatable) {
        return resource("geo/" + SHAPE + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BananaBlockEntity animatable) {
        return resource("textures/block/" + SHAPE + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(BananaBlockEntity animatable) {
        return resource("animations/" + SHAPE + ".animation.json");
    }

    private static ResourceLocation resource(String path) {
        return new ResourceLocation(PleroCottage.MODID, path);
    }
}
