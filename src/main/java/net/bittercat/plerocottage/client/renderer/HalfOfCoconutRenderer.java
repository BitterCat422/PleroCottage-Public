package net.bittercat.plerocottage.client.renderer;

import net.bittercat.plerocottage.blockentities.HalfOfCoconutBlockEntity;
import net.bittercat.plerocottage.client.renderer.model.HalfOfCoconutGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HalfOfCoconutRenderer extends GeoBlockRenderer<HalfOfCoconutBlockEntity> {

    public HalfOfCoconutRenderer() {
        super(new HalfOfCoconutGeoModel());
    }

    @Override
    public RenderType getRenderType(HalfOfCoconutBlockEntity animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
