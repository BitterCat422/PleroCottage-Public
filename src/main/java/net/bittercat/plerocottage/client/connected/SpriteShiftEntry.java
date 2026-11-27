package net.bittercat.plerocottage.client.connected;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

class SpriteShiftEntry {
    private ResourceLocation originalLocation;
    private ResourceLocation targetLocation;
    private TextureAtlasSprite original;
    private TextureAtlasSprite target;

    void set(ResourceLocation original, ResourceLocation target) {
        this.originalLocation = original;
        this.targetLocation = target;
    }

    void resolve() {
        // 资源重载后图集里的 sprite 是新对象，缓存的旧对象 UV 不再对得上，所以每次都按名字取
        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
        original = atlas.getSprite(originalLocation);
        target = atlas.getSprite(targetLocation);
    }

    TextureAtlasSprite getOriginal() {
        resolve();
        return original;
    }

    TextureAtlasSprite getTarget() {
        resolve();
        return target;
    }

    float getUnInterpolatedU(TextureAtlasSprite sprite, float u) {
        return (u - sprite.getU0()) / (sprite.getU1() - sprite.getU0()) * sprite.contents().width();
    }

    float getUnInterpolatedV(TextureAtlasSprite sprite, float v) {
        return (v - sprite.getV0()) / (sprite.getV1() - sprite.getV0()) * sprite.contents().height();
    }
}
