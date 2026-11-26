package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blockentities.BananaBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BananaFrondSaplingGeoModel extends GeoModel<BananaBlockEntity> {

    private static final String SHAPE = "banana_frond_sapling";

    @Override
    public ResourceLocation getModelResource(BananaBlockEntity animatable) {
        return resource("geo/" + SHAPE + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BananaBlockEntity animatable) {
        return resource("textures/block/banana_frond_block_none.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BananaBlockEntity animatable) {
        return resource("animations/" + SHAPE + ".animation.json");
    }

    private static ResourceLocation resource(String path) {
        return new ResourceLocation(PleroCottage.MODID, path);
    }
}
