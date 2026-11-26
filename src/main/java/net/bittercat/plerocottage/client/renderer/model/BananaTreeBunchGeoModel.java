package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blockentities.BananaBlockEntity;
import net.bittercat.plerocottage.blocks.BananaTreeBunchBlock;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BananaTreeBunchGeoModel extends GeoModel<BananaBlockEntity> {

    private static final String[] SHAPES = {"banana_bunch_1", "banana_bunch_2", "banana_bunch_3"};
    private static final String[] TEXTURES = {"banana_bunch_1", "banana_bunch_2", "banana_bunch"};

    @Override
    public ResourceLocation getModelResource(BananaBlockEntity animatable) {
        return resource("geo/" + shape(animatable) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BananaBlockEntity animatable) {
        return resource("textures/block/" + TEXTURES[stage(animatable)] + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(BananaBlockEntity animatable) {
        return resource("animations/" + shape(animatable) + ".animation.json");
    }

    private static String shape(BananaBlockEntity animatable) {
        return SHAPES[stage(animatable)];
    }

    private static int stage(BananaBlockEntity animatable) {
        int stage = animatable.getBlockState().getValue(BananaTreeBunchBlock.BLOCKSTATE);
        return Math.min(Math.max(stage, 0), SHAPES.length - 1);
    }

    private static ResourceLocation resource(String path) {
        return new ResourceLocation(PleroCottage.MODID, path);
    }
}
