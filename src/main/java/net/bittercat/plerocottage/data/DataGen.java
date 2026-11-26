package net.bittercat.plerocottage.data;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGen {

    private DataGen() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new ResourceTemplateProvider(output));
        generator.addProvider(event.includeClient(), new ContentBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new GeckoItemModelProvider(output));
        generator.addProvider(event.includeClient(), new ModSpriteSourceProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new net.bittercat.plerocottage.machine.data.ItemModelProvider(
            output, existingFileHelper));
        generator.addProvider(event.includeClient(), new net.bittercat.plerocottage.machine.data.MachineBlockStateProvider(
            output, existingFileHelper));
        generator.addProvider(event.includeClient(), new net.bittercat.plerocottage.machine.data.SoundProvider(
            output, existingFileHelper));
        if (event.includeServer()) {
            generator.addProvider(true, new RecipeGenerator(output,
                Path.of(System.getProperty("plerocottage.resources"))));
        }
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output));
        generator.addProvider(event.includeServer(), new net.bittercat.plerocottage.machine.data.PoiTypeTagsProvider(
            output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new WorldgenProvider(output, lookupProvider));

        BlockTagProvider blockTags = new BlockTagProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(),
            new ItemTagProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
    }
}
