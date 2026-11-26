package net.bittercat.plerocottage.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.RegistryOps;
import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.worldgen.WorldGenRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

public final class WorldgenProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, WorldGenRegistry::bootstrapConfigured)
        .add(Registries.PLACED_FEATURE, WorldGenRegistry::bootstrapPlaced);

    private final PackOutput.PathProvider placedPaths;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public WorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(PleroCottage.MODID));
        this.registries = registries;
        this.placedPaths = output.createPathProvider(PackOutput.Target.DATA_PACK, "worldgen/placed_feature");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> writes = new ArrayList<>();
        writes.add(super.run(cachedOutput));

        var ops = RegistryOps.create(JsonOps.INSTANCE, registries.join());
        WorldGenRegistry.registerAdditionalPlacements((name, placement) -> {
            var feature = WorldGenRegistry.configuredKey(name).location();
            JsonElement modifiers = PlacementModifier.CODEC.listOf().encodeStart(ops, placement)
                .getOrThrow(false, error -> {
                    throw new IllegalStateException("无法编码放置特征 " + feature + "：" + error);
                });
            JsonObject json = new JsonObject();
            json.addProperty("feature", feature.toString());
            json.add("placement", modifiers);
            writes.add(DataProvider.saveStable(cachedOutput, json, placedPaths.json(feature)));
        });
        return CompletableFuture.allOf(writes.toArray(CompletableFuture<?>[]::new));
    }
}
