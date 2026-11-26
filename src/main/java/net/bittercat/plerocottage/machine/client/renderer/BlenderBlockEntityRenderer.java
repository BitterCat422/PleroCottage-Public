package net.bittercat.plerocottage.machine.client.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.blockentities.BlenderBlockEntity;
import net.bittercat.plerocottage.machine.client.renderer.model.BlenderBlockEntityModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@OnlyIn(Dist.CLIENT)
public class BlenderBlockEntityRenderer extends GeoBlockRenderer<BlenderBlockEntity> {
    public BlenderBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        super(new BlenderBlockEntityModel());
    }
}
