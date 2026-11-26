package net.bittercat.plerocottage.client.connected;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.Arrays;

final class BakedQuadHelper {
    private static final int INTS_PER_VERTEX = DefaultVertexFormat.BLOCK.getIntegerSize();
    private static final int UV_OFFSET = 4;

    private BakedQuadHelper() {
    }

    static BakedQuad clone(BakedQuad quad) {
        return new BakedQuad(Arrays.copyOf(quad.getVertices(), quad.getVertices().length),
            quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade());
    }

    static float getU(int[] data, int vertex) {
        return Float.intBitsToFloat(data[vertex * INTS_PER_VERTEX + UV_OFFSET]);
    }

    static float getV(int[] data, int vertex) {
        return Float.intBitsToFloat(data[vertex * INTS_PER_VERTEX + UV_OFFSET + 1]);
    }

    static void setU(int[] data, int vertex, float value) {
        data[vertex * INTS_PER_VERTEX + UV_OFFSET] = Float.floatToRawIntBits(value);
    }

    static void setV(int[] data, int vertex, float value) {
        data[vertex * INTS_PER_VERTEX + UV_OFFSET + 1] = Float.floatToRawIntBits(value);
    }
}
