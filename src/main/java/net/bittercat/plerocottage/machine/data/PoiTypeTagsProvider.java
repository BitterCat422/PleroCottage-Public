package net.bittercat.plerocottage.machine.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.bittercat.plerocottage.machine.PleroCottageMachine;
import net.bittercat.plerocottage.machine.init.VillagerRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PoiTypeTagsProvider extends TagsProvider<PoiType> {
    public PoiTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.POINT_OF_INTEREST_TYPE, pProvider, PleroCottageMachine.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(
                VillagerRegistry.CHEF_POI.getKey(),
                VillagerRegistry.BARTENDER_POI.getKey(),
                VillagerRegistry.ORCHARDIST_POI.getKey()
        );
    }
}
