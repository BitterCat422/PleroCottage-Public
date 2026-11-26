package net.bittercat.plerocottage.machine.client.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.blockentities.SaucepanAndWhiskBlockEntity;
import net.bittercat.plerocottage.machine.client.renderer.model.SaucepanAndWhiskBlockEntityModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@OnlyIn(Dist.CLIENT)
public class SaucepanAndWhiskBlockEntityRenderer extends GeoBlockRenderer<SaucepanAndWhiskBlockEntity> {
    public SaucepanAndWhiskBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        super(new SaucepanAndWhiskBlockEntityModel());
    }
}
