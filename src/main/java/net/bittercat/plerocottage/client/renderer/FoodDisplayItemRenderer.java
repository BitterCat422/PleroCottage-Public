package net.bittercat.plerocottage.client.renderer;

import net.bittercat.plerocottage.client.renderer.model.FoodDisplayItemGeoModel;
import net.bittercat.plerocottage.items.GeckoDisplayFoodItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public final class FoodDisplayItemRenderer extends GeoItemRenderer<GeckoDisplayFoodItem> {
    public FoodDisplayItemRenderer(FoodDisplayItemGeoModel model) {
        super(model);
        addRenderLayer(new StaticModelLayer<>(this));
    }

    @Override
    public RenderType getRenderType(GeckoDisplayFoodItem animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
