package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blockentities.BananaBlockEntity;
import net.bittercat.plerocottage.blocks.BananaCrownBlock;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BananaCrownGeoModel extends GeoModel<BananaBlockEntity> {

    private static final String SHAPE = "banana_frond_block";

    @Override
    public ResourceLocation getModelResource(BananaBlockEntity animatable) {
        return resource("geo/" + SHAPE + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BananaBlockEntity animatable) {
        boolean opened = animatable.getBlockState().getValue(BananaCrownBlock.BLOCKSTATE) > 0;
        return resource("textures/block/" + (opened ? SHAPE : SHAPE + "_none") + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(BananaBlockEntity animatable) {
        return resource("animations/" + SHAPE + ".animation.json");
    }

    private static ResourceLocation resource(String path) {
        return new ResourceLocation(PleroCottage.MODID, path);
    }
}
