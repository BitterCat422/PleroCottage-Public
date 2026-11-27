package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class RichBlackDirtFarmlandBlock extends FarmBlock {

    private static final float GROW_CHANCE = 0.25F;
    private static final float SECOND_GROW_CHANCE = 0.05F;

    public RichBlackDirtFarmlandBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_GRAY)
            .sound(SoundType.ROOTED_DIRT)
            .strength(1.6F, 0.6F)
            .noOcclusion()
            .randomTicks()
            .ignitedByLava()
            .isRedstoneConductor((state, level, pos) -> false));
        registerDefaultState(stateDefinition.any().setValue(MOISTURE, 7));
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
        // 原版作物的生长速度看这个方法，默认只认原版耕地
        return state.getValue(MOISTURE) > 0;
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction direction,
                                   IPlantable plantable) {
        PlantType plantType = plantable.getPlantType(level, pos.relative(direction));
        return plantType == PlantType.CROP || plantType == PlantType.PLAINS;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(MOISTURE) < 7) {
            level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        }

        BlockState above = level.getBlockState(pos.above());
        if (!isGrowable(above)) {
            return;
        }
        BlockPos cropPos = pos.above();
        if (random.nextFloat() < GROW_CHANCE) {
            grow(level, cropPos);
            if (random.nextFloat() < SECOND_GROW_CHANCE) {
                grow(level, cropPos);
            }
        }
    }

    private static boolean isGrowable(BlockState state) {
        Block block = state.getBlock();
        return block instanceof CropBlock || block instanceof BushBlock || block instanceof SaplingBlock;
    }

    private static void grow(ServerLevel level, BlockPos pos) {
        if (BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), level, pos)) {
            level.levelEvent(2005, pos, 0);
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance, 1.0F, level.damageSources().fall());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

}
