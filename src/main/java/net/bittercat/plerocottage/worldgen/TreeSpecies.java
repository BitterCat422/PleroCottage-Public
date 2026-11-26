package net.bittercat.plerocottage.worldgen;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.blocks.AbstractFruitLeavesBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

public record TreeSpecies(String name, Supplier<Block> trunk, Supplier<? extends Block> leaves,
                          @Nullable TreeShape shape, @Nullable WildSpawn wildSpawn) {

    public TreeConfiguration buildTree() {
        return buildTree(List.of());
    }

    public TreeConfiguration buildTree(List<TreeDecorator> decorators) {
        if (shape == null) {
            throw new IllegalStateException("结构树没有 TreeShape：" + name);
        }
        Block canopy = leaves.get();
        BlockStateProvider foliage = canopy instanceof AbstractFruitLeavesBlock<?> fruitingLeaves
            ? BlockStateProvider.simple(fruitingLeaves.unrolledState())
            : BlockStateProvider.simple(canopy.defaultBlockState());
        return shape.build(BlockStateProvider.simple(trunk.get()), foliage, decorators);
    }
}
