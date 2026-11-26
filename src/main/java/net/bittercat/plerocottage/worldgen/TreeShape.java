package net.bittercat.plerocottage.worldgen;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Supplier;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BushFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;

public enum TreeShape {

    TALL(() -> new StraightTrunkPlacer(6, 3, 0),
        () -> new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 4),
        () -> new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))),

    FANCY(() -> new FancyTrunkPlacer(6, 11, 3),
        () -> new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
        () -> new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))),

    ORCHARD(() -> new StraightTrunkPlacer(5, 1, 0),
        () -> new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
        () -> new TwoLayersFeatureSize(1, 0, 1)),

    ORCHARD_SMALL(() -> new StraightTrunkPlacer(4, 1, 0),
        () -> new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
        () -> new TwoLayersFeatureSize(1, 0, 1)),

    NUT_ORCHARD(() -> new StraightTrunkPlacer(5, 2, 0),
        () -> new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
        () -> new TwoLayersFeatureSize(1, 0, 1)),

    ORCHARD_TALL(() -> new StraightTrunkPlacer(6, 2, 0),
        () -> new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
        () -> new TwoLayersFeatureSize(1, 0, 1)),

    BUSH_FRUIT(() -> new StraightTrunkPlacer(4, 1, 0),
        () -> new BushFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
        () -> new TwoLayersFeatureSize(0, 0, 0)),

    FORKING(() -> new ForkingTrunkPlacer(5, 2, 0),
        () -> new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
        () -> new TwoLayersFeatureSize(1, 0, 2)),

    FORKING_TALL(() -> new ForkingTrunkPlacer(7, 1, 0),
        () -> new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
        () -> new TwoLayersFeatureSize(1, 0, 2)),

    FANCY_FRUIT(() -> new FancyTrunkPlacer(4, 2, 0),
        () -> new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
        () -> new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))),

    CHERRY_LARGE(() -> cherryTrunk(6, 2, 0, 1, 2, 2, 3),
        () -> new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5),
            0.25F, 0.25F, 0.0F, 0.0F),
        () -> new TwoLayersFeatureSize(1, 0, 2)),

    CHERRY_BROAD(() -> cherryTrunk(6, 2, 0, 1, 2, 2, 3),
        () -> new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(4),
            0.0F, 0.0F, 0.0F, 0.0F),
        () -> new TwoLayersFeatureSize(1, 0, 2)),

    CHERRY_ROUND(() -> cherryTrunk(5, 2, 0, 1, 2, 2, 3),
        () -> new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(4),
            0.0F, 0.0F, 0.0F, 0.0F),
        () -> new TwoLayersFeatureSize(1, 0, 2)),

    CHERRY_PEACH(() -> cherryTrunk(5, 1, 0, 1, 3, 2, 4),
        () -> new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5),
            0.1F, 0.0F, 0.0F, 0.0F),
        () -> new TwoLayersFeatureSize(1, 0, 2)),

    CHERRY_CHESTNUT(() -> cherryTrunk(5, 2, 0, 2, 2, 2, 4),
        () -> new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(4),
            0.0F, 0.0F, 0.0F, 0.0F),
        () -> new TwoLayersFeatureSize(1, 0, 2)),

    BUSH_CASHEW(() -> new StraightTrunkPlacer(5, 2, 0),
        () -> new BushFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 2),
        () -> new TwoLayersFeatureSize(0, 0, 0)),

    DARK_OAK(() -> new DarkOakTrunkPlacer(6, 1, 0),
        () -> new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
        () -> new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())),

    HOLLY(() -> new StraightTrunkPlacer(5, 2, 1),
        () -> new SpruceFoliagePlacer(UniformInt.of(2, 3), UniformInt.of(0, 2), UniformInt.of(1, 2)),
        () -> new TwoLayersFeatureSize(2, 0, 2));

    private final Supplier<TrunkPlacer> trunkPlacer;
    private final Supplier<FoliagePlacer> foliagePlacer;
    private final Supplier<FeatureSize> minimumSize;

    TreeShape(Supplier<TrunkPlacer> trunkPlacer, Supplier<FoliagePlacer> foliagePlacer,
              Supplier<FeatureSize> minimumSize) {
        this.trunkPlacer = trunkPlacer;
        this.foliagePlacer = foliagePlacer;
        this.minimumSize = minimumSize;
    }

    private static CherryTrunkPlacer cherryTrunk(int baseHeight, int heightRandA, int heightRandB,
                                                 int minBranches, int maxBranches,
                                                 int minBranchLength, int maxBranchLength) {
        IntProvider branches = UniformInt.of(minBranches, maxBranches);
        IntProvider branchLength = UniformInt.of(minBranchLength, maxBranchLength);
        return new CherryTrunkPlacer(baseHeight, heightRandA, heightRandB, branches, branchLength,
            UniformInt.of(-4, -3), UniformInt.of(-1, 0));
    }

    public TreeConfiguration build(BlockStateProvider trunk, BlockStateProvider foliage,
                                   List<TreeDecorator> decorators) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            trunk, trunkPlacer.get(),
            foliage, foliagePlacer.get(),
            minimumSize.get())
            .decorators(decorators)
            .ignoreVines()
            .build();
    }
}
