package net.bittercat.plerocottage.client.renderer.model;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blockentities.StaticFoodDisplayBlockEntity;
import net.bittercat.plerocottage.blocks.CheeseWheelBlock;
import net.bittercat.plerocottage.blocks.MultiBlockPieBlock;
import net.bittercat.plerocottage.blocks.PizzaBlock;
import net.bittercat.plerocottage.blocks.PortionFoodBlock;
import net.bittercat.plerocottage.content.food.DisplayModelResources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.model.GeoModel;

public class StaticFoodDisplayGeoModel extends GeoModel<StaticFoodDisplayBlockEntity> {

    private static final String ANIMATION_RESOURCE = "plerocottage:animations/static_food.animation.json";

    @Override
    public ResourceLocation getModelResource(StaticFoodDisplayBlockEntity animatable) {
        String id = path(animatable);
        Block block = animatable.getBlockState().getBlock();
        DisplayModelResources.Composition composition = DisplayModelResources.compositionForBlock(id);
        if (composition != null) {
            return composition.main().model();
        }
        if (block instanceof CheeseWheelBlock) {
            int bites = animatable.getBlockState().getValue(PortionFoodBlock.BITES);
            return model("cheese_wheel" + (bites == 0 ? "" : "_" + bites + "cut"));
        }
        if (block instanceof PortionFoodBlock) {
            int bites = animatable.getBlockState().getValue(PortionFoodBlock.BITES);
            return model(id + (bites == 0 ? "" : "_" + bites));
        }
        if (block instanceof MultiBlockPieBlock) {
            int part = animatable.getBlockState().getValue(MultiBlockPieBlock.PART);
            return model("giant_cherries_pie_" + part);
        }
        return model(id);
    }

    @Override
    public ResourceLocation getTextureResource(StaticFoodDisplayBlockEntity animatable) {
        String id = path(animatable);
        Block block = animatable.getBlockState().getBlock();
        DisplayModelResources.Composition composition = DisplayModelResources.compositionForBlock(id);
        if (composition != null) {
            return composition.main().texture();
        }
        return texture(block instanceof PizzaBlock || block instanceof CheeseWheelBlock
            ? id : DisplayModelResources.blockTextureName(id));
    }

    @Override
    public ResourceLocation getAnimationResource(StaticFoodDisplayBlockEntity animatable) {
        DisplayModelResources.Composition composition =
            DisplayModelResources.compositionForBlock(path(animatable));
        if (composition != null) {
            return composition.main().animation();
        }
        if (animatable.getBlockState().getBlock() instanceof MultiBlockPieBlock) {
            return new ResourceLocation(ANIMATION_RESOURCE);
        }
        if (animatable.getBlockState().getBlock() instanceof CheeseWheelBlock) {
            int bites = animatable.getBlockState().getValue(PortionFoodBlock.BITES);
            return animation("cheese_wheel" + (bites == 0 ? "" : "_" + bites + "cut"));
        }
        if (animatable.getBlockState().getBlock() instanceof PizzaBlock) {
            String id = path(animatable);
            int bites = animatable.getBlockState().getValue(PortionFoodBlock.BITES);
            return animation(id + (bites == 0 ? "" : "_" + bites));
        }
        return new ResourceLocation(ANIMATION_RESOURCE);
    }

    private static String path(StaticFoodDisplayBlockEntity animatable) {
        Block block = animatable.getBlockState().getBlock();
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    private static ResourceLocation model(String name) {
        return new ResourceLocation(PleroCottage.MODID, "geo/" + name + ".geo.json");
    }

    private static ResourceLocation animation(String name) {
        return new ResourceLocation(PleroCottage.MODID, "animations/" + name + ".animation.json");
    }

    private static ResourceLocation texture(String name) {
        return new ResourceLocation(PleroCottage.MODID, "textures/block/" + name + ".png");
    }
}
