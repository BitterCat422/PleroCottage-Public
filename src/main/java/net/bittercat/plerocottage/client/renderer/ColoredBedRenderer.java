package net.bittercat.plerocottage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blocks.ColoredBedBlock;
import net.bittercat.plerocottage.blockentities.ColoredBedBlockEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public final class ColoredBedRenderer implements BlockEntityRenderer<ColoredBedBlockEntity> {
    private final ModelPart head;
    private final ModelPart foot;

    public ColoredBedRenderer(BlockEntityRendererProvider.Context context) {
        head = context.bakeLayer(ModelLayers.BED_HEAD);
        foot = context.bakeLayer(ModelLayers.BED_FOOT);
    }

    @Override
    public void render(ColoredBedBlockEntity bed, float partialTick, PoseStack poseStack, MultiBufferSource buffers,
                       int packedLight, int packedOverlay) {
        String color = bed.getBlockState().getBlock() instanceof ColoredBedBlock colored
            ? colored.colorName() : "snow";
        Material material = new Material(Sheets.BED_SHEET,
            new ResourceLocation(PleroCottage.MODID, "entity/bed/" + color));
        if (bed.hasLevel()) {
            BlockState state = bed.getBlockState();
            ModelPart piece = state.getValue(BedBlock.PART) == BedPart.HEAD ? head : foot;
            renderPiece(poseStack, buffers, piece, state.getValue(BedBlock.FACING), material, packedLight,
                packedOverlay, false);
        } else {
            renderPiece(poseStack, buffers, head, Direction.SOUTH, material, packedLight, packedOverlay, false);
            renderPiece(poseStack, buffers, foot, Direction.SOUTH, material, packedLight, packedOverlay, true);
        }
    }

    private static void renderPiece(PoseStack poseStack, MultiBufferSource buffers, ModelPart piece,
                                    Direction facing, Material material, int packedLight, int packedOverlay,
                                    boolean footPiece) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5625F, footPiece ? -1.0F : 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F + facing.toYRot()));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        VertexConsumer consumer = material.buffer(buffers, RenderType::entitySolid);
        piece.render(poseStack, consumer, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
