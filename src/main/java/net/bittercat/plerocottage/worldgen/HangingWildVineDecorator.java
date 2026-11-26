package net.bittercat.plerocottage.worldgen;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.bittercat.plerocottage.blocks.WildVineBlock;
import net.bittercat.plerocottage.init.TreeDecoratorRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public final class HangingWildVineDecorator extends TreeDecorator {
    private static final int MAX_VINES = 3;
    private static final int MAX_LENGTH = 3;
    private static final float FRUIT_CHANCE = 0.25F;

    private static final Codec<WildVineBlock> VINE_CODEC = BuiltInRegistries.BLOCK.byNameCodec().comapFlatMap(
        block -> block instanceof WildVineBlock wildVine ? DataResult.success(wildVine)
            : DataResult.error(() -> "树冠垂藤装饰要求 WildVineBlock：" + block),
        wildVine -> wildVine);

    public static final Codec<HangingWildVineDecorator> CODEC =
        VINE_CODEC.fieldOf("vine").xmap(HangingWildVineDecorator::new, decorator -> decorator.vine).codec();

    private final WildVineBlock vine;

    public HangingWildVineDecorator(WildVineBlock vine) {
        this.vine = vine;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorRegistry.HANGING_WILD_VINE.get();
    }

    @Override
    public void place(Context context) {
        if (!(context.level() instanceof WorldGenLevel level)) {
            return;
        }
        BlockState defaultState = vine.defaultBlockState();
        List<BlockPos> anchors = new ArrayList<>();
        for (BlockPos leaf : context.leaves()) {
            BlockPos anchor = leaf.below();
            if (canPlaceAt(level, anchor) && defaultState.canSurvive(level, anchor)
                && Direction.Plane.HORIZONTAL.stream().anyMatch(side -> level.isEmptyBlock(leaf.relative(side)))) {
                anchors.add(anchor);
            }
        }

        RandomSource random = context.random();
        int attempts = 1 + random.nextInt(MAX_VINES);
        for (int i = 0; i < attempts && !anchors.isEmpty(); i++) {
            BlockPos anchor = anchors.remove(random.nextInt(anchors.size()));
            int maxLength = 1 + random.nextInt(MAX_LENGTH);
            int length = 1;
            while (length < maxLength && canPlaceAt(level, anchor.below(length))) {
                length++;
            }

            boolean fruit = random.nextFloat() < FRUIT_CHANCE;
            for (int segment = 0; segment < length; segment++) {
                boolean tip = segment == length - 1;
                context.setBlock(anchor.below(segment), defaultState
                    .setValue(WildVineBlock.TIP, tip)
                    .setValue(WildVineBlock.FRUIT, tip && fruit));
            }
        }
    }

    private static boolean canPlaceAt(WorldGenLevel level, BlockPos pos) {
        return !level.isOutsideBuildHeight(pos) && level.ensureCanWrite(pos) && level.isEmptyBlock(pos);
    }
}
