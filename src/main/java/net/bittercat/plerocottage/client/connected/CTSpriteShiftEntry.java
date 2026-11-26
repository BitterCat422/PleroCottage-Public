package net.bittercat.plerocottage.client.connected;

import net.minecraft.resources.ResourceLocation;

final class CTSpriteShiftEntry extends SpriteShiftEntry {
    private final int sheetSize;

    CTSpriteShiftEntry(int sheetSize) {
        this.sheetSize = sheetSize;
    }

    void set(ResourceLocation original, ResourceLocation target) {
        super.set(original, target);
    }

    float getTargetU(float localU, int index) {
        return getTarget().getU((getUnInterpolatedU(getOriginal(), localU) + (index % sheetSize) * 16) / sheetSize);
    }

    float getTargetV(float localV, int index) {
        return getTarget().getV((getUnInterpolatedV(getOriginal(), localV) + (index / sheetSize) * 16) / sheetSize);
    }
}
