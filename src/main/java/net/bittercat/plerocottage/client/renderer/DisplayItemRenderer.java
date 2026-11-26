package net.bittercat.plerocottage.client.renderer;

import net.bittercat.plerocottage.items.DisplayBlockItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DisplayItemRenderer extends GeoItemRenderer<DisplayBlockItem> {

    public DisplayItemRenderer(GeoModel<DisplayBlockItem> model) {
        super(model);
        addRenderLayer(new StaticModelLayer<>(this));
    }

    @Override
    public RenderType getRenderType(DisplayBlockItem animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
