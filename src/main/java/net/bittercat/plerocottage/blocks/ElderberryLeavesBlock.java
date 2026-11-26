package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.bittercat.plerocottage.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ElderberryLeavesBlock extends AbstractFruitLeavesBlock<ElderberryLeavesBlock.Stage> {

    public static final EnumProperty<Stage> STAGE = EnumProperty.create("stage", Stage.class);

    private static final Stages<Stage> PLAN =
        new Stages<>(Stage.UNROLLED, Stage.FRUITLESS, Stage.LEAVES, Stage.FLOWERS);

    private final Supplier<ElderberryFlowerBlock> flower;

    public ElderberryLeavesBlock(Settings settings, Supplier<ElderberryFlowerBlock> flower) {
        super(STAGE, Stage.UNROLLED, PLAN, settings);
        this.flower = flower;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STAGE);
    }

    @Override
    protected boolean isRipe(Stage stage) {
        return stage == Stage.RIPE;
    }

    @Override
    protected boolean isOpenNeighbour(BlockState neighbour) {
        return neighbour.getBlock() instanceof ElderberryFlowerBlock;
    }

    @Override
    protected boolean repair(ServerLevel level, BlockPos pos, BlockState state) {
        boolean expectsFlower = stageOf(state) == Stage.SMALL || stageOf(state) == Stage.RIPE;
        if (!expectsFlower || hasFlower(level, pos)) {
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
        Stage stage = stageOf(state);
        if (stage == Stage.LEAVES || stage == Stage.FLOWERS) {
            return level.getBlockState(pos.above()).canBeReplaced();
        }
        return true;
    }

    @Override
    protected void grow(LevelAccessor level, BlockPos pos, BlockState state, RandomSource random) {
        switch (stageOf(state)) {
            case UNROLLED -> setStage(level, pos, random.nextDouble() < Config.leavesCanFruitChance()
                ? Stage.LEAVES : Stage.FRUITLESS);
            case LEAVES -> setStage(level, pos, Stage.FLOWERS);
            case FLOWERS -> {
                BlockState above = level.getBlockState(pos.above());
                if (isNaturalFlower(above)) {
                    setStage(level, pos, Stage.SMALL);
                } else if (above.canBeReplaced()) {
                    level.setBlock(pos.above(), flower.get().defaultBlockState(), 3);
                    setStage(level, pos, Stage.SMALL);
                }
            }
            case SMALL -> {
                // 花方块被提前取走时退回开花阶段重新来过，而不是让叶片阶段空推进。
                if (setFlowerAge(level, pos, ElderberryFlowerBlock.MAX_AGE)) {
                    setStage(level, pos, Stage.RIPE);
                } else {
                    setStage(level, pos, Stage.LEAVES);
                }
            }
            default -> {
                
            }
        }
    }

    @Override
    protected void harvest(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (!hasFlower(level, pos)) {
            setStage(level, pos, Stage.LEAVES);
            return;
        }
        switch (rollDrop(random)) {
            case KEEP -> {
                
            }
            case CLEAR -> {
                removeFlower(level, pos);
                setStage(level, pos, Stage.LEAVES);
            }
            case DROP -> {
                removeFlower(level, pos);
                setStage(level, pos, Stage.LEAVES);
                dropBerry(level, pos);
                playPickSound(level, pos);
            }
        }
    }

    @Override
    protected boolean harvestByHand(ServerLevel level, BlockPos pos, BlockState state, Player player) {
        if (!hasFlower(level, pos)) {
            setStage(level, pos, Stage.LEAVES);
            return false;
        }
        removeFlower(level, pos);
        setStage(level, pos, Stage.LEAVES);
        Settings settings = settings();
        giveHarvest(player, level.getRandom(), settings.fruit().get(), settings.pickMin(), settings.pickMax());
        playPickSound(level, pos);
        return true;
    }

    private boolean hasFlower(LevelReader level, BlockPos pos) {
        return isNaturalFlower(level.getBlockState(pos.above()));
    }

    public boolean ripen(LevelAccessor level, BlockPos leafPos) {
        BlockState leafState = level.getBlockState(leafPos);
        if (!(leafState.getBlock() instanceof ElderberryLeavesBlock)) {
            return false;
        }
        Stage stage = stageOf(leafState);
        if (stage == Stage.FRUITLESS || stage == Stage.RIPE) {
            return false;
        }
        setStage(level, leafPos, Stage.RIPE);
        return true;
    }

    private boolean setFlowerAge(LevelAccessor level, BlockPos pos, int age) {
        BlockPos flowerPos = pos.above();
        BlockState flowerState = level.getBlockState(flowerPos);
        if (!(flowerState.getBlock() instanceof ElderberryFlowerBlock)
            || flowerState.getValue(ElderberryFlowerBlock.PERSISTENT)) {
            return false;
        }
        level.setBlock(flowerPos, flowerState.setValue(ElderberryFlowerBlock.AGE, age), 3);
        return true;
    }

    private void removeFlower(LevelAccessor level, BlockPos pos) {
        BlockPos flowerPos = pos.above();
        if (level.getBlockState(flowerPos).getBlock() instanceof ElderberryFlowerBlock) {
            level.removeBlock(flowerPos, false);
        }
    }

    private boolean isNaturalFlower(BlockState state) {
        return state.getBlock() instanceof ElderberryFlowerBlock
            && !state.getValue(ElderberryFlowerBlock.PERSISTENT);
    }

    private void dropBerry(ServerLevel level, BlockPos pos) {
        double dropY = pos.getY();
        for (int i = 1; i <= 16; i++) {
            BlockState belowState = level.getBlockState(pos.below(i));
            if (belowState.isAir()) {
                dropY = pos.getY() - i;
                break;
            }
            if (!(belowState.getBlock() instanceof LeavesBlock)) {
                break;
            }
        }
        Settings settings = settings();
        ItemStack drop = new ItemStack(settings.fruit().get(),
            Mth.nextInt(level.getRandom(), settings.dropMin(), settings.dropMax()));
        ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5D, dropY, pos.getZ() + 0.5D, drop);
        entity.setPickUpDelay(10);
        level.addFreshEntity(entity);
    }

    public enum Stage implements StringRepresentable {
        UNROLLED("unrolled"),
        FRUITLESS("fruitless"),
        LEAVES("leaves"),
        FLOWERS("flowers"),
        SMALL("small"),
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
}
