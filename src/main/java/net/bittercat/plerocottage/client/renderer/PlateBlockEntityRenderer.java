package net.bittercat.plerocottage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.bittercat.plerocottage.blockentities.PlateBlockEntity;

public final class PlateBlockEntityRenderer implements BlockEntityRenderer<PlateBlockEntity> {
    private static final float[][][] LAYOUT = {
        {{0.5F, 0.375F}},
        {{0.7F, 0.375F}, {0.3F, 0.375F}},
        {{0.55F, 0.675F}, {0.7F, 0.225F}, {0.3F, 0.225F}},
        {{0.55F, 0.675F}, {0.7F, 0.225F}, {0.3F, 0.225F}, {0.5F, 0.4F}}
    };
    private static final float[][] TILTS = {
        {0F}, {22.5F, -22.5F}, {-45F, 22.5F, -22.5F}, {-45F, 22.5F, -22.5F, 0F}
    };

    public PlateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PlateBlockEntity entity, float partialTick, PoseStack pose, MultiBufferSource buffers,
                       int light, int overlay) {
        renderItems(entity, pose, buffers, light, overlay);
    }

    static void renderItems(PlateBlockEntity entity, PoseStack pose, MultiBufferSource buffers,
                            int light, int overlay) {
        int count = 0;
        for (int i = 0; i < entity.getContainerSize(); i++) if (!entity.getItem(i).isEmpty()) count++;
        if (count == 0) return;
        Direction facing = entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float angle = switch (facing) { case SOUTH -> 180F; case WEST -> 90F; case EAST -> -90F; default -> 0F; };
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        float[][] positions = LAYOUT[count - 1];
        int rendered = 0;
        for (int i = 0; i < entity.getContainerSize() && rendered < count; i++) {
            ItemStack stack = entity.getItem(i);
            if (stack.isEmpty()) continue;
            pose.pushPose();
            pose.translate(0.5F, rendered < 3 ? 0.175F + rendered * 0.0001F : 0.2175F, 0.5F);
            pose.mulPose(Axis.YP.rotationDegrees(angle));
            pose.translate(positions[rendered][0] - 0.5F, 0, positions[rendered][1] - 0.5F);
            pose.mulPose(Axis.XP.rotationDegrees(-90F));
            pose.mulPose(Axis.ZP.rotationDegrees(180F + TILTS[count - 1][rendered]));
            pose.scale(0.9F, 0.9F, 1.5F);
            renderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, pose, buffers, entity.getLevel(), 0);
            pose.popPose();
            rendered++;
        }
    }
}
