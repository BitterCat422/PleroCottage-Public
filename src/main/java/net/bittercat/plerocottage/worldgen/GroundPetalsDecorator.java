package net.bittercat.plerocottage.worldgen;

import java.util.Comparator;
import java.util.function.BiConsumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.bittercat.plerocottage.init.TreeDecoratorRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public final class GroundPetalsDecorator extends TreeDecorator {
    static final Codec<PinkPetalsBlock> FLOWER_CODEC = BuiltInRegistries.BLOCK.byNameCodec().comapFlatMap(
        block -> block instanceof PinkPetalsBlock petals ? DataResult.success(petals)
            : DataResult.error(() -> "花簇装饰要求 PinkPetalsBlock：" + block),
        petals -> petals);

    public static final Codec<GroundPetalsDecorator> CODEC =
        FLOWER_CODEC.fieldOf("flower").xmap(GroundPetalsDecorator::new, decorator -> decorator.flower).codec();

    private static final int TRIES = 96;
    private static final int RADIUS = 4;
    private static final int VERTICAL_RANGE = 2;

    private final PinkPetalsBlock flower;

    public GroundPetalsDecorator(PinkPetalsBlock flower) {
        this.flower = flower;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorRegistry.GROUND_PETALS.get();
    }

    @Override
    public void place(Context context) {
        if (context.level() instanceof WorldGenLevel level) {
            context.logs().stream().min(Comparator.comparingInt(BlockPos::getY)).ifPresent(base ->
                scatter(level, context.random(), base, flower, context::setBlock));
        }
    }

    static void scatter(WorldGenLevel level, RandomSource random, BlockPos base, PinkPetalsBlock flower,
                        BiConsumer<BlockPos, BlockState> place) {
        BlockState defaultState = flower.defaultBlockState();
        for (int i = 0; i < TRIES; i++) {
            int x = random.nextInt(RADIUS * 2 + 1) - RADIUS;
            int z = random.nextInt(RADIUS * 2 + 1) - RADIUS;
            for (int y = VERTICAL_RANGE; y >= -VERTICAL_RANGE; y--) {
                BlockPos pos = base.offset(x, y, z);
                if (level.isOutsideBuildHeight(pos) || !level.isEmptyBlock(pos)
                    || !defaultState.canSurvive(level, pos) || !level.ensureCanWrite(pos)) {
                    continue;
                }
                int amount = PinkPetalsBlock.MIN_FLOWERS
                    + random.nextInt(PinkPetalsBlock.MAX_FLOWERS - PinkPetalsBlock.MIN_FLOWERS + 1);
                place.accept(pos, defaultState.setValue(PinkPetalsBlock.AMOUNT, amount)
                    .setValue(PinkPetalsBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random)));
                break;
            }
        }
    }
}
