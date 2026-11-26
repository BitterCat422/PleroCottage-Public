package net.bittercat.plerocottage.client.renderer;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.bittercat.plerocottage.client.renderer.model.ConfiguredGeoModel;
import net.bittercat.plerocottage.content.food.DisplayModelResources;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public final class StaticModelLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
    private final Map<DisplayModelResources, ConfiguredGeoModel<T>> models = new HashMap<>();

    public StaticModelLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel,
                       RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer,
                       float partialTick, int packedLight, int packedOverlay) {
        DisplayModelResources.Composition composition = DisplayModelResources.compositionForModel(
            getGeoModel().getModelResource(animatable));
        if (composition == null) {
            return;
        }

        Color color = getRenderer().getRenderColor(animatable, partialTick, packedLight);
        for (DisplayModelResources resources : composition.additions()) {
            ConfiguredGeoModel<T> model = models.computeIfAbsent(resources, ConfiguredGeoModel::new);
            RenderType partType = RenderType.entityTranslucent(resources.texture());
            
            getRenderer().reRender(model.getBakedModel(resources.model()), poseStack, bufferSource,
                animatable, partType, bufferSource.getBuffer(partType), partialTick, packedLight,
                packedOverlay, color.getRedFloat() * resources.red(),
                color.getGreenFloat() * resources.green(), color.getBlueFloat() * resources.blue(),
                color.getAlphaFloat());
        }
    }
}
