package net.bittercat.plerocottage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;

import net.bittercat.plerocottage.blockentities.EggIncubatorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public final class EggIncubatorBlockEntityRenderer implements BlockEntityRenderer<EggIncubatorBlockEntity> {
    private static final float[][][] LAYOUT = {
        {{0.5165F, 0.4845F}},
        {{0.375F, 0.375F}, {0.6686F, 0.625F}},
        {{0.498F, 0.3125F}, {0.6875F, 0.5829F}, {0.375F, 0.6445F}}
    };
    private static final Quaternionf[] ROTATIONS = {
        new Quaternionf(-0.0784F, -0.1033F, 0.1005F, 0.9864F),
        new Quaternionf(0.0285F, -0.1618F, -0.1293F, 0.9779F),
        new Quaternionf(0.1048F, -0.0884F, 0.1624F, 0.9772F)
    };

    public EggIncubatorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(EggIncubatorBlockEntity entity, float partialTick, PoseStack pose, MultiBufferSource buffers,
                       int light, int overlay) {
        int count = 0;
        for (int i = 0; i < entity.getContainerSize(); i++) if (!entity.getItem(i).isEmpty()) count++;
        if (count == 0) return;
        int rendered = 0;
        for (int i = 0; i < entity.getContainerSize() && rendered < count; i++) {
            ItemStack stack = entity.getItem(i);
            if (stack.isEmpty()) continue;
            pose.pushPose();
            pose.translate(LAYOUT[count - 1][rendered][0], 0.9375F, LAYOUT[count - 1][rendered][1]);
            pose.mulPose(ROTATIONS[rendered]);
            pose.translate(-0.5F, 0F, -0.5F);
            HayNestBlockEntityRenderer.renderEgg(stack, pose, buffers, light, overlay);
            pose.popPose();
            rendered++;
        }
    }
}
