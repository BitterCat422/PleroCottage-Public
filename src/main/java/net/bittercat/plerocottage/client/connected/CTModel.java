package net.bittercat.plerocottage.client.connected;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class CTModel extends BakedModelWrapper<BakedModel> {
    static final ModelProperty<CTData> CT_PROPERTY = new ModelProperty<>();
    private final CTSpriteShiftEntry shift;
    private final Block target;
    private final boolean pane;

    CTModel(BakedModel original, CTSpriteShiftEntry shift, Block target, boolean pane) {
        super(original);
        this.shift = shift;
        this.target = target;
        this.pane = pane;
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData data) {
        CTData result = new CTData();
        if (pane) {
            result.sameAbove = world.getBlockState(pos.above()).getBlock() == target;
            result.sameBelow = world.getBlockState(pos.below()).getBlock() == target;
        }
        for (Direction face : Direction.values()) {
            boolean positive = face.getAxisDirection() == AxisDirection.POSITIVE;
            Direction h = right(face);
            Direction v = up(face);
            h = positive ? h.getOpposite() : h;
            if (face == Direction.DOWN) {
                v = v.getOpposite();
                h = h.getOpposite();
            }
            boolean flip = pane && !face.getAxis().isVertical()
                && face.getAxisDirection() == AxisDirection.NEGATIVE;
            int sign = flip ? -1 : 1;
            boolean up = connected(world, pos, v);
            boolean down = connected(world, pos, v.getOpposite());
            boolean left = connected(world, pos, sign < 0 ? h : h.getOpposite());
            boolean right = connected(world, pos, sign < 0 ? h.getOpposite() : h);
            int index = FullCTType.getTextureIndex(up, down, left, right,
                up && left && connected(world, pos, v, sign < 0 ? h : h.getOpposite()),
                up && right && connected(world, pos, v, sign < 0 ? h.getOpposite() : h),
                down && left && connected(world, pos, v.getOpposite(), sign < 0 ? h : h.getOpposite()),
                down && right && connected(world, pos, v.getOpposite(), sign < 0 ? h.getOpposite() : h));
            result.put(face, index);
        }
        return ModelData.builder().with(CT_PROPERTY, result).build();
    }

    private boolean connected(BlockAndTintGetter world, BlockPos pos, Direction... dirs) {
        BlockPos check = pos;
        for (Direction direction : dirs) check = check.relative(direction);
        return world.getBlockState(check).getBlock() == target;
    }

    private static Direction up(Direction face) {
        return face == Direction.UP || face == Direction.DOWN ? Direction.NORTH : Direction.UP;
    }

    private static Direction right(Direction face) {
        return face == Direction.EAST || face == Direction.WEST ? Direction.SOUTH : Direction.WEST;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                    ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, data, renderType);
        CTData ct = data.get(CT_PROPERTY);
        if (ct == null) return quads;
        if (pane && ((side == Direction.UP && ct.sameAbove) || (side == Direction.DOWN && ct.sameBelow))) {
            return List.of();
        }
        if (pane && side == null && (ct.sameAbove || ct.sameBelow)) {
            quads = new ArrayList<>(quads);
            quads.removeIf(quad -> (quad.getDirection() == Direction.UP && ct.sameAbove)
                || (quad.getDirection() == Direction.DOWN && ct.sameBelow));
        }
        TextureAtlasSprite original = shift.getOriginal();
        quads = new ArrayList<>(quads);
        for (int i = 0; i < quads.size(); i++) {
            BakedQuad quad = quads.get(i);
            if (quad.getSprite() != original) continue;
            BakedQuad copy = BakedQuadHelper.clone(quad);
            int[] vertices = copy.getVertices();
            int index = ct.get(quad.getDirection());
            for (int vertex = 0; vertex < 4; vertex++) {
                BakedQuadHelper.setU(vertices, vertex, shift.getTargetU(BakedQuadHelper.getU(vertices, vertex), index));
                BakedQuadHelper.setV(vertices, vertex, shift.getTargetV(BakedQuadHelper.getV(vertices, vertex), index));
            }
            quads.set(i, copy);
        }
        return quads;
    }

    static final class CTData {
        private final int[] indices = new int[6];
        boolean sameAbove;
        boolean sameBelow;
        CTData() { Arrays.fill(indices, -1); }
        void put(Direction face, int index) { indices[face.get3DDataValue()] = index; }
        int get(Direction face) { return indices[face.get3DDataValue()]; }
    }
}
