package net.bittercat.plerocottage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.bittercat.plerocottage.blocks.ColoredShulkerBoxBlock;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class ColoredShulkerBoxItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final BlockEntityRenderDispatcher dispatcher;

    public ColoredShulkerBoxItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.dispatcher = dispatcher;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource buffers, int packedLight, int packedOverlay) {
        if (stack.getItem() instanceof BlockItem blockItem
            && blockItem.getBlock() instanceof ColoredShulkerBoxBlock shulker) {
            BlockEntity blockEntity = shulker.newBlockEntity(BlockPos.ZERO, shulker.defaultBlockState());
            if (blockEntity != null) {
                dispatcher.renderItem(blockEntity, poseStack, buffers, packedLight, packedOverlay);
                return;
            }
        }
        super.renderByItem(stack, displayContext, poseStack, buffers, packedLight, packedOverlay);
    }
}
