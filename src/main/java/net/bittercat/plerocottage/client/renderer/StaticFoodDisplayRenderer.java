package net.bittercat.plerocottage.client.renderer;

import net.bittercat.plerocottage.blockentities.StaticFoodDisplayBlockEntity;
import net.bittercat.plerocottage.client.renderer.model.StaticFoodDisplayGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.bittercat.plerocottage.blocks.PortionFoodBlock;
import net.bittercat.plerocottage.blocks.FoodModelProperties;
import net.bittercat.plerocottage.blocks.MultiBlockPieBlock;
import net.bittercat.plerocottage.content.food.DisplayModelResources;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class StaticFoodDisplayRenderer extends GeoBlockRenderer<StaticFoodDisplayBlockEntity> {
    public StaticFoodDisplayRenderer() {
        super(new StaticFoodDisplayGeoModel());
        addRenderLayer(new StaticModelLayer<>(this));
    }

    @Override
    public RenderType getRenderType(StaticFoodDisplayBlockEntity animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        String blockName = ForgeRegistries.BLOCKS.getKey(animatable.getBlockState().getBlock()).getPath();
        if (animatable.getBlockState().getBlock() instanceof MultiBlockPieBlock) {
            return RenderType.entityTranslucent(texture);
        }
        if (animatable.getBlockState().getBlock() instanceof PortionFoodBlock) {
            return RenderType.entityTranslucent(texture);
        }
        if (DisplayModelResources.compositionForBlock(blockName) != null) {
            return RenderType.entityTranslucent(texture);
        }
        return switch (FoodModelProperties.get(blockName).transparencyType()) {
            case "SOLID" -> RenderType.entitySolid(texture);
            case "TRANSLUCENT" -> RenderType.entityTranslucent(texture);
            default -> RenderType.entityCutout(texture);
        };
    }

    @Override
    protected Direction getFacing(StaticFoodDisplayBlockEntity animatable) {
        Direction facing = super.getFacing(animatable);
        return animatable.getBlockState().getBlock() instanceof MultiBlockPieBlock
            ? facing.getOpposite() : facing;
    }
}
