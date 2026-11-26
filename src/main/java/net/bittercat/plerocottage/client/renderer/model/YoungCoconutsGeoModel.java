package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blockentities.YoungCoconutsBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class YoungCoconutsGeoModel extends GeoModel<YoungCoconutsBlockEntity> {

    private static final int[] STAGE_SUFFIX = {1, 2, 3, 4};

    private static ResourceLocation resource(String kind, String file, int stage) {
        int suffix = STAGE_SUFFIX[Math.min(Math.max(stage, 0), STAGE_SUFFIX.length - 1)];
        return new ResourceLocation(PleroCottage.MODID, kind + "/young_coconut_" + suffix + file);
    }

    @Override
    public ResourceLocation getModelResource(YoungCoconutsBlockEntity animatable) {
        return resource("geo", ".geo.json", animatable.stage());
    }

    @Override
    public ResourceLocation getTextureResource(YoungCoconutsBlockEntity animatable) {
        return resource("textures/block", ".png", animatable.stage());
    }

    @Override
    public ResourceLocation getAnimationResource(YoungCoconutsBlockEntity animatable) {
        return resource("animations", ".animation.json", animatable.stage());
    }
}
