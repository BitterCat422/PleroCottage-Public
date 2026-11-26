package net.bittercat.plerocottage.worldgen;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record StructureTemplateFeatureConfig(ResourceLocation structure, boolean randomRotation,
                                             boolean randomMirror, HolderSet<Block> ignoredBlocks, Vec3i offset,
                                             Optional<PinkPetalsBlock> groundPetals)
    implements FeatureConfiguration {

    public static final Codec<StructureTemplateFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(ResourceLocation.CODEC.fieldOf("structure").forGetter(StructureTemplateFeatureConfig::structure),
            Codec.BOOL.fieldOf("random_rotation").orElse(false)
                .forGetter(StructureTemplateFeatureConfig::randomRotation),
            Codec.BOOL.fieldOf("random_mirror").orElse(false)
                .forGetter(StructureTemplateFeatureConfig::randomMirror),
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("ignored_blocks")
                .forGetter(StructureTemplateFeatureConfig::ignoredBlocks),
            Vec3i.offsetCodec(48).optionalFieldOf("offset", Vec3i.ZERO)
                .forGetter(StructureTemplateFeatureConfig::offset),
            GroundPetalsDecorator.FLOWER_CODEC.optionalFieldOf("ground_petals")
                .forGetter(StructureTemplateFeatureConfig::groundPetals))
        .apply(instance, StructureTemplateFeatureConfig::new));
}
