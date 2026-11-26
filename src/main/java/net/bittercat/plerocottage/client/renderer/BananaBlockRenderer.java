package net.bittercat.plerocottage.client.renderer;

import net.bittercat.plerocottage.blockentities.BananaBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class BananaBlockRenderer extends GeoBlockRenderer<BananaBlockEntity> {

    public BananaBlockRenderer(GeoModel<BananaBlockEntity> model) {
        super(model);
    }

    @Override
    public RenderType getRenderType(BananaBlockEntity animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
