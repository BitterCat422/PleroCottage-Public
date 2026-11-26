package net.bittercat.plerocottage.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.bittercat.plerocottage.blockentities.BowlBlockEntity;
import net.bittercat.plerocottage.client.renderer.model.BowlGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public final class BowlRenderer extends GeoBlockRenderer<BowlBlockEntity> {
    private static final float[][][] LAYOUTS = {
        {},
        {{0.625F, 0.175F, 0.375F}, {0.375F, 0.1751F, 0.375F}},
        {
            {0.55F, 0.175F, 0.625F},
            {0.6052F, 0.1751F, 0.375F},
            {0.3965F, 0.25F, 0.3125F}
        },
        {
            {0.55F, 0.175F, 0.625F},
            {0.6052F, 0.1751F, 0.375F},
            {0.3965F, 0.25F, 0.3125F},
            {0.5F, 0.25F, 0.4F}
        }
    };
    private static final float[][][] ROTATIONS = {
        {},
        {
            {-0.0769805F, 0.7029039F, 0.6199394F, -0.34011036F},
            {0.07698052F, 0.702904F, 0.6199395F, 0.3401104F}
        },
        {
            {-0.3378894F, 0.8157371F, 0.43373513F, 0.179659F},
            {0.082345635F, 0.54431844F, 0.4755225F, -0.6861597F},
            {0.058254026F, 0.49071777F, 0.8326096F, 0.25012752F}
        },
        {
            {-0.3378894F, 0.8157371F, 0.43373513F, 0.179659F},
            {0.082345635F, 0.54431844F, 0.4755225F, -0.6861597F},
            {0.058254026F, 0.49071777F, 0.8326096F, 0.25012752F},
            {0F, 0.707107F, 0.707107F, 0F}
        }
    };
    private static final float[][][] SCALES = {
        {},
        {{0.90000004F, 0.8999998F, 1.4999998F}, {0.9000002F, 0.9000001F, 1.5000002F}},
        {
            {0.8999988F, 0.89999866F, 1.4999975F},
            {0.9F, 0.9000002F, 1.5000001F},
            {0.90000033F, 0.9F, 1.5000002F}
        },
        {
            {0.8999988F, 0.89999866F, 1.4999975F},
            {0.9F, 0.9000002F, 1.5000001F},
            {0.90000033F, 0.9F, 1.5000002F},
            {0.9F, 0.9F, 1.5F}
        }
    };

    public BowlRenderer() {
        super(new BowlGeoModel());
    }

    public void renderWithItems(BowlBlockEntity entity, float partialTick, PoseStack pose,
                                MultiBufferSource buffers, int light, int overlay) {
        pose.pushPose();
        super.render(entity, partialTick, pose, buffers, light, overlay);
        pose.popPose();
        renderContents(entity, pose, buffers, light, overlay);
    }

    @Override
    protected Direction getFacing(BowlBlockEntity animatable) {
        
        return Direction.NORTH;
    }

    private static void renderContents(BowlBlockEntity entity, PoseStack pose,
                                       MultiBufferSource buffers, int light, int overlay) {
        int count = 0;
        for (int slot = 0; slot < entity.getContainerSize(); slot++) {
            if (!entity.getItem(slot).isEmpty()) count++;
        }
        if (count == 0) return;
        if (count == 1) {
            PlateBlockEntityRenderer.renderItems(entity, pose, buffers, light, overlay);
            return;
        }

        Direction facing = entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        float facingAngle = switch (facing) {
            case SOUTH -> 180F;
            case WEST -> 90F;
            case EAST -> -90F;
            default -> 0F;
        };
        float[][] positions = LAYOUTS[count - 1];
        float[][] rotations = ROTATIONS[count - 1];
        float[][] scales = SCALES[count - 1];
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        int rendered = 0;
        for (int slot = 0; slot < entity.getContainerSize() && rendered < count; slot++) {
            ItemStack stack = entity.getItem(slot);
            if (stack.isEmpty()) continue;

            pose.pushPose();
            pose.translate(0.5F, positions[rendered][1], 0.5F);
            pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(facingAngle));
            pose.translate(positions[rendered][0] - 0.5F, 0, positions[rendered][2] - 0.5F);
            pose.mulPose(new Quaternionf(rotations[rendered][0], rotations[rendered][1],
                rotations[rendered][2], rotations[rendered][3]));
            pose.scale(scales[rendered][0], scales[rendered][1], scales[rendered][2]);
            renderer.renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, pose, buffers,
                entity.getLevel(), 0);
            pose.popPose();
            rendered++;
        }
    }

    @Override
    public RenderType getRenderType(BowlBlockEntity animatable, ResourceLocation texture,
                                    MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture);
    }
}
