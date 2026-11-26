package net.bittercat.plerocottage.machine.client.renderer.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.blocks.BlenderBlock;
import net.bittercat.plerocottage.machine.blockentities.BlenderBlockEntity;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class BlenderBlockEntityModel extends GeoModel<BlenderBlockEntity> {
    @Override
    public ResourceLocation getModelResource(BlenderBlockEntity animatable) {
        if (animatable.getBlockState().getValue(BlenderBlock.OPEN)) {
            return ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "geo/blender_open.geo.json");
        } else {
            return ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "geo/blender.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureResource(BlenderBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "textures/block/blender.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlenderBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(PleroCottageMachine.MOD_ID, "animations/blender.animation.json");
    }

    @Override
    public RenderType getRenderType(BlenderBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}
