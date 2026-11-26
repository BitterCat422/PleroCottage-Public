package net.bittercat.plerocottage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;

import net.bittercat.plerocottage.blockentities.HayNestBlockEntity;
import net.bittercat.plerocottage.content.entities.PoultryContent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public final class HayNestBlockEntityRenderer implements BlockEntityRenderer<HayNestBlockEntity> {
    private static final float[][][] EGG_LAYOUT = {
        {{0.5165F, 0.4845F}},
        {{0.375F, 0.375F}, {0.6686F, 0.625F}},
        {{0.498F, 0.3125F}, {0.6875F, 0.5829F}, {0.375F, 0.6445F}}
    };
    private static final float[][][] FEATHER_LAYOUT = {
        {{0.3125F, 0.44F}},
        {{0.3125F, 0.44F}, {0.6107F, 0.3025F}},
        {{0.3125F, 0.44F}, {0.6107F, 0.3025F}, {0.5572F, 0.6286F}}
    };
    private static final Quaternionf[] EGG_ROTATIONS = {
        new Quaternionf(-0.0784F, -0.1033F, 0.1005F, 0.9864F),
        new Quaternionf(0.0285F, -0.1618F, -0.1293F, 0.9779F),
        new Quaternionf(0.1048F, -0.0884F, 0.1624F, 0.9772F)
    };
    private static final Quaternionf[] FEATHER_ROTATIONS = {
        new Quaternionf(-0.5488F, 0.1598F, -0.0437F, 0.8194F),
        new Quaternionf(0.2831F, 0.5743F, 0.4722F, -0.6059F),
        new Quaternionf(-0.1660F, 0.8343F, 0.5249F, 0.0286F)
    };

    public HayNestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(HayNestBlockEntity entity, float partialTick, PoseStack pose, MultiBufferSource buffers,
                       int light, int overlay) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        renderGroup(entity, renderer, pose, buffers, light, overlay, 0, 3, EGG_LAYOUT, EGG_ROTATIONS, 0.3125F, 1F, true);
        renderGroup(entity, renderer, pose, buffers, light, overlay, 3, 6, FEATHER_LAYOUT, FEATHER_ROTATIONS, 0.375F, 1F, false);
    }

    private static void renderGroup(HayNestBlockEntity entity, ItemRenderer renderer, PoseStack pose,
                                    MultiBufferSource buffers, int light, int overlay, int first, int end,
                                    float[][][] layout, Quaternionf[] rotations, float y, float scale, boolean eggs) {
        int count = 0;
        for (int i = first; i < end; i++) if (!entity.getItem(i).isEmpty()) count++;
        if (count == 0) return;
        int rendered = 0;
        for (int i = first; i < end && rendered < count; i++) {
            ItemStack stack = entity.getItem(i);
            if (stack.isEmpty()) continue;
            pose.pushPose();
            pose.translate(layout[count - 1][rendered][0], y, layout[count - 1][rendered][1]);
            pose.mulPose(rotations[rendered]);
            if (eggs) {
                pose.translate(-0.5F, 0F, -0.5F);
            } else {
                pose.scale(scale, scale, scale);
            }
            if (eggs) {
                renderEgg(stack, pose, buffers, light, overlay);
            } else {
                renderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay,
                    pose, buffers, entity.getLevel(), 0);
            }
            pose.popPose();
            rendered++;
        }
    }

    static void renderEgg(ItemStack stack, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
            blockRenderer.renderSingleBlock(blockItem.getBlock().defaultBlockState(), pose, buffers, light, overlay);
        } else {
            BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
            blockRenderer.renderSingleBlock(PoultryContent.EGG_DISPLAY_BLOCK.get().defaultBlockState(),
                pose, buffers, light, overlay);
        }
    }
}
