package net.bittercat.plerocottage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.blocks.ColoredShulkerBoxBlock;
import net.bittercat.plerocottage.blockentities.ColoredShulkerBoxBlockEntity;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class ColoredShulkerBoxRenderer implements BlockEntityRenderer<ColoredShulkerBoxBlockEntity> {
    private final ShulkerModel<?> model;

    public ColoredShulkerBoxRenderer(BlockEntityRendererProvider.Context context) {
        model = new ShulkerModel<>(context.bakeLayer(ModelLayers.SHULKER));
    }

    @Override
    public void render(ColoredShulkerBoxBlockEntity shulker, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        String color = shulker.getBlockState().getBlock() instanceof ColoredShulkerBoxBlock colored
            ? colored.colorName() : "snow";
        ResourceLocation texture = new ResourceLocation(PleroCottage.MODID,
            "entity/shulker_box/" + color + "_shulker_box");
        Material material = new Material(Sheets.SHULKER_SHEET, texture);
        net.minecraft.core.Direction facing = net.minecraft.core.Direction.UP;
        if (shulker.hasLevel()) {
            BlockState state = shulker.getLevel().getBlockState(shulker.getBlockPos());
            if (state.getBlock() instanceof ShulkerBoxBlock) {
                facing = state.getValue(ShulkerBoxBlock.FACING);
            }
        }

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.scale(0.9995F, 0.9995F, 0.9995F);
        poseStack.mulPose(facing.getRotation());
        poseStack.scale(1.0F, -1.0F, -1.0F);
        poseStack.translate(0.0F, -1.0F, 0.0F);
        ModelPart lid = model.getLid();
        lid.setPos(0.0F, 24.0F - shulker.getProgress(partialTick) * 8.0F, 0.0F);
        lid.yRot = (float) Math.toRadians(270.0F * shulker.getProgress(partialTick));
        VertexConsumer consumer = material.buffer(buffers, RenderType::entityCutoutNoCull);
        model.renderToBuffer(poseStack, consumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }
}
