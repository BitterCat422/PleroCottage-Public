package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class HangingFruitLeavesBlock extends AbstractFruitLeavesBlock<HangingFruitLeavesBlock.Stage> {

    public static final EnumProperty<Stage> STAGE = EnumProperty.create("stage", Stage.class);

    private static final Stages<Stage> PLAN =
        new Stages<>(Stage.UNROLLED, Stage.FRUITLESS, Stage.LEAVES, Stage.FLOWERS);

    private final HangingSettings hanging;

    public HangingFruitLeavesBlock(HangingSettings settings) {
        super(STAGE, Stage.UNROLLED, PLAN, settings.leaves());
        this.hanging = settings;
    }

    // 阶段属性必须在构造期就注册，此时实例字段还没赋值，所以只能由子类用静态的 STAGE 完成。
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
    }

    public HangingSettings hangingSettings() {
        return hanging;
    }

    @Override
    protected boolean isRipe(Stage stage) {
        return stage == Stage.RIPE || stage == Stage.RARE_RIPE;
    }

    @Override
    protected boolean isRare(Stage stage) {
        return stage == Stage.RARE_RIPE;
    }

    @Override
    protected boolean repair(ServerLevel level, BlockPos pos, BlockState state) {
        boolean expectsFruit = switch (stageOf(state)) {
            case SMALL, GROWING, RIPE, RARE_RIPE -> true;
            default -> false;
        };
        if (!expectsFruit || hasFruit(level, pos)) {
            return false;
        }
        setStage(level, pos, Stage.LEAVES);
        return true;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        if (!super.isValidBonemealTarget(level, pos, state, isClient)) {
            return false;
        }
        if (state.getValue(PERSISTENT)) {
            
            return true;
        }
        return stageOf(state) != Stage.FLOWERS || level.getBlockState(pos.below()).canBeReplaced();
    }

    @Override
    protected double growthChance(Stage stage) {
        return stage == Stage.LEAVES
            ? Config.leavesBlossomChance() : Config.leavesFruitChance();
    }

    @Override
    protected boolean isOpenNeighbour(BlockState neighbour) {
        return neighbour.getBlock() == hanging.fruitBlock().get();
    }

    @Override
    protected void grow(LevelAccessor level, BlockPos pos, BlockState state, RandomSource random) {
        switch (stageOf(state)) {
            case UNROLLED -> setStage(level, pos, random.nextDouble() < Config.leavesCanFruitChance()
                ? Stage.LEAVES : Stage.FRUITLESS);
            case LEAVES -> setStage(level, pos, Stage.FLOWERS);
            case FLOWERS -> {
                if (level.getBlockState(pos.below()).canBeReplaced()) {
                    level.setBlock(pos.below(), hanging.fruitBlock().get().defaultBlockState(), 3);
                    setStage(level, pos, Stage.SMALL);
                }
            }
            case SMALL -> {
                // 果实被提前取走时退回普通阶段重新开花，而不是让叶片阶段空推进。
                setStage(level, pos, setFruitStage(level, pos, HangingFruitBlock.Stage.GROWING)
                    ? Stage.GROWING : Stage.LEAVES);
            }
            case GROWING -> {
                if (!setFruitStage(level, pos, HangingFruitBlock.Stage.RIPE)) {
                    setStage(level, pos, Stage.LEAVES);
                    return;
                }
                boolean rare = settings().canBeRare() && random.nextDouble() < Config.rareVariantChance();
                setStage(level, pos, rare ? Stage.RARE_RIPE : Stage.RIPE);
                if (level instanceof ServerLevel serverLevel) {
                    spreadBlossom(serverLevel, pos, random);
                }
            }
            default -> {
                
            }
        }
    }

    @Override
    protected void harvest(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (!hasFruit(level, pos)) {
            // 挂果方块被提前取走：叶片回到普通阶段，否则会永远停在成熟态。
            setStage(level, pos, Stage.LEAVES);
            return;
        }
        switch (rollDrop(random)) {
            case KEEP -> {
                
            }
            case CLEAR -> discard(level, pos);
            case DROP -> collect(level, pos, state, null);
        }
    }

    @Override
    protected boolean harvestByHand(ServerLevel level, BlockPos pos, BlockState state, Player player) {
        if (!hasFruit(level, pos)) {
            setStage(level, pos, Stage.LEAVES);
            return false;
        }
        collect(level, pos, state, player);
        return true;
    }

    private boolean hasFruit(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.below()).getBlock() == hanging.fruitBlock().get();
    }

    private boolean setFruitStage(LevelAccessor level, BlockPos leafPos, HangingFruitBlock.Stage stage) {
        BlockPos fruitPos = leafPos.below();
        BlockState fruitState = level.getBlockState(fruitPos);
        if (fruitState.getBlock() != hanging.fruitBlock().get()) {
            return false;
        }
        level.setBlock(fruitPos, fruitState.setValue(HangingFruitBlock.STAGE, stage), 3);
        return true;
    }

    private void collect(ServerLevel level, BlockPos pos, BlockState state, @Nullable Player player) {
        BlockPos fruitPos = pos.below();
        BlockState fruitState = level.getBlockState(fruitPos);
        boolean rare = stageOf(state) == Stage.RARE_RIPE;
        setStage(level, pos, Stage.LEAVES);
        if (rare) {
            level.setBlock(fruitPos, Blocks.AIR.defaultBlockState(), 3);
            if (player != null) {
                
                giveHarvest(player, level.getRandom(), settings().rareFruit().get(), 1, 1);
            } else {
                
                dropHarvest(level, pos, settings().rareFruit().get(), 1, 1);
            }
        } else {
            hanging.fruitBlock().get().drop(level, fruitPos,
                fruitState.setValue(HangingFruitBlock.STAGE, HangingFruitBlock.Stage.RIPE));
        }
        playPickSound(level, pos);
    }

    private void discard(ServerLevel level, BlockPos pos) {
        BlockPos fruitPos = pos.below();
        if (level.getBlockState(fruitPos).getBlock() == hanging.fruitBlock().get()) {
            level.setBlock(fruitPos, Blocks.AIR.defaultBlockState(), 3);
        }
        setStage(level, pos, Stage.LEAVES);
    }

    public record HangingSettings(Settings leaves, Supplier<? extends HangingFruitBlock> fruitBlock) {
    }

    public enum Stage implements StringRepresentable {
        UNROLLED("unrolled"),
        FRUITLESS("fruitless"),
        LEAVES("leaves"),
        FLOWERS("flowers"),
        SMALL("small"),
        GROWING("growing"),
        RIPE("ripe"),
        RARE_RIPE("rare_ripe");

        private final String name;

        Stage(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
