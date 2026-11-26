package net.bittercat.plerocottage.worldgen;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;

import net.bittercat.plerocottage.PleroCottage;
import net.bittercat.plerocottage.config.Config;
import net.bittercat.plerocottage.mixin.NoiseBasedChunkGeneratorAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PleroCottage.MODID)
public final class ModBiomes {

    public static final ResourceKey<Biome> MEDITERRANEAN_PLAIN =
        biomeKey("mediterranean_plain");

    private ModBiomes() {
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void addBiomes(ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();
        Registry<Biome> biomes = server.registryAccess().registryOrThrow(Registries.BIOME);

        for (LevelStem stem : server.registryAccess().registryOrThrow(Registries.LEVEL_STEM).stream().toList()) {
            if (!stem.type().is(BuiltinDimensionTypes.OVERWORLD)) {
                continue;
            }
            ChunkGenerator generator = stem.generator();
            if (!(generator.getBiomeSource() instanceof MultiNoiseBiomeSource source)) {
                continue;
            }

            List<Pair<Climate.ParameterPoint, Holder<Biome>>> parameters =
                new ArrayList<>(source.parameters().values());
            if (Config.enableMediterraneanPlain()) {
                Holder<Biome> biome = biomes.getHolderOrThrow(MEDITERRANEAN_PLAIN);
                addIfAbsent(parameters, climate(-0.25F, 0.52F, 0.12F, 1.0F, -0.37F, 0.5F,
                    -0.78F, 0.68F, 0.0F, -0.68F, 0.35F), biome);
                addIfAbsent(parameters, climate(-0.25F, 0.52F, 0.12F, 1.0F, -0.37F, 0.5F,
                    -0.78F, 0.68F, 1.0F, -0.68F, 0.35F), biome);
            }

            generator.biomeSource = MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(parameters));
            generator.featuresPerStep = Suppliers.memoize(() -> FeatureSorter.buildFeaturesPerStep(
                List.copyOf(generator.biomeSource.possibleBiomes()),
                biome -> generator.generationSettingsGetter.apply(biome).features(), true));

            if (generator instanceof NoiseBasedChunkGenerator noiseGenerator) {
                NoiseGeneratorSettings settings = ((NoiseBasedChunkGeneratorAccessor) noiseGenerator)
                    .pleroCottage$getSettings().value();
                ((SurfaceRuleTarget) (Object) settings).pleroCottage$setDimensionType(stem.type());
            }
        }
    }

    public static SurfaceRules.RuleSource withBiomeSurfaceRules(SurfaceRules.RuleSource original) {
        List<SurfaceRules.RuleSource> rules = new ArrayList<>();
        rules.add(preliminarySurfaceRule(MEDITERRANEAN_PLAIN,
            Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.SAND.defaultBlockState()));
        rules.add(original);
        return SurfaceRules.sequence(rules.toArray(SurfaceRules.RuleSource[]::new));
    }

    private static SurfaceRules.RuleSource preliminarySurfaceRule(ResourceKey<Biome> biome, BlockState surface,
                                                                   BlockState underground, BlockState underwater) {
        return SurfaceRules.ifTrue(SurfaceRules.isBiome(biome),
            SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, CaveSurface.FLOOR),
                        SurfaceRules.sequence(
                            SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), SurfaceRules.state(surface)),
                            SurfaceRules.state(underwater))),
                    SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, true, 0, CaveSurface.FLOOR),
                        SurfaceRules.state(underground)))));
    }

    private static Climate.ParameterPoint climate(float tempMin, float tempMax, float humidityMin, float humidityMax,
                                                   float continentalMin, float continentalMax, float erosionMin,
                                                   float erosionMax, float depthPoint, float weirdnessMin,
                                                   float weirdnessMax) {
        return new Climate.ParameterPoint(
            Climate.Parameter.span(tempMin, tempMax),
            Climate.Parameter.span(humidityMin, humidityMax),
            Climate.Parameter.span(continentalMin, continentalMax),
            Climate.Parameter.span(erosionMin, erosionMax),
            Climate.Parameter.point(depthPoint),
            Climate.Parameter.span(weirdnessMin, weirdnessMax), 0L);
    }

    private static void addIfAbsent(List<Pair<Climate.ParameterPoint, Holder<Biome>>> parameters,
                                    Climate.ParameterPoint point, Holder<Biome> biome) {
        Pair<Climate.ParameterPoint, Holder<Biome>> entry = Pair.of(point, biome);
        if (!parameters.contains(entry)) {
            parameters.add(entry);
        }
    }

    private static ResourceKey<Biome> biomeKey(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation("plerocottage", name));
    }

    public interface SurfaceRuleTarget {
        void pleroCottage$setDimensionType(Holder<DimensionType> dimensionType);
    }
}
