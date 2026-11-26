package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blockentities.BananaBlockEntity;
import net.bittercat.plerocottage.blocks.BananaFrondBlock;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BananaFrondGeoModel extends GeoModel<BananaBlockEntity> {

    private static final String[] SHAPES = {"banana_frond_down", "banana_frond"};

    @Override
    public ResourceLocation getModelResource(BananaBlockEntity animatable) {
        return resource("geo/" + shape(animatable) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BananaBlockEntity animatable) {
        return resource("textures/block/banana_frond.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BananaBlockEntity animatable) {
        return resource("animations/" + shape(animatable) + ".animation.json");
    }

    private static String shape(BananaBlockEntity animatable) {
        int shape = animatable.getBlockState().getValue(BananaFrondBlock.BLOCKSTATE);
        return SHAPES[Math.min(Math.max(shape, 0), SHAPES.length - 1)];
    }

    private static ResourceLocation resource(String path) {
        return new ResourceLocation(PleroCottage.MODID, path);
    }
}
