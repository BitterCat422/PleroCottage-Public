package net.bittercat.plerocottage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.BedBlock;

public final class ColoredBedItemRenderer extends BlockEntityWithoutLevelRenderer {
    public ColoredBedItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource buffers, int packedLight, int packedOverlay) {
        if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof BedBlock bed) {
            BlockStateHolder.render(bed, poseStack, buffers, packedLight, packedOverlay);
            return;
        }
        super.renderByItem(stack, displayContext, poseStack, buffers, packedLight, packedOverlay);
    }

    private static final class BlockStateHolder {
        private static void render(BedBlock bed, PoseStack poseStack, MultiBufferSource buffers,
                                   int packedLight, int packedOverlay) {
            BlockEntity blockEntity = bed.newBlockEntity(BlockPos.ZERO, bed.defaultBlockState());
            if (blockEntity != null) {
                net.minecraft.client.Minecraft.getInstance().getBlockEntityRenderDispatcher()
                    .renderItem(blockEntity, poseStack, buffers, packedLight, packedOverlay);
            }
        }
    }
}
