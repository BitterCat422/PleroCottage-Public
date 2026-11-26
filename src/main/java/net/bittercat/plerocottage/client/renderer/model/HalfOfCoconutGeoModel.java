package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blockentities.HalfOfCoconutBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HalfOfCoconutGeoModel extends GeoModel<HalfOfCoconutBlockEntity> {

    @Override
    public ResourceLocation getModelResource(HalfOfCoconutBlockEntity animatable) {
        return new ResourceLocation(PleroCottage.MODID, "geo/half_of_coconut.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HalfOfCoconutBlockEntity animatable) {
        return new ResourceLocation(PleroCottage.MODID, "textures/block/half_of_coconut.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HalfOfCoconutBlockEntity animatable) {
        return new ResourceLocation(PleroCottage.MODID, "animations/half_of_coconut.animation.json");
    }
}
