package net.bittercat.plerocottage.data;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

public final class ModSpriteSourceProvider extends SpriteSourceProvider {

    public ModSpriteSourceProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, existingFileHelper, PleroCottage.MODID);
    }

    @Override
    protected void addSources() {
        atlas(SIGNS_ATLAS)
            .addSource(new DirectoryLister("entity/signs", "entity/signs/"))
            .addSource(new DirectoryLister("entity/signs/hanging", "entity/signs/hanging/"));
        atlas(BEDS_ATLAS).addSource(new DirectoryLister("entity/bed", "entity/bed/"));
        atlas(SHULKER_BOXES_ATLAS).addSource(new DirectoryLister("entity/shulker_box", "entity/shulker_box/"));
    }
}
