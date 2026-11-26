package net.bittercat.plerocottage.machine.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.bittercat.plerocottage.machine.blocks.IceCreamMachineBlock;
import net.bittercat.plerocottage.machine.blockentities.IceCreamMachineBlockEntity;
import net.bittercat.plerocottage.machine.compat.plerocottage.PleroCottageCompat;

@OnlyIn(Dist.CLIENT)
public class IceCreamMachineBlockEntityRenderer extends BaseBlockEntityRenderer<IceCreamMachineBlockEntity> {
    public IceCreamMachineBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(IceCreamMachineBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 0.38, 0.5);
        Direction face = pBlockEntity.getBlockState().getValue(IceCreamMachineBlock.FACING);
        float angle = pBlockEntity.getBlockState().getValue(IceCreamMachineBlock.FACING).toYRot();
        
        if (face.getAxis() == Direction.Axis.X) angle = 360 - angle;
        pPoseStack.mulPose(Axis.YP.rotationDegrees(angle));
        pPoseStack.translate(0.01, 0, 0.38);
        ItemStack output = pBlockEntity.getItem(IceCreamMachineBlockEntity.OUTPUT_SLOT);
        if (output.isEmpty()) {
            output = pBlockEntity.getItem(IceCreamMachineBlockEntity.CONTAINER_SLOT);
        }
        if (PleroCottageCompat.isIceCreamCone(output)) {
            pPoseStack.translate(0, -0.035, 0);
        }
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(-40));
        float scale = 0.4F;
        pPoseStack.scale(scale, scale, scale);
        this.renderItem(output, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        pPoseStack.popPose();
    }
}
