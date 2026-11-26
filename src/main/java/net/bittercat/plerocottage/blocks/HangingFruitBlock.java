package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HangingFruitBlock extends FallingBlock implements BonemealableBlock {

    public static final EnumProperty<Stage> STAGE = EnumProperty.create("stage", Stage.class);

    private final Settings settings;

    public HangingFruitBlock(Settings settings) {
        super(BlockBehaviour.Properties.of()
            .mapColor(settings.mapColor())
            .sound(settings.soundType())
            .strength(settings.strength())
            .lightLevel(state -> 0)
            .noCollission()
            .noOcclusion()
            .isRedstoneConductor((state, level, pos) -> false)
            .ignitedByLava());
        this.settings = settings;
        registerDefaultState(stateDefinition.any().setValue(STAGE, Stage.IMMATURE));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    public Settings settings() {
        return settings;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 0;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape[] shapes = settings.shapes();
        return shapes[Math.min(state.getValue(STAGE).ordinal(), shapes.length - 1)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isHanging(level, pos)) {
            
            return;
        }
        if (state.getValue(STAGE) != Stage.RIPE) {

            level.removeBlock(pos, false);
            return;
        }
        super.tick(state, level, pos, random);
    }

    public void drop(Level level, BlockPos pos, BlockState state) {
        falling(FallingBlockEntity.fall(level, pos, state));
    }

    public boolean isHanging(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.above()).getBlock() instanceof HangingFruitLeavesBlock leaves
            && leaves.hangingSettings().fruitBlock().get() == this;
    }

    @Override
    protected void falling(FallingBlockEntity entity) {
        entity.setHurtsEntities(settings.hurtDamage(), settings.maxHurtDamage());
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        return aboveState.getBlock() instanceof HangingFruitLeavesBlock leaves
            && leaves.isValidBonemealTarget(level, above, aboveState, isClient);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (aboveState.getBlock() instanceof HangingFruitLeavesBlock leaves) {
            leaves.performBonemeal(level, random, above, aboveState);
        }
    }

    public enum Stage implements StringRepresentable {
        IMMATURE("immature"),
        GROWING("growing"),
        RIPE("ripe");

        private final String name;

        Stage(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public record Settings(MapColor mapColor, SoundType soundType, float strength,
                           float hurtDamage, int maxHurtDamage, VoxelShape[] shapes) {
    }
}
