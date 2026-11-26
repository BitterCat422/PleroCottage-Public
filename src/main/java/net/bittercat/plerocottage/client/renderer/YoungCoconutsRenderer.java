package net.bittercat.plerocottage.client.renderer;

import net.bittercat.plerocottage.blockentities.YoungCoconutsBlockEntity;
import net.bittercat.plerocottage.client.renderer.model.YoungCoconutsGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class YoungCoconutsRenderer extends GeoBlockRenderer<YoungCoconutsBlockEntity> {

    public YoungCoconutsRenderer() {
        super(new YoungCoconutsGeoModel());
    }

    @Override
    public RenderType getRenderType(YoungCoconutsBlockEntity animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
